package com.rahdeva.bencanaapp.data

import com.rahdeva.bencanaapp.api.ApiConfig
import com.rahdeva.bencanaapp.data.model.DisasterItems
import com.rahdeva.bencanaapp.utils.DataResults

/*
 * Single source of truth
 * A bit boilerplate but more structured and more readable than last one
 */
class DisasterRepository {

    suspend fun getDisaster(): DataResults<List<DisasterItems?>?> {
        return try {
            val res = ApiConfig.getApiService().getDisasters()
            if(res.result?.objects?.output?.geometries!!.isNotEmpty()) {
                DataResults.Success(res.result.objects.output.geometries)
            } else {
                DataResults.Error("Disaster Not Found!")
            }
        } catch (ex: Exception){
            DataResults.Error("Network Error: $ex")
        }
    }

    suspend fun searchDisaster(admin: String): DataResults<List<DisasterItems?>?> {
        return try {
            val res = ApiConfig.getApiService().getDisastersByLocation(admin)
            if(res.result?.objects?.output?.geometries!!.isNotEmpty()) {
                DataResults.Success(res.result.objects.output.geometries)
            } else {
                DataResults.Error("Disaster Not Found!")
            }
        } catch (ex: Exception){
            DataResults.Error("Network Error: $ex")
        }
    }

    suspend fun getFilterDisaster(disasterType: String): DataResults<List<DisasterItems?>?> {
        return try {
            val res = ApiConfig.getApiService().getDisastersByType(disasterType)
            if(res.result?.objects?.output?.geometries!!.isNotEmpty()) {
                DataResults.Success(res.result.objects.output.geometries)
            } else {
                DataResults.Error("Disaster Not Found!")
            }
        } catch (ex: Exception){
            DataResults.Error("Network Error: $ex")
        }
    }

    suspend fun getDisasterByLocationAndType(location: String, type: String): DataResults<List<DisasterItems?>?> {
        return try {
            val res = ApiConfig.getApiService().getDisastersByLocationAndType(location, type)
            if(res.result?.objects?.output?.geometries!!.isNotEmpty()) {
                DataResults.Success(res.result.objects.output.geometries)
            } else {
                DataResults.Error("Disaster Not Found!")
            }
        } catch (ex: Exception){
            DataResults.Error("Network Error: $ex")
        }
    }


}