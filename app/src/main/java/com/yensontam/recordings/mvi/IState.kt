package com.yensontam.recordings.mvi

interface IState {
  fun consumeAction(action: IAction) : IState
}