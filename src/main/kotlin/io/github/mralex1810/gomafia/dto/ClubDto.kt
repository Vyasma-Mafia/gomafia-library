package io.github.mralex1810.gomafia.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ClubDto(
    @JsonProperty("avatar_link") val avatarLink: String?,
    @JsonProperty("city") val city: String?,
    @JsonProperty("club_score") val clubScore: String?,
    @JsonProperty("country") val country: String?,
    @JsonProperty("elo_average") val eloAverage: String?,
    @JsonProperty("id") val id: String?,
    @JsonProperty("rank") val rank: String?,
    @JsonProperty("title") val title: String?,
    @JsonProperty("total_rows") val totalRows: String?
)
