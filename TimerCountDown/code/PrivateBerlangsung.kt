package com.llc.thelegionpt.fitur

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.llc.thelegionpt.data.network.response.TransaksiPrivate
import com.llc.thelegionpt.fitur.detailriwayat.detailjadwalprivate.showDetailTransaksi
import com.llc.thelegionpt.fitur.detailriwayat.DetailPemesananViewModel
import com.llc.thelegionpt.ui.helper.circularProgressBar
import com.llc.thelegionpt.ui.helper.spacerH
import com.llc.thelegionpt.ui.helper.spacerV
import com.llc.thelegionpt.ui.theme.GoldColor
import com.llc.thelegionpt.ui.theme.GrayColor
import com.llc.thelegionpt.ui.theme.PrimaryColor
import com.llc.thelegionpt.ui.theme.SecondaryTextColor

@Composable
fun privateBerlangsung(navController: NavController, detailVm: DetailPemesananViewModel) {
    val dataTransaksi by remember { detailVm.dataTransaksi }
    val minutes by remember {
        detailVm.minute
    }
    val hours by remember {
        detailVm.hour
    }
    val seconds by remember {
        detailVm.second
    }
    val progress by remember {
        detailVm.progress
    }
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) showDialogDetailTransaksi(dataTransaksi = dataTransaksi) { showDialog = false }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Private Berlangsung",
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
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            elevation = 0.dp,
        )
    }) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            dataTransaksi?.let {
                Column {
                    spacerV(height = 16.dp)
                    Row(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        it.trainer?.let {
                            Card(modifier = Modifier.weight(1f)) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Private Trainer",
                                        color = SecondaryTextColor,
                                        fontSize = 10.sp
                                    )
                                    Text(text = it.ptNama.orEmpty())
                                }
                            }
                        }
                        spacerH(width = 12.dp)
                        it.customer?.let {
                            Card(modifier = Modifier.weight(1f)) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Pelanggan",
                                        color = SecondaryTextColor,
                                        fontSize = 10.sp
                                    )
                                    Text(text = it.name.orEmpty())
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    circularProgressBar(brush = Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryColor,
                            GoldColor,
                        )
                    ), precentage = progress, modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .aspectRatio(1f), text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = GrayColor, fontSize = 12.sp)) {
                            append("Sisa Waktu\n")
                        }
                        if (hours > 0) {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.W600
                                )
                            ) {
                                append(hours.toString())
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600
                                )
                            ) {
                                append("h  ")
                            }
                        }
                        if (minutes > 0) {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.W600
                                )
                            ) {
                                append(minutes.toString())
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600
                                )
                            ) {
                                append("m  ")
                            }
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.W600
                            )
                        ) {
                            append(seconds.toString())
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W600
                            )
                        ) {
                            append("s")
                        }
                        append("\n\n")
                        withStyle(style = SpanStyle(color = GrayColor, fontSize = 12.sp)) {
                            append("Total Durasi : ${it.hargaTrainer?.htWaktu} Menit")
                        }
                    })
                }
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Privat akan otomatis selesai jika timer countdown selesai atau pelanggan menekan tombol selesai.")
                    OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Kirim Pesan", color = Color.White)
                    }
                    spacerV(height = 16.dp)
                }
            }
        }
    }
}

@Composable
fun showDialogDetailTransaksi(
    dataTransaksi: TransaksiPrivate?,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(8.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Detail Private")
                spacerV(height = 16.dp)
                showDetailTransaksi(dataTranksaksi = dataTransaksi)
                spacerV(height = 16.dp)
                Button(onClick = onDismiss,shape = RoundedCornerShape(8.dp)) {
                    Text(text = "OK",color = Color.White)
                }
            }
        }
    }
}