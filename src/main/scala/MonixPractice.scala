
import monix.eval.Task
import monix.execution.Callback
import monix.execution.schedulers.TestScheduler
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.Random

//Monix is a high-performance Scala library built for composing asynchronous and
// event-driven programming.. Hence, it supports backpressure handling and ReactiveStreams protocols
// by default. It offers concurrency primitives such as Observable, Task, and many more.
// Being a Typelevel project, it’s compatible with other Typelevel libraries such as Cats.
// A Typelevel project, Monix proudly exemplifies pure, typeful, functional programming in Scala,
// while making no compromise on performance.


//Sub-projects
//monix-eval
//monix-execution
//monix-catnap
//monix-reactive
//monix-tail


//monix-eval #
//The monix-eval sub-project exposes the Task and Coeval data types, for dealing with purely functional effects in a principled way:
//Task
//Task is a data type for controlling possibly lazy & asynchronous computations, useful for controlling side-effects, avoiding nondeterminism and callback-hell.
//
//
//Let’s look at an example using the Task data type to construct an asynchronous program:
object MonixPractice extends App{
  // In order to evaluate tasks, we'll need a Scheduler
  implicit val s: TestScheduler = TestScheduler()
  def sampleMonixTask(a:Int , b:Int) = Task {
    val result = {
      a+b
    }
    result
  }
  val task = sampleMonixTask(5,6)

  // first way to execute task


  //The Task data type is lazy by default. Therefore, to start the execution of the program,
  // we need a trigger.Let’s call the runToFuture method and examine the result:
  val f = task.runToFuture
  println(f.value)


  // second way to execute task by runasync

  //There are several other methods to run the task. Let’s look at the runAsync method that takes a
  // callback method and returns a Cancelable:
  val cancelable = task.runAsync {
    result => result match {
      case Right(value) => println(value)
      case Left(e) => println(e.getMessage)
    }
  }


  //Moreover, there are several advantages of using the Task data type.
  // For one, it gives us more fine-grained control over the execution —
  // we can decide how and when the asynchronous program executes.

  //In case you just want an empty callback that doesn’t do anything on onSuccess,
  // but that can log errors when onError happens, maybe because you just want the side-effects:
  task.runAsync(Callback.empty[Throwable, Int])

  val ref = Callback.empty[Throwable, String]

  // callbacks calling other callbacks can quickly and easily lead to stack-overflow errors.
  val asyncCallback = Callback.forked(ref)


  //We can also runAsync with a Callback instance.

  val task2 = Task(2 + 1)
  val cancelable2 = task2.runAsync(
    new Callback[Throwable , Int] {

       def onSuccess(value: Int): Unit = println(s"value is $value")

       def onError(e: Throwable): Unit = println(s" error : ${e.getMessage}")
    }
  )


//But if you just want to trigger some side-effects quickly, you can just use foreach directly:

  val task3 = Task {
    println("Effect!"); "Result"
  }

  task3.foreach { result => println(result) }
  //=> Effect!
  //=> Result

  // Or we can use for-comprehensions
  for (result <- task3) {
    println(result)
  }

//  Monix is against blocking, we’ve established that.
//  But clearly some Task instances can be evaluated immediately on the current logical thread,
//  if allowed by the execution model. And for optimization purposes,
//  we might want to act immediately on their results, avoiding dealing with callbacks.
//
//    To do that, we can use runSyncStep:
  val task4 = Task.eval("Hello")
  task4.runSyncStep match {
    case Left(task4) => task4.runToFuture.foreach(r => println(s"Async: $r"))
    case Right(value) =>
      println(s"Got lucky: $value")
  }

  //Task.defer is about building a factory of tasks.
  // For example this will behave approximately like Task.eval:
  val task5 = Task.defer {
    Task.now { println("Effect") }
  }
  task5.runToFuture.foreach(println)

//  //error handling
  val source = Task(Random.nextInt()).flatMap {
    case even if even % 2 == 0 =>
      Task.now(even)
    case odd =>
      throw new IllegalStateException(odd.toString)
  }
  source.runAsync(r => println(r))

  // Will retry 4 times for a random even number,
  // or fail if the maxRetries is reached!
  val randomEven = source.onErrorRestart(maxRetries = 4)

  //triger a timeout

  //In case a Task is too slow to execute, we can cancel it and trigger a TimeoutException using Task.timeout
  val source2 =
    Task("Hello!").delayExecution(10.seconds)

  // Triggers error if the source does not
  // complete in 3 seconds after runAsync
  val timedOut = source2.timeout(3.seconds)
  timedOut.runAsync(r => println(r))
  //=> Failure(TimeoutException)


}
