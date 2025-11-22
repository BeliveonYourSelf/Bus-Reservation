package com.bus.reservation.presentation.bus_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bus.reservation.comman.Resource
import com.bus.reservation.data.model.BusSearchResponse
import com.bus.reservation.domain.model.BusListData
import com.bus.reservation.domain.usecase.BusListUserCase
import com.bus.reservation.domain.usecase.BusSearchUseCase
import com.bus.reservation.paging.BuslistRepo
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class BusViewModel @Inject constructor(
    private val busListUserCase: BusListUserCase,
    private val busSearchUseCase: BusSearchUseCase,
    private val buslistRepo: BuslistRepo,
) : ViewModel() {



    val busPagerdata: Flow<PagingData<BusListData>> = buslistRepo.getAllBusList().cachedIn(viewModelScope)

    private var _MypagingData = MutableStateFlow(AllBusListState())
    var pagingState = _MypagingData.asStateFlow()



    suspend fun getAllBusLists() {
        Log.e("TAG", " ViewModel getAllBusLists: ", )
        Log.e("TAG", " ViewModel getAllBusLists _pagingData ===> ${GsonBuilder().setPrettyPrinting().create().toJson(busPagerdata)} ", )

        busPagerdata
            .onStart {
                Log.e("TAG", "getAllBusLists: onStart ", )
                _MypagingData.emit(AllBusListState(isLoading = true))
            }
            .catch { exception ->
                Log.e("TAG", "getAllBusLists: catch $exception ", )
                _MypagingData.emit(AllBusListState(message = exception.message))

            }
            .collectLatest { pagingData: PagingData<BusListData> ->
                Log.e("TAG", "getAllBusLists: collectLatest   $pagingData ", )
                _MypagingData.emit(AllBusListState(data = pagingData))

            }
    }

    private val _busListResponse = MutableStateFlow(BusListState())
    var busListResponse: StateFlow<BusListState> = _busListResponse

    private val _searchBusResponse = MutableStateFlow(BusSearchState())
    var searchBusResponse: StateFlow<BusSearchState> = _searchBusResponse

    fun getBusList(requestBody: RequestBody) {
        viewModelScope.launch {
            busListUserCase.invoke(requestBody).collect {
                when (it) {
                    is Resource.Loading -> {
                        _busListResponse.value = BusListState(isLoading = true)
                    }

                    is Resource.Success -> {
                        _busListResponse.value = BusListState(data = it.data?.data)
                    }

                    is Resource.Error -> {
                        _busListResponse.value = BusListState(message = it.message)
                    }
                }
            }
        }
    }


    fun searchBus(requestBody: RequestBody) {
        viewModelScope.launch {
            busSearchUseCase.invoke(requestBody).collect { it: Resource<BusSearchResponse> ->
                when (it) {
                    is Resource.Loading -> {
                        _searchBusResponse.emit(BusSearchState(isLoading = true))
                    }

                    is Resource.Error -> {
                        _searchBusResponse.emit(BusSearchState(message = it.message))

                    }

                    is Resource.Success -> {
                        _searchBusResponse.emit(BusSearchState(data = it.data?.data))
                    }


                }
            }
        }
    }

}
