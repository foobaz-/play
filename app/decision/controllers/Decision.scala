package decision.controllers

import play.api._
import play.api.mvc._
import decision.views.html

object Decision extends Controller {
  def index = Action { implicit request =>
    Ok(html.index())
  }

  def getQuestion(id: Int) = Action {
    println("Got called with id " + id)
    id match {
      case 1 => Ok(html.q1())
      case _ => BadRequest("This is no valid question nr")
    }
  }
}
