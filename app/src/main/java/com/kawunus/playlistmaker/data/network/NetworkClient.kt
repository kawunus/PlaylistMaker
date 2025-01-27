package com.kawunus.playlistmaker.data.network

import com.kawunus.playlistmaker.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}