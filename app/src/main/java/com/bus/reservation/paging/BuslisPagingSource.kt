package com.bus.reservation.paging

import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bus.reservation.data.network.BusApiService
import com.bus.reservation.domain.model.BusListData
import com.google.gson.GsonBuilder
import okio.IOException
import retrofit2.HttpException

class BuslisPagingSource(private val busApiService: BusApiService) :
    PagingSource<Int, BusListData>() {
    override fun getRefreshKey(state: PagingState<Int, BusListData>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BusListData> {
        val position = params.key ?: 1
        return try {
            val remoteData = busApiService.getAllBusList(position.toString(), params.loadSize.toString())
            Log.e(
                "TAG",
                "load Response: --------> ${
                    GsonBuilder().setPrettyPrinting().create().toJson(remoteData.data)
                }",
            )
            val nextKey = if (remoteData.data.size < params.loadSize) {
                null
            } else {
                position + 1
            }

            LoadResult.Page(
                data = remoteData.data,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            Log.e("TAG", " IOException -------->${e.message}")
         return   LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e("TAG", "HttpException: -------->${e.message}")
          return  LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e("TAG", " Unexpected Exception -> ${e.message}")
          return  LoadResult.Error(e)
        }catch (e: UserNotAuthenticatedException){
            Log.e("TAG", " UserNotAuthenticatedException  -> ${e.message}")
        return    LoadResult.Error(e)
        }
    }
}