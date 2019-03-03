package com.distribuitech

import java.util

import com.distribuitech.Engine._
import dist.{DiscreteDependencyTree, DiscreteUniformDistribution}
import opt.example.FourPeaksEvaluationFunction
import opt.ga.{DiscreteChangeOneMutation, GenericGeneticAlgorithmProblem, SingleCrossOver}
import opt.prob.GenericProbabilisticOptimizationProblem
import opt.{DiscreteChangeOneNeighbor, GenericHillClimbingProblem}

object FourPeaks extends App {

  private val N = 200
  private val T = N / 10
  val ranges = new Array[Int](N)
  util.Arrays.fill(ranges, 2)
  val ef = new FourPeaksEvaluationFunction(T)
  val name = "Four Peaks"


  val odd: DiscreteUniformDistribution = new DiscreteUniformDistribution(ranges)
  val nf: DiscreteChangeOneNeighbor = new DiscreteChangeOneNeighbor(ranges)
  val mf: DiscreteChangeOneMutation = new DiscreteChangeOneMutation(ranges)
  val cf: SingleCrossOver = new SingleCrossOver
  val df: DiscreteDependencyTree = new DiscreteDependencyTree(.1, ranges)
  implicit val hcp: GenericHillClimbingProblem = new GenericHillClimbingProblem(ef, odd, nf)
  implicit val gap: GenericGeneticAlgorithmProblem = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
  implicit val pop: GenericProbabilisticOptimizationProblem = new GenericProbabilisticOptimizationProblem(ef, odd, df)
  val iterations: List[Int] = List(100, 200, 500, 800, 1000, 2000, 5000)
  val iterationMimic = List(100, 200, 400, 600, 900, 1000, 2000)
  val iterationsSa = iterationMimic.map(_ * 30000)
  val iterationsGa = iterationsSa.map(x => x / 300)
  plotScores(ef, name, iterations, iterations, iterations)
  plotTimes(ef, name, iterationsSa, iterationsGa, iterationMimic)

}
