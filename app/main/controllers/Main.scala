package main.controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current

import main.views.html
import main.models.User


object Main extends Controller {

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


  //////////////// SIMPLE INDEX METHODS ////////////////
  /*
   * Show the register screen
   */
  def showRegisterScreen = Action { implicit request =>
    Ok(html.register(registerForm))
  }

  def logout = Action { request =>
    Redirect((new ReverseMain).index).withNewSession
  }

  def index = Action { implicit request =>
      Ok(main.views.html.index())
  }

  /*
   * Show the login screen
   */
  def showLoginScreen = Action { implicit request =>
    Ok(html.loginscreen(loginForm))
  }
  //////////////////////////////////////////////////////


  /*
   * Try to login the user with the form data supplied in the request.
   * User not in DB: Redirect to login screen. TODO show alreadt exists message in getOrElse
   * User in DB: Redirect to index and show greeting
   */
  def login = Action(parse.urlFormEncoded) { implicit request =>
    val form = loginForm.bindFromRequest()(request)
    form.fold(
      // Handle case if form had errors
      // Display the login page and highlight wrong input
      f => BadRequest(html.loginscreen(f)),
      // Handle case if form was filled out correctly
      {
        case (email, pw) =>
          User.checkLogin(email, pw)
          .map(u => Redirect((new ReverseMain).index)
          .withSession( "session" -> email
                      , "fname"   -> u.firstName) )
          .getOrElse( Redirect((new ReverseMain).showLoginScreen))
      }
    )
  }

  /*
   * Try to insert a new user into the database.
   * Redirect to register screen if the email already exists in the database.
   * Else redirect to login screen
   */
  def newUser() = Action(parse.urlFormEncoded) { implicit request =>
    val form = registerForm.bindFromRequest()(request)
    form.fold(
      // Handle case if form had errors
      // Display the register page and highlight wrong input
      f => BadRequest(html.register(f)),
      // Handle case if form was filled out correctly
      {
        case (fname, lname, email, pw) =>
          // Try to insert new user, return false flag if already in DB
          val doesAlreadyExist = User.insert(User(fname, lname, email, pw))
          // User does already exist, send Bad Request
          if (doesAlreadyExist) BadRequest(html.register(registerForm))
          // New user! Redirect to login
          else Redirect((new ReverseMain).index)
          .withSession( "session" -> email
                      , "fname"   -> fname)
        // This case can't happen. Keeping the compiler happy
        //case _ => Ok(html.loginscreen(loginForm))
      }
    )
  }
}
