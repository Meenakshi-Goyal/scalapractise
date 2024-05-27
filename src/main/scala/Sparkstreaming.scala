import org.apache.spark.sql.SparkSession

object Sparkstreaming extends App{


  val spark = SparkSession.builder()
    .appName("spark streaming")
    .master("local[2]")
    .getOrCreate()

  //reading a df
  def readFromSocket() = {
    val lines = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", "12345")
      .load


    //consuming a df
    val query = lines.writeStream
      .format("console")
      .outputMode("append")
      .start()

    query.awaitTermination()

  }

  readFromSocket()

}
