package com.distribuitech

import java.io.File
import java.util

import func.nn.feedfwd.{FeedForwardNetwork, FeedForwardNeuralNetworkFactory}
import opt.OptimizationAlgorithm
import opt.example.NeuralNetworkOptimizationProblem
import shared._

object NNRandomizedOptimization {


  val instances: Array[Instance] = Helper.readWineQalityData(new File(".").getAbsolutePath + "/data/wine_quality.csv", 1599, 10)
  val trainData: Array[Instance] = util.Arrays.copyOfRange(instances, 0, (instances.length * 0.75) toInt)
  val testData: Array[Instance] = util.Arrays.copyOfRange(instances, (instances.length * 0.75) toInt, instances.size)

  val set = new DataSet(trainData)
  set.setDescription(new DataSetDescription(set))


  def getOptimizationProblem(): (FeedForwardNetwork, NeuralNetworkOptimizationProblem) = {
    val factory = new FeedForwardNeuralNetworkFactory
    val network = factory.createClassificationNetwork(Array[Int](11, 25, 10))
    val measure = new SumOfSquaresError
    val nno = new NeuralNetworkOptimizationProblem(set, network, measure)
    (network, nno)
  }

  def trainAndReportTestAndValidationAccracy(algo: OptimizationAlgorithm,
                                             iterations: Int = 3000,
                                             network: FeedForwardNetwork) = {
    val fit: FixedIterationTrainer = new FixedIterationTrainer(algo, iterations)
    fit.train
    val rhcOpt: Instance = algo.getOptimal
    network.setWeights(rhcOpt.getData)
    var correct = 0
    var incorrect = 0
    var j = 0
    while ( {
      j < testData.length
    }) {
      network.setInputValues(testData(j).getData)
      network.run()
      val predicted = testData(j).getLabel.getData.argMax
      val actual = network.getOutputValues.argMax
      if (predicted == actual) correct += 1
      else incorrect += 1

      {
        j += 1
        j - 1
      }
    }
    val validationAccuracy: Double = correct.toDouble / testData.length
    correct = 0
    incorrect = 0
    j = 0
    while ( {
      j < trainData.length
    }) {
      network.setInputValues(trainData(j).getData)
      network.run()
      val predicted = trainData(j).getLabel.getData.argMax
      val actual = network.getOutputValues.argMax
      if (predicted == actual) correct += 1
      else incorrect += 1

      {
        j += 1
        j - 1
      }
    }

    val testAccuracy: Double = correct.toDouble / trainData.length
    (testAccuracy, validationAccuracy)
  }


}
