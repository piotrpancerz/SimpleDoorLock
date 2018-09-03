package client

import server.Settings.{host, port}
import server.Response
import doorlock.DoorLockState
import client.ImagePaths.{imgLocked, imgUnlocked}

import java.io.{DataInputStream, DataOutputStream, ObjectInputStream, ObjectOutputStream}
import java.net.{InetAddress, Socket}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.image.{Image, ImageView}

object Client extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title = "Door Lock Controller"
    scene = new Scene(400, 400) {
      /* Create and initialize button */
      val button = new Button("Unlock")
      button.layoutX = 100
      button.layoutY = 100

      /* Create and initialize text field */
      val stateTextField = new TextField()
      stateTextField.layoutX = 50
      stateTextField.layoutY = 50
      stateTextField.editable = false
      stateTextField.focusTraversable = false
      stateTextField.text = "Locked"

      /* Create and initialize image */
      //FIXME img is not visible
      val imgView = new ImageView(imgLocked)
      imgView.prefHeight(20)
      imgView.prefWidth(20)
      imgView.layoutX = 50
      imgView.layoutY = 50

      /* Define actions */
      button.onAction = () => {
        if(button.text() == "Unlock"){
          unlock(button, stateTextField, imgView)
        } else {
          lock(button, stateTextField, imgView)
        }
      }

      /* Draw GUI */
      content = List(button, stateTextField, imgView)
    }

    def unlock(btn: Button, txt: TextField, img: ImageView) = {
      if(sendRequest(DoorLockState.Unlocked)){
        btn.text = "Lock"
        txt.text = "Unlocked"
        //FIXME change img path
//        img.image = Image(imgUnlocked)
      }
    }

    def lock(btn: Button, txt: TextField, img: ImageView) = {
      if(sendRequest(DoorLockState.Locked)){
        btn.text = "Unlock"
        txt.text = "Locked"
        //FIXME change img path
//        img.image = Image(imgLocked)
      }
    }

    def sendRequest(doorLockState: DoorLockState.Value): Boolean = {
      try {
        /* Creating server socket */
        val socket = new Socket(InetAddress.getByName(host), port)

        /* Request area */
        val request = Request(doorLockState)
        val objectOutputStream = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()))
        objectOutputStream.writeObject(request)
        objectOutputStream.flush()

        /* Response area */
        val objectInputStream = new ObjectInputStream(new DataInputStream(socket.getInputStream()))
        val response = objectInputStream.readObject().asInstanceOf[Response]
        socket.close()
        response.success
      } catch {
        case ex: java.net.ConnectException => {
          println("Please run Server before trying to update state")
          false
        }
      }
    }
  }
}
