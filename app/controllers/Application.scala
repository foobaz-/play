package controllers

import play.api._
import play.api.mvc._

object EntryPoint extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome"))
  }

}
