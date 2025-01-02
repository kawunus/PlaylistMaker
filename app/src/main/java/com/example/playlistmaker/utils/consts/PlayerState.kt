package com.example.playlistmaker.utils.consts

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val buttonText: String) {

    class Default : PlayerState(false, "PLAY")

    class Prepared : PlayerState(true, "PLAY")

    class Playing : PlayerState(true, "PAUSE")

    class Paused : PlayerState(true, "PLAY")
}