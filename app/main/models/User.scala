package main.models

import play.api.db._
import play.api.Play.current
import anorm._

case class User(firstName: String, familyName: String, email: String, password: String)

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
  def checkLogin(email: String, pw: String): Option[User] = {
    DB.withConnection { implicit conn =>
      val rows = SQL(
      """
      select * from user where email = {email}
        and password = {pw}
      """
      ).on( "email" -> email
          , "pw"    -> pw )()

      rows.toList.headOption map {
        case Row(id, fname: String, lname: String, email: String, pw: String) =>
          User(fname, lname, email, pw)
      }
    }
  }

  /*
   * Insert new user into DB.
   * If email already exists: return TRUE
   * Else: return FALSE
   */
  def insert(user: User) = {
    println("Checking if user already is in DB:" + user)
    DB.withConnection { implicit conn =>
      val rows = SQL(
        """
        select email from user where email = {email}
        """
      ).on("email" -> user.email)()

      if (!rows.isEmpty) {
        // User is already in the database!
        true
      }
      else {
        // User is not in the database, add him!
        println("Trying to add user: " + user)
        val id = SQL(
          """
          insert into user (fname, lname, email, password)
          values ({fname}, {lname}, {email}, {password})
          """
        ).on( "fname"    -> user.firstName
            , "lname"    -> user.familyName
            , "email"    -> user.email
            , "password" -> user.password ).executeInsert()
        println("User added with ID: " + id.get)
        false
      }
    }
  }
}
