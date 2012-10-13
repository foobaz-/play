package favcolor.models

import play.api.db._
import play.api.Play.current
import anorm._

case class ColorSurvey(id: Long, userId: Long, a1: String, a2: String)

object ColorSurvey {
  def insert(userId: Long, form: Map[String, Seq[String]]) : Option[Long] = {
    DB.withConnection { implicit conn =>
      val a1 = form("q1").head
      println("a1: " + a1)
      val a2 = form("q2").head
      println("a2: " + a2)
      SQL(
        """
        insert into color_survey (user_id, a1, a2)
        values ({user_id}, {a1}, {a2})
        """
      ).on( "user_id" -> userId
          , "a1"      -> a1
          , "a2"      -> a2 ).executeInsert()
    }
  }
}
