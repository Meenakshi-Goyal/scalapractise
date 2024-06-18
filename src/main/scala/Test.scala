import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object Test extends App {

  //Given list of strings: sum up all the chars from strings which are of even length
  val list = List("abc", "mini", "2", "4", "3")
  val s = list.filter(_.length % 2 == 0).map(_.length).sum

  println(s)

  /**
   * step 1 = convert list of string into int
   * step 2 = convert every elemnt into int if possible else consider 1
   * step 3 = filter the value and sum
   */


  def checkInt(str: String): Boolean = {
    Try(str.toInt).isSuccess
  }

  println(list.filter(checkInt))

  val res: List[Int] = list.map { x =>
    Try {
      x.toInt
    } match {
      case Failure(exception) => 1 //null , none , nil
      case Success(value) => value
    }
  }
  println(res.filter(_ % 2 == 0).sum)


  val res2 = list.filter(checkInt).map(_.toInt % 2).filter(_ % 2 == 0).sum
  println(s"final res $res2")



  // revert a integer wthout converting into string




  @tailrec
  def reverseNumber(x: Int, acc: Int = 0): Int = {
    x match {
      case num if num < 10 => acc * 10 + num
      case num => reverseNumber((num / 10), (acc * 10 + num % 10))
    }
  }




  def reverse(x:Int ) ={
    if( x < 0) reverseNumber(x * -1) * -1 else reverseNumber(x)
  }


  println(reverse(-7899))

  /**
   * factorial
   * fibonacci initial 0 , 1
   *
   */

  def factorial(x:Int):Int = {
    def factorial1(x:Int , acc :Int):Int = {
      if(x <= 1) acc
      else factorial1(x-1 , x*acc)
    }
    factorial1(x,1)
  }
  println(factorial(5))

  def printFibonacciSeries(n: Int): Unit = {
    def fibonacci(n: Int): Int = {
      if (n <= 1) n
      else fibonacci(n - 1) + fibonacci(n - 2)
    }
    for (i <- 0 until n) {
      print(fibonacci(i))
    }
  }

  println(printFibonacciSeries(3))




  val string = "ABCD"

  def reverseString(str:String) = {
    str.reverse
  }

  println(reverseString(string))


  def findAllTwoSumPairs(nums: List[Int], target: Int): List[(Int, Int)] = {
    val numToIndex = scala.collection.mutable.Map[Int, Int]()
    val pairs = scala.collection.mutable.ListBuffer[(Int, Int)]()

    for ((num, index) <- nums.zipWithIndex) {
      val complement = target - num
      if (numToIndex.contains(complement)) {
        pairs += ((complement, num))
      }
      numToIndex(num) = index
    }

    pairs.toList
  }

  println(findAllTwoSumPairs(List(2, 7, 11, 15) , 9))




  def checkPalindrome(str:String) = {
    if(str.reverse == str) true else false
  }

  println(checkPalindrome("NITINI"))


  def removeVowels(str:String) ={
    val list = Set('a' , 'e' ,'i' , 'o', 'u')
    val lis2 = List('a' , 'e' ,'i' , 'o', 'u')
    val lowerString = str.toLowerCase()
    //first way
    val res  =  lowerString.filter(x => !list(x))

    //second way
    val res1 = str.replaceAll("[AEIOUaeiou]", "")



    //third way
    val vowels = "aeiou"
    println("yyyyy"+str.filter(x => !(vowels.contains(x))))
  }

  println(removeVowels("Meenakshi"))


  def factorFromDigit(no: Int ): Int = {
    val num = no.toString.toList
    println(num)
    val res = num.map{ x =>
      if((no % x.asDigit) == 0)  1
      else 0

    }
    println("res" + res)
    res.sum
  }

  println("factor " + factorFromDigit(143))


  def longestSubstringWithoutRepeating(s: String): Int = {
    def helper(start: Int, current: Int, seen: Set[Char], maxLength: Int): Int = {
      if (current == s.length) {
        maxLength
      } else {
        val currentChar = s(current)
        if (seen.contains(currentChar)) {
          helper(start + 1, start + 1, Set.empty, maxLength)
        } else {
          val newSeen = seen + currentChar
          val newMaxLength = math.max(maxLength, current - start + 1)
          helper(start, current + 1, newSeen, newMaxLength)
        }
      }
    }

    helper(0, 0, Set.empty, 0)
  }


  println(longestSubstringWithoutRepeating("pwwkew"))


  def compressString(str: String): String = {
    val s = str.toList
    var curStr: List[String] = List()
    var count: Int = 1
    for (i <- 1 until s.length) {
      println("ffffffffffffff" + s(i))
      if (s(i) == s(i - 1)) {
        count = count + 1
      }
      else {
        curStr = (curStr :+ s(i - 1).toString :+ count.toString)
        count = 1
      }
    }
    (curStr :+ s.last.toString :+ count.toString).mkString
  }

  println("compress string" + compressString("aaabb"))

  def removeDuplicates(str:String):String = {
    str.distinct
  }


  println("remove duplicates " + removeDuplicates("aabbcdddddddrrrriii"))


  def isBalanced(s: String): Boolean = {
    var balance = 0

    for (char <- s) {
      char match {
        case '(' => balance += 1
        case ')' =>
          balance -= 1
          if (balance < 0) return false
        case _ => // Ignore other characters
      }
    }

    balance == 0
  }
  println(isBalanced("(())()"))



}









