package com.pemcrack

import java.io.{Reader, StringReader}
import java.security.KeyPair

import akka.actor.Actor
import org.bouncycastle.openssl.{PEMReader, PasswordFinder}

class DefaultPasswordFinder(password: String) extends PasswordFinder {
  override def getPassword: Array[Char] = password.toCharArray
}

class PemCrackWorker extends Actor {
  def receive = {
    case attempt: CrackAttempt => {
      val reader = new StringReader(attempt.pem)
      for ( guess <- attempt.passwordGuess ) {
        if ( this.attempt(reader, guess) )
          sender() ! CrackResult(guess, true)
        else
          sender() ! CrackResult(guess, false)
        reader.close()
      }
    }
  }

  def attempt(reader: Reader, guess: String) : Boolean = {
    val r: PEMReader = new PEMReader(reader, new DefaultPasswordFinder(guess))
    try {
      r.readObject().asInstanceOf[KeyPair]
      true
    } catch {
      case e: Throwable => false
    } finally {
      r.close()
    }
  }
}
