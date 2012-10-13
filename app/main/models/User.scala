package main.models

import play.api.db._
import play.api.Play.current
import anorm._

case class User(id: Long, firstName: String, familyName: String, email: String, password: String)

/*
 * Manage database operations for table User
 */
object User {

  /*
   * Check if login is valid.
   * If this email and password combination matches,
   * return Some(User)
   * else None
   */
  def get(email: String, pw: String): Option[User] = {
    DB.withConnection { implicit conn =>
      val rows = SQL(
      """
      select * from user where email = {email}
        and password = {pw}
      """
      ).on( "email" -> email
          , "pw"    -> pw )()

      rows.toList.headOption map {
        case Row(id: Int, fname: String, lname: String, email: String, pw: String) =>
          User(id: Long, fname, lname, email, pw)
      }
    }
  }

  /*
   * Insert new user into DB.
   * Returns ID of added user
   * Throws an exception if the supplied email ad is already in the db.
   */
  def insert(fname: String, lname: String, email: String, password: String): Long = {
    DB.withConnection { implicit conn =>
      val rows = SQL(
        """
        select email from user where email = {email}
        """
      ).on("email" -> email)()

      if (!rows.isEmpty) {
        throw new Exception("Error: Email already in DB")
      }
      else {
        // User is not in the database, add him!
        val id = SQL(
          """
          insert into user (fname, lname, email, password)
          values ({fname}, {lname}, {email}, {password})
          """
        ).on( "fname"    -> fname
            , "lname"    -> lname
            , "email"    -> email
            , "password" -> password ).executeInsert()
        println("User added with ID: " + id.get)
        id.get
      }
    }
  }
}
