package main.controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import main.views.html


object UserManager extends Controller {

  def showLoginScreen = Action {
    Ok(html.loginscreen())
  }

  def loginAsUser = Action{ Ok(html.loginscreen()) }

  def showRegisterScreen = Action {
    val loginForm: Form[(String, String, String)] = Form(
      tuple(
        "First name" -> nonEmptyText,
        "Family name" -> nonEmptyText,
        "Email address" -> nonEmptyText
      )
    )
    Ok(html.register(loginForm))
  }

  def register = Action{ Ok(html.loginscreen()) }

  def newUser = Action{ request =>
    Ok(html.loginscreen())
  }


}
