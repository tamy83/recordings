package com.yensontam.recordings.helper

class LetterOrDigitFileNameValidator: FileNameValidator {

  override fun isValidFileName(input: String?): Boolean {
    if (input.isNullOrBlank()) {
      return false
    }
    return input.all {
      it.isLetterOrDigit()
    }
  }

}