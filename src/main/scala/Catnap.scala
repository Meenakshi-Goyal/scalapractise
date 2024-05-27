import monix.catnap.CircuitBreaker
import monix.eval._
import monix.execution.schedulers.TestScheduler

import scala.concurrent.duration._


object Catnap extends App{

  implicit val s: TestScheduler = TestScheduler()

  val circuitBreaker: Task[CircuitBreaker[Task]] = CircuitBreaker[Task].of(
    maxFailures = 5 ,
    resetTimeout = 10.seconds,
    onRejected = Task {
      println("Task rejected in Open or HalfOpen")
    },
    onClosed = Task {
      println("Switched to Close, accepting tasks again")
    },
    onHalfOpen = Task {
      println("Switched to HalfOpen, accepted one task for testing")
    },
    onOpen = Task {
      println("Switched to Open, all incoming tasks rejected for the next 10 seconds")}
  )

  val unsafeCircuit = CircuitBreaker[Task].unsafe(
    maxFailures = 5,
    resetTimeout = 10.seconds
  )
  val problematic = Task{
    val nr = 48

    if(nr % 2 == 0) nr else throw new RuntimeException("dummy")
  }

  val res = for {
    ci <- circuitBreaker
    r <- ci.protect(problematic)
  } yield r

  println(res.runToFuture.value)
  


}
