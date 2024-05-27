import monix.eval.Task
import monix.execution.schedulers.TestScheduler
import monix.reactive.{Consumer, Observable}

import scala.concurrent.Future


object Observable1 extends App{


  implicit val s: TestScheduler = TestScheduler()
  val list:Observable[Long] = Observable.range(0,1000).take(100)
  val consumer: Consumer[Long, Long] = Consumer.foldLeft(0L)(_ + _)
  val task : Task[Long] = list.consumeWith(consumer)
  task.runAsync {
    result => result match {
      case Right(value) => println(value)
      case Left(e) => println(e.getMessage)
    }
  }



  //Transforming observables

  //1/ map


    val stream = Observable.range(1 , 5).map(el => s"new elem: $el")

   stream.foreachL(println).runToFuture


  //2. mapEval

  val stream2 = Observable.range(1, 5).mapEvalF(l => Future(println(s"$l: run asynchronously")))

  stream2.subscribe()

  //3. mapParallel

  





}
