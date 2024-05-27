import monix.eval.Task

import scala.util.Random

object ErrorHnadlingTask extends App{


  val source = Task(Random.nextInt()).flatMap {
    case 32 if 32 % 2 == 0 =>
      Task.now(32)
    case other =>
      Task.raiseError(new IllegalStateException(other.toString))
  }

  // Will retry 4 times for a random even number,
  // or fail if the maxRetries is reached!
  val randomEven = source.onErrorRestartIf {
    case _: IllegalStateException => true
    case _ => false
  }
}
