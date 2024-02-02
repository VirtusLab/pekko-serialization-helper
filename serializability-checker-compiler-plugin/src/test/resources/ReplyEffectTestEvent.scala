package org.random.project

import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect
import org.virtuslab.psh.annotation.SerializabilityTrait

object ReplyEffectTestEvent {
  @SerializabilityTrait
  trait NoTest
  trait Command extends MySerializable

  def test: ReplyEffect[Command, NoTest] = ???
}
