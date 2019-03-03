package com.distribuitech

import opt.{GenericHillClimbingProblem, RandomizedHillClimbing, SimulatedAnnealing}
import opt.ga.{GenericGeneticAlgorithmProblem, StandardGeneticAlgorithm}
import opt.prob.{GenericProbabilisticOptimizationProblem, MIMIC}

trait OptimizationGenerator {

  def getGenericHillClimbingProblem: GenericHillClimbingProblem

  def getGenericGeneticAlgorithmProblem: GenericGeneticAlgorithmProblem

  def getGenericProbabilisticOptimizationProblem: GenericProbabilisticOptimizationProblem


  def getRHC(): RandomizedHillClimbing

  def getSA(): SimulatedAnnealing

  def getGA(): StandardGeneticAlgorithm

  def getMIMIC(): MIMIC

}
