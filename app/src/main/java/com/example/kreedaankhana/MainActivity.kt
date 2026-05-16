package com.example.kreedaankhana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kreedaankhana.ui.navigation.Screen
import com.example.kreedaankhana.ui.theme.KreedaAnkhanaTheme
import com.example.kreedaankhana.ui.auth.LoginScreen
import com.example.kreedaankhana.ui.auth.SignUpScreen
import com.example.kreedaankhana.ui.home.HomeScreen
import com.example.kreedaankhana.ui.splash.SplashScreen
import com.example.kreedaankhana.ui.booking.BookSlotScreen
import com.example.kreedaankhana.ui.booking.CalendarScreen
import com.example.kreedaankhana.ui.challenge.ChallengeBoardScreen
import com.example.kreedaankhana.ui.challenge.ChallengeResponseScreen
import com.example.kreedaankhana.ui.score.ScoreWallScreen
import com.example.kreedaankhana.ui.score.AddScoreScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KreedaAnkhanaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KreedaApp()
                }
            }
        }
    }
}

@Composable
fun KreedaApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.SignUp.route) { SignUpScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.BookSlot.route) { BookSlotScreen(navController) }
        composable(Screen.Calendar.route) { CalendarScreen(navController) }
        composable(Screen.ChallengeBoard.route) { ChallengeBoardScreen(navController) }
        composable(Screen.ChallengeResponse.route) { backStackEntry ->
            val challengeId = backStackEntry.arguments?.getString("challengeId")?.toInt() ?: 0
            ChallengeResponseScreen(navController, challengeId)
        }
        composable(Screen.ScoreWall.route) { ScoreWallScreen(navController) }
        composable(Screen.AddScore.route) { AddScoreScreen(navController) }
    }
}
