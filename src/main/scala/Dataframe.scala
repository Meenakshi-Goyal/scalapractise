import org.apache.spark
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.types._

object Dataframe extends App{
  val sparkSession = SparkSession.builder()
    .appName("dataframe basics")
    .config("spark.master","local")
    .getOrCreate()

  val firstDf = sparkSession.read.format("json")
    .option("inferSchema","true")
    .load("src/main/resources/data/cars.json")

  //reading df
  firstDf.show()
  firstDf.printSchema()

  //to get rows
  firstDf.take(10).foreach(println)

  val longtype = LongType

  val carsSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", DoubleType),
    StructField("Cylinders", LongType),
    StructField("Displacement", DoubleType),
    StructField("Horsepower", LongType),
    StructField("Weight_in_lbs", LongType),
    StructField("Acceleration", DoubleType),
    StructField("Year", StringType),
    StructField("Origin", StringType)
  ))

  val carsDFSchema = firstDf.schema

  // read a DF with your schema
  val carsDFWithSchema = sparkSession.read
    .format("json")
    .schema(carsDFSchema)
    .load("src/main/resources/data/cars.json")


  // create rows by hand
  val myRow = Row("chevrolet chevelle malibu", 18, 8, 307, 130, 3504, 12.0, "1970-01-01", "USA")

  // create DF from tuples
  val cars = Seq(
    ("chevrolet chevelle malibu", 18, 8, 307, 130, 3504, 12.0, "1970-01-01", "USA"),
    ("buick skylark 320", 15, 8, 350, 165, 3693, 11.5, "1970-01-01", "USA"),
    ("plymouth satellite", 18, 8, 318, 150, 3436, 11.0, "1970-01-01", "USA"),
    ("amc rebel sst", 16, 8, 304, 150, 3433, 12.0, "1970-01-01", "USA"),
    ("ford torino", 17, 8, 302, 140, 3449, 10.5, "1970-01-01", "USA"),
    ("ford galaxie 500", 15, 8, 429, 198, 4341, 10.0, "1970-01-01", "USA"),
    ("chevrolet impala", 14, 8, 454, 220, 4354, 9.0, "1970-01-01", "USA"),
    ("plymouth fury iii", 14, 8, 440, 215, 4312, 8.5, "1970-01-01", "USA"),
    ("pontiac catalina", 14, 8, 455, 225, 4425, 10.0, "1970-01-01", "USA"),
    ("amc ambassador dpl", 15, 8, 390, 190, 3850, 8.5, "1970-01-01", "USA")
  )
  val manualCarsDF = sparkSession.createDataFrame(cars) // schema auto-inferred

  // note: DFs have schemas, rows do not

  // create DFs with implicits

  import sparkSession.implicits._

  val manualCarsDFWithImplicits = cars.toDF("Name", "MPG", "Cylinders", "Displacement", "HP", "Weight", "Acceleration", "Year", "CountryOrigin")


  val mobile = Seq(
    ("Samsung", "Galaxy S10", "Android", 12),
    ("Apple", "iPhone X", "iOS", 13),
    ("Nokia", "3310", "THE BEST", 0)
  )

  val mobiledf = mobile.toDF("Brand", "company","type","ram")
  mobiledf.show()
 println( mobiledf.count())



  //writing a df
  val s = carsDFWithSchema.write.
  format("json")
  .mode(SaveMode.Overwrite)
  .save("src/main/resources/data/cars_dupe.json")




}
