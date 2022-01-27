package com.llc.thelegionpt.fitur.detailriwayat.detailstatus

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.llc.thelegionpt.R
import com.llc.thelegionpt.fitur.detailriwayat.DetailPemesananViewModel
import com.llc.thelegionpt.ui.helper.dividerSmall
import com.llc.thelegionpt.ui.helper.spacerH
import com.llc.thelegionpt.ui.helper.spacerV
import com.llc.thelegionpt.ui.theme.BGCard2
import com.llc.thelegionpt.ui.theme.GoldColor

@Composable
fun detailStatusScreen(
    navController: NavController,
    detailVm: DetailPemesananViewModel = hiltViewModel()
) {

    val data by remember { detailVm.dataTransaksi }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Detail Status Pesanan",
                        color = Color.White
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            actions = {
            },
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            elevation = 0.dp,
        )
    }) {
        Column {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxWidth()
                    .background(color = BGCard2)
            ) {
                spacerV(height = 40.dp)
                Box(Modifier.fillMaxWidth()) {
                    Column(Modifier.align(Alignment.BottomCenter)) {
                        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        Canvas(
                            Modifier
                                .fillMaxWidth(0.65f)
                                .height(3.dp)
                        ) {

                            drawLine(
                                color = GoldColor,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                pathEffect = pathEffect,
                                strokeWidth = 2f
                            )
                        }
                        spacerV(height = 9.dp)
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        cardStatus(
                            status = when {
                                data?.tpIsCancel == true -> Status.Cancel
                                data?.tpIsDone == true -> Status.Selesai
                                data?.tpIsConfirm == true -> Status.Selesai
                                data?.tpIsPaid == true -> Status.Proses
                                else -> Status.OnProgress
                            }
                        )
                        cardStatus(
                            backgroundColor = Color(0xFFF1EFF6),
                            iconCard = R.drawable.onproses,
                            status = when {
                                data?.tpIsCancel == true -> Status.Cancel
                                data?.tpIsDone == true -> Status.Selesai
                                data?.tpIsConfirm == true -> Status.Proses
                                data?.tpIsPaid == true -> Status.OnProgress
                                else -> Status.OnProgress
                            }
                        )
                        cardStatus(
                            backgroundColor = Color(0xFFF0FEF8), iconCard = -1, status = when {
                                data?.tpIsCancel == true -> Status.Cancel
                                data?.tpIsDone == true -> Status.Selesai
                                data?.tpIsConfirm == true -> Status.OnProgress
                                data?.tpIsPaid == true -> Status.OnProgress
                                else -> Status.OnProgress
                            }
                        )
                    }
                }
                spacerV(height = 32.dp)
                data?.tpStatus?.text?.let { it1 ->
                    Text(
                        text = it1,
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp
                    )
                }
                spacerV(height = 40.dp)
            }
            spacerV(height = 24.dp)
            Text(
                text = "Status Pesanan",
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            spacerV(height = 12.dp)
            dividerSmall()
            spacerV(height = 16.dp)
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                itemsIndexed(data?.log.orEmpty().reversed()) { index, it ->
                    Box {
                        if (index!=data?.log.orEmpty().size-1){
                            Column {
                                spacerV(height = 10.dp)
                                Row (Modifier.fillMaxSize()){
                                    spacerH(width = 5.dp)
                                    Spacer(
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(1.dp)
                                            .background(
                                                color = if (0 == index) GoldColor
                                                else Color(0xFF58595C)
                                            )
                                    )
                                }
                            }
                        }
                        Column {
                            Row(verticalAlignment = Alignment.Top) {
                                val color = if (it.type?.logRedFlags == 1) Color.Red else {
                                    if (0 == index) GoldColor
                                    else Color(0xFFC4C4C4)
                                }
                                val colortext = if (it.type?.logRedFlags == 1) Color.Red else {
                                    if (0 == index) GoldColor
                                    else Color.White
                                }
                                Card(
                                    shape = CircleShape, modifier = Modifier
                                        .size(12.dp)
                                        .offset(y = 3.dp), backgroundColor = color
                                ) { }
                                spacerH(width = 12.dp)
                                Column {
                                    Text(text = it.type?.logTitle.orEmpty(), color = colortext)
                                    spacerV(height = 6.dp)
                                    Text(text = it.type?.logBody.orEmpty(), fontSize = 12.sp)
                                }
                            }
                            spacerV(height = 16.dp)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun cardStatus(
    backgroundColor: Color = Color(0xFFFFFAEB),
    isRedLine: Boolean = false,
    status: Status = Status.Selesai,
    iconCard: Int = R.drawable.pesanan_baru
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = backgroundColor,
            modifier = Modifier.size(64.dp),
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (iconCard != -1) {
                    Image(
                        painter = painterResource(id = iconCard),
                        contentDescription = "",
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Card(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        backgroundColor = Color.Green
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }
        }
        spacerV(height = 12.dp)
        Column {
            if (status != Status.OnProgress) Card(
                modifier = Modifier.size(24.dp),
                backgroundColor = when (status) {
                    Status.Selesai -> Color.Green
                    Status.Cancel -> Color.Red
                    else -> GoldColor
                },
                shape = CircleShape
            ) {
                Icon(
                    imageVector = when (status) {
                        Status.Selesai -> Icons.Default.Check
                        Status.Cancel -> Icons.Default.Close
                        else -> Icons.Default.Refresh
                    }, contentDescription = ""
                )
            }
            else {
                spacerV(height = 4.dp)
                Card(
                    shape = CircleShape,
                    modifier = Modifier.size(16.dp),
                    backgroundColor = Color(0xFFC4C4C4)
                ) {}
            }
        }

    }
}

enum class Status {
    Proses,
    Selesai,
    Cancel,
    OnProgress,
}