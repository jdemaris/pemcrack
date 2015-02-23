package com.pemcrack

import java.security.Security

import akka.actor.{ActorSystem, Props}
import java.nio.file.{Paths, Files}

import org.bouncycastle.jce.provider.BouncyCastleProvider

object PemCrack {
  def main(args: Array[String]): Unit = {
    if ( args.length != 3 )
      println("Usage: pemcrack <pem-file> <max-pass-length> <max-processes-per-node>")

    else if ( !Files.exists(Paths.get(args(0))) ) {
      println("PEM File '" + args(0) + "' not found")
    }

    else if ( getInt(args(1)) < 0 ) {
      println("Max Password Length must be >= 0")
    }

    else if ( getInt(args(2)) < 0 ) {
      println("Max Processes Per Node must be >= 0")
    }

    else {
      Security.addProvider(new BouncyCastleProvider());
      val system = ActorSystem("pemcrack")
      system.actorOf(Props[PemCrackManager]) ! CrackFile(
        args(0), getInt(args(1)), getInt(args(2))
      )
    }
  }

  def getInt(string: String): Int = try {
    string.toInt
  } catch {
    case _ : Throwable => -1
  }
}