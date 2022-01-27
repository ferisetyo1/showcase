package com.llc.thelegionpt.fitur.detailriwayat

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.thelegionpt.data.network.response.TransaksiPrivate
import com.llc.thelegionpt.data.repository.LegionRepository
import com.llc.thelegionpt.utils.Resource
import com.llc.thelegionpt.utils.toDateorNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DetailPemesananViewModel @Inject constructor(private val repository: LegionRepository) :
    ViewModel() {
    private var countDownTimer: CountDownTimer? = null
    private var countDownTimerBerlangsung: CountDownTimer? = null
    val dataTransaksi = mutableStateOf<TransaksiPrivate?>(null)
    val isLoading = mutableStateOf(false)
    val isSuccess = mutableStateOf(false)
    val message = mutableStateOf("")
    val timerakanberlangsung = mutableStateOf(false)
    val timerberlangsung = mutableStateOf(false)
    val second = mutableStateOf(0L)
    val minute = mutableStateOf(0L)
    val hour = mutableStateOf(0L)
    val progress = mutableStateOf(0f)

    fun showTransaksiPrivate(id: String) {
        viewModelScope.launch {
            isLoading.value = true
            message.value = ""
            val result = repository.showTransaksiPrivate(id)
            when (result) {
                is Resource.Success -> {
                    isLoading.value = false
                    isSuccess.value = true
                    dataTransaksi.value = result.data?.data
                    message.value = result.message.orEmpty()
                    startTimer()
                }
                is Resource.Error -> {
                    isLoading.value = false
                    message.value = result.message.orEmpty()
                }
            }
        }
    }

    fun terimaTransaksiPrivate(id: String) {
        viewModelScope.launch {
            isLoading.value = true
            message.value = ""
            val result = repository.terimaTransaksiPrivate(id)
            when (result) {
                is Resource.Success -> {
                    isLoading.value = false
                    isSuccess.value = true
                    dataTransaksi.value = result.data?.data
                    message.value = result.message.orEmpty()
                    startTimer()
                }
                is Resource.Error -> {
                    isLoading.value = false
                    message.value = result.message.orEmpty()
                }
            }
        }
    }

    fun tolakTransaksiPrivate(id: String) {
        viewModelScope.launch {
            isLoading.value = true
            message.value = ""
            val result = repository.tolakTransaksiPrivate(id)
            when (result) {
                is Resource.Success -> {
                    isLoading.value = false
                    isSuccess.value = true
                    dataTransaksi.value = result.data?.data
                    message.value = result.message.orEmpty()
                }
                is Resource.Error -> {
                    isLoading.value = false
                    message.value = result.message.orEmpty()
                }
            }
        }
    }

    fun inputMeetUrl(id: String, url: String) {
        viewModelScope.launch {
            isLoading.value = true
            message.value = ""
            val result = repository.inputMeetUrlTransaksiPrivate(id, url)
            when (result) {
                is Resource.Success -> {
                    isLoading.value = false
                    isSuccess.value = true
                    dataTransaksi.value = result.data?.data
                    message.value = result.message.orEmpty()
                    startTimer()
                }
                is Resource.Error -> {
                    isLoading.value = false
                    message.value = result.message.orEmpty()
                }
            }
        }
    }

    fun startTimer() {
        countDownTimer?.cancel()
        val diff = dataTransaksi.value?.getDiffMilis()?:0
        if (diff > 0) {
            timerakanberlangsung.value = true
            countDownTimer = object : CountDownTimer(diff, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var seconds = millisUntilFinished / 1000
                    var minutes = seconds / 60
                    val hours = minutes / 60
                    if (hours > 0) minutes %= 60
                    minute.value = minutes
                    hour.value = hours
                    seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    second.value = seconds
                }

                override fun onFinish() {
                    timerakanberlangsung.value = false
                    startTimer()
                }

            }
            countDownTimer?.start()
        }

        countDownTimerBerlangsung?.cancel()
        val diffBerlangsung = dataTransaksi.value?.getDiffMilisBerlangsung()?:0
        if (diffBerlangsung > 0) {
            timerberlangsung.value = true
            println("set" + timerberlangsung.value)
            countDownTimerBerlangsung = object : CountDownTimer(diffBerlangsung, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var seconds = millisUntilFinished / 1000
                    var minutes = seconds / 60
                    val hours = minutes / 60
                    if (hours > 0) minutes %= 60
                    minute.value = minutes
                    hour.value = hours
                    seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    second.value = seconds
                    progress.value=dataTransaksi.value?.getProgress()?:0f
                }

                override fun onFinish() {
                    timerberlangsung.value = false
                    println("onFinish" + timerberlangsung.value)
                }

            }
            countDownTimerBerlangsung?.start()
        }
    }

    fun reset() {
        timerakanberlangsung.value=false
        timerberlangsung.value=false
    }
}
