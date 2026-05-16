package com.example.kreedaankhana.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object BookSlot : Screen("book_slot")
    object Calendar : Screen("calendar")
    object ChallengeBoard : Screen("challenge_board")
    object ChallengeResponse : Screen("challenge_response/{challengeId}") {
        fun createRoute(challengeId: Int) = "challenge_response/$challengeId"
    }
    object ScoreWall : Screen("score_wall")
    object AddScore : Screen("add_score")
}
