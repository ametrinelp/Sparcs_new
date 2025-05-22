package com.example.sparcs_new.ui.theme.screen

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparcs_new.DTO.EventResponseDTO
import com.example.sparcs_new.R
import com.example.sparcs_new.SparcsScreen
import com.example.sparcs_new.data.PreferencesHelper
import com.example.sparcs_new.viewModel.AppViewModelFactory
import com.example.sparcs_new.viewModel.GetAttendeesViewModel
import com.example.sparcs_new.viewModel.GetEventState
import com.example.sparcs_new.viewModel.GetEventsViewModel
import com.example.sparcs_new.viewModel.GetUserViewModel


@Composable
fun StartScreen(navController : NavHostController) {
    val getEventsViewModel: GetEventsViewModel= viewModel(factory = AppViewModelFactory(LocalContext.current))
    val getUserViewModel: GetUserViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))

    val eventState by getEventsViewModel.eventState.collectAsState()
    val offset by getEventsViewModel.offset.collectAsState()
    val currentPage by getEventsViewModel.currentPage.collectAsState()
    val context = LocalContext.current

    val preferencesHelper = remember { PreferencesHelper(context) }
    var showUsageDialog by remember { mutableStateOf(preferencesHelper.isFirstLaunch()) }

    //처음 실행 시 설명서
    if (showUsageDialog) {
        AlertDialog(
            onDismissRequest = { showUsageDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    preferencesHelper.setFirstLaunchDone()
                    showUsageDialog = false
                }) {
                    Text("확인")
                }
            },
            title = {
                Text(
                    text= "사용법 안내",
                    color = MaterialTheme.colorScheme.scrim) },
            text = {
                Text(
                    text = "\uD83D\uDD0D \t이벤트 목록을 탐색하고 참여하세요!\n" +
                            "\uD83D\uDDD3\uFE0F \t이벤트를 클릭하면 날짜, 시간, 장소, 설명 등 상세한 정보를 확인할 수 있습니다.\n" +
                            "✅ \t\"참석\", \"불참\" 등을 선택해 자신의 참석 여부를 전달할 수 있습니다.\n" +
                            "\uD83D\uDC65 \t참가한 사람들의 리스트를 확인할 수 있습니다.\n",
                    color = MaterialTheme.colorScheme.scrim) }
        )
    }

    Scaffold{
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){


            when (eventState) {
                GetEventState.Idle -> {
                    LaunchedEffect(Unit) {
                        getUserViewModel.getUserInfo("user_query_value", "nick_query_value")
                        getEventsViewModel.getEventInfo(offset)
                    }
                }
                GetEventState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is GetEventState.Success -> {
                    val events = (eventState as GetEventState.Success).response

                    PaginationBar(
                        currentPage = currentPage,
                        events = events,
                        onNext = { getEventsViewModel.goToNextPage() },
                        onPrev = { getEventsViewModel.goToPreviousPage() }
                    )

                    LazyColumn(
                        contentPadding = it,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_large))
                    ) {
                        items(events) { event ->
                            EventItem(event = event, navController = navController, offset = offset)
                        }

                    }
                }
                is GetEventState.Error -> {
                    val errorMessage = (eventState as GetEventState.Error).message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: $errorMessage")
                    }
                }
            }

        }

    }


}


@Composable
fun EventItem(
    event: EventResponseDTO,
    navController:NavHostController,
    offset:Int
){
    val openDialog = remember { mutableStateOf(false) }
    val isLoading by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_small))
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(10.dp))
            .clickable {
                if (!isLoading) {
                    openDialog.value = true
                }
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = event.title,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.scrim,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_small))
                        .align(Alignment.Center)
                )

            }


            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .padding(bottom = dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row{
                    EventInformation(
                        string = formatDateTimeString(event.datetime),
                        icon = painterResource(R.drawable.calendar)

                    )
                    EventInformation(
                        string = event.location,
                        icon = painterResource(R.drawable.location)
                    )


                }
            }

        }
    }
    if (openDialog.value) {
        val getAttendeesViewModel: GetAttendeesViewModel =
            viewModel(factory = AppViewModelFactory(LocalContext.current))

        LaunchedEffect(Unit) {
            getAttendeesViewModel.loadAttendees(event.event_id)
        }

        val isLoadingAt by getAttendeesViewModel.isLoading.collectAsState()
        if (!isLoadingAt) {
            openDialog.value = false
            val route = SparcsScreen.createRoute(event.event_id, offset)
            navController.navigate(route)
        }
    }


}




@Composable
fun EventInformation(
    string: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.scrim,
    style: TextStyle = MaterialTheme.typography.bodySmall
){
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        ) {
            Icon(
                painter = icon,
                tint = color,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_small))
                    .size(dimensionResource(R.dimen.padding_medium))
            )
            Text(
                text = string,
                maxLines = 1,
                style = style,
                color = MaterialTheme.colorScheme.scrim,
                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small), end = dimensionResource(R.dimen.padding_small))

            )


        }

    }

}

@Composable
fun PaginationBar(
    currentPage: Int,
    events: List<EventResponseDTO>,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {
    val isPrevEnabled = currentPage > 1
    val isNextEnabled = events.size >= 10

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "< 이전",
            modifier = Modifier
                .clickable(enabled = isPrevEnabled) {
                    onPrev()
                }
                .padding(8.dp),
            color = if (isPrevEnabled) Color.Black else Color.Gray
        )

        Text("현재 페이지: $currentPage", modifier = Modifier.padding(8.dp))

        Text(
            "다음 >",
            modifier = Modifier
                .clickable(enabled = isNextEnabled) {
                    onNext()
                }
                .padding(8.dp),
            color = if (isNextEnabled) MaterialTheme.colorScheme.scrim else MaterialTheme.colorScheme.outline
        )
    }
}


@Preview
@Composable
fun preview(){

}