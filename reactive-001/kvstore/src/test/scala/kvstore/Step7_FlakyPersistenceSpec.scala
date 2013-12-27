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

class Step7_FlakyPersistenceSpec extends TestKit(ActorSystem("Step7FlakyPersistenceSpec"))
  with FunSuite
  with BeforeAndAfterAll
  with ShouldMatchers
  with ImplicitSender
  with Tools {

  override def afterAll(): Unit = {
    system.shutdown()
  }

  test("case1: Primary works with flaky persistence") {
    val arbiter = TestProbe()
    val primary = system.actorOf(Replica.props(arbiter.ref, Persistence.props(flaky = true)), "case1-primary")
    val client = session(primary)

    arbiter.expectMsg(Join)
    arbiter.send(primary, JoinedPrimary)

    val setId1 = client.set("foo", "bar1")
    client.waitAck(setId1)
    val setId2 = client.set("foo", "bar2")
    client.waitAck(setId2)
    val setId3 = client.set("foo", "bar3")
    client.waitAck(setId3)
  }
  
  test("case2: Primary and secondary works with flaky persistence") {
    val arbiter = TestProbe()
    val primary = system.actorOf(Replica.props(arbiter.ref, Persistence.props(flaky = true)), "case2-primary")
    val client = session(primary)
    arbiter.expectMsg(Join)
    arbiter.send(primary, JoinedPrimary)
    
    val secondary = system.actorOf(Replica.props(arbiter.ref, Persistence.props(flaky = true)), "case2-secondary")
    val clientSecondary = session(secondary)
    arbiter.expectMsg(Join)
    arbiter.send(secondary, JoinedSecondary)
    arbiter.send(primary, Replicas(Set(primary, secondary)))

    val setId1 = client.set("foo", "bar1")
    client.waitAck(setId1)
    val setId2 = client.set("foo", "bar2")
    client.waitAck(setId2)
    val setId3 = client.set("foo", "bar3")
    client.waitAck(setId3)
    
    client.getAndVerify("foo")
    clientSecondary.get("foo") should be (Some("bar3"))
  }

}