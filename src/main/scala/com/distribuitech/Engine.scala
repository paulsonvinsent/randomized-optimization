package com.distribuitech

import co.theasi.plotly.{AxisOptions, Plot, ScatterMode, ScatterOptions, draw, writer}
import opt.EvaluationFunction

object Engine {
  def plotScores(ef: EvaluationFunction,
                 name: String, iterations: List[Int],
                 geneticIterations: List[Int],
                 mimicIterations: List[Int])
                (helper: OptimizationGenerator): ExperimentResults = {

    val results: ExperimentResults = Experiment(ef, iterations, geneticIterations, mimicIterations, 1,
      helper).runExperiment()

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
      .xAxisOptions(xAxisOptions).yAxisOptions(yAxisOptions)


    draw(p, s"$name Scores", writer.FileOptions(overwrite = true))
    results
  }


  def plotTimes(ef: EvaluationFunction, name: String, iterations: List[Int]
                , geneticIterations: List[Int], mimicIterations: List[Int])
               (helper: OptimizationGenerator): ExperimentResults = {

    val results: ExperimentResults = Experiment(ef, iterations, geneticIterations, mimicIterations, 1, helper).runExperiment()

    plotThisForTime(name, results)
    results
  }

  def plotThisForTime(name: String, results: ExperimentResults) = {
    val commonAxisOptions = AxisOptions()

    val xAxisOptions = commonAxisOptions.title("Time in ms").noZeroLine
    val yAxisOptions = commonAxisOptions.title("Score")


    val p = Plot()
      .withScatter(results.rhc.sortBy(_.averageTime).map(_.averageTime.toInt), results.rhc.sortBy(_.averageTime).map(_.score),
        ScatterOptions().mode(ScatterMode.Line).name("RHC"))
      .withScatter(results.ga.sortBy(_.averageTime).map(_.averageTime.toInt), results.ga.sortBy(_.averageTime).map(_.score),
        ScatterOptions().mode(ScatterMode.Line).name("GA"))
      .withScatter(results.sa.sortBy(_.averageTime).map(_.averageTime.toInt), results.sa.sortBy(_.averageTime).map(_.score),
        ScatterOptions().mode(ScatterMode.Line).name("SA"))
      .withScatter(results.mimic.sortBy(_.averageTime).map(_.averageTime.toInt), results.mimic.sortBy(_.averageTime).map(_.score),
        ScatterOptions().mode(ScatterMode.Line).name("MIMIC"))
      .xAxisOptions(xAxisOptions).yAxisOptions(yAxisOptions)


    draw(p, s"$name Times", writer.FileOptions(overwrite = true))
  }
}
