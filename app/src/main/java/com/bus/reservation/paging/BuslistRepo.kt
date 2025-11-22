package com.bus.reservation.paging

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bus.reservation.data.network.BusApiService
import com.bus.reservation.domain.model.BusListData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BuslistRepo @Inject constructor(private val busApiService: BusApiService) {
    fun getAllBusList(): Flow<PagingData<BusListData>> {
        Log.e("TAG", "BuslistRepo getAllBusLists: ")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = { BuslisPagingSource(busApiService) }
        ).flow
    }
}