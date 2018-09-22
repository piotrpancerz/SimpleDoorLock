package client

import server.Settings.{host, port}
import server.Response
import doorlock.DoorLockState
import client.ImageObjects.{imgLocked, imgUnlocked}
import java.io.{DataInputStream, DataOutputStream, ObjectInputStream, ObjectOutputStream}
import java.net.{InetAddress, Socket}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}

object Client extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title = "Door Lock Controller"
    scene = new Scene(400, 350) {
      /* Create and initialize button */
      val button = new Button("Unlock")
      button.layoutX = 150
      button.layoutY = 240
      button.prefWidth = 100

      /* Create and initialize text field */
      val stateTextField = new TextField()
      stateTextField.layoutX = 125
      stateTextField.layoutY = 190
      stateTextField.prefWidth = 150
      stateTextField.editable = false
      stateTextField.focusTraversable = false
      stateTextField.text = "Locked"

      /* Create and initialize image */
      val imgView = new ImageView(imgLocked)
      imgView.layoutX = (400-128)/2
      imgView.layoutY = 40

      /* Create and initialize action text field */
      val resultTextField = new TextField()
      resultTextField.text = ""
      resultTextField.editable = false
      resultTextField.layoutX = 50
      resultTextField.layoutY = 300
      resultTextField.prefWidth = 300

      /* Define actions */
      button.onAction = () => {
        if(button.text() == "Unlock"){
          unlock(button, stateTextField, imgView, resultTextField)
        } else {
          lock(button, stateTextField, imgView, resultTextField)
        }
      }

      /* Draw GUI */
      content = List(button, stateTextField, imgView, resultTextField)
    }

    def unlock(btn: Button, txt1: TextField, img: ImageView, txt2: TextField) = {
      val response = sendRequest(DoorLockState.Unlocked)
      if(response._1){
        btn.text = "Lock"
        txt1.text = "Unlocked"
        img.image_=(imgUnlocked)
      }
      txt2.text = response._2
    }

    def lock(btn: Button, txt1: TextField, img: ImageView, txt2: TextField) = {
      val response = sendRequest(DoorLockState.Locked)
      if(response._1){
        btn.text = "Unlock"
        txt1.text = "Locked"
        img.image_=(imgLocked)
      }
      txt2.text = response._2
    }

    def sendRequest(doorLockState: DoorLockState.Value): (Boolean, String) = {
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
        response.success -> response.comment
      } catch {
        case ex: java.net.ConnectException => {
          false -> "Please run Server before trying to update"
        }
      }
    }
  }
}
