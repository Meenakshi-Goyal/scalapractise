class Node[T](val value: T, var next: Option[Node[T]] = None)

class LinkedList[T] {
  var head: Option[Node[T]] = None

  def addElement(value: T): Unit = {
    val newNode = new Node(value)
    if (head.isEmpty) {
      head = Some(newNode)
    } else {
      var current = head
      while (current.get.next.isDefined) {
        current = current.get.next
      }
      current.get.next = Some(newNode)
    }
  }
  def printList(): Unit = {
    var current = head
    while (current.isDefined) {
      current match {
        case Some(node) =>
          print(node.value + " -> ")
          current = node.next
        case None =>
          // This case should not occur, but handling it for safety
          current = None
      }
    }
    println("null")
  }
}
object Main extends App {
  val linkedList = new LinkedList[Int]

  linkedList.addElement(1)
  linkedList.addElement(2)
  linkedList.addElement(3)

  linkedList.printList()
}
