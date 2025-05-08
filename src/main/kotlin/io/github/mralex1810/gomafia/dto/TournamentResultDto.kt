package io.github.mralex1810.gomafia.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class TournamentResultDto(
    @JsonProperty("compensation_point") val compensationPoint: String?,
    @JsonProperty("extra_point") val extraPoint: String?,
    @JsonProperty("fine") val fine: String?,
    @JsonProperty("first_kill") val firstKill: String?,
    @JsonProperty("first_kill_point") val firstKillPoint: String?,
    @JsonProperty("global_game") val globalGame: String?,
    @JsonProperty("icon") val icon: String?,
    @JsonProperty("icon_type") val iconType: String?,
    @JsonProperty("is_paid") val isPaid: String?,
    @JsonProperty("login") val login: String?,
    @JsonProperty("place") val place: String?,
    @JsonProperty("sum") val sum: String?,
    @JsonProperty("sum_extra") val sumExtra: String?,
    @JsonProperty("win") val win: String?,
    @JsonProperty("win_as_don") val winAsDon: String?,
    @JsonProperty("win_as_sheriff") val winAsSheriff: String?
)
