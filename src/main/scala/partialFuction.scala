object partialFuction extends App {


  val r = new PartialFunction[Int,Int]{

  def isDefinedAt(q:Int) = q != 0

    def apply(q:Int) = q*10

  }

  println(r(12))

  //use case statement


  val q : PartialFunction[Int,Int] = {

    case x if(x % 2 == 0) => x * 2
  }

  println(q(8))

  //orElse

  val p:PartialFunction[Int,Int] = {
    case y if(y % 2 == 0) => y * 2
  }

  val s:PartialFunction[Int,Int] = {
    case z if(z % 3 == 0 ) => z * 3
  }

  val res = p.orElse(s)
  println(res(15))


  //collect
  val t: PartialFunction[Int, Int] = {
    case y if (y % 2 == 0) => y * 2
  }

  val y =   List(2,4,9) collect(t)
  println(y)


  //andThen
  val z: PartialFunction[Int, Int] = {
    case y if (y % 2 == 0) => y * 2
  }

  val append = (x:Int) => x*10
  val result = z andThen(append)

  println(result(18))




  //monads
/**
  Monads are a powerful abstraction in functional programming
  , and Scala provides rich support
  for working
  with monads.Monads provide a way to chain operations together
  while handling context
  , such as handling optional values
  , computations that might fail
  , or dealing
  with side effects
  .

  A Monad must adhere to three laws:

    Left Identity: unit(x).flatMap(f) == f(x)
  Right Identity: m.flatMap(unit) == m
  Associativity: m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
  In Scala
  , Option
  , List
  , Future
  , and many other types are monads
  .
**/


  //pure functions

  /**
   * A pure function is a function that depends only on its declared inputs and
   * its internal algorithm to produce its output. It does not read any other values from
   * “the outside world” — the world outside of the function’s scope — and it does not
   * modify any values in the outside world.
   * @param a
   * @param b
   * @return
   */
  def sum(a:Int , b:Int): Int = {
    a+b
  }
  println(sum(2,3))

  def impure(a:Int): Int = {
    var counter = 0
    counter += counter + a
    counter
  }


  /**
   * implicits parameter
   */
  implicit val defaultMultiplier: Int = 2
  def multiply(x: Int)(implicit multiplier: Int): Int = {
    x * multiplier
  }
  println(multiply(4))

  /**
   * implicit methods / function / conversion
   *
   * Implicit methods can be used to define implicit conversions from one type to another.
   * This is useful when you want to add functionality to existing types or
   * when you need to interface with APIs that require different types.
   */


  implicit def intToSeq(i:Int) = Seq(i)


  val value:Int = 10


  implicit class StringOps(val s: String) {
    def reverseWords: String = s.split(" ").reverse.mkString(" ")
  }
}


/**
 * implicit classs
 * an implicit class is a way to add new methods to existing types without modifying them directly.
 */

object ImplicitClassExample extends App{
    import partialFuction._

    val sentence = "Hello world from Scala"
    println(sentence.reverseWords)  // Output: Scala from world Hello
  }
