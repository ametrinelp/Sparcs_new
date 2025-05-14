package com.example.sparcs_new.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparcs_new.R
import com.example.sparcs_new.ViewModel.AppViewModelFactory
import com.example.sparcs_new.ViewModel.AuthManager
import com.example.sparcs_new.ViewModel.GetEventState
import com.example.sparcs_new.ViewModel.GetUserViewModel
import com.example.sparcs_new.ViewModel.UpdateNicknameViewModel

//로그인 했을 때, 안 했을 때 구분해야됨
//다크모드 정도는 준비해야하지 않으까
@Composable
fun Account (
    getViewModel: GetUserViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)) )
{
    val username by getViewModel.username.collectAsState()
    val nickname by getViewModel.nickname.collectAsState()
    val isUserInfoLoaded by getViewModel.isUserInfoLoaded.collectAsState()

    val openDialog = remember { mutableStateOf(false) }
    LaunchedEffect(username) {
        if (isUserInfoLoaded && username == ""){
            AuthManager.setUnauthenticated()
        }
    }

    LaunchedEffect(Unit) {
        getViewModel.GetUserInfo("user_query_value", "nick_query_value")
    }


    Surface (color = MaterialTheme.colorScheme.background ){
        Column(modifier = Modifier.fillMaxSize()) {
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
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            imageVector = Icons.TwoTone.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                        Column {

                            Text(
                                text = username,
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.scrim
                            )
                            Text(
                                text = nickname,
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.scrim
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .align(CenterEnd)
                            .padding(dimensionResource(R.dimen.padding_medium))
                            .clickable {
                                openDialog.value = true
                            }
                    )
                }
            }
            if (openDialog.value) {
                EditDialogPop(
                    onDismissRequest = { openDialog.value = false },
                    username = username,
                    nickname = nickname
                )
            }

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
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .fillMaxWidth()
                        .clickable { }
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null
                    )
                    Text(
                        text = "내가 참여했던 방",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )
                }
            }
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
                Row (verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .fillMaxWidth()
                        .clickable {
                            AuthManager.setUnauthenticated()
                        }){
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null
                    )
                    Text(
                        text = "로그 아웃 하기",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )
                }
            }
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
                Row (verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .fillMaxWidth()
                        .clickable {

                        }){
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                    Text(
                        text = "설정",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )
                }
            }
        }

    }
}

@Composable
fun EditDialogPop(
    onDismissRequest: () -> Unit,
    username : String,
    nickname : String,
    updateNicknameViewModel: UpdateNicknameViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current)),
    getViewModel: GetUserViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
){
    var _nickname by remember { mutableStateOf("") }
    LaunchedEffect(Unit){
        if (_nickname == "") {
            _nickname = nickname
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(50.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(R.dimen.padding_small))
                    ) {
                        Image(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Center)
                                .size(70.dp)
                        )

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 2.dp)
                                .clickable {
                                    onDismissRequest()
                                }
                        )
                    }

                }

                Column (
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small))
                    //contentPadding = it
                ){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))) {
                        Text(
                            text = "이름 : ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = username,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.scrim,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Row(horizontalArrangement = Arrangement.Center ) {
                        Text(
                            text = "닉네임 : ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        BasicTextField(
                            value = _nickname,
                            onValueChange = {
                                _nickname= it },
                            modifier = Modifier
                                .fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.secondary),
                            decorationBox = { innerTextField ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        innerTextField()
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null,
                                            modifier = Modifier.align(CenterEnd)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }


                Box (
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                ){
                    Button(
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 10.dp
                        ),
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            onDismissRequest()
                            updateNicknameViewModel.updateUserNickname(_nickname)
                            getViewModel.GetUserInfo("user_query_value", "nick_query_value")

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.align(alignment = Center)
                    ) {
                        Text(
                            text = "수정 완료",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }



            }



        }

    }
}

@Preview
@Composable
fun Previw(){
    EditDialogPop(
        onDismissRequest = { },
        username = "user",
        nickname = "nick"
    )
}