package server

import java.io.{DataInputStream, DataOutputStream, ObjectInputStream, ObjectOutputStream}


object Main extends App {
//  while (true) {
//    val s = server.accept()
//    val oos = new ObjectOutputStream(new DataOutputStream(s.getOutputStream()))
//    val ois = new ObjectInputStream(new DataInputStream(s.getInputStream()))
//    val req = ois.readObject().asInstanceOf[Req]
//    println(s"Received request object : $req")
//    val res = reqToRes(req)
//    oos.writeObject(res)
//    oos.flush()
//    println(s"Sent response object : $res")
//    s.close()
//  }
}
