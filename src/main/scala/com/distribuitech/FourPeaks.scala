package com.distribuitech

import java.util

import com.distribuitech.Engine._
import dist.{DiscreteDependencyTree, DiscreteUniformDistribution}
import opt.example.FourPeaksEvaluationFunction
import opt.ga.{DiscreteChangeOneMutation, GenericGeneticAlgorithmProblem, SingleCrossOver, StandardGeneticAlgorithm}
import opt.prob.{GenericProbabilisticOptimizationProblem, MIMIC}
import opt.{DiscreteChangeOneNeighbor, GenericHillClimbingProblem, RandomizedHillClimbing, SimulatedAnnealing}

object FourPeaks extends App {

  private val N = 200
  private val T = N / 10
  val ranges = new Array[Int](N)
  util.Arrays.fill(ranges, 2)
  val ef = new FourPeaksEvaluationFunction(T)
  val name = "Four Peaks"

  val iterations: List[Int] = List(100, 200, 500, 800, 1000, 2000, 5000)
  val iterationMimic = List(100, 200, 400, 600, 900, 1000, 2000)
  val iterationsSa = iterationMimic.map(_ * 30000)
  val iterationsGa = iterationsSa.map(x => x / 300)

  object generator extends OptimizationGenerator {
    override def getGenericHillClimbingProblem: GenericHillClimbingProblem = {
      val odd: DiscreteUniformDistribution = new DiscreteUniformDistribution(ranges)
      val nf: DiscreteChangeOneNeighbor = new DiscreteChangeOneNeighbor(ranges)
      new GenericHillClimbingProblem(ef, odd, nf)
    }

    override def getGenericGeneticAlgorithmProblem: GenericGeneticAlgorithmProblem = {
      val odd: DiscreteUniformDistribution = new DiscreteUniformDistribution(ranges)
      val mf: DiscreteChangeOneMutation = new DiscreteChangeOneMutation(ranges)
      val cf: SingleCrossOver = new SingleCrossOver
      new GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
    }

    override def getGenericProbabilisticOptimizationProblem: GenericProbabilisticOptimizationProblem = {
      val odd: DiscreteUniformDistribution = new DiscreteUniformDistribution(ranges)
      val df: DiscreteDependencyTree = new DiscreteDependencyTree(.1, ranges)
      new GenericProbabilisticOptimizationProblem(ef, odd, df)
    }

    override def getRHC(): RandomizedHillClimbing =
      new RandomizedHillClimbing(getGenericHillClimbingProblem)

    override def getSA(): SimulatedAnnealing = new SimulatedAnnealing(1E11, .95, getGenericHillClimbingProblem)

    override def getGA(): StandardGeneticAlgorithm = {
      import opt.ga.StandardGeneticAlgorithm
      new StandardGeneticAlgorithm(200, 100, 10, getGenericGeneticAlgorithmProblem)
    }

    override def getMIMIC(): MIMIC = {
      import opt.prob.MIMIC
      new MIMIC(200, 20, getGenericProbabilisticOptimizationProblem)
    }
  }

  plotScores(ef, name, iterations, iterations, iterations)(generator)
  plotTimes(ef, name, iterationsSa, iterationsGa, iterationMimic)(generator)

}
