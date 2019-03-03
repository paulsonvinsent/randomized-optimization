package com.distribuitech

import opt._
import opt.ga.{GenericGeneticAlgorithmProblem, StandardGeneticAlgorithm}
import opt.prob.{GenericProbabilisticOptimizationProblem, MIMIC}
import shared.FixedIterationTrainer


case class Experiment(evalFunction: EvaluationFunction,
                      iterations: List[Int], mimicIterations: List[Int],
                      runs: Int,hcp:GenericHillClimbingProblem,
                      gap:GenericGeneticAlgorithmProblem,
                      pop:GenericProbabilisticOptimizationProblem) {



  def runExperiment(): ExperimentResults = {

    val allResults: List[(String, AtomicResult)] = iterations.flatMap(iter => {
      //All optimization Algorithms
      val rhc = new RandomizedHillClimbing(hcp)
      val simulatedAnnealing = new SimulatedAnnealing(1E13, .97, hcp)
      val ga = new StandardGeneticAlgorithm(200, 20, 10, gap)
      List(
        ("rhc", rhc), ("ga", ga), ("sa", simulatedAnnealing)
      ).map(
        pair => {
          val tuple = (pair._1, runAndReport(pair._2, iter, 2))
          println(tuple)
          tuple
        }
      )
    }
    ) ::: mimicIterations.map(iter => {
      //All optimization Algorithms
      val mimic = new MIMIC(200, 20, pop)
      val tuple = ("mimic", runAndReport(mimic, iter, 2))
      println(tuple)
      tuple
    }
    )
    val groupedResults = allResults.groupBy(_._1).mapValues(_.map(_._2)).mapValues(_.sortBy(_.iterations))
    ExperimentResults(groupedResults("rhc"), groupedResults("ga"), groupedResults("mimic"), groupedResults("sa"))
  }


  def runAndReport(algorithm: OptimizationAlgorithm, iterations: Int, runs: Int): AtomicResult = {
    val intermediateResult = (1 to runs).map(
      _ => {
        val startTime: Long = System.currentTimeMillis()
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
