package net.revenj.database.postgres.converters

import net.revenj.database.postgres.PostgresReader

import scala.collection.mutable.ArrayBuffer

trait Converter[T] {

  val dbName: String

  protected def parseRaw(reader: PostgresReader, context: Int, canBeNull: Boolean): T

  def parse(reader: PostgresReader, context: Int): T = parseRaw(reader, context, canBeNull = false)

  def parseOption(reader: PostgresReader, context: Int): Option[T] = Option(parseRaw(reader, context, canBeNull = true))

  protected def parseCollectionItem(reader: PostgresReader, context: Int): T
  protected def parseNullableCollectionItem(reader: PostgresReader, context: Int): Option[T]

  def parseCollection(reader: PostgresReader, context: Int): ArrayBuffer[T] = {
    parseCollectionOption(reader, context).getOrElse(ArrayBuffer.empty[T])
  }

  def parseNullableCollection(reader: PostgresReader, context: Int): ArrayBuffer[Option[T]] = {
    parseNullableCollectionOption(reader, context).getOrElse(ArrayBuffer.empty[Option[T]])
  }

  def parseCollectionOption(reader: PostgresReader, context: Int): Option[ArrayBuffer[T]] = {
    var cur = reader.read()
    if (cur == ',' || cur == ')') {
      None
    } else {
      val escaped = cur != '{'
      if (escaped) {
        reader.read(context)
      }
      cur = reader.peek
      if (cur == '}') {
        if (escaped) {
          reader.read(context + 2)
        } else {
          reader.read(2)
        }
        Some(ArrayBuffer.empty[T])
      } else {
        val innerContext = context << 1
        val list = ArrayBuffer.newBuilder[T]
        do {
          list += parseCollectionItem(reader, innerContext)
        } while (reader.last == ',')
        if (escaped) {
          reader.read(context + 1)
        } else {
          reader.read()
        }
        Some(list.result())
      }
    }
  }

  def parseNullableCollectionOption(reader: PostgresReader, context: Int): Option[ArrayBuffer[Option[T]]] = {
    var cur = reader.read()
    if (cur == ',' || cur == ')') {
      None
    } else {
      val escaped = cur != '{'
      if (escaped) {
        reader.read(context)
      }
      cur = reader.peek
      if (cur == '}') {
        if (escaped) {
          reader.read(context + 2)
        } else {
          reader.read(2)
        }
        Some(ArrayBuffer.empty[Option[T]])
      } else {
        val innerContext = context << 1
        val list = ArrayBuffer.newBuilder[Option[T]]
        do {
          list += parseNullableCollectionItem(reader, innerContext)
        } while (reader.last == ',')
        if (escaped) {
          reader.read(context + 1)
        } else {
          reader.read()
        }
        Some(list.result())
      }
    }
  }

  def toTuple(value: T): PostgresTuple

  def toTuple(value: Option[T]): PostgresTuple = {
    if (value.isEmpty) {
      PostgresTuple.NULL
    } else {
      toTuple(value.get)
    }
  }

}
