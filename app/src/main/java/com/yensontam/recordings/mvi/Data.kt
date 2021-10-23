package com.yensontam.recordings.mvi

sealed class Data<T> {
  data class Success<T>(val value: T) : Data<T>()
  data class Failure<T>(val throwable: Throwable) : Data<T>()
}