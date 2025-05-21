package com.example.sparcs_new.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.sparcs_new.DTO.EventResponseDTO
import com.example.sparcs_new.viewModel.AppViewModelFactory
import com.example.sparcs_new.viewModel.JoinEventViewModel
import com.example.sparcs_new.viewModel.PostEventState
import com.example.sparcs_new.viewModel.PostEventViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparcs_new.R
import java.util.Calendar

@Composable
fun NewEvent() {
    val postEventViewModel: PostEventViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
    val joinViewModel: JoinEventViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))

    val context = LocalContext.current
    var titleValue by remember { mutableStateOf("") }
    var descriptionValue by remember { mutableStateOf("") }
    var dateTimeValue by remember { mutableStateOf("") }
    var locationValue by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val postState by postEventViewModel.postState.collectAsState()

    val buttonFocusRequester = remember { FocusRequester() }

    LaunchedEffect(postState) {
        when (val currentState = postState) {
            is PostEventState.Success -> {
                val response: EventResponseDTO = currentState.response
                joinViewModel.joinEvent(response.event_id)
                titleValue = ""
                dateTimeValue = ""
                locationValue = ""
                descriptionValue = ""
                Toast.makeText(context, "이벤트가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                postEventViewModel.resetPostStateToIdle()
            }
            is PostEventState.Error -> {
                Toast.makeText(context, "문제가 발생하였습니다. ${currentState.message}", Toast.LENGTH_SHORT).show()
                postEventViewModel.resetPostStateToIdle()
            }
            PostEventState.Loading -> {}
            PostEventState.Idle -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        CardInput(
            value = titleValue,
            onValueChange = { titleValue = it },
            placeholderText = "제목을 입력해주세요.",
            icon = Icons.Default.Menu
        )
        Spacer(modifier = Modifier.height(16.dp))
        CardInput(
            value = descriptionValue,
            onValueChange = { descriptionValue = it },
            placeholderText = "내용을 입력해주세요.",
            icon = Icons.Default.Edit,
            maxLines = 10
        )
        Spacer(modifier = Modifier.height(16.dp))
        DateTimeInput(
            value = dateTimeValue,
            onValueChange = { dateTimeValue = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CardInput(
            value = locationValue,
            onValueChange = { locationValue = it },
            placeholderText = "위치를 입력해주세요.",
            icon = Icons.Default.LocationOn
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
            shape = RoundedCornerShape(6.dp),
            onClick = {
                if (titleValue == "" || descriptionValue == "" || dateTimeValue == "" || locationValue == "") {
                    Toast.makeText(context, "칸을 전부 작성해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    postEventViewModel.postEvent(titleValue, dateTimeValue, locationValue, descriptionValue)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .focusRequester(buttonFocusRequester)
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 50.dp,
                    end = 50.dp
                )
        ) {
            Text(
                text = "방 개설하기",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun CardInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    icon: ImageVector,
    maxLines: Int = 1
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.scrim)
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                maxLines = maxLines,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.scrim),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.surface),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(text = placeholderText, color = MaterialTheme.colorScheme.scrim)
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun DateTimeInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSurface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clickable { showPicker = true }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.scrim)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = value.ifEmpty { "날짜 및 시간을 선택해주세요." },
                color = MaterialTheme.colorScheme.scrim
            )
        }
    }

    if (showPicker) {
        DateTimePickerDialog(
            initialDateTime = value,
            onDismissRequest = { showPicker = false },
            onDateTimeSelected = {
                onValueChange(it)
                showPicker = false
            }
        )
    }
}

@Composable
fun DateTimeInputWithoutCard(
    value: String,
    onValueChange: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
        Text(
            text = value.ifEmpty { "날짜 및 시간을 선택해주세요." },
            color = MaterialTheme.colorScheme.scrim,
            modifier = Modifier.clickable { showPicker = true }
                .padding(start = dimensionResource(R.dimen.padding_small))
        )
    if (showPicker) {
        DateTimePickerDialog(
            initialDateTime = value,
            onDismissRequest = { showPicker = false },
            onDateTimeSelected = {
                onValueChange(it)
                showPicker = false
            },
        )
    }
}


@Composable
fun DateTimePickerDialog(
    initialDateTime: String,
    onDismissRequest: () -> Unit,
    onDateTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()

    if (initialDateTime.isNotEmpty()) {
        try {
            val parts = initialDateTime.split(" ")
            val dateParts = parts[0].split("-").map { it.toInt() }
            val timeParts = parts[1].split(":").map { it.toInt() }
            calendar.set(dateParts[0], dateParts[1] - 1, dateParts[2], timeParts[0], timeParts[1])
        } catch (_: Exception) {}
    }

    var year by remember { mutableStateOf<Int?>(calendar.get(Calendar.YEAR)) }
    var month by remember { mutableStateOf<Int?>(calendar.get(Calendar.MONTH) + 1) }
    var day by remember { mutableStateOf<Int?>(calendar.get(Calendar.DAY_OF_MONTH)) }
    var hour by remember { mutableStateOf<Int?>(calendar.get(Calendar.HOUR)) }
    var minute by remember { mutableStateOf<Int?>(calendar.get(Calendar.MINUTE)) }
    var isAm by remember { mutableStateOf(calendar.get(Calendar.AM_PM) == Calendar.AM) }
    val context = LocalContext.current

    val focusRequesterYear = remember { FocusRequester() }
    val focusRequesterMonth = remember { FocusRequester() }
    val focusRequesterDay = remember { FocusRequester() }
    val focusRequesterHour = remember { FocusRequester() }
    val focusRequesterMinute = remember { FocusRequester() }
    val confirmButtonClicked = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("날짜 및 시간 선택", color = MaterialTheme.colorScheme.scrim) },
        text = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NumberWithLabel(
                        number = year,
                        label = "년",
                        focusRequester = focusRequesterYear,
                        imeAction = ImeAction.Next,
                        onNextFocus = { focusRequesterMonth.requestFocus() },
                        onValueChange = { year = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NumberWithLabel(
                        number = month,
                        label = "월",
                        focusRequester = focusRequesterMonth,
                        imeAction = ImeAction.Next,
                        onNextFocus = { focusRequesterDay.requestFocus() },
                        onValueChange = { month = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NumberWithLabel(
                        number = day,
                        label = "일",
                        focusRequester = focusRequesterDay,
                        imeAction = ImeAction.Next,
                        onNextFocus = { focusRequesterHour.requestFocus() },
                        onValueChange = { day = it}
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AmPmToggle(isAm = isAm, onToggle = { isAm = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    NumberWithLabel(
                        number = hour,
                        label = "시",
                        focusRequester = focusRequesterHour,
                        imeAction = ImeAction.Next,
                        onNextFocus = { focusRequesterMinute.requestFocus() },
                        onValueChange = { hour = it }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    NumberWithLabel(
                        number = minute,
                        label = "분",
                        focusRequester = focusRequesterMinute,
                        imeAction = ImeAction.Done,
                        onNextFocus = {
                            confirmButtonClicked.value = true
                        },
                        onValueChange = { minute = it}
                    )
                }
            }
        },

        confirmButton = {
            TextButton(onClick = {
                fun isLeapYear(year: Int): Boolean {
                    return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
                }

                val hour24 = if (isAm) {
                    if (hour == 12) 0 else (hour ?: 0)
                } else {
                    if (hour == 12) 12 else (hour ?: 0) + 12
                }

                val isValid = year != null && year!! > 0 && month in 1..12 && day in 1..31 && hour in 1..12 && minute in 0..59

                val maxDaysInMonth = when (month) {
                    2 -> if (isLeapYear(year!!)) 29 else 28
                    4, 6, 9, 11 -> 30
                    else -> 31
                }

                val isDateValid = day!! <= maxDaysInMonth


                if (isValid && isDateValid) {
                    val formatted = "%04d-%02d-%02d %02d:%02d".format(year, month, day, hour24, minute)
                    onDateTimeSelected(formatted)
                } else {
                    Toast.makeText(context, "유효한 날짜를 입력하세요.", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("확인", color = MaterialTheme.colorScheme.scrim)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("취소", color = MaterialTheme.colorScheme.scrim)
            }
        }

    )

    LaunchedEffect(confirmButtonClicked.value) {
        if (confirmButtonClicked.value) {
            val hour24 = if (isAm) {
                if (hour == 12) 0 else (hour ?: 0)
            } else {
                if (hour == 12) 12 else (hour ?: 0) + 12
            }
            val formatted = "%04d-%02d-%02d %02d:%02d".format(year, month, day, hour24, minute)
            onDateTimeSelected(formatted)
            confirmButtonClicked.value = false
        }
    }
}

@Composable
fun AmPmToggle(isAm: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.scrim,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        listOf("오전", "오후").forEachIndexed { index, label ->
            val selected = (index == 0 && isAm) || (index == 1 && !isAm)
            Text(
                text = label,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.scrim,
                modifier = Modifier
                    .clickable { onToggle(index == 0) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
            )
        }
    }
}


@Composable
fun NumberWithLabel(
    number: Int?,
    label: String,
    focusRequester: FocusRequester,
    imeAction: ImeAction,
    onNextFocus: () -> Unit,
    onValueChange: (Int?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(80.dp)
    ) {
        BasicTextField(
            value = number?.toString() ?: "",
            onValueChange = { input ->
                if (input.all { it.isDigit() } || input.isEmpty()) {
                    onValueChange(input.toIntOrNull())
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.scrim),
            singleLine = true,
            modifier = Modifier
                .width(40.dp)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNextFocus() },
                onDone = {
                    if (imeAction == ImeAction.Done) onNextFocus()
                }
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, color = MaterialTheme.colorScheme.scrim)
    }
}
