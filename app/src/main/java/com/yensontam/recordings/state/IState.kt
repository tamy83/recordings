package com.yensontam.recordings.state

interface IState {
  fun consumeAction(action: IAction) : IState
}