package com.example.sparcs_new.ui.theme.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.sparcs_new.R


@Composable
fun DialogPop(
    onDismissRequest: () -> Unit,
    title : String,
    time : String,
    location : String,
    attendees : List<String>,
    description : String,
    isLoading: Boolean,
    error: String?
    ){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(10.dp)
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
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.scrim,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(
                                    start = dimensionResource(R.dimen.padding_small),
                                    end = dimensionResource(R.dimen.padding_small),
                                    bottom = dimensionResource(R.dimen.padding_medium)
                                )
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.scrim,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 2.dp)
                                .clickable {
                                    onDismissRequest()
                                }
                        )
                    }
                        Column {
                            EventInformation(
                                string = time,
                                icon = painterResource(R.drawable.calendar)
                            )
                            EventInformation(
                                string = location,
                                icon = painterResource(R.drawable.location)
                            )
                            Row{

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
                                            string = "None",
                                            icon = painterResource(R.drawable.group)
                                        )
                                        EventInformation(
                                            string = "0",
                                            icon = painterResource(R.drawable.group)
                                        )
                                    }

                                    else -> {
                                        EventInformation(
                                            string = attendees.toString(),
                                            icon = painterResource(R.drawable.group)
                                        )
                                        EventInformation(
                                            string = (attendees.size).toString(),
                                            icon = painterResource(R.drawable.group)
                                        )
                                    }
                                }
                            }
                            EventInformation(
                                string = "감정",
                                icon = painterResource(R.drawable.emotion)
                            )
                        }



                    }

                    Column (
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onSurface)
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_small))
                            .height(100.dp)
                            .verticalScroll(rememberScrollState())
                        //contentPadding = it
                    ){
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.scrim
                        )


                    }

                    Box (
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onSurface)
                            .fillMaxWidth()
                    ){
                        Button(
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 10.dp
                            ),
                            shape = RoundedCornerShape(6.dp),
                            onClick = {

                                //참가 여부에 따라 색과 텍스트가 달라져야 함

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


@Preview
@Composable
fun Preview(){

}