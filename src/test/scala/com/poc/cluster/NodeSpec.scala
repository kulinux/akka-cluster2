package com.poc.cluster

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class NodeSpec(_system: ActorSystem)
  extends TestKit(_system)
    with Matchers
    with FlatSpecLike
  with BeforeAndAfterAll
{

  def this() = this( ActorSystem("test") )


  override def afterAll = shutdown( system )

  "An Actor" should "count" in {
    val testProbe = TestProbe()
    system.actorOf(CountPartner.props())

    Thread.sleep(10000)
  }




}
