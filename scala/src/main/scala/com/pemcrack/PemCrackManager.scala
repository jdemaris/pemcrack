package com.pemcrack

import akka.actor.{Actor, Props}

class PemCrackManager extends Actor {
   val worker = context.actorOf(Props[PemCrackWorker])

   def receive = {
     case CrackFile(file) => {
       println("Beginning brute force of '" + file + "'")
       worker ! CrackAttempt(file, "abcdefg")
     }
     case result: CrackResult =>
       if ( result.success )
         println("Crack succeeded. Password: " + result.password)
       else
         println("Crack failed")
       System.exit(0)
   }
 }
