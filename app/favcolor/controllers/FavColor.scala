package favcolor.controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current

import favcolor.models.ColorSurvey
import favcolor.views.html

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
  	  case 2 => Ok(html.q2())
      case _ => Ok(html.q2())
  	}
  }

  def save = Action { request =>
    val surveyId: Option[Long] = for(
      answers <- request.body.asFormUrlEncoded;
      userId <- request.session.get("userId").map(_.toLong);
      surveyId <- ColorSurvey.insert(userId, answers)
    ) yield surveyId

    // TODO HAS TO BE SENT BACK TO AJAX CALL FROM SCRIPT
    surveyId.map( (id: Long) => println("Added survey with id: " + id))
    .map ( _ => Ok("Thank you for your participation!"))
    .getOrElse (BadRequest("Something fucked up while saving the form"))
  }

}
