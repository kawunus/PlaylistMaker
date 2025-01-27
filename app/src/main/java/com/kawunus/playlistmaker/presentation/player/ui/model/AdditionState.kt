package com.kawunus.playlistmaker.presentation.player.ui.model

sealed interface AdditionState {

    data class Successful(val playlistName: String) : AdditionState

    data class Error(val playlistName: String) : AdditionState
}