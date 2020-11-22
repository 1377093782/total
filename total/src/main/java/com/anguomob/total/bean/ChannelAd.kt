package com.anguomob.total.bean

data class DataForYzdzy(
        val `data`: ArrayList<AnguoAdParams>,
        val msg: String,
        val status: Boolean
)

data class AnguoAdParams(
        val ad_type: String,
        val channel: String,
        val game_type: String,
        val id: String,
        val name: String,
        val package_name: String,
        val params: String,
        val show_ad: String,
        val show_game: String,
        val version_code: String,
        val app_desc: String,
        val logo_url: String,
        val down_app_url: String,
        val pangolin_app_id: String,
        val pangolin_open_screen_id: String,
        val pangolin_excitation_id: String,
        val pangolin_banner_id: String,
        val market_type: String           //android  ios [android,ios]
)
