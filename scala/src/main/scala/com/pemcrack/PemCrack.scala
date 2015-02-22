package com.pemcrack

import java.security.Security

import akka.actor.{ActorSystem, Props}
import java.nio.file.{Paths, Files}

import org.bouncycastle.jce.provider.BouncyCastleProvider

object PemCrack {
  def main(args: Array[String]): Unit = {
    if ( args.length != 1 )
      println("Usage: pemcrack <pem-file>")

    else if ( !Files.exists(Paths.get(args(0))) ) {
      println("PEM File '" + args(0) + "' not found")
    }

    else {
      Security.addProvider(new BouncyCastleProvider());
      val system = ActorSystem("pemcrack")
      system.actorOf(Props[PemCrackManager]) ! CrackFile(args(0))
    }
  }
}