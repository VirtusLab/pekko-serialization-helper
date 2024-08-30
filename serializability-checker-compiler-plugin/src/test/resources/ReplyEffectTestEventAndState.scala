package org.random.project

import org.apache.pekko.persistence.typed.scaladsl.ReplyEffect

object ReplyEffectTestEventAndState {
  trait Event extends MySerializable
  trait State extends MySerializable

  def test: ReplyEffect[Event, State] = ???
}
