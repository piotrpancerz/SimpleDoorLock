package client

import doorlock.DoorLockState

import java.io.{DataInputStream, DataOutputStream, ObjectInputStream, ObjectOutputStream}
import java.net.{InetAddress, Socket}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.image.Image
import server.Settings.{host, port}


object Main extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title = "Door Lock Controller"
    scene = new Scene(400, 400) {
      /* Create and initialize button */
      val button = new Button("Unlock")
      button.layoutX = 0
      button.layoutY = 0
      /* Create and initialize text field */
      val stateTextField = new TextField()
      stateTextField.layoutX = 50
      stateTextField.layoutY = 50
      stateTextField.editable = false
      stateTextField.focusTraversable = false
      /* Create and initialize image */
//      val img = new Image()
//      img.layoutX = 50
//      img.layoutY = 50


      content = List(button, stateTextField)
    }

    def unlock(btn: Button, txt: TextField, img: Image) = {
      if(sendRequest(DoorLockState.Unlocked)){
        btn.text = "Lock"
        txt.text = "Unlocked"
        txt.setStyle("-fx-text-inner-color: green")
        // TODO add image change
      }

    }

    def lock(btn: Button, txt: TextField, img: Image) = {
      if(sendRequest(DoorLockState.Locked)){
        btn.text = "Unlock"
        txt.text = "Locked"
        txt.setStyle("-fx-text-inner-color: red")
        // TODO add image change
      }
    }

    //TODO finish send request
    def sendRequest(doorLockState: DoorLockState.Value): Boolean = {
      //TODO add exception (when server is not running)
      //FIXME change object to DoorLockState Value somehow
//      val socket = new Socket(InetAddress.getByName(host), port)
//      val oos = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))
//      val ois = new ObjectInputStream(new DataInputStream(s.getInputStream()))
      //        val req = new Req(currentValue, lowerRange, upperRange)
      //        oos.writeObject(req)
      //        oos.flush()
      //        val res = ois.readObject().asInstanceOf[Res]
      //        println(res.message)
      //        currentValue = res.currentValue
      //        Thread.sleep(1000L * timeResolution)
      //        s.close()
    }
  }
}
