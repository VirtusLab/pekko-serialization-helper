package org.virtuslab.example

import java.util.concurrent.TimeoutException

import scala.concurrent.Future

import io.grpc.Status
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.cluster.sharding.typed.scaladsl.ClusterSharding
import org.apache.pekko.grpc.GrpcServiceException
import org.apache.pekko.util.Timeout
import org.slf4j.LoggerFactory

class ShoppingCartServiceImpl(system: ActorSystem[_]) extends proto.ShoppingCartService {

  import system.executionContext

  private val logger = LoggerFactory.getLogger(getClass)

  implicit private val timeout: Timeout =
    Timeout.create(system.settings.config.getDuration("pekko-persistence-app.ask-timeout"))

  private val sharding = ClusterSharding(system)

  override def addItem(in: proto.AddItemRequest): Future[proto.Cart] = {
    logger.info("addItem {} to cart {}", in.itemId, in.cartId)
    val entityRef = sharding.entityRefFor(ShoppingCart.EntityKey, in.cartId)
    val reply: Future[ShoppingCart.Summary] =
      entityRef.askWithStatus(ShoppingCart.AddItem(in.itemId, in.quantity, _))
    val response = reply.map(cart => toProtoCart(cart))
    convertError(response)
  }

  private def toProtoCart(cart: ShoppingCart.Summary): proto.Cart = {
    proto.Cart(cart.items.iterator.map { case (itemId, quantity) =>
      proto.Item(itemId, quantity)
    }.toSeq)
  }

  private def convertError[T](response: Future[T]): Future[T] = {
    response.recoverWith {
      case _: TimeoutException =>
        Future.failed(new GrpcServiceException(Status.UNAVAILABLE.withDescription("Operation timed out")))
      case exc =>
        Future.failed(new GrpcServiceException(Status.INVALID_ARGUMENT.withDescription(exc.getMessage)))
    }
  }

}
