package com.rahdeva.bencanaapp.utils

import android.graphics.drawable.Drawable
import com.rahdeva.bencanaapp.R
import com.rahdeva.bencanaapp.data.model.DisasterEnum
import com.rahdeva.bencanaapp.utils.provider.ResourceProvider

class DisasterUtils(private val resourceProvider: ResourceProvider) {

    fun getRegionCode(location: String): String {
        return when(location) {
            resourceProvider.getString(R.string.city_aceh) -> { "ID-AC" }
            resourceProvider.getString(R.string.city_bali) -> { "ID-BA" }
            resourceProvider.getString(R.string.city_banten) -> { "ID-BT" }
            resourceProvider.getString(R.string.city_bengkulu) -> { "ID-BE" }
            resourceProvider.getString(R.string.city_jawa_tengah) -> { "ID-JT" }
            resourceProvider.getString(R.string.city_kalimantan_tengah) -> { "ID-KT" }
            resourceProvider.getString(R.string.city_sulawesi_tengah) -> { "ID-ST" }
            resourceProvider.getString(R.string.city_jawa_timur) -> { "ID-JI" }
            resourceProvider.getString(R.string.city_kalimantan_timur) -> { "ID-KI" }
            resourceProvider.getString(R.string.city_nusa_tenggara_timur) -> { "ID-NT" }
            resourceProvider.getString(R.string.city_gorontalo) -> { "ID-GO" }
            resourceProvider.getString(R.string.city_dki_jakarta) -> { "ID-JK"}
            resourceProvider.getString(R.string.city_jambi) -> { "ID-JA" }
            resourceProvider.getString(R.string.city_lampung) -> { "ID-LA" }
            resourceProvider.getString(R.string.city_maluku) -> { "ID-MA" }
            resourceProvider.getString(R.string.city_kalimantan_utara) -> { "ID-KU" }
            resourceProvider.getString(R.string.city_maluku_utara) -> { "ID-MU" }
            resourceProvider.getString(R.string.city_sulawesi_utara) -> { "ID-SA" }
            resourceProvider.getString(R.string.city_sumatera_utara) -> { "ID-SU" }
            resourceProvider.getString(R.string.city_papua) -> { "ID-PA" }
            resourceProvider.getString(R.string.city_riau) -> { "ID-RI" }
            resourceProvider.getString(R.string.city_kepulauan_riau) -> { "ID-KR" }
            resourceProvider.getString(R.string.city_sulawesi_tenggara) -> { "ID-SG" }
            resourceProvider.getString(R.string.city_kalimantan_selatan) -> { "ID-KS" }
            resourceProvider.getString(R.string.city_sulawesi_selatan) -> { "ID-SN" }
            resourceProvider.getString(R.string.city_sumatera_selatan) -> { "ID-SS" }
            resourceProvider.getString(R.string.city_di_yogyakarta) -> { "ID-YO" }
            resourceProvider.getString(R.string.city_jawa_barat) -> { "ID-JB" }
            resourceProvider.getString(R.string.city_kalimantan_barat) -> { "ID-KB" }
            resourceProvider.getString(R.string.city_nusa_tenggara_barat) -> { "ID-NB" }
            resourceProvider.getString(R.string.city_papua_barat) -> { "ID-PB" }
            resourceProvider.getString(R.string.city_sulawesi_barat) -> { "ID-SR" }
            resourceProvider.getString(R.string.city_sumatera_barat) -> { "ID-SB" }
            else -> { "" }
        }
    }

    fun getRegionString(code: String): String {
        val location = when (code) {
            "ID-AC" -> { resourceProvider.getString(R.string.city_aceh) }
            "ID-BA" -> { resourceProvider.getString(R.string.city_bali) }
            "ID-BT" -> { resourceProvider.getString(R.string.city_banten) }
            "ID-BE" -> { resourceProvider.getString(R.string.city_bengkulu) }
            "ID-JT" -> { resourceProvider.getString(R.string.city_jawa_tengah) }
            "ID-KT" -> { resourceProvider.getString(R.string.city_kalimantan_tengah) }
            "ID-ST" -> { resourceProvider.getString(R.string.city_sulawesi_tengah) }
            "ID-JI" -> { resourceProvider.getString(R.string.city_jawa_timur) }
            "ID-KI" -> { resourceProvider.getString(R.string.city_kalimantan_timur) }
            "ID-NT" -> { resourceProvider.getString(R.string.city_nusa_tenggara_timur) }
            "ID-GO" -> { resourceProvider.getString(R.string.city_gorontalo) }
            "ID-JK" -> { resourceProvider.getString(R.string.city_dki_jakarta) }
            "ID-JA" -> { resourceProvider.getString(R.string.city_jambi) }
            "ID-LA" -> { resourceProvider.getString(R.string.city_lampung) }
            "ID-MA" -> { resourceProvider.getString(R.string.city_maluku) }
            "ID-KU" -> { resourceProvider.getString(R.string.city_kalimantan_utara) }
            "ID-MU" -> { resourceProvider.getString(R.string.city_maluku_utara) }
            "ID-SA" -> { resourceProvider.getString(R.string.city_sulawesi_utara) }
            "ID-SU" -> { resourceProvider.getString(R.string.city_sumatera_utara) }
            "ID-PA" -> { resourceProvider.getString(R.string.city_papua) }
            "ID-RI" -> { resourceProvider.getString(R.string.city_riau) }
            "ID-KR" -> { resourceProvider.getString(R.string.city_kepulauan_riau) }
            "ID-SG" -> { resourceProvider.getString(R.string.city_sulawesi_tenggara) }
            "ID-KS" -> { resourceProvider.getString(R.string.city_kalimantan_selatan) }
            "ID-SN" -> { resourceProvider.getString(R.string.city_sulawesi_selatan) }
            "ID-SS" -> { resourceProvider.getString(R.string.city_sumatera_selatan) }
            "ID-YO" -> { resourceProvider.getString(R.string.city_di_yogyakarta) }
            "ID-JB" -> { resourceProvider.getString(R.string.city_jawa_barat) }
            "ID-KB" -> { resourceProvider.getString(R.string.city_kalimantan_barat) }
            "ID-NB" -> { resourceProvider.getString(R.string.city_nusa_tenggara_barat) }
            "ID-PB" -> { resourceProvider.getString(R.string.city_papua_barat) }
            "ID-SR" -> { resourceProvider.getString(R.string.city_sulawesi_barat) }
            "ID-SB" -> {
                resourceProvider.getString(R.string.city_sumatera_barat) }
            else -> { "" }
        }
        return location
    }

    fun getDisasterType(disasterType: String?): String {
        return when (disasterType) {
            resourceProvider.getString(R.string.flood) -> DisasterEnum.FLOOD.title
            resourceProvider.getString(R.string.haze) -> DisasterEnum.HAZE.title
            resourceProvider.getString(R.string.wind) -> DisasterEnum.WIND.title
            resourceProvider.getString(R.string.earthquake) -> DisasterEnum.EARTHQUAKE.title
            resourceProvider.getString(R.string.volcano) -> DisasterEnum.VOLCANO.title
            resourceProvider.getString(R.string.fire) -> DisasterEnum.FIRE.title
            else -> resourceProvider.getString(R.string.unknown)
        }
    }

    fun getMarkerIcon(disasterType: String?): Int {
        return when (disasterType) {
            resourceProvider.getString(R.string.flood) -> R.drawable.ic_location_flood
            resourceProvider.getString(R.string.haze) -> R.drawable.ic_location_haze
            resourceProvider.getString(R.string.wind) -> R.drawable.ic_location_wind
            resourceProvider.getString(R.string.earthquake) -> R.drawable.ic_location_earthquake
            resourceProvider.getString(R.string.volcano) -> R.drawable.ic_location_volcano
            resourceProvider.getString(R.string.fire) -> R.drawable.ic_location_fire
            else -> R.drawable.ic_not_listed_location_24
        }
    }

    fun getDisasterDefaultImg(disasterType: String?): Int {

        return when(disasterType) {
            resourceProvider.getString(R.string.flood) -> R.drawable.img_flood
            resourceProvider.getString(R.string.haze) -> R.drawable.img_haze
            resourceProvider.getString(R.string.wind) -> R.drawable.img_wind
            resourceProvider.getString(R.string.earthquake) -> R.drawable.img_earthquake
            resourceProvider.getString(R.string.volcano) -> R.drawable.img_volcano
            resourceProvider.getString(R.string.fire) -> R.drawable.img_fire
            else -> R.drawable.ic_not_listed_location_24
        }

    }

    fun getWarningImage(warningText: String): Drawable {
        return when (warningText) {
            resourceProvider.getString(R.string.warning_no_data) -> resourceProvider.getDrawable(R.drawable.img_no_data)
            resourceProvider.getString(R.string.warning_not_found) -> resourceProvider.getDrawable(R.drawable.img_not_found)
            resourceProvider.getString(R.string.warning_exception) -> resourceProvider.getDrawable(R.drawable.img_exception)
            else -> resourceProvider.getDrawable(R.drawable.img_exception)
        }
    }

}