package com.korop.yaroslavhorach.domain.game.model

 class  ChallengeTimeLimited(
    id: Long = 0,
    titleText: Map<String, String>,
    descriptionText: Map<String, String>,
    acceptText: Map<String, String>,
    completeText: Map<String, String>,
    tokenCost: Int,
    bonusOnComplete: Int,
    status: Status = Status(),
    var progressInMinutes: Int,
    val theme: Game.Skill,
    val durationMinutes: Int,
) : Challenge(
    id,
    titleText,
    descriptionText,
    acceptText,
    completeText,
    tokenCost,
    bonusOnComplete,
    status
){

}