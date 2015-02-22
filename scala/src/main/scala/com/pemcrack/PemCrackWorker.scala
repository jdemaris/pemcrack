package com.pemcrack

import akka.actor.Actor

class PemCrackWorker extends Actor {
  def receive = {
    case message: String => sender() ! false
  }
}
