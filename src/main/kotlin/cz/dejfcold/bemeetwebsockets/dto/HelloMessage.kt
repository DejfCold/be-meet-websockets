package cz.dejfcold.bemeetwebsockets.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class HelloMessage(@JsonProperty("name") val name: String)