import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}
object FuturesDemo extends App {

  def cakeStock(flavour: String): Future[Int] = Future {
    if (flavour == "vanilla") 10
    else throw new IllegalStateException("Out of stock")
  }

  def cakeStockOption(flavour: String): Future[Option[Int]] = Future {
    println("checking  stock")
    if (flavour == "vanilla ") Some(10) else None
  }

  def cakePrice(): Future[Double] = Future.successful(3.25)


  //creating futures


  def sum(x:Int , y:Int): Future[Int] =
    Future{
      x+y
    }


  println(sum(2, 4))




  //execution in separation context
  //cancelling future


  Await.result(sum(3,2) , 5 seconds)

  sum(3,2).onComplete{
    case Success(value) => println(value)
    case Failure(exception) => println(exception)
  }

  //map

  val num = Future{3}
  val futureString = num.map( x => Future(x.toString))
  println(futureString)

  val list = Future(List(1,2,3))
  val listOpt = list.map( x => x.toString())
  println(listOpt)

  //flatmap
  //future[future[Int]]

  val finalFuture = futureString.flatMap(x => x)
  println(finalFuture)

  // for - comprehension

val f1  = Future{1}
  val f2 = Future{2}
  val f3 = Future{3}

  val result = for{
    res1 <- f1
    res2 <- f2
    res3 <- f3
  } yield (res1 + res2 + res3)

  result.onComplete{
    case Success(result) => println(s"For comprehension result is " + result)
    case Failure(ex) => println(ex)
  }

  //combining futures

  // traverse

  val futureOperationTraverse = List(cakeStockOption("vanilla"),
    cakeStockOption("black"),
    cakeStockOption("chocolate"),
    cakeStockOption("plain")
  )

  val resTraverse = Future.traverse(futureOperationTraverse){
    qty => qty.map(q1 => q1.getOrElse(0))
  }

  resTraverse.onComplete{
    case Success(value ) => println(value)
    case Failure(ex) => println(ex)
  }

  //zip

  val cakeStockAndPrice = cakeStock("vanilla") zip cakePrice()
  cakeStockAndPrice.onComplete {
    case Success(value) => println(value)
    case Failure(ex) => println(ex)
  }

  // zipWith

  val qtyAndPriceForZip : (Option[Int] , Double) => (Int , Double) = (someqty , price) => (someqty.getOrElse(0),price)
  val cakeAndPriceOperation = cakeStockOption("vanilla").zipWith(cakePrice())(qtyAndPriceForZip)

  cakeAndPriceOperation.onComplete {
    case Success(value) => println(value)
    case Failure(ex) => println(ex)
  }


  val l1 = Future{List(1,2,3)}
  val l2 = Future{List(4)}
  val l3 : (List[Int], List[Int]) => (String , List[Int]) = (x,y) => (x.toString() , y)
  val res = l1.zipWith(l2)(l3).onComplete {
    case Failure(exception) => println(s"eeeeeeeeeee $exception")
    case Success(value) => println(s"gggggg $value")
  }



  // andThen

  val cakeForAndThen = cakeStock("vanilla")
  cakeForAndThen.andThen{
    case stockQty => println(s" Qty is $stockQty")
  }


  //error handling

//  def recover(U >:T) (pf:PartialFunction(Throwable , U))
//
//  def recover(U >:T) (pf:PartialFunction(Throwable , Future[U]))


  //run future in a separate execution context

  implicit val ex: ExecutorService = Executors.newFixedThreadPool(5)
  implicit val m = ExecutionContext.fromExecutorService(ex)

  def getNew(int:Int) = {
    Future(int)
  }

  getNew(4).map(println)


  def age(x:Int) = Future{
    if(x >= 18) 1 else throw new IllegalStateException
  }.recover{
    case e:IllegalStateException  => e.getCause }
    .onComplete {
      case Failure(exception) => println(exception)
      case Success(value) => println(value)
    }

  age(12)

val s = List(1,2,3)
  println(s.foldLeft(0)((a,b) => a+b))


}