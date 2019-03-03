package com.distribuitech

import com.distribuitech.Engine.{plotScores, plotTimes}
import dist.{DiscreteDependencyTree, DiscretePermutationDistribution}
import opt._
import opt.ga._
import opt.prob.{GenericProbabilisticOptimizationProblem, MIMIC}

import scala.util.Random


object MaxKColouring extends App {

  val N = 200
  val L = 8
  val K = 10
  val random = new Random(N * L)

  val name = "Max K Colouring"

  val vertices: Array[ga.Vertex] = Helper.getGraph(N, L)

  val ef = new MaxKColorFitnessFunction(vertices)

  val iterations: List[Int] = (1 to 39).toList ::: (1 to 5).map(_ * 40).toList
  val iterationMimic = (1 to 9).toList ::: ((1 to 10) map (_ * 10) toList)
  val iterationsSa = iterationMimic.map(_ * 50)

  object generator extends OptimizationGenerator {


    override def getGenericHillClimbingProblem: GenericHillClimbingProblem = {

      val ef = new MaxKColorFitnessFunction(vertices)
      val odd = new DiscretePermutationDistribution(K)
      val nf = new SwapNeighbor
      new GenericHillClimbingProblem(ef, odd, nf)
    }

    override def getGenericGeneticAlgorithmProblem: GenericGeneticAlgorithmProblem = {
      val ef = new MaxKColorFitnessFunction(vertices)
      val odd = new DiscretePermutationDistribution(K)
      val mf = new SwapMutation
      val cf = new SingleCrossOver
      new GenericGeneticAlgorithmProblem(ef, odd, mf, cf)
    }

    override def getGenericProbabilisticOptimizationProblem: GenericProbabilisticOptimizationProblem = {
      val ef = new MaxKColorFitnessFunction(vertices)
      val odd = new DiscretePermutationDistribution(K)
      val df = new DiscreteDependencyTree(.1)
      new GenericProbabilisticOptimizationProblem(ef, odd, df)
    }

    override def getRHC(): RandomizedHillClimbing =
      new RandomizedHillClimbing(getGenericHillClimbingProblem)

    override def getSA(): SimulatedAnnealing = new SimulatedAnnealing(1E11, .96, getGenericHillClimbingProblem)

    override def getGA(): StandardGeneticAlgorithm = {
      import opt.ga.StandardGeneticAlgorithm
      new StandardGeneticAlgorithm(200, 10, 60, getGenericGeneticAlgorithmProblem)
    }

    override def getMIMIC(): MIMIC = {
      import opt.prob.MIMIC
      new MIMIC(200, 100, getGenericProbabilisticOptimizationProblem)
    }
  }


  plotScores(ef, name, iterations, iterations, iterations)(generator)
  plotTimes(ef, name, iterationsSa, iterationMimic, iterationMimic)(generator)

}
