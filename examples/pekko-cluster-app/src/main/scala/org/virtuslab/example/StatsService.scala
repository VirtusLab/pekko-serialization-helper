package org.virtuslab.example

import scala.concurrent.duration._

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

import org.virtuslab.psh.circe.PekkoCodecs

object StatsService {

  sealed trait Command extends CircePekkoSerializable
  final case class ProcessText(text: String, replyTo: ActorRef[Response]) extends Command {
    require(text.nonEmpty)
  }
  case object Stop extends Command

  sealed trait Response extends CircePekkoSerializable
  final case class JobResult(meanWordLength: Double) extends Response
  final case class JobFailed(reason: String) extends Response

  implicit lazy val codecResponse: Codec[Response] = deriveCodec
  implicit lazy val codecActorRefResponse: Codec[ActorRef[Response]] = new PekkoCodecs {}.actorRefCodec
  implicit lazy val codecCommand: Codec[Command] = deriveCodec

  def apply(workers: ActorRef[StatsWorker.Process]): Behavior[Command] =
    Behaviors.setup { ctx =>
      // if all workers would crash/stop we want to stop as well
      ctx.watch(workers)

      Behaviors.receiveMessage {
        case ProcessText(text, replyTo) =>
          ctx.log.info("Delegating request")
          val words = text.split(' ').toIndexedSeq
          // create per request actor that collects replies from workers
          ctx.spawnAnonymous(StatsAggregator(words, workers, replyTo))
          Behaviors.same
        case Stop =>
          Behaviors.stopped
      }
    }
}

object StatsAggregator {

  sealed trait Event extends CircePekkoSerializable
  private case object Timeout extends Event
  private case class CalculationComplete(length: Int) extends Event

  implicit lazy val codecEvent: Codec[Event] = deriveCodec

  def apply(
      words: Seq[String],
      workers: ActorRef[StatsWorker.Process],
      replyTo: ActorRef[StatsService.Response]): Behavior[Event] =
    Behaviors.setup { ctx =>
      ctx.setReceiveTimeout(3.seconds, Timeout)
      val responseAdapter =
        ctx.messageAdapter[StatsWorker.Processed](processed => CalculationComplete(processed.length))

      words.foreach { word =>
        workers ! StatsWorker.Process(word, responseAdapter)
      }
      waiting(replyTo, words.size, Nil)
    }

  private def waiting(
      replyTo: ActorRef[StatsService.Response],
      expectedResponses: Int,
      results: List[Int]): Behavior[Event] =
    Behaviors.receiveMessage {
      case CalculationComplete(length) =>
        val newResults = results :+ length
        if (newResults.size == expectedResponses) {
          val meanWordLength = newResults.sum.toDouble / newResults.size
          replyTo ! StatsService.JobResult(meanWordLength)
          Behaviors.stopped
        } else {
          waiting(replyTo, expectedResponses, newResults)
        }
      case Timeout =>
        replyTo ! StatsService.JobFailed("Service unavailable, try again later")
        Behaviors.stopped
    }

}
