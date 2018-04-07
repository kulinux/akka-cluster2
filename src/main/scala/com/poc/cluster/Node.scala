package com.poc.cluster


import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

object Node {
}

class Node {
  val system = ActorSystem.create( "poc" )

}

object CountPartner {
  case class FindPartners()
  case class TocToc()
  case class IAmAlive(val ar: ActorRef)

  def props() = Props[CountPartner]

}

class CountPartner extends Actor {
  import CountPartner._

  val log = Logging(context.system, this)
  var counter = 0

  override def receive: Receive = {
    case msg: FindPartners => {
      log.info(s"find $counter, find again")
      counter = 0
    }
    case msg: TocToc => {
      sender() ! IAmAlive(self)
    }
    case IAmAlive(node) => {
      counter = counter + 1
    }
  }


  override def preStart(): Unit =  {
    val cancellable =
      context.system.scheduler.schedule(
        0 seconds,
        2 seconds,
        self,
        FindPartners() )

  }
}
