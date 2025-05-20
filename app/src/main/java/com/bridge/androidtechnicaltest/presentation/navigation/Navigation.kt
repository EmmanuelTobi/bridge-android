package com.bridge.androidtechnicaltest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bridge.androidtechnicaltest.data.model.Pupil
import com.bridge.androidtechnicaltest.presentation.screens.CreatePupilScreen
import com.bridge.androidtechnicaltest.presentation.screens.EditPupilScreen
import com.bridge.androidtechnicaltest.presentation.screens.PupilDetailScreen
import com.bridge.androidtechnicaltest.presentation.screens.PupilListScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.StudentList.route
    ) {
        composable(route = Screen.StudentList.route) {
            PupilListScreen(
                onNavigateToDetail = { studentId ->
                    navController.navigate(Screen.StudentDetail.createRoute(studentId))
                },
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateStudent.route)
                }
            )
        }

        composable(
            route = Screen.StudentDetail.route + "/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            PupilDetailScreen(
                studentId = backStackEntry.arguments?.getInt("studentId") ?: 0,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { pupil ->
                    val pupilJson = Json.encodeToString(pupil)
                    val encodedPupilJson = URLEncoder.encode(pupilJson, "UTF-8")
                    println(encodedPupilJson)
                    navController.navigate(Screen.EditStudent.createRoute(pupil.pupilId ?: 0, encodedPupilJson))
                }
            )
        }

        composable(route = Screen.CreateStudent.route) {
            CreatePupilScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditStudent.route + "/{studentId}/{pupilData}",
            arguments = listOf(
                navArgument("studentId") { type = NavType.IntType },
                navArgument("pupilData") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            val encodedPupilJson = backStackEntry.arguments?.getString("pupilData") ?: ""
            val pupilJson = URLDecoder.decode(encodedPupilJson, "UTF-8")
            val pupil = if (pupilJson.isNotEmpty()) Json.decodeFromString<Pupil>(pupilJson) else null
            
            EditPupilScreen(
                studentId = studentId,
                pupil = pupil,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object StudentList : Screen("student_list")
    data object StudentDetail : Screen("student_detail") {
        fun createRoute(studentId: Int) = "$route/$studentId"
    }
    data object CreateStudent : Screen("create_student")
    data object EditStudent : Screen("edit_student") {
        fun createRoute(studentId: Int, pupilJson: String) = "$route/$studentId/$pupilJson"
    }

}