package com.pemcrack

import akka.actor.{ActorSystem, Props}
import java.nio.file.{Paths, Files}

object PemCrack {
  def main(args: Array[String]): Unit = {
    if ( args.length != 1 )
      println("Usage: pemcrack <pem-file>")

    else if ( !Files.exists(Paths.get(args(0))) ) {
      println("PEM File '" + args(0) + "' not found")
    }

    else {
      println("Spooling up to crack file: " + args(0))
      val system = ActorSystem("pemcrack")
      system.actorOf(Props[PemCrackManager]) ! CrackFile(args(0))
    }
  }
}