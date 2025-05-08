package io.github.mralex1810.gomafia.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class StatsDto(
    @JsonProperty("primary") var primary: Primary? = Primary(),
    @JsonProperty("win_rate") var winRate: WinRate? = WinRate(),
    @JsonProperty("games_stats") var gamesStats: GamesStats? = GamesStats(),
    @JsonProperty("advanced_points") var advancedPoints: AdvancedPoints? = AdvancedPoints()
) {
    data class Primary(

        @JsonProperty("mafia") var mafia: Int? = null,
        @JsonProperty("red") var red: Int? = null,
        @JsonProperty("don") var don: Int? = null,
        @JsonProperty("sheriff") var sheriff: Int? = null,
        @JsonProperty("total_games") var totalGames: Int? = null

    )

    data class Win(

        @JsonProperty("value") var value: Int? = null,
        @JsonProperty("percent") var percent: Int? = null

    )

    data class Total(

        @JsonProperty("value") var value: Int? = null

    )

    data class Mafia(

        @JsonProperty("win") var win: Win? = Win(),
        @JsonProperty("total") var total: Total? = Total()

    )

    data class Red(

        @JsonProperty("win") var win: Win? = Win(),
        @JsonProperty("total") var total: Total? = Total()

    )

    data class Don(

        @JsonProperty("win") var win: Win? = Win(),
        @JsonProperty("total") var total: Total? = Total()

    )

    data class Sheriff(

        @JsonProperty("win") var win: Win? = Win(),
        @JsonProperty("total") var total: Total? = Total()

    )

    data class TotalWins(

        @JsonProperty("value") var value: Int? = null,
        @JsonProperty("percent") var percent: Int? = null

    )

    data class WinRate(

        @JsonProperty("mafia") var mafia: Mafia? = Mafia(),
        @JsonProperty("red") var red: Red? = Red(),
        @JsonProperty("don") var don: Don? = Don(),
        @JsonProperty("sheriff") var sheriff: Sheriff? = Sheriff(),
        @JsonProperty("total_games") var totalGames: Int? = null,
        @JsonProperty("total_wins") var totalWins: TotalWins? = TotalWins()

    )

    data class GamesStats(

        @JsonProperty("average_points") var averagePoints: Double? = null,
        @JsonProperty("prize_places") var prizePlaces: Int? = null

    )

    data class AdvancedPoints(

        @JsonProperty("red") var red: Map<String, Double> = mutableMapOf(),
        @JsonProperty("black") var black: Map<String, Double> = mutableMapOf(),
        @JsonProperty("sheriff") var sheriff: Map<String, Double> = mutableMapOf(),
        @JsonProperty("points_10_games") var points10Games: Double? = null

    )
}
