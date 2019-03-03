package com.distribuitech

import java.util

import com.distribuitech.Engine.{plotScores, plotTimes}
import dist.{AbstractDistribution, DiscreteDependencyTree, DiscreteUniformDistribution}
import opt.example.TravelingSalesmanRouteEvaluationFunction
import opt.ga.StandardGeneticAlgorithm
import opt.prob.{GenericProbabilisticOptimizationProblem, MIMIC}
import opt.{RandomizedHillClimbing, SimulatedAnnealing}

import scala.util.Random

object TravellingSalesman extends App {

  val N = 100
  val points = Array.ofDim[Double](N, 2)
  points.foreach(
    x => {
      x(0) = Random.nextDouble()
      x(1) = Random.nextDouble()
    }
  )

  val name = "Travelling salesman"

  val ef = new TravelingSalesmanRouteEvaluationFunction(points)

  import dist.DiscretePermutationDistribution
  import opt.example.TravelingSalesmanCrossOver
  import opt.ga.{GenericGeneticAlgorithmProblem, SwapMutation}
  import opt.{GenericHillClimbingProblem, SwapNeighbor}

  val ranges = new Array[Int](N)
  util.Arrays.fill(ranges, N)

  val iterations: List[Int] = (1 to 8).map(_ * 40) toList
  val iterationMimic = (1 to 10).map(_ * 40) toList
  val iterationsSa = iterationMimic.map(_ * 2000)
  val iterationsGa = iterationsSa.map(x => x / 100)


  object helper extends OptimizationGenerator {
    override def getGenericHillClimbingProblem: GenericHillClimbingProblem = {
      val odd: AbstractDistribution = new DiscretePermutationDistribution(N)
      val nf = new SwapNeighbor
      new GenericHillClimbingProblem(ef, odd, nf)
    }

    override def getGenericGeneticAlgorithmProblem: GenericGeneticAlgorithmProblem = {
      val odd: AbstractDistribution = new DiscretePermutationDistribution(N)
      val mf = new SwapMutation
      val cf = new TravelingSalesmanCrossOver(ef)
      new GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
    }

    override def getGenericProbabilisticOptimizationProblem: GenericProbabilisticOptimizationProblem = {
      val odd = new DiscreteUniformDistribution(ranges)
      val df = new DiscreteDependencyTree(.1, ranges)
      new GenericProbabilisticOptimizationProblem(ef, odd, df)
    }

    override def getRHC(): RandomizedHillClimbing =
      new RandomizedHillClimbing(getGenericHillClimbingProblem)

    override def getSA(): SimulatedAnnealing = new SimulatedAnnealing(1E11, .96, getGenericHillClimbingProblem)

    override def getGA(): StandardGeneticAlgorithm = {
      import opt.ga.StandardGeneticAlgorithm
      new StandardGeneticAlgorithm(200, 150, 20, getGenericGeneticAlgorithmProblem)
    }

    override def getMIMIC(): MIMIC = {
      import opt.prob.MIMIC
      new MIMIC(200, 100, getGenericProbabilisticOptimizationProblem)
    }
  }

  plotScores(ef, name, iterations, iterations, iterations)(helper)
  plotTimes(ef, name, iterationsSa, iterationsGa, iterationMimic)(helper)

}
