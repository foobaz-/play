package main.controllers

import play.api._
import play.api.mvc._

object EntryPoint extends Controller {

  def index = Action { request =>
    request.session.get("fname").map { user =>
      Ok(main.views.html.index(isLoggedIn = true, "Welcome " + user))
    }.getOrElse( Redirect(  (new ReverseUserManager).showLoginScreen) )
  }

}
