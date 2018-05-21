package com.poc.cluster

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Publish
import com.typesafe.config.ConfigFactory
import kamon.Kamon
import kamon.prometheus.PrometheusReporter
import kamon.zipkin.ZipkinReporter
import org.jboss.netty.channel.ChannelException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Node {
}

class Node {

  val system = createSystem()

  //val cluster = Cluster(system)
  //cluster.join(cluster.selfAddress)

  1 to 10 foreach { i =>
    val countParter = system.actorOf(CountPartner.props(), "CountPartner" + i)
  }

  def createSystem(): ActorSystem = {

    try {
      val config = ConfigFactory.load()
      ActorSystem.create(  config.getString("clustering.cluster.name"), config )
    } catch {
      case be: ChannelException => {
        val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=0").
          withFallback(ConfigFactory.load())
        ActorSystem.create(  config.getString("clustering.cluster.name"), config )
      }
    }
  }
}

object Main extends App {
  Kamon.addReporter(new PrometheusReporter())
  Kamon.addReporter(new ZipkinReporter())

  val node = new Node()
}

object CountPartner {
  case class FindPartners()
  case class TocToc(snd: ActorRef)
  case class IAmAlive(val ar: ActorRef)

  def props() = Props[CountPartner]

}

class CountPartner extends Actor with ActorLogging {
  import CountPartner._
  import akka.cluster.pubsub.DistributedPubSubMediator.{ Subscribe, SubscribeAck }

  val mediator = DistributedPubSub(context.system).mediator
  mediator ! Subscribe("content", self)

  var counter = 0
  var globalCounter = 0


  override def receive: Receive = {
    case msg: FindPartners => {
      globalCounter = globalCounter + 1
      if(globalCounter % 10 == 0 ) {
        1 to 100 foreach { _ => mediator ! Publish("content", TocToc(self)) }
      }
      log.info(s"find $counter, find again")
      counter = 0
      mediator ! Publish("content", TocToc(self))
    }
    case TocToc(snd) => {
      log.info("TocToc, received")
      snd ! IAmAlive(self)
    }
    case IAmAlive(node) => {
      counter = counter + 1
    }
    case SubscribeAck(Subscribe("content", None, `self`)) ⇒
      log.info("subscribing")
  }

  override def preStart(): Unit =  {
    val cancellable =
      context.system.scheduler.schedule(
        0 seconds,
        1 seconds,
        self,
        FindPartners() )

  }
}
