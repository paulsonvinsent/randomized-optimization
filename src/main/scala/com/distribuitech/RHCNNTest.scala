package com.distribuitech

import co.theasi.plotly.{AxisOptions, Plot, ScatterMode, ScatterOptions, draw, writer}
import opt.{OptimizationAlgorithm, RandomizedHillClimbing}

object RHCNNTest extends App {

  val iterations: List[Int] = (1 to 4).toList.map(_ * 200) ::: (1 to 6).toList.map(_ * 1000)

  private val otput: List[(Int, Double, Double)] = iterations.map(
    iter => {
      val tuple = NNRandomizedOptimization.getOptimizationProblem()
      val rhc: OptimizationAlgorithm = new RandomizedHillClimbing(tuple._2)
      val results = NNRandomizedOptimization.trainAndReportTestAndValidationAccracy(rhc, iter, tuple._1)
      (iter, results._1, results._2)
    }

  )

  println(s"RHC best : " + otput.maxBy(_._3))

  val commonAxisOptions = AxisOptions()

  val xAxisOptions = commonAxisOptions.title("Iteration").noZeroLine
  val yAxisOptions = commonAxisOptions.title("Accuracy")


  val p = Plot()
    .withScatter(otput.map(_._1), otput.map(_._3),
      ScatterOptions().mode(ScatterMode.Line).name("Accuracy"))
    .xAxisOptions(xAxisOptions).yAxisOptions(yAxisOptions)


  draw(p, s"RHC NN Optimization", writer.FileOptions(overwrite = true))


}
