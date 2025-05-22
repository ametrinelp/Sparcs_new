package com.example.sparcs_new.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sparcs_new.R
import com.example.sparcs_new.SparcsScreen
import com.example.sparcs_new.viewModel.AddCommentViewModel
import com.example.sparcs_new.viewModel.AppViewModelFactory
import com.example.sparcs_new.viewModel.DeleteEventViewModel
import com.example.sparcs_new.viewModel.EditEventViewModel
import com.example.sparcs_new.viewModel.ExitEventViewModel
import com.example.sparcs_new.viewModel.GetAttendeesViewModel
import com.example.sparcs_new.viewModel.GetCommentViewModel
import com.example.sparcs_new.viewModel.GetEventState
import com.example.sparcs_new.viewModel.GetEventsViewModel
import com.example.sparcs_new.viewModel.GetUserViewModel
import com.example.sparcs_new.viewModel.JoinEventViewModel

@Composable
fun EventInformationScreen(
    eventId: String,
    navController: NavHostController,
    offset: Int
    ){
    Surface (color = MaterialTheme.colorScheme.background){
        //viewmodel
        val getViewModel: GetUserViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
        val joinViewModel: JoinEventViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
        val exitViewModel: ExitEventViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
        val getAttendeesViewModel: GetAttendeesViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
        val getEventsViewModel: GetEventsViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
        val editEventViewModel: EditEventViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
        val deleteEventViewModel: DeleteEventViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
        val getCommentViewModel: GetCommentViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))

        //variable
        val eventState by getEventsViewModel.eventState.collectAsState()
        val attendeeNicknames by getAttendeesViewModel.attendeeNicknames.collectAsState()
        val isLoading by getAttendeesViewModel.isLoading.collectAsState()
        val error by getAttendeesViewModel.error.collectAsState()
        val userId by getViewModel.userid.collectAsState()
        val context = LocalContext.current


        LaunchedEffect(Unit) {
            getEventsViewModel.getEventInfo(offset)
            getAttendeesViewModel.loadAttendees(eventId)
            getViewModel.getUserInfo()
            getCommentViewModel.getCommentInfo(eventId)
        }
        LaunchedEffect(offset) {
            if (!isLoading) {
                getEventsViewModel.getEventInfo(offset)
            }
        }
        val events = when (val state = eventState) {
            is GetEventState.Success -> state.response
            else -> emptyList()
        }
        val event = events.find { it.event_id == eventId }
        val expanded = remember { mutableStateOf(false) }
        val editing = remember { mutableStateOf(false) }
        var title by remember { mutableStateOf("${event?.title}")}
        var description by remember { mutableStateOf("${event?.description}") }
        var location by remember { mutableStateOf("${event?.location}") }
        var time by remember { mutableStateOf("${event?.datetime}") }
        val attendees = attendeeNicknames
        val ownerId = event?.user_id
        val nickname by getViewModel.nickname.collectAsState()

        LaunchedEffect(event) {
            title = event?.title ?: ""
            description = event?.description ?: ""
            location = event?.location ?: ""
            time = event?.datetime ?: ""
        }

        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
        Column {
            TopAppBar(
                title = { Text("이벤트ㄴㄷ 상세") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceTint
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(SparcsScreen.Start.name)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                actions = {
                    if (userId == ownerId) {
                        IconButton(onClick = { expanded.value = !expanded.value }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "설정",
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "수정",
                                        color = MaterialTheme.colorScheme.scrim,
                                        style = MaterialTheme.typography.bodySmall) },
                                onClick = {
                                    editing.value = true
                                    expanded.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "삭제",
                                        color = MaterialTheme.colorScheme.scrim,
                                        style = MaterialTheme.typography.bodySmall) },
                                onClick = {
                                    deleteEventViewModel.deleteEvent(eventId)
                                    Toast.makeText(context, "게시글을 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                                    navController.navigate(SparcsScreen.Start.name)
                                    expanded.value = false
                                }
                            )
                        }
                    }
                })
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(10.dp),
            shadowElevation = 8.dp
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
                            .padding(top = dimensionResource(R.dimen.padding_medium))
                    ) {
                        if (editing.value) {
                            EditTextField(
                                value = title,
                                onValueChange = {title = it},
                                style = MaterialTheme.typography.titleLarge,
                                icon = null
                            )
                        } else {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.scrim,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(
                                        start = dimensionResource(R.dimen.padding_medium),
                                        end = dimensionResource(R.dimen.padding_medium),
                                        bottom = dimensionResource(R.dimen.padding_small)
                                    )
                            )
                        }
                    }
                    HorizontalDivider(
                        thickness = 2.dp,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        color = MaterialTheme.colorScheme.surfaceBright)
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = dimensionResource(R.dimen.padding_small),
                                        end = dimensionResource(R.dimen.padding_small)
                                    )
                            ) {

                                if (editing.value) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.calendar),
                                                tint = MaterialTheme.colorScheme.surface,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(start = dimensionResource(R.dimen.padding_small))
                                                    .size(dimensionResource(R.dimen.padding_medium))
                                            )
                                            DateTimeInputWithoutCard(
                                                value = time,
                                                onValueChange = { time = it }
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    EditTextField(
                                        value = location,
                                        onValueChange = {location = it},
                                        style = MaterialTheme.typography.bodyLarge,
                                        icon = painterResource(R.drawable.location)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                } else {
                                    EventInformation(
                                        string = formatDateTimeString(time),
                                        icon = painterResource(R.drawable.calendar),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    EventInformation(
                                        string = location,
                                        icon = painterResource(R.drawable.location),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                            Row(modifier = Modifier.padding(
                                start = dimensionResource(R.dimen.padding_small),
                                end = dimensionResource(R.dimen.padding_small)
                            )){
                                when {
                                    isLoading -> {
                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Center
                                        ) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        }
                                    }

                                    error != null -> {
                                        Text(
                                            "Error loading attendees: $error",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }

                                    attendees.isEmpty() -> {
                                        EventInformation(
                                            string = "참가자가 없습니다.",
                                            icon = painterResource(R.drawable.group),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        EventInformation(
                                            string = (attendees.size).toString(),
                                            icon = painterResource(R.drawable.group),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                    else ->{
                                        EventInformation(
                                            string = attendees.toString(),
                                            icon = painterResource(R.drawable.group),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        EventInformation(
                                            string = (attendees.size).toString(),
                                            icon = painterResource(R.drawable.group),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                            Row(modifier = Modifier.padding(
                                start = dimensionResource(R.dimen.padding_small),
                                end = dimensionResource(R.dimen.padding_small),
                                bottom = dimensionResource(R.dimen.padding_small)
                            )) {
                                EmojiSelectionRow(eventId)
                            }
                        }

                    Column (
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onSurface)
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                            .verticalScroll(rememberScrollState())
                    ){
                        //설명 수정
                        if (editing.value) {
                            EditTextField(
                                value = description,
                                onValueChange = {description = it},
                                style = MaterialTheme.typography.bodyLarge,
                                maxlines = 10,
                                icon = null
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.scrim
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                            Box (modifier = Modifier
                                .background(MaterialTheme.colorScheme.onSurface)
                                .fillMaxWidth()
                            ){
                                val isAttendee = remember(nickname, attendees) {
                                    attendees.any { it.trim() == nickname.trim() }
                                }
                                //수정
                                if(editing.value) {
                                    Button(
                                        elevation = ButtonDefaults.buttonElevation(
                                            defaultElevation = 10.dp
                                        ),
                                        shape = RoundedCornerShape(6.dp),
                                        onClick = {
                                            //edit
                                            editEventViewModel.editEvent(eventId, title, description, location, time)
                                            editing.value = false
                                            Toast.makeText(context, "수정을 완료하였습니다.", Toast.LENGTH_SHORT).show()

                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                        ),
                                        modifier = Modifier.align(alignment = Center)
                                    ) {
                                        Text(
                                            text = "수정 완료",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }else{

                                    //참가 취소 버튼
                                    val showDialog = remember { mutableStateOf(false) }
                                    if (isAttendee) {
                                        Button(
                                            elevation = ButtonDefaults.buttonElevation(
                                                defaultElevation = 10.dp
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            onClick = {
                                                if (userId == ownerId) {
                                                    showDialog.value = true
                                                }else {
                                                    //exit
                                                    exitViewModel.exitEvent(eventId)
                                                    getAttendeesViewModel.loadAttendees(eventId)
                                                    navController.navigate(SparcsScreen.Start.name)
                                                    Toast.makeText(context, "팟에서 나갔습니다.", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                                            ),
                                            modifier = Modifier.align(alignment = Center)
                                        ) {
                                            Text(
                                                text = "참가 취소",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }

                                        //이벤트 취소와 삭제
                                        if (showDialog.value) {
                                            AlertDialog(
                                                onDismissRequest = { showDialog.value = false },
                                                title = {
                                                    Text(
                                                    text = "확인",
                                                    color = MaterialTheme.colorScheme.scrim)
                                                        },
                                                text = {
                                                    Text(
                                                    text = "최초 개설자가 나가게 되면 팟이 삭제됩니다.\n정말로 참가 취소하시겠습니까?",
                                                    color = MaterialTheme.colorScheme.scrim)
                                                       },
                                                confirmButton = {
                                                    Button(
                                                        onClick = {
                                                            showDialog.value = false
                                                            exitViewModel.exitEvent(eventId)
                                                            getAttendeesViewModel.loadAttendees(eventId)
                                                            navController.navigate(SparcsScreen.Start.name)
                                                            deleteEventViewModel.deleteEvent(eventId)
                                                            Toast.makeText(context, "팟에서 나갔습니다.", Toast.LENGTH_SHORT).show()
                                                        }
                                                    ) {
                                                        Text(
                                                            text = "확인",
                                                            color = MaterialTheme.colorScheme.onSurface)
                                                    }
                                                },
                                                dismissButton = {
                                                    Button(
                                                        onClick = { showDialog.value = false }
                                                    ) {
                                                        Text(
                                                            text = "취소",
                                                            color = MaterialTheme.colorScheme.onSurface)
                                                    }
                                                }
                                            )
                                        }
                                    } else {

                                        Button(
                                            elevation = ButtonDefaults.buttonElevation(
                                                defaultElevation = 10.dp
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            onClick = {
                                                joinViewModel.joinEvent(eventId)
                                                getAttendeesViewModel.loadAttendees(eventId)
                                                navController.navigate(SparcsScreen.Start.name)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            ),
                                            modifier = Modifier.align(alignment = Center)
                                        ) {
                                            Text(
                                                text = "참가 신청",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }





    }
}


@Composable
fun EditTextField(
    value : String,
    onValueChange: (String) -> Unit,
    color: Color = MaterialTheme.colorScheme.surface,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    maxlines: Int = 1,
    icon : Painter?
){
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_small))
                        .size(dimensionResource(R.dimen.padding_medium))
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                maxLines = maxlines,
                textStyle = style.copy(
                    color = color
                ),
                cursorBrush = SolidColor(color),
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_small),
                        bottom = dimensionResource(R.dimen.padding_small)
                    ),
            )
        }
    }
}

@Composable
fun EmojiSelectionRow(eventId: String) {
    val context = LocalContext.current
    val getCommentViewModel: GetCommentViewModel = viewModel(factory = AppViewModelFactory(context))
    val addCommentViewModel: AddCommentViewModel = viewModel(factory = AppViewModelFactory(context))
    val emojiCount by getCommentViewModel.emojiCount.collectAsState()

    val expanded = remember { mutableStateOf(false) }


    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { expanded.value = !expanded.value }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "이모티콘 추가",
                tint = MaterialTheme.colorScheme.scrim
            )
        }

        emojiCount.forEach { (reaction, count) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.scrim
                ),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable {
                        addCommentViewModel.addCommentInfo(eventId, reaction)
                        getCommentViewModel.getCommentInfo(eventId)
                    }
            ) {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = if (count > 1) "$reaction $count" else reaction,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.onSurface)
        ) {
            listOf("👍", "❤️", "😂", "😮", "😢", "👏").forEach { reaction ->
                DropdownMenuItem(
                    text = { Text(reaction) },
                    onClick = {
                        expanded.value = false
                        addCommentViewModel.addCommentInfo(eventId = eventId, content = reaction)
                        getCommentViewModel.getCommentInfo(eventId)
                    },
                    colors = MenuDefaults.itemColors(
                        MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

fun formatDateTimeString(isoString: String): String {
    return if (isoString.contains("T") && isoString.length >= 16) {
        isoString.replace("T", " ").substring(0, 16)
    } else {
        isoString
    }
}




@Preview
@Composable
fun Preview() {


}