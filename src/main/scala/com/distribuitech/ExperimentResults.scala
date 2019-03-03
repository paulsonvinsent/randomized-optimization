package com.distribuitech


case class AtomicResult(iterations: Int, averageTime: Long, score: Double)

case class ExperimentResults(rhc: List[AtomicResult], ga: List[AtomicResult],
                             mimic: List[AtomicResult], sa: List[AtomicResult])
