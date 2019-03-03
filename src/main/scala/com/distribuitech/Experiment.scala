package com.distribuitech

import opt._
import shared.FixedIterationTrainer


case class Experiment(evalFunction: EvaluationFunction,
                      hillClimbingIterations: List[Int], geneticIterations: List[Int],
                      mimicIterations: List[Int],
                      runs: Int, generator: OptimizationGenerator) {


  def runExperiment(): ExperimentResults = {

    val allResults: List[(String, AtomicResult)] = hillClimbingIterations.flatMap(iter => {
      //All optimization Algorithms

      List(
        "rhc", "sa"
      ).map(
        id => {
          val tuple = (id, runAndReport(id, iter, runs))
          println(tuple)
          tuple
        }
      )
    }
    ) :::
      geneticIterations.map(iter => {
        //All optimization Algorithms
        val tuple = ("ga", runAndReport("ga", iter, runs))
        println(tuple)
        tuple
      }
      ) ::: mimicIterations.map(iter => {
      //All optimization Algorithms
      val tuple = ("mimic", runAndReport("mimic", iter, runs))
      println(tuple)
      tuple
    }
    )
    val groupedResults = allResults.groupBy(_._1).mapValues(_.map(_._2)).mapValues(_.sortBy(_.iterations))
    ExperimentResults(groupedResults("rhc"), groupedResults("ga"), groupedResults("mimic"), groupedResults("sa"))
  }

  def getAlgorithm(id: String): OptimizationAlgorithm = id match {
    case "rhc" => generator.getRHC()
    case "sa" => generator.getSA()
    case "ga" => generator.getGA()
    case "mimic" => generator.getMIMIC()
  }


  def runAndReport(id: String, iterations: Int, runs: Int): AtomicResult = {
    val intermediateResult = (1 to runs).map(
      _ => {
        val startTime: Long = System.currentTimeMillis()
        val algorithm = getAlgorithm(id)
        val fit = new FixedIterationTrainer(algorithm, iterations)
        fit.train()
        val score = evalFunction.value(algorithm.getOptimal)
        val totalTime = System.currentTimeMillis() - startTime
        AtomicResult(iterations, totalTime, score)
      }

    ).reduce(
      (x1, x2) => AtomicResult(x1.iterations, x1.averageTime + x2.averageTime, x1.score + x2.score)
    )
    intermediateResult.copy(averageTime = intermediateResult.averageTime / runs, score = intermediateResult.score / runs)
  }


}
