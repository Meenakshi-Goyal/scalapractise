import org.apache.spark
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

object ColumnAndExpression extends App {
  val spark = SparkSession.builder()
    .appName("column and expression")
    .config("spark.master", "local")
    .config("spark.sql.warehouse.dir", "src/main/resources/warehouse")
    .getOrCreate()


  val carDf = spark.read
    .option("inferSchema", "true")
    .json("src/main/resources/data/cars.json")

  carDf.show()


  //columns

  carDf.select("Name" , "Year")

  carDf.col("Weight_in_lbs") / 2.2

  val carsWithWeightsDF =  carDf.select(col("Name"),
    col("Weight_in_lbs"),
    expr("Weight_in_lbs / 2.2").as("Weight_in_kg_2"))

  carsWithWeightsDF.show()



  val filterCar = carDf.filter(col("Horsepower") > 130)
   val s = carDf.select(col("Horsepower") , col("Year")).where("Cylinders == '8'" )
  filterCar.show()
  s.show()

//spark sql

  carDf.createOrReplaceTempView("cars")
  val sparksql = spark.sql(
    """
      |select Year from cars where origin == 'USA'
      |""".stripMargin
  )
  sparksql.show()
  spark.sql("create database rtjvm")
  spark.sql(" use rtjvm")
  val databasesDf = spark.sql("show databases")
  databasesDf.show()


//  val driver = "org.postgresql.Driver"
//  val url = "jdbc:postgresql://localhost:5432/rtjvm"
//  val user = "docker"
//  val password = "docker"
//
//  def readTable(tableName: String) = spark.read
//    .format("jdbc")
//    .option("driver", driver)
//    .option("url", url)
//    .option("user", user)
//    .option("password", password)
//    .option("dbtable", s"public.$tableName")
//    .load()
//
//  def transferTables(tableNames: List[String], shouldWriteToWarehouse: Boolean = false) = tableNames.foreach { tableName =>
//    val tableDF = readTable(tableName)
//    tableDF.createOrReplaceTempView(tableName)
//
//    if (shouldWriteToWarehouse) {
//      tableDF.write
//        .mode(SaveMode.Overwrite)
//        .saveAsTable(tableName)
//    }
//  }
//
//  transferTables(List(
//    "employees",
//    "departments",
//    "titles",
//    "dept_emp",
//    "salaries",
//    "dept_manager")
//  )

  // read DF from loaded Spark tables
 // val employeesDF2 = spark.read.table("employees")

  //1. Read the movies DF and store it as a Spark table in the rtjvm database.
    carDf.write
      .mode(SaveMode.Overwrite)
      .saveAsTable("car2")


}
