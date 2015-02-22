package com.pemcrack

import java.io.StringReader
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
      val r: PEMReader = new PEMReader(reader, new DefaultPasswordFinder(attempt.passwordGuess))
      try {
        r.readObject().asInstanceOf[KeyPair]
        sender() ! CrackResult(attempt.passwordGuess, true)
      } catch {
        case e: Throwable => {
          sender() ! CrackResult(attempt.passwordGuess, false)
        }
      } finally {
        r.close()
        reader.close()
      }
    }
  }
}
