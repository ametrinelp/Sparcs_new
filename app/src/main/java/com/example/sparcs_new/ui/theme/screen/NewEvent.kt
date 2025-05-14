package com.example.sparcs_new.ui.theme.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sparcs_new.R

@Composable
fun NewEvent() {
    var title by remember { mutableStateOf("") }
    var context by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    Surface (color = MaterialTheme.colorScheme.background ){
        Column(modifier = Modifier.fillMaxSize()) {
            CardView("title","제목을 입력해주세요.", icon = Icons.Default.Menu, maximum = 70)
            Spacer(modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)))
            CardView("description","내용을 입력해주세요.", icon = Icons.Default.Edit, maximum = 400)
            Spacer(modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)))
            CardView("date","날짜", icon = Icons.Default.DateRange, maximum = 70)
            Spacer(modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small)))
            CardView("location","위치", icon = Icons.Default.LocationOn, maximum = 70)
            Button(
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp
                ),
                shape = RoundedCornerShape(6.dp),
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_small),start = 50.dp, end = 50.dp)
            ) {
                Text(
                    text = "방 개설하기",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.scrim
                )
            }
        }
    }
}

@Composable
fun CardView(
    string: String,
    text: String,
    icon: ImageVector,
    maximum: Int){
    var textvalue by remember(string) { mutableStateOf("") }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp, top = 5.dp, bottom = 5.dp).heightIn(max = maximum.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        BasicTextField(
            value = textvalue,
            onValueChange = {
                textvalue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.scrim),
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                    if (textvalue.isEmpty()) {
                        Text(
                            text = "${text}",
                            style = TextStyle(color = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Preview
@Composable
fun prev(){
    NewEvent()
}