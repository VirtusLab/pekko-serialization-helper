package org.virtuslab.example.repository

import java.sql.Connection

import org.apache.pekko.japi.function.Function
import org.apache.pekko.projection.jdbc.JdbcSession

import scalikejdbc._

object ScalikeJdbcSession {
  def withSession[R](f: ScalikeJdbcSession => R): R = {
    val session = new ScalikeJdbcSession()
    try {
      f(session)
    } finally {
      session.close()
    }
  }
}

/**
 * Provide database connections within a transaction to Pekko Projections.
 */
final class ScalikeJdbcSession extends JdbcSession {
  val db: DB = DB.connect()
  db.autoClose(false)

  override def withConnection[Result](func: Function[Connection, Result]): Result = {
    db.begin()
    db.withinTxWithConnection(func(_))
  }

  override def commit(): Unit = db.commit()

  override def rollback(): Unit = db.rollback()

  override def close(): Unit = db.close()
}
