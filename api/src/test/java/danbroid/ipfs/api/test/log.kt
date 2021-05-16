package danbroid.ipfs.api.test

import danbroid.logging.DBLog
import danbroid.logging.LogConfig
import danbroid.logging.StdOutLog

object log : DBLog by StdOutLog {
  init {
    LogConfig.apply {
      COLOURED = true
      DEBUG = true
      GET_LOG = { StdOutLog }
    }
  }
}