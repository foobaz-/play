package todolist.models

case class Task(id: Long, content: String)

object Task {
  // TODO
  def getAll(): List[Task] = Nil

  def create(content: String) {}

  def delete(id: Long) {}
}




