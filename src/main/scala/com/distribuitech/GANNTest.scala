package com.distribuitech

import co.theasi.plotly.{AxisOptions, Plot, ScatterMode, ScatterOptions, draw, writer}
import opt.OptimizationAlgorithm
import opt.ga.StandardGeneticAlgorithm

object GANNTest extends App {


  val iterations: List[Int] = (1 to 19).toList.map(_ * 5) ::: (1 to 10).toList.map(_ * 100)

  val populations: List[Int] = List(100, 200, 300, 400, 500)

  val reslts1: List[(Int, Double)] = populations.map(population => {
    val best = runAndPlot(population, 100, 20,"Find Population")
    (population, best)
  })

  println("poplation to results")
  println(reslts1)

  val bestPopulation: Int = reslts1.maxBy(_._2)._1

  val limit: Int = bestPopulation / 2

  val toMateList: List[Int] = (25 to limit).by(50) toList


  val reslts2: List[(Int, Double)] = toMateList.map(mateCount => {
    val best = runAndPlot(bestPopulation, mateCount, 20,"Find Mate param")
    (mateCount, best)
  })


  println("toMate to results")
  println(reslts2)

  val bestToMate: Int = reslts2.maxBy(_._2)._1

  val limitToMutate: Int = Math.min(bestToMate / 2, 50)

  val toMutateList: List[Int] = (5 to limitToMutate).by(5).toList

  val reslts3: List[(Int, Double)] = toMutateList.map(mutateCount => {
    val best = runAndPlot(bestPopulation, bestToMate, mutateCount,"Find Mutate param")
    (mutateCount, best)
  })


  println("toMutate to results")
  println(reslts3)


  runAndPlot(bestPopulation, bestToMate, reslts3.maxBy(_._2)._1,"Final", iterations = (1 to 4).toList.map(_ * 200))


  private def runAndPlot(population: Int, toMate: Int, toMuate: Int,tag: String = "", iterations: List[Int] = (1 to 10).toList.map(_ * 10)) = {


    val otput: List[(Int, Double, Double)] = iterations.map(
      iter => {
        val tuple = NNRandomizedOptimization.getOptimizationProblem()
        val ga: OptimizationAlgorithm = new StandardGeneticAlgorithm(population, toMate, toMuate, tuple._2)
        val results = NNRandomizedOptimization.trainAndReportTestAndValidationAccracy(ga, iter, tuple._1)
        println(s"$iter,$results")
        (iter, results._1, results._2)
      }

    )

    println(s"$tag SA best for ($population,$toMate,$toMuate) : " + otput.maxBy(_._3))

    val commonAxisOptions = AxisOptions()

    val xAxisOptions = commonAxisOptions.title("Iteration").noZeroLine
    val yAxisOptions = commonAxisOptions.title("Accuracy")


    val p = Plot()
      .withScatter(otput.map(_._1), otput.map(_._2),
        ScatterOptions().mode(ScatterMode.Line).name("Test Accuracy"))
      .withScatter(otput.map(_._1), otput.map(_._3),
        ScatterOptions().mode(ScatterMode.Line).name("Validation Accuracy"))
      .xAxisOptions(xAxisOptions).yAxisOptions(yAxisOptions)


    draw(p, s"$tag GA NN Optimization Cooling Poplation:$population toMate:$toMate toMate:$toMuate)", writer.FileOptions(overwrite = true))
    otput.map(_._3).max
  }


}
