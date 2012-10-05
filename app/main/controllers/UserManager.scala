package main.controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db._
import anorm._
import play.api.Play.current

import main.views.html
import main.models.User


object UserManager extends Controller {

  val loginForm = Form(
    tuple(
      "Email address" -> email,
      "Password" -> nonEmptyText
    )
  )

  val registerForm = Form(
    tuple(
      "First name" -> nonEmptyText,
      "Family name" -> nonEmptyText,
      "Email address" -> email,
      "Password" -> nonEmptyText
    )
  )

  def showLoginScreen = Action {
    Ok(html.loginscreen(loginForm))
  }

  def loginAsUser = Action(parse.urlFormEncoded) { request =>
    val form = loginForm.bindFromRequest()(request)
    form.fold(
      // Handle case if form had errors
      // Display the login page and highlight wrong input
      f => BadRequest(html.loginscreen(f)),
      // Handle case if form was filled out correctly
      {
        case (email, pw) =>
          checkLogin(email, pw)
          .map(u => Redirect((new ReverseEntryPoint).index)
          .withSession( "session" -> email
                      , "fname"   -> u.firstName) )
          .getOrElse( Redirect((new ReverseUserManager).showLoginScreen))
      }
    )
  }


  def showRegisterScreen = Action {
    Ok(html.register(registerForm))
  }

  def newUser() = Action(parse.urlFormEncoded) { request =>
    val form = registerForm.bindFromRequest()(request)
    form.fold(
      // Handle case if form had errors
      // Display the register page and highlight wrong input
      f => BadRequest(html.register(f)),
      // Handle case if form was filled out correctly
      {
        case (fname, lname, email, pw) =>
          // Try to insert new user, return false flag if already in DB
          val doesAlreadyExist = insertNewUser(User(fname, lname, email, pw))
          // User does already exist, send Bad Request
          if (doesAlreadyExist) BadRequest(html.register(registerForm))
          // New user! Redirect to login
          else Redirect((new ReverseUserManager).showLoginScreen)
        // This case can't happen. Keeping the compiler happy
        case _ => Ok(html.loginscreen(loginForm))
      }
    )
  }

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

  def insertNewUser(user: User) = {
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
