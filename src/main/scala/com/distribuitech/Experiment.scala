package com.distribuitech

import com.distribuitech.FourPeaks.{ef, ranges}
import dist.{DiscreteDependencyTree, DiscreteUniformDistribution}
import opt.ga.{DiscreteChangeOneMutation, GenericGeneticAlgorithmProblem, SingleCrossOver, StandardGeneticAlgorithm}
import opt.prob.{GenericProbabilisticOptimizationProblem, MIMIC}
import opt._
import shared.FixedIterationTrainer


case class Experiment(evalFunction: EvaluationFunction,
                      iterations: List[Int],
                      runs: Int) {


  val odd = new DiscreteUniformDistribution(ranges)
  val nf = new DiscreteChangeOneNeighbor(ranges)
  val mf = new DiscreteChangeOneMutation(ranges)
  val cf = new SingleCrossOver
  val df = new DiscreteDependencyTree(.1, ranges)
  val hcp = new GenericHillClimbingProblem(ef, odd, nf)
  val gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
  val pop = new GenericProbabilisticOptimizationProblem(ef, odd, df)


  def runExperiment(): ExperimentResults = {

    val allResults: List[(String, AtomicResult)] = iterations.flatMap(iter => {
      //All optimization Algorithms
      val rhc = new RandomizedHillClimbing(hcp)
      val simulatedAnnealing = new SimulatedAnnealing(1E13, .97, hcp)
      val ga = new StandardGeneticAlgorithm(200, 20, 10, gap)
      val mimic = new MIMIC(200, 20, pop)
      List(
        ("rhc", rhc), ("ga", ga), ("mimic", mimic), ("sa", simulatedAnnealing)
      ).map(
        pair => {
          val tuple = (pair._1, runAndReport(pair._2, iter, 2))
          println(tuple)
          tuple
        }
      )
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
        val score = ef.value(algorithm.getOptimal)
        val totalTime = System.currentTimeMillis() - startTime
        AtomicResult(iterations, totalTime, score)
      }

    ).reduce(
      (x1, x2) => AtomicResult(x1.iterations, x1.averageTime + x2.averageTime, x1.score + x2.score)
    )
    intermediateResult.copy(averageTime = intermediateResult.averageTime / runs, score = intermediateResult.score / runs)
  }


}
