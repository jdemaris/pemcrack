package com.pemcrack

import java.nio.file.{Files, Paths}

import akka.actor.{Actor, Props}

class PemCrackManager extends Actor {
   val worker = context.actorOf(Props[PemCrackWorker])

   def receive = {
     case CrackFile(file) => {
       println("Reading file contents")
       val content = new String(Files.readAllBytes(Paths.get(file)))

       println("Beginning brute force of '" + file + "' (" + content.size + " bytes)")

       worker ! CrackAttempt(content, "abcdefg")
     }
     case result: CrackResult =>
       if ( result.success ) {
         println("Crack succeeded. Password: " + result.password)
       } else {
         println("Crack failed")
       }
       System.exit(0)
   }
 }
