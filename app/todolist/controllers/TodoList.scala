package todolist.controllers

import play.api._
import play.api.mvc._

import todolist.models
import todolist.views

object TodoList extends Controller {

  def index = Action {
    Redirect(routes.TodoList.showTasks)
  }

  def showTasks = Action {
    Ok(views.html.tasks(models.Task.getAll))
  }

  def deleteTask(id: Long) = TODO

  def newTask() = TODO
}
