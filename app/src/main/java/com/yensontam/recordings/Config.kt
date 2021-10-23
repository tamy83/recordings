package com.yensontam.recordings

class Config {
  lateinit var recordingsDirectory: String

  companion object {
    @JvmStatic
    val instance = Config()
  }
}
