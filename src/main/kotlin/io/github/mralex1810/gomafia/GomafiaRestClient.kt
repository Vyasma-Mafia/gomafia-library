package io.github.mralex1810.gomafia

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.mralex1810.gomafia.dto.ClubDto
import io.github.mralex1810.gomafia.dto.GameDto
import io.github.mralex1810.gomafia.dto.GomafiaRegion
import io.github.mralex1810.gomafia.dto.StatsDto
import io.github.mralex1810.gomafia.dto.TournamentDto
import io.github.mralex1810.gomafia.dto.TournamentResponse
import io.github.mralex1810.gomafia.dto.UserDto
import io.github.mralex1810.gomafia.dto.UserWithStats
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType

class GomafiaRestClient(private val httpClient: HttpClient) {
    private val baseUrl = "https://gomafia.pro/api"
    private val mapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    // --- Helper extension functions ---

    private fun Map<*, *>.getMap(key: String): Map<*, *> = this[key] as? Map<*, *> ?: emptyMap<Any, Any>()
    private fun Map<*, *>.getList(key: String): List<*> = this[key] as? List<*> ?: emptyList<Any>()
    private fun Map<*, *>.getInt(key: String): Int = when (val v = this[key]) {
        is Int -> v
        is Number -> v.toInt()
        is String -> v.toIntOrNull() ?: 0
        else -> 0
    }

    private inline fun <reified T> convert(value: Any?): T = mapper.convertValue(value, T::class.java)

    // --- API POST Helper ---

    private suspend fun postForm(url: String, parameters: Map<String, String>): String {
        val bodyContent = FormDataContent(Parameters.build {
            parameters.forEach { (k, v) -> append(k, v) }
        })
        val response = httpClient.post(url) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(bodyContent)
        }
        return response.bodyAsText()
    }

    suspend fun getClubResidents(clubId: Int): List<UserDto> =
        postForm(
            "$baseUrl/club/getResidents", mapOf(
                "id" to clubId.toString(),
                "limit" to "1000",
                "offset" to "0"
            )
        ).let { getDataList(it).map { user -> convert<UserDto>(user) } }

    suspend fun getUsers(year: Int, region: GomafiaRegion): List<UserDto> =
        postForm(
            "$baseUrl/user/getTop", mapOf(
                "year" to year.toString(),
                "region" to region.region,
                "limit" to "10000",
                "offset" to "0"
            )
        ).let { getDataList(it).map { user -> convert<UserDto>(user) } }

    suspend fun getUserWithStats(id: Int): UserWithStats =
        postForm(
            "$baseUrl/stats/get", mapOf(
                "id" to id.toString(),
                "period" to "all",
                "gameType" to "all",
                "tournamentType" to "all"
            )
        ).let { responseText ->
            val data = getDataMap(responseText)
            mapUserWithStats(data)
        }

    suspend fun getClubs(): List<ClubDto> =
        postForm(
            "$baseUrl/club/getTop", mapOf(
                "limit" to "10000",
                "offset" to "0"
            )
        ).let { getDataList(it).map { club -> convert<ClubDto>(club) } }

    suspend fun getTournaments(): List<TournamentDto> =
        postForm(
            "$baseUrl/tournament/getAll", mapOf(
                "time" to "finished",
                "fsm" to "yes",
                "type" to "all",
                "limit" to "10000",
                "offset" to "0"
            )
        ).let {
            getDataList(it).map { t -> convert<TournamentDto>(t) }
        }

    suspend fun getTournament(id: Int): TournamentResponse =
        postForm("$baseUrl/tournament/get", mapOf("id" to id.toString()))
            .let { responseText ->
                val data = getDataMap(responseText)
                val games = data["games"]
                    ?.let { gamesList ->
                        (gamesList as List<*>).flatMap { gameEntry ->
                            val gMap = gameEntry as? Map<*, *> ?: return@flatMap emptyList<GameDto>()
                            val gNum = gMap.getInt("gameNum")
                            (gMap["game"] as? List<*>)?.map {
                                convert<GameDto>(it).also { g -> g.gameNum = gNum }
                            } ?: emptyList()
                        }
                    } ?: emptyList()
                TournamentResponse(
                    tournamentDto = convert(data["tournament"]!!),
                    games = games,
                    tournamentResults = null // Fix/implement as needed
                )
            }

    // --- Data Extractor helpers ---

    private fun getDataMap(response: String): Map<*, *> =
        mapper.readValue<Map<String, Any>>(response)["data"] as? Map<*, *> ?: emptyMap<Any, Any>()

    private fun getDataList(response: String): List<*> =
        mapper.readValue<Map<String, Any>>(response)["data"] as? List<*> ?: emptyList<Any>()

    private fun mapUserWithStats(data: Map<*, *>): UserWithStats {
        val user = convert<UserDto>(data["user"])
        val statsMap = data["stats"] as? Map<*, *> ?: emptyMap<Any, Any>()

        val primary = convert<StatsDto.Primary>(statsMap["primary"])
        primary.totalGames = statsMap.getMap("primary").getInt("total_games")

        val winrate = convert<StatsDto.WinRate>(statsMap["win_rate"])
        winrate.totalGames = statsMap.getMap("win_rate").getInt("total_games")
        winrate.totalWins = StatsDto.TotalWins(
            value = statsMap.getMap("win_rate").getMap("total_wins").getInt("value"),
            percent = statsMap.getMap("win_rate").getMap("total_wins").getInt("percent")
        )

        val avgPoints = statsMap.getMap("games_stats")["average_points"]
        val averagePoints = when (avgPoints) {
            is Double -> avgPoints
            is Int -> avgPoints.toDouble()
            else -> 0.0
        }
        val gamesStats = StatsDto.GamesStats(
            averagePoints = averagePoints,
            prizePlaces = statsMap.getMap("games_stats").getInt("prize_places")
        )
        val advancedPoints = convert<StatsDto.AdvancedPoints>(statsMap["advanced_points"])
        val points10Games = statsMap.getMap("advanced_points")["points_10_games"]
        advancedPoints.points10Games = when (points10Games) {
            is Int -> points10Games.toDouble()
            is Double -> points10Games
            else -> 0.0
        }

        return UserWithStats(user, StatsDto(primary, winrate, gamesStats, advancedPoints))
    }
}
