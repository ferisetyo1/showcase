package com.llc.thelegionpt.fitur.detailperforma

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.thelegionpt.data.network.response.TotalCustomer
import com.llc.thelegionpt.data.network.response.TransaksiPrivate
import com.llc.thelegionpt.data.repository.LegionRepository
import com.llc.thelegionpt.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPerformaViewModel @Inject constructor(val repository: LegionRepository) : ViewModel() {

    val performa = mutableStateOf<TotalCustomer?>(null)
    val listTrans = mutableStateOf<List<TransaksiPrivate>>(emptyList())
    val isLoading = mutableStateOf(false)
    val message = mutableStateOf("")


    fun getPerforma(status: Int) {
        viewModelScope.launch {
            isLoading.value = true
            message.value = ""
            val result = repository.detailperformabar(status = status)
            when (result) {
                is Resource.Success -> {
                    isLoading.value = false
                    performa.value = result.data?.data
                    message.value = result.message.orEmpty()
                }
                is Resource.Error -> {
                    isLoading.value = false
                    message.value = result.message.orEmpty()
                }
            }
        }
    }

    fun detailPerformaTrans(
        limit: Int = 10,
        status: Int? = null,
        page: Int = 1
    ) {
        viewModelScope.launch {
            isLoading.value = true
            message.value = ""
            val result = repository.detailPerformaTrans(status = status,page = page)
            when (result) {
                is Resource.Success -> {
                    isLoading.value = false
                    listTrans.value = result.data?.data?.items.orEmpty()
                    message.value = result.message.orEmpty()
                }
                is Resource.Error -> {
                    isLoading.value = false
                    message.value = result.message.orEmpty()
                }
            }
        }
    }
}
