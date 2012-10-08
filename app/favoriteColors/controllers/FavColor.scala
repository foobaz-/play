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



}
