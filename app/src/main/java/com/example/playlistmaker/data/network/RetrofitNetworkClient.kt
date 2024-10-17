package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import java.io.IOException
import java.net.UnknownHostException

class RetrofitNetworkClient(private val iTunesService: ITunesApiService) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            try {

                val resp = iTunesService.search(dto.expression).execute()

                val body = resp.body() ?: Response()

                return body.apply {
                    resultCode = resp.code()
                }
            } catch (e: UnknownHostException) {
                return Response().apply { resultCode = 400 }
            } catch (e: IOException) {
                return Response().apply { resultCode = 400 }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }


}