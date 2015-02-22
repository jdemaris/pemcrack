package com.pemcrack

import java.io.FileReader
import java.security.KeyPair

import akka.actor.Actor
import org.bouncycastle.openssl.{PEMReader, PasswordFinder}

class DefaultPasswordFinder(password: String) extends PasswordFinder {
  override def getPassword: Array[Char] = password.toCharArray
}

class PemCrackWorker extends Actor {
  def receive = {
    case attempt: CrackAttempt => {
      val fileReader: FileReader = new FileReader(attempt.pem)
      val r: PEMReader = new PEMReader(fileReader, new DefaultPasswordFinder(attempt.passwordGuess))
      try {
        val result = r.readObject().asInstanceOf[KeyPair]
        sender() ! CrackResult(attempt.passwordGuess, true)
      } catch {
        case e: Throwable => {
          sender() ! CrackResult(attempt.passwordGuess, false)
        }
      } finally {
        r.close()
        fileReader.close()
      }
    }
  }
}
