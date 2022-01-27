package com.llc.thelegionpt.fitur.main.privatetrainer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.llc.thelegionpt.MainViewModel
import com.llc.thelegionpt.R
import com.llc.thelegionpt.ui.helper.screenLoading
import com.llc.thelegionpt.ui.helper.spacerH
import com.llc.thelegionpt.ui.helper.spacerV
import com.llc.thelegionpt.ui.theme.IconColor
import com.llc.thelegionpt.ui.theme.PrimaryColor
import com.llc.thelegionpt.ui.theme.SecondaryTextColor
import com.llc.thelegionpt.utils.Constant
import com.llc.thelegionpt.utils.toFormattedDate
import com.llc.thelegionpt.utils.toFormattedString
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun privateTrainerScreen(
    navController: NavHostController,
    vm: MainViewModel,
    privateVm: PrivateTrainerViewModel = hiltViewModel()
) {
    val dataUser by remember {
        vm.dataUser
    }
    val cal by remember {
        mutableStateOf(Calendar.getInstance(Locale.getDefault()))
    }
    var monthText by remember {
        mutableStateOf(Date().toFormattedString("MMMM YYYY"))
    }
    var dates by remember {
        mutableStateOf((cal.getActualMinimum(Calendar.DAY_OF_MONTH)..cal.getActualMaximum(Calendar.DAY_OF_MONTH)).map {
            cal.set(Calendar.DAY_OF_MONTH, it)
            cal.time
        })
    }
    var selected by remember {
        mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    }
    val transaksi by remember {
        privateVm.listTransaksi
    }
    val isLoading by remember {
        privateVm.isLoading
    }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        spacerV(height = 16.dp)
        trainerCardHorizontal(
            navController = navController,
            modifier = Modifier.padding(horizontal = 24.dp),
            trainer = dataUser?.trainer
        )
        spacerV(height = 24.dp)
        Text(
            text = "Jadwal Private",
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 24.dp)
        )
        spacerV(height = 16.dp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                cal.add(Calendar.MONTH, -1)
                dates =
                    (cal.getActualMinimum(Calendar.DAY_OF_MONTH)..cal.getActualMaximum(Calendar.DAY_OF_MONTH)).map {
                        cal.set(Calendar.DAY_OF_MONTH, it)
                        cal.time
                    }
                monthText = cal.time.toFormattedString("MMMM YYYY")
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "",
                    tint = IconColor
                )
            }
            spacerH(width = 36.dp)
            Text(text = monthText, color = SecondaryTextColor)
            spacerH(width = 36.dp)
            IconButton(onClick = {
                cal.add(Calendar.MONTH, 1)
                dates =
                    (cal.getActualMinimum(Calendar.DAY_OF_MONTH)..cal.getActualMaximum(Calendar.DAY_OF_MONTH)).map {
                        cal.set(Calendar.DAY_OF_MONTH, it)
                        cal.time
                    }
                monthText = cal.time.toFormattedString("MMMM YYYY")
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "",
                    tint = IconColor
                )
            }
        }
        val state = rememberLazyListState()
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), state = state) {
            itemsIndexed(dates) { i, d ->
                Card(
                    modifier = Modifier
                        .size(60.dp, 80.dp)
                        .clickable { selected = i + 1 },
                    backgroundColor = if ((i + 1) == selected) PrimaryColor else MaterialTheme.colors.surface,
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = d.toFormattedString("EEE"),
                            fontSize = 12.sp,
                            color = if ((i + 1) == selected) Color.White else SecondaryTextColor
                        )
                        Text(text = d.toFormattedString("dd"), color = Color.White)
                    }
                }
                spacerH(width = 16.dp)
            }
        }
        LaunchedEffect(key1 = true) {
            state.animateScrollToItem(index = selected - 1)
        }
        LaunchedEffect(key1 = dates) {
            privateVm.jadwalTrainerHome(
                dates.getOrNull(selected - 1)?.toFormattedString("dd-MM-yyyy").orEmpty()
            )
        }
        LaunchedEffect(key1 = selected) {
            privateVm.cariTransaksibyDate(
                dates.getOrNull(selected - 1)?.toFormattedString("dd-MM-yyyy").orEmpty()
            )
        }
        spacerV(height = 8.dp)
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
            item {
                spacerV(height = 8.dp)
            }
            items(transaksi) {
                Card(shape = RoundedCornerShape(8.dp), modifier = Modifier
                    .clickable {
                        navController.navigate("${Constant.Route.detailPemesananTrainer}/${it.tpId}")
                    }
                    .fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Box(
                                Modifier
                                    .background(color = PrimaryColor, shape = CircleShape)
                                    .size(40.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_jam),
                                    contentDescription = "",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            spacerH(width = 10.dp)
                            Column {
                                Text(
                                    text = it.hargaTrainer?.htKategory?.khtNama.orEmpty(),
                                    fontWeight = FontWeight.W500
                                )
                                Text(
                                    text = it.getJadwalWithWIB(),
                                    fontSize = 10.sp
                                )
                            }
                        }
                        if (it.getDiffMilis() > 0) Text(
                            text = buildAnnotatedString {
                                append("Dimulai dalam\n")
                                val hours = TimeUnit.MILLISECONDS.toHours(it.getDiffMilis())
                                var minutes = TimeUnit.MILLISECONDS.toMinutes(it.getDiffMilis())
                                val seconds = TimeUnit.MILLISECONDS.toSeconds(it.getDiffMilis())
                                if (hours > 0) {
                                    append("$hours Jam")
                                    minutes %=60
                                }
                                if (minutes > 0) append(" $minutes menit")
                                else append("$seconds detik")
                                append(" lagi")
                            },
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp,
                            color = SecondaryTextColor
                        )
                        if (it.getDiffMilisBerlangsung() > 0) Text(
                            text = "Sedang berlangsung",
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp,
                            color = SecondaryTextColor
                        )
                    }
                }
                spacerV(height = 12.dp)
            }
        }
    }
    screenLoading(loading = isLoading)
}