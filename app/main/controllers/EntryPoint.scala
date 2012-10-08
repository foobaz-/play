package main.controllers

import play.api._
import play.api.mvc._

object EntryPoint extends Controller {

  def index = Action { implicit request =>
      Ok(main.views.html.index())
  }

}
