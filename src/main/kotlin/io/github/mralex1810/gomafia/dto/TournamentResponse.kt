package io.github.mralex1810.gomafia.dto

data class TournamentResponse(
    val tournamentDto: TournamentDto,
    val games: List<GameDto>,
    val tournamentResults: List<TournamentResultDto>?
)
