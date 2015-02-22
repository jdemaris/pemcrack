package com.pemcrack

import akka.actor.{Props, Actor}

class PemCrackManager extends Actor {
   val worker = context.actorOf(Props[PemCrackWorker])

   def receive = {
     case CrackFile => worker ! "test key"
     case success: Boolean =>
       if ( success )
         println("Crack succeeded")
       else
         println("Crack failed")
   }
 }
