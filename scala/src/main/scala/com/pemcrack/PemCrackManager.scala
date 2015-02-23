package com.pemcrack

import java.nio.file.{Files, Paths}
import java.text.{DecimalFormat, NumberFormat}

import akka.actor.{ActorRef, Actor, Props}

class PemCrackManager extends Actor {
  val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    "1234567890-=!@#$%^&*()_+[]\\{}|;':\",./<>? "

  var counter : BigDecimal = 0
  var generator : Option[PasswordGenerator] = None

  def receive = {
    case CrackFile(file, bruteDepth, procsPerNode) => {
      println("Reading file contents for '" + file + "'")
      val content = new String(Files.readAllBytes(Paths.get(file)))

      val formatter = NumberFormat.getInstance.asInstanceOf[DecimalFormat]
      generator = Some(new PasswordGenerator(alphabet, "", bruteDepth))
      println("Beginning brute force of '" + file + "' (" + content.size + " bytes)")
      println("with brute depth of " + bruteDepth)
      println("totalling " + formatter.format(generator.get.combinations) + " possible combinations")

      // split the working set across the alphabet and distribute it to the worker actors
      val subsets = generator.get.split
      println("Launching " + procsPerNode + " workers")
      val workerPool : List[ActorRef] = Range(0, procsPerNode)
        .toList
        .map(a => context.actorOf(Props[PemCrackWorker]))
      var iterator = 0
      for ( i <- subsets ) {
        workerPool(iterator) ! CrackAttempt(content, i)
        iterator = (iterator + 1) % workerPool.length
      }
      println(workerPool.length + " workers launched")
    }
    case result: CrackResult =>
      if ( result.success ) {
        println("Crack succeeded. Password: " + result.password)
        System.exit(0)
      } else {
        counter += 1
        val formatter = new DecimalFormat()
        if ( counter % 10000 == 0 ) {
          val percent = (counter * 100 / (generator.get.combinations))
          println(
            formatter.format(counter) + " failures of " +
              formatter.format(generator.get.combinations) + " possible" +
              "(" + formatter.format(percent) + "%)"
          )
        }
        if ( counter == generator.get.combinations ) {
          println("Crack failed. " + formatter.format(counter) + " attempted")
          System.exit(0)
        }
      }
   }
}
