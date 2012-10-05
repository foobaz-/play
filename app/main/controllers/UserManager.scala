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

  val registerForm = Form(
    tuple(
      "First name" -> nonEmptyText,
      "Family name" -> nonEmptyText,
      "Email address" -> email,
      "Password" -> nonEmptyText
    )
  )

  def showLoginScreen = Action {
    Ok(html.loginscreen())
  }

  def loginAsUser = Action { Ok(html.loginscreen()) }

  def showRegisterScreen = Action {
    Ok(html.register(registerForm))
  }

  def register = Action { Ok(html.loginscreen()) }

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
          val (doesAlreadyExist, user) = insertNewUser(User(fname, lname, email, pw))
          // User does already exist, send Bad Request
          if (doesAlreadyExist) BadRequest(html.register(registerForm))
          // New user! Redirect to login
          else Redirect((new ReverseUserManager).showLoginScreen)
        // This case can't happen. Keeping the compiler happy
        case _ => Ok(html.loginscreen())
      }
    )
  }

  def insertNewUser(user: User) = {
    println("Checking if user already is in DB:" + user)
    DB.withConnection { conn =>
      ()
    }
//    val firstName = data("First name")
//    val familyName = data("Family name")
//    val email = data("Email address")
//    val password = data("Password")
    (true, user)
  }

}
