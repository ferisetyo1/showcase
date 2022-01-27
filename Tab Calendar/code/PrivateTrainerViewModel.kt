package com.llc.thelegionpt.fitur.main.privatetrainer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.thelegionpt.data.network.response.JadwalHome
import com.llc.thelegionpt.data.network.response.TransaksiPrivate
import com.llc.thelegionpt.data.repository.LegionRepository
import com.llc.thelegionpt.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateTrainerViewModel @Inject constructor(val repository: LegionRepository) : ViewModel() {
    val jadwalHome = mutableStateOf<List<JadwalHome>>(emptyList())
    val isLoading = mutableStateOf(false)
    val msgError = mutableStateOf("")
    val listTransaksi = mutableStateOf<List<TransaksiPrivate>>(emptyList())

    fun jadwalTrainerHome(date: String) {
        viewModelScope.launch {
            msgError.value = ""
            isLoading.value = true
            val result = repository.transbymonth(date = date)
            when (result) {
                is Resource.Success -> {
                    isLoading.value = false
                    result.data?.data?.let {
                        jadwalHome.value = it
                        cariTransaksibyDate(date = date)
                    }
                }
                is Resource.Error -> {
                    isLoading.value = false
                    msgError.value = result.message.toString()
                }
            }
        }
    }

    fun cariTransaksibyDate(date: String) {
        listTransaksi.value = jadwalHome.value.firstOrNull { it.tgl == date }?.data.orEmpty()
    }
}