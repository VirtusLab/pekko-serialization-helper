package org.random.project

import org.virtuslab.ash.annotation.Serializer

@Serializer(classOf[SerializableTrait], ".*Option.*")
class EmptySerializer {
  val r: (StdData, GenericData[Int, Int], IndirectData) = ???
}