package doorlock

case class DoorLock() {
  private var state: DoorLockState.Value = DoorLockState.Locked
  def apply(): DoorLockState.Value = state
  def update(newState: DoorLockState.Value): Unit = state = newState
}
