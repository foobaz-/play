package favoriteColors.controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current

import favoriteColors.views.html

object FavColor extends Controller {

  def index = Action { implicit request =>
    session.get("session")
           .map(_ => Ok(html.favcolorindex()))
           .getOrElse(
             Redirect((new main.controllers.ReverseMain).showLoginScreen))
  }

  def getQuestion(number: Int) = Action { implicit request => 
  	println("Got: " + request.uri)
  	number match {
  		case 1 => println(html.q1()); Ok(html.q1())
  		case _ => Ok(html.q1())
  	}
  }

}
