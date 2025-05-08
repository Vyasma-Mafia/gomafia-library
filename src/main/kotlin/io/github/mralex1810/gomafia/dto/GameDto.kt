package io.github.mralex1810.gomafia.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GameDto(
    @JsonProperty("tableNum") var tableNum: Int? = null,
    @JsonProperty("table") var table: ArrayList<PlayerDto> = arrayListOf(),
    @JsonProperty("win") var win: String? = null,
    @JsonProperty("referee") var referee: String? = null,
    @JsonProperty("referee_is_paid") var refereeIsPaid: String? = null,
    @JsonProperty("referee_icon_type") var refereeIconType: String? = null,
    @JsonProperty("referee_icon") var refereeIcon: String? = null,
    @JsonProperty("game_num") var gameNum: Int? = null
)
