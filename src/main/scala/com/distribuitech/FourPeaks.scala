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
  val iterations1: List[Int] = List(10, 50, 500, 1000, 2000, 6000, 12000, 16000, 25000, 40000)
  val mimicIterations1: List[Int] = List(100, 200, 500, 800, 1000, 2000, 5000)
  val iterations2: List[Int] = List(10, 50, 500, 1000, 2000, 3000, 4000, 6000, 8000, 12000,
    16000, 25000, 30000, 40000, 60000, 100000, 200000)
  val mimicIterations2: List[Int] = List(100, 200, 500, 800, 1000, 2000, 5000)
  plotScores(ef, name, iterations1, mimicIterations1)
  plotTimes(ef, name, iterations2, mimicIterations2)

}
