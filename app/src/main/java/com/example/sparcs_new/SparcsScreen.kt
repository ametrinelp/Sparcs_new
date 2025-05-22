package com.example.sparcs_new

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sparcs_new.data.AuthManager
import com.example.sparcs_new.data.AuthState
import com.example.sparcs_new.ui.theme.screen.Account
import com.example.sparcs_new.ui.theme.screen.EventInformationScreen
import com.example.sparcs_new.ui.theme.screen.MyEvent
import com.example.sparcs_new.ui.theme.screen.NewEvent
import com.example.sparcs_new.ui.theme.screen.SigninScreen
import com.example.sparcs_new.ui.theme.screen.SignupScreen
import com.example.sparcs_new.ui.theme.screen.StartScreen

enum class SparcsScreen(@StringRes val title: Int) {
    Appname(title = R.string.app_name),
    Start(title = R.string.start),
    Auth(title = R.string.auth),
    New(title = R.string.new_event),
    My(title = R.string.my_event),
    Account(title = R.string.account),
    Login(title = R.string.login),
    Signup(title = R.string.signup),
    Information(
        title = R.string.event_info
    );
    companion object {
        fun createRoute(eventId: String, offset:Int): String {
            return "$Information/$eventId/$offset"
        }
    }
}


@Composable
fun SparcsApp(
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentScreen = SparcsScreen.entries.find { screen ->
        currentRoute?.startsWith(screen.name) == true
    } ?: SparcsScreen.Start
    val showAppBar = when (currentRoute) {
        SparcsScreen.Login.name, SparcsScreen.Signup.name -> false
        else -> true
    }
    val authState by AuthManager.authState.collectAsState()


    Scaffold(
        topBar = {
            if (showAppBar) {
                SparcsAppBar(
                    currentScreen = currentScreen
                )
            }
        },
        bottomBar = {
            if (showAppBar) {
                SparcsAppDownBar(
                    navController = navController,
                    modifier = Modifier,
                    currentScreen = currentScreen
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SparcsScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SparcsScreen.Start.name) { StartScreen(navController) }
            composable(route = SparcsScreen.New.name) { NewEvent() }
            composable(route = SparcsScreen.My.name) { MyEvent(navController = navController) }
            composable(route = SparcsScreen.Account.name) { Account() }
            composable(route = SparcsScreen.Login.name) { SigninScreen(navController) }
            composable(route = SparcsScreen.Signup.name) { SignupScreen(navController) }
            composable(
                route = "${SparcsScreen.Information.name}/{eventId}/{offset}",
                arguments = listOf(navArgument("eventId") { nullable = false }, navArgument("offset") { defaultValue = 0 })
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                val offset = backStackEntry.arguments?.getInt("offset")?: 0
                EventInformationScreen(eventId = eventId, navController = navController, offset = offset)
            }
        }
    }
        when(authState){
            AuthState.Loading ->{}
            is AuthState.Unauthenticated -> {
                navController.navigate(route = SparcsScreen.Login.name)
                AuthManager.setLoading()
            }
            is AuthState.Authenticated -> {
                navController.navigate(route = SparcsScreen.Start.name)
                AuthManager.setLoading()
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SparcsAppBar(
    currentScreen: SparcsScreen
) {

    if (currentScreen != SparcsScreen.Information) {
        TopAppBar(
            title = { Text(stringResource(currentScreen.title)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceTint
            )
        )
    }
}


@Composable
fun SparcsAppDownBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    currentScreen: SparcsScreen,
) {
    if (currentScreen != SparcsScreen.Information) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.surfaceDim,
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconAndText(
                    icon = Icons.Default.Home,
                    text = "홈",
                    onClick = { navController.navigate(SparcsScreen.Start.name) }
                )
                IconAndText(
                    icon = Icons.Default.Add,
                    text = "개설",
                    onClick = { navController.navigate(SparcsScreen.New.name) }
                )
                IconAndText(
                    icon = Icons.Default.CheckCircle,
                    text = "내 팟",
                    onClick = { navController.navigate(SparcsScreen.My.name) }
                )
                IconAndText(
                    icon = Icons.Default.AccountCircle,
                    text = "계정",
                    onClick = { navController.navigate(SparcsScreen.Account.name) }
                )
            }
        }
    }
}

@Composable
fun IconAndText(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = text)
        Text(text = text)
    }
}






