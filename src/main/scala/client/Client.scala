package client

import server.Settings.{host, port}
import server.Response
import doorlock.DoorLockState
import client.ImageObjects.{imgLocked, imgUnlocked}
import java.io.{DataInputStream, DataOutputStream, ObjectInputStream, ObjectOutputStream}
import java.net.{InetAddress, Socket}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.Orientation
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}

object Client extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title = "Door Lock Controller"
    scene = new Scene(300, 300) {
      /* Create and initialize button */
      val button = new Button("Unlock")
      button.layoutX = 100
      button.layoutY = 240
      button.prefWidth = 100

      /* Create and initialize text field */
      val stateTextField = new TextField()
      stateTextField.layoutX = 50
      stateTextField.layoutY = 190
      stateTextField.prefWidth = 200
      stateTextField.editable = false
      stateTextField.focusTraversable = false
      stateTextField.text = "Locked"

      /* Create and initialize image */
      val imgView = new ImageView(imgLocked)
      imgView.layoutX = (300-128)/2
      imgView.layoutY = 40

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
        img.image_=(imgUnlocked)
      }
    }

    def lock(btn: Button, txt: TextField, img: ImageView) = {
      if(sendRequest(DoorLockState.Locked)){
        btn.text = "Unlock"
        txt.text = "Locked"
        img.image_=(imgLocked)
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
