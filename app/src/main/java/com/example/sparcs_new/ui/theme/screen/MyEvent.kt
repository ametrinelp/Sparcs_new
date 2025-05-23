package com.example.sparcs_new.ui.theme.screen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sparcs_new.R
import com.example.sparcs_new.viewModel.GetEventState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparcs_new.DTO.EventResponseDTO
import com.example.sparcs_new.SparcsScreen
import com.example.sparcs_new.viewModel.AppViewModelFactory
import com.example.sparcs_new.viewModel.GetAttendeesViewModel
import com.example.sparcs_new.viewModel.GetUserJoinedEventsViewModel
import com.example.sparcs_new.viewModel.GetUserViewModel

@Composable
fun MyEvent(navController : NavHostController
    ) {
    val getUserJoinedEventViewModel: GetUserJoinedEventsViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
    val getUserViewModel: GetUserViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
    val eventState by getUserJoinedEventViewModel.eventState.collectAsState()
    val offset by getUserJoinedEventViewModel.offset.collectAsState()
    val currentPage by getUserJoinedEventViewModel.currentPage.collectAsState()


    Scaffold{
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){
            when (eventState) {
                GetEventState.Idle -> {
                    LaunchedEffect(Unit) {
                        getUserViewModel.getUserInfo()
                        getUserJoinedEventViewModel.getJoinedEventInfo(offset)
                    }
                }
                GetEventState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is GetEventState.Success -> {
                    val events = (eventState as GetEventState.Success).response
                    if (events.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "참가한 방이 없습니다.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.scrim
                            )
                        }
                    } else {
                        PaginationBar(
                            currentPage = currentPage,
                            events = events,
                            onNext = { getUserJoinedEventViewModel.goToNextPage() },
                            onPrev = { getUserJoinedEventViewModel.goToPreviousPage() }
                        )
                        LazyColumn(
                            contentPadding = it,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(R.dimen.padding_large))
                        ) {
                            items(events) { event ->
                                InfoMyItem(
                                    event = event,
                                    navController = navController,
                                    offset = offset
                                )
                            }
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
//modifier.clickable
@Composable
fun InfoMyItem(event : EventResponseDTO,
               navController:NavHostController,
               offset:Int){
    val openDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_small))
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(10.dp))
            .clickable {
                openDialog.value = true
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
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
                        string = event.datetime,
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

        val isLoading by getAttendeesViewModel.isLoading.collectAsState()
        if (!isLoading) {
            openDialog.value = false
            val route = SparcsScreen.createRoute(event.event_id, offset)
            navController.navigate(route)
        }
    }


}