package com.example.sparcs_new.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparcs_new.DTO.EventResponseDTO
import com.example.sparcs_new.R
import com.example.sparcs_new.ViewModel.AppViewModelFactory
import com.example.sparcs_new.ViewModel.GetAttendeesViewModel
import com.example.sparcs_new.ViewModel.GetEventState
import com.example.sparcs_new.ViewModel.GetEventsViewModel


@Composable
fun StartScreen(
    getEventsViewModel: GetEventsViewModel= viewModel(factory = AppViewModelFactory(LocalContext.current))
    ) {
    val eventState by getEventsViewModel.eventState.collectAsState()

    Scaffold{
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ){
            getEventsViewModel.getEventInfo()
            when (eventState) {
                GetEventState.Idle -> {

                }

                GetEventState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is GetEventState.Success -> {
                    val events = (eventState as GetEventState.Success).response
                    LazyColumn(
                        contentPadding = it,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_large))
                    ) {
                        items(events) { event ->
                            EventItem(event = event)
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
    event : EventResponseDTO,

){
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

        val getAttendeesViewModel: GetAttendeesViewModel = viewModel(
            factory = AppViewModelFactory(LocalContext.current, event.event_id))
        LaunchedEffect(event.event_id) {
            getAttendeesViewModel.loadAttendees(event.event_id)
        }

        val attendeeNicknames by getAttendeesViewModel.attendeeNicknames.collectAsState()
        val isLoading by getAttendeesViewModel.isLoading.collectAsState()
        val error by getAttendeesViewModel.error.collectAsState()

        DialogPop(
            onDismissRequest = { openDialog.value = false },
            title = event.title,
            time = event.datetime,
            location = event.location,
            description = event.description,
            attendees = attendeeNicknames,
            isLoading = isLoading,
            error = error
        )

    }
}
@Composable
fun EventInformation(
    string: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.scrim
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
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.scrim,
                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small), end = dimensionResource(R.dimen.padding_small))

            )


        }

    }

}