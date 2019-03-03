package com.distribuitech

import com.distribuitech.Engine.{plotScores, plotTimes}
import opt.ga
import dist.{DiscreteDependencyTree, DiscretePermutationDistribution}
import opt.{GenericHillClimbingProblem, SwapNeighbor}
import opt.ga.{GenericGeneticAlgorithmProblem, MaxKColorFitnessFunction, SingleCrossOver, SwapMutation}
import opt.prob.GenericProbabilisticOptimizationProblem
import opt.ga.MaxKColorFitnessFunction
import scala.util.Random


object MaxKColouring extends App {

  val N = 50
  val L = 4
  val K = 8
  val random = new Random(N * L)

  val name = "Max K Colouring"

  val vertices: Array[ga.Vertex] = Helper.getGraph(N, L)

  val ef = new MaxKColorFitnessFunction(vertices)
  val odd = new DiscretePermutationDistribution(K)
  val nf = new SwapNeighbor
  val mf = new SwapMutation
  val cf = new SingleCrossOver
  implicit val hcp: GenericHillClimbingProblem = new GenericHillClimbingProblem(ef, odd, nf)
  implicit val gap: GenericGeneticAlgorithmProblem = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
  val df = new DiscreteDependencyTree(.1)
  implicit val pop: GenericProbabilisticOptimizationProblem = new GenericProbabilisticOptimizationProblem(ef, odd, df)

  val iterations: List[Int] = List(100, 200, 500, 800, 1000, 2000, 5000)
  val iterationMimic = List(100, 200, 400, 600, 900, 1000, 2000)
  val iterationsSa = iterationMimic.map(_ * 30000)
  val iterationsGa = iterationsSa.map(x => x / 300)
  plotScores(ef, name, iterations, iterations, iterations)
  plotTimes(ef, name, iterationsSa, iterationsGa, iterationMimic)

}
