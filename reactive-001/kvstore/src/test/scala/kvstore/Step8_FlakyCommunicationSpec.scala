package kvstore

import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import akka.actor.ActorSystem
import akka.testkit.TestProbe
import Arbiter._
import Replicator._

class Step8_FlakyCommunicationSpec extends TestKit(ActorSystem("Step7FlakyCommunicationSpec"))
  with FunSuite
  with BeforeAndAfterAll
  with ShouldMatchers
  with ImplicitSender
  with Tools {

  override def afterAll(): Unit = {
    system.shutdown()
  }
  
  test("case1: Primary and secondary have communication problems") {
    val arbiter = TestProbe()
    val primary = system.actorOf(Replica.props(arbiter.ref, Persistence.props(flaky = false)), "case1-primary")
    val client = session(primary)
    arbiter.expectMsg(Join)
    arbiter.send(primary, JoinedPrimary)
    
    val secondary = system.actorOf(Replica.props(arbiter.ref, Persistence.props(flaky = false)), "case1-secondary")
    val secondaryWrapper = system.actorOf(flakyProps(secondary), "case1-secondary-wrapper")
    val clientSecondary = session(secondary)
    arbiter.expectMsg(Join)
    arbiter.send(secondary, JoinedSecondary)
    arbiter.send(primary, Replicas(Set(primary, secondaryWrapper)))

    val setId = client.set("foo", "bar")
    client.waitAck(setId)
    
    client.getAndVerify("foo")
    clientSecondary.get("foo") should be (Some("bar"))
  }
  
  test("case2: guarantee order with communication problems") {
    val arbiter = TestProbe()
    val primary = system.actorOf(Replica.props(arbiter.ref, Persistence.props(flaky = false)), "case2-primary")
    val client = session(primary)
    arbiter.expectMsg(Join)
    arbiter.send(primary, JoinedPrimary)
    
    val secondary = system.actorOf(Replica.props(arbiter.ref, Persistence.props(flaky = false)), "case2-secondary")
    val secondaryWrapper = system.actorOf(flakyProps(secondary), "case2-secondary-wrapper")
    val clientSecondary = session(secondary)
    arbiter.expectMsg(Join)
    arbiter.send(secondary, JoinedSecondary)
    arbiter.send(primary, Replicas(Set(primary, secondaryWrapper)))

    val setId1 = client.set("foo", "bar1")
    val setId2 = client.set("foo", "bar2")
    val setId3 = client.set("foo", "bar3")
    client.waitAck(setId1)
    client.waitAck(setId2)
    client.waitAck(setId3)
    
    client.getAndVerify("foo")
    clientSecondary.get("foo") should be (Some("bar3"))
  }
}