
import monix.eval.Task
import monix.execution.{Callback, Scheduler}
import monix.execution.misc.Local
import monix.execution.schedulers.TestScheduler

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

object LocalExample extends App{

    def req(requestId: String, userName: String): Future[Unit] = Future {
      println(s"Received a request to create a user $userName")
      // business logic
    }.flatMap(_ => registerUser(userName))

    def registerUser(name: String): Future[Unit] = Future {
      // business logic
      println(s"Registering a new user named $name")
    }

    val requests = List(req("1", "Clark"), req("2", "Bruce"), req("3", "Diana"))
    Await.result(Future.sequence(requests), Duration.Inf)

    //=> Received a request to create a user Bruce
    //=> Registering a new user named Bruce
    //=> Received a request to create a user Diana
    //=> Registering a new user named Diana
    //=> Received a request to create a user Clark
    //=> Registering a new user named Clark


  //Local works with Future if monix.execution.TracingScheduler is used as an ExecutionContext.
  //
  //Locals are shared by default, meaning that in the following example:
  implicit val s = Scheduler.traced

  val local = Local(0)

  val f1 = Future {
    local := 200 + local.get * 2
  }.map(_ => local.get)
  f1.onComplete{
    case Success(result) => println(s"For f1 local is " + result)
    case Failure(ex) => println(ex)
  }

  val f2 = Future {
    local := 100 + local.get * 3
  }.map(_ => local.get)
  f2.onComplete {
    case Success(result) => println(s"For f2 local is " + result)
    case Failure(ex) => println(ex)
  }


  //In the case of Future, isolation needs to be explicit and is achieved with Local.isolate:

  val local1 = Local(0)
  val f3 = Local.isolate {
    Future {
      local1 := 200 + local1.get * 2
    }.map(_ => local1.get)
  }
  f3.onComplete {
    case Success(result) => println(s"For f3 local is " + result)
    case Failure(ex) => println(ex)
  }

  val f4 = Local.isolate { Future{
    local1 := 100 + local1.get * 3
  }.map(_ => local1.get) }
  f4.onComplete {
    case Success(result) => println(s"For f4 local is " + result)
    case Failure(ex) => println(ex)
  }



val l = Local(0)
  val f5 = Task.evalAsync {
    l := 200 + l.get * 2
  }.map(_ => l.get).runToFuture
  println(f5.value)

  val f6 = Task.evalAsync {
    l := 100 + l.get * 3
  }.map(_ => l.get).runToFuture
  println(f6.value)

  val childTaskA = Task(local := 200)

  val childTaskB = for {
    _ <- Task.sleep(100.millis)
    v1 <- Task(local.get)
    _ <- Task(local := 100)
  } yield v1 + local.get

  val task = Task.parZip2(childTaskA, childTaskB)

  //runToFuture
  //Task#runToFuture isolates the Local automatically,
  // but the isolated reference is kept in the Future continuation:


  for {
    _ <- Task(local.update(1)).runToFuture
    value <- Future(local.get)
  } yield println(s"Local value in Future $value")






}

