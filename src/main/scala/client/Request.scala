package client

import doorlock.DoorLockState

case class Request(newVal: DoorLockState.Value)
