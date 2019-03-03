package com.distribuitech

import java.util

import opt.example.FourPeaksEvaluationFunction
import co.theasi.plotly._

object FourPeaks extends App {

  //  val iterations: List[Int] = List(10, 50, 500, 1000, 2000, 4000, 8000, 16000, 25000)

  val iterations: List[Int] = List(10, 50, 500, 1000, 2000)


  private val N = 200
  private val T = N / 10
  val ranges = new Array[Int](N)
  util.Arrays.fill(ranges, 2)
  val ef = new FourPeaksEvaluationFunction(T)


  private val results: ExperimentResults = Experiment(ef, iterations, 3).runExperiment()

  val commonAxisOptions = AxisOptions()

  val xAxisOptions = commonAxisOptions.title("Iterations").noZeroLine
  val yAxisOptions = commonAxisOptions.title("Score")


  val p = Plot()
    .withScatter(results.rhc.map(_.iterations), results.rhc.map(_.score),
      ScatterOptions().mode(ScatterMode.Line).name("RHC"))
    .withScatter(results.ga.map(_.iterations), results.ga.map(_.score),
      ScatterOptions().mode(ScatterMode.Line).name("GA"))
    .withScatter(results.sa.map(_.iterations), results.sa.map(_.score),
      ScatterOptions().mode(ScatterMode.Line).name("SA"))
    .withScatter(results.mimic.map(_.iterations), results.mimic.map(_.score),
      ScatterOptions().mode(ScatterMode.Line).name("MIMIC"))


  draw(p, "Four Peaks Scores", writer.FileOptions(overwrite = true))


}
