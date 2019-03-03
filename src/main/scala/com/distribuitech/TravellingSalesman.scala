package com.distribuitech

import java.util

import com.distribuitech.Engine.{plotScores, plotTimes}
import dist.{AbstractDistribution, DiscreteDependencyTree, DiscreteUniformDistribution}
import opt.example.TravelingSalesmanRouteEvaluationFunction
import opt.prob.GenericProbabilisticOptimizationProblem

import scala.util.Random

object TravellingSalesman extends App {

  val N = 50
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

  var odd: AbstractDistribution = new DiscretePermutationDistribution(N)
  val nf = new SwapNeighbor
  val mf = new SwapMutation
  val cf = new TravelingSalesmanCrossOver(ef)
  implicit val hcp: GenericHillClimbingProblem = new GenericHillClimbingProblem(ef, odd, nf)
  implicit val gap: GenericGeneticAlgorithmProblem = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf)

  val ranges = new Array[Int](N)
  util.Arrays.fill(ranges, 2)

  odd = new DiscreteUniformDistribution(ranges)
  val df = new DiscreteDependencyTree(.1, ranges)
  implicit val pop: GenericProbabilisticOptimizationProblem = new GenericProbabilisticOptimizationProblem(ef, odd, df)

  val iterations: List[Int] = List(10, 50, 500, 1000, 2000, 6000, 12000, 16000, 25000, 40000)
  val iterationMimic = List(100, 200, 400, 600, 900, 1500, 2000, 2500, 3000, 3500)
  val iterationsSa = iterationMimic.map(_ * 30000)
  val iterationsGa = iterationsSa.map(x => x / 300)
  plotScores(ef, name, iterations, iterations, iterations)
  plotTimes(ef, name, iterationsSa, iterationsGa, iterationMimic)

}
