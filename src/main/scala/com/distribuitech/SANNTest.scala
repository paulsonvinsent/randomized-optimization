package com.distribuitech

import co.theasi.plotly.{AxisOptions, Plot, ScatterMode, ScatterOptions, draw, writer}
import opt.{OptimizationAlgorithm, SimulatedAnnealing}

object SANNTest extends App {

  List(0.3, 0.5, 0.6, 0.8, 0.9, 0.95, 0.98)
    .foreach(
      coolingParam => {
        runAndPlot(coolingParam)
      }
    )

  private def runAndPlot(coolingParam: Double) = {

    val iterations: List[Int] = (1 to 4).toList.map(_ * 200) ::: (1 to 6).toList.map(_ * 1000)

    val otput: List[(Int, Double, Double)] = iterations.map(
      iter => {
        val tuple = NNRandomizedOptimization.getOptimizationProblem()
        val sa: OptimizationAlgorithm = new SimulatedAnnealing(1E11, coolingParam, tuple._2)
        val results = NNRandomizedOptimization.trainAndReportTestAndValidationAccracy(sa, iter, tuple._1)
        (iter, results._1, results._2)
      }

    )

    println(s"SA best for $coolingParam : " + otput.maxBy(_._3))

    val commonAxisOptions = AxisOptions()

    val xAxisOptions = commonAxisOptions.title("Iteration").noZeroLine
    val yAxisOptions = commonAxisOptions.title("Accuracy")


    val p = Plot()
      .withScatter(otput.map(_._1), otput.map(_._2),
        ScatterOptions().mode(ScatterMode.Line).name("Test Accuracy"))
      .withScatter(otput.map(_._1), otput.map(_._3),
        ScatterOptions().mode(ScatterMode.Line).name("Validation Accuracy"))
      .xAxisOptions(xAxisOptions).yAxisOptions(yAxisOptions)


    draw(p, s"SA NN Optimization Cooling $coolingParam", writer.FileOptions(overwrite = true))
  }


}
