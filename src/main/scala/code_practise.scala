import MonixPractice.{task, task3}
import monix.eval.Task
import monix.execution.Callback
import monix.execution.schedulers.TestScheduler

import scala.concurrent.{ExecutionContext, Future}
import monix.execution.Scheduler.Implicits.global

import scala.collection.mutable
import scala.util.Random
object DeferFutureActionExample extends App {

  def filterEvenNumbers(numbers: List[Int]): List[Int] = {
    var result: List[Int] = List()
    for (num <- numbers) {
      if (num % 2 == 0)
        result = num :: result
    }
    result.reverse
  }

  println(filterEvenNumbers(List(2, 3, 4)))

  // Sample list
  val originalList = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

  // Filtering even numbers
  val evenNumbersList = originalList.filter(num => num % 2 == 0)

  // Printing the result
  println(s"Original List: $originalList")
  println(s"Even Numbers List: $evenNumbersList")


  def sumOfSquares(list: List[Int]) = {
    list.map(x => x * x).foldLeft(0)((a, b) => a + b)
    //  list.map(x => x*x ).reduce((a,b) => a+b)
  }

  println(sumOfSquares(List(1, 2, 3)))


  def findMaxRecursive(lst: List[Int]): Option[Int] = {
    // Base case: if the list is empty, return None
    if (lst.isEmpty) {
      None
    } else {
      // Recursive case: compare the head with the maximum of the tail
      val currentElement = lst.head
      val maxOfRest = findMaxRecursive(lst.tail)

      // Return the maximum of the current element and the maximum of the rest of the list
      Some(maxOfRest.map(max => if (currentElement > max) currentElement else max).getOrElse(currentElement))
    }
  }

  println(findMaxRecursive(List(1, 4, 6, 9, 0, 120)))


  //remove duplicate from list

  val duplist = List(1, 1, 2, 3, 3, 2, 4, 4, 5, 5, 6)

  /*
  My understanding is that distinct will also maintain the order whereas toSet.toList won't.
   */
  println("unique list " + duplist.distinct)
  println("unique list " + duplist.toSet.toList)


  // example of immutability
  case class Person(name: String, age: Int)

  // Create an immutable instance of Person
  val person = Person("Alice", 30)

  // Attempt to modify the person object
  // This will create a new instance with the updated age
  val updatedPerson = person.copy(age = 31)

  println(s"Original Person: $person")
  println(s"Updated Person: $updatedPerson")


  val array = Array(1, 2, 3, 4)
  println("fffffffffffffffff")
  println(array.map(x => x * x).max)


  def primeNumber(no: Int) = {
    if (no == 1) false
    else {
      var x = true
      for (i <- 2 until no) {
        if (no % i == 0) {
          x = false
        }
      }
      x
    }
  }


  println(primeNumber(5))

  //sorted list and remove duplicates
  val l1 = List(9, 4, 2, 7)
  val l2 = List(3, 6, 8, 9, 7)

  val res = (l1 ::: l2).sorted.distinct
  println(res)

  //word count without uing spark
  val list = List("Rahul Patidar", "Bangalore Pune ", "BigData Spark Scala Hive Hadoop Rahul Patidar ")
  val words = list.flatMap(line => line.split(" "))
  val groupedmapData = words.groupBy(identity).mapValues(_.size)
  val s = groupedmapData.map { case (word, count) => s"($word,$count)" }.toList
  s.foreach(println)




  //pure function are the function which does not change the variable its passed
  // like return x+10 it does not chnage the value of x if use like x = x+10 it change the value of x. so
  // first function is pure other is impure


  //write a program to get the first non-repeatative character in the string "my name is meenakshi:"

  def nonRepeative(str: String): Char = {
    val count = str.groupBy(identity).mapValues(_.length)
    println(str.groupBy(identity))
    str.find(c => count(c) == 1).get
  }


  def orderNonRepeative(str: String) = {
    val countMap = new mutable.LinkedHashMap[Char, Int]
    str.foreach {
      c => countMap.update(c, countMap.getOrElseUpdate(c, 0) + 1)
    }
    println(countMap)
    str.find(c => countMap(c) == 1).getOrElse(' ')
  }

  println(orderNonRepeative("my name is meenakshi"))

  //println(nonRepeative("my name is meenakshi"))


  def findFirstNonRepeatingChar(str: String): Option[Char] = {
    val charCounts = str.foldLeft(Map.empty[Char, Int]) { (counts, char) =>
      counts + (char -> (counts.getOrElse(char, 0) + 1))
    }

    str.find(char => charCounts(char) == 1)
  }

  findFirstNonRepeatingChar("My meee")


  val map = Map("a" -> 1, "b" -> 2)
  val newMap = map + ("c" -> 3) // Map("a" -> 1, "b" -> 2, "c" -> 3)


}

