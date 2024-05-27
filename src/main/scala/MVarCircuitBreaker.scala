import monix.execution.CancelableFuture
import monix.catnap.MVar
import monix.eval.Task
import monix.execution.schedulers.TestScheduler
import monix.execution.Scheduler.Implicits.global

object MVarCircuitBreaker extends App {

  //implicit val s = TestScheduler()

  //Use-case: Synchronized Mutable Variables #
  def sum(state: MVar[Task, Int], list: List[Int]): Task[Int] =
    list match {
      case Nil => state.take
      case x :: xs => state.take.flatMap { current =>
        state.put(current + x).flatMap(_ => sum(state, xs))
      }
    }

  val task = for {
    state <- MVar[Task].of(0)
    r <- sum(state, (0 until 100).toList)
  } yield r


  //task.runToFuture.foreach(println)
  println(task.runToFuture.value)

  }



