package com.wavesplatform.it.activation

import com.wavesplatform.features.BlockchainFeatureStatus
import com.wavesplatform.features.api.{ActivationStatusFeature, NodeFeatureStatus}
import com.wavesplatform.it.Node
import org.scalatest.Matchers

import scala.concurrent.duration._
import scala.concurrent.Await


trait ActivationStatusRequest extends Matchers {
  def activationStatus(node: Node, height: Int, featureNum: Short, timeout: Duration): ActivationStatusFeature = {
    Await.result(node.waitForHeight(height), timeout)
    val nodeActivationInfo = Await.result(node.activationStatus, timeout)
    nodeActivationInfo.features.find(_.id == featureNum).get
  }

  def assertVotingStatus(activationStatusFeature: ActivationStatusFeature, supportedBlocks: Int, blockchainFeatureStatus: BlockchainFeatureStatus, nodeFeatureStatus: NodeFeatureStatus): Unit = {
    activationStatusFeature.supportedBlocks.get shouldBe supportedBlocks
    activationStatusFeature.blockchainStatus shouldBe blockchainFeatureStatus
    activationStatusFeature.nodeStatus shouldBe nodeFeatureStatus
  }

  def assertApprovedStatus(activationStatusFeature: ActivationStatusFeature, height: Int, nodeFeatureStatus: NodeFeatureStatus): Unit = {
    activationStatusFeature.activationHeight.get shouldBe height
    activationStatusFeature.blockchainStatus shouldBe BlockchainFeatureStatus.Approved
    activationStatusFeature.nodeStatus shouldBe nodeFeatureStatus
  }

  def assertActivatedStatus(activationStatusFeature: ActivationStatusFeature, height: Int, nodeFeatureStatus: NodeFeatureStatus): Unit = {
    activationStatusFeature.activationHeight.get shouldBe height
    activationStatusFeature.blockchainStatus shouldBe BlockchainFeatureStatus.Activated
    activationStatusFeature.nodeStatus shouldBe nodeFeatureStatus
  }
}
