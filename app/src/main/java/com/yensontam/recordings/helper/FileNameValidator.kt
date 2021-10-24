package com.yensontam.recordings.helper

interface FileNameValidator {
  fun isValidFileName(input: String?): Boolean
}