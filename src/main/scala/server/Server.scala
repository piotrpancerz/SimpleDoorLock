package server

import server.Settings.port
import java.io.{DataInputStream, DataOutputStream, ObjectInputStream, ObjectOutputStream}
import java.net.ServerSocket

import client.Request
import doorlock.{DoorLock, DoorLockState}

object Server extends App {
  /* Create server */
  val server = new ServerSocket(port)

  /* Create DoorLock instance */
  val doorLock = DoorLock()

  /* Run server */
  println(s"Server running on port : $port...")
  while (true) {
    val socket = server.accept()

    /* Request area */
    val objectInputStream = new ObjectInputStream(new DataInputStream(socket.getInputStream()))
    val request = objectInputStream.readObject().asInstanceOf[Request]
    val requestedValue = request.newVal
    println(s"Received request to set door lock state to : $requestedValue")

    /* Setting door lock state */
    //FIXME ERROR!
    println(requestedValue)
//    doorLock(requestedValue)

    /* Measuring success */
    val success = requestedValue match {
      case DoorLockState.Locked => if(doorLock() == DoorLockState.Locked) true else false
      case DoorLockState.Unlocked => if(doorLock() == DoorLockState.Unlocked) true else false
    }

    /* Response area */
    val objectOutputStream = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))
    val comment = if(success) {
      "Successfully updated value"
    } else {
      "Updating value was unsuccessful"
    }
    val res = Response(success,comment)
    objectOutputStream.writeObject(res)
    objectOutputStream.flush()
    println("Sent response with comment \"" + comment + "\"")
    socket.close()
  }
}
