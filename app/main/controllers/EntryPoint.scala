package main.controllers

import play.api._
import play.api.mvc._

object EntryPoint extends Controller {

  def index = Action {
    Ok(main.views.html.index("Willkommen Hier"))
  }

}
