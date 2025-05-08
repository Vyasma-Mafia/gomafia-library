package io.github.mralex1810.gomafia.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDto(
    @JsonProperty("avatar_link") val avatarLink: String?,
    @JsonProperty("elo") val elo: String?,
    @JsonProperty("icon") val icon: String?,
    @JsonProperty("icon_type") val iconType: String?,
    @JsonProperty("id") val id: String?,
    @JsonProperty("is_paid") val isPaid: String?,
    @JsonProperty("login") val login: String?,
    @JsonProperty("paid_account") val paidAccount: String?,
    @JsonProperty("rank") val rank: String?,
    @JsonProperty("title") val title: String?,
    @JsonProperty("total_rows") val totalRows: String?,
    @JsonProperty("tournaments_gg") val tournamentsGg: List<Int>?,
    @JsonProperty("tournaments_played") val tournamentsPlayed: String?,
    @JsonProperty("tournaments_score") val tournamentsScore: String?,
    @JsonProperty("country") val country: String?,
    @JsonProperty("city") val city: String?,
)
