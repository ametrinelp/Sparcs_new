package com.example.sparcs_new.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparcs_new.R
import com.example.sparcs_new.SparcsScreen
import com.example.sparcs_new.viewModel.AppViewModelFactory
import com.example.sparcs_new.data.AuthManager
import com.example.sparcs_new.viewModel.LoginState
import com.example.sparcs_new.viewModel.LoginViewModel
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@Composable
fun SigninScreen(
    navController: NavHostController
) {
    val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
    val focusRequesterPassword = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Surface(color = MaterialTheme.colorScheme.background) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val currentLoginState by loginViewModel.loginState.collectAsState()

        val usernameInteractionSource = remember { MutableInteractionSource() }
        val usernameIsFocused by usernameInteractionSource.collectIsFocusedAsState()

        val passwordInteractionSource = remember { MutableInteractionSource() }
        val passwordIsFocused by passwordInteractionSource.collectIsFocusedAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp, top = 10.dp, bottom = 10.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.onSecondary)) {
                    Text(
                        text = "로그인",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
                    )
                    Text(
                        text = "이름",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.scrim
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        BasicTextField(
                            value = username,
                            onValueChange = { username = it },
                            textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth(),
                            interactionSource = usernameInteractionSource,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusRequesterPassword.requestFocus()
                                }
                            ),
                            decorationBox = { innerTextField ->
                                if (username.isEmpty() && !usernameIsFocused) {
                                    Text(
                                        text = "이름 입력",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.scrim
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                    Text(
                        text = "비밀번호",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        BasicTextField(
                            value = password,
                            onValueChange = { password = it },
                            textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesterPassword),
                            interactionSource = passwordInteractionSource,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    loginViewModel.performLogin(username, password)

                                }
                            ),
                            decorationBox = { innerTextField ->
                                if (password.isEmpty() && !passwordIsFocused) {
                                    Text(
                                        text = "비밀 번호 입력(문자, 숫자, 특수문자 포함 8~20자)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.scrim
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }
                Button(
                    onClick = {
                        loginViewModel.performLogin(username, password)
                    },
                    enabled = currentLoginState != LoginState.Loading,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = dimensionResource(R.dimen.padding_small))
                ) {
                    Text(
                        text = "로그인",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "회원가입",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            navController.navigate(route = SparcsScreen.Signup.name)
                        },
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.Underline,
                )

                when (currentLoginState) {
                    LoginState.Idle -> {}
                    LoginState.Loading -> CircularProgressIndicator()
                    is LoginState.Success -> {
//                        Text(
//                            "Login Successful! AccessToken: ${(currentLoginState as LoginState.Success).response.access_token}, " +
//                                    "refresh_token :${(currentLoginState as LoginState.Success).response.refresh_token}"
//                        )
                        Toast.makeText(context, "로그인을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                        loginViewModel.loginSuccess()
                        AuthManager.setAuthenticated()
                    }
                    is LoginState.Error ->
//                        Text(
//                        "Login Failed: ${(currentLoginState as LoginState.Error).message}",
//                        color = MaterialTheme.colorScheme.error
//                    )
                        Toast.makeText(context, "로그인에 실패하였습니다.${(currentLoginState as LoginState.Error).message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
