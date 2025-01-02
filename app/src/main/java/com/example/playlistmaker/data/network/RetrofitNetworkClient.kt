package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException

class RetrofitNetworkClient(private val iTunesService: ITunesApiService) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {


            return withContext(Dispatchers.IO) {
                try {
                    val response = iTunesService.search(dto.expression)
                    response.apply { resultCode = 200 }
                } catch (e: UnknownHostException) {
                    Response().apply { resultCode = 400 }
                } catch (e: IOException) {
                    Response().apply { resultCode = 400 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

}