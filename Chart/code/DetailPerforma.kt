package com.llc.thelegionpt.fitur.detailperforma

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.llc.thelegionpt.R
import com.llc.thelegionpt.fitur.main.home.barChart
import com.llc.thelegionpt.ui.helper.screenLoading
import com.llc.thelegionpt.ui.helper.spacerH
import com.llc.thelegionpt.ui.helper.spacerV
import com.llc.thelegionpt.ui.theme.BackgroundColor
import com.llc.thelegionpt.ui.theme.GoldColor
import com.llc.thelegionpt.ui.theme.PrimaryColor
import com.llc.thelegionpt.ui.theme.SecondaryTextColor
import com.llc.thelegionpt.utils.Constant

@Composable
fun detailPerformaScreen(
    navController: NavController,
    detailVM: DetailPerformaViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        detailVM.getPerforma(1)
        detailVM.detailPerformaTrans(page = 1,status = 1)
    }
    var selected by remember { mutableStateOf(0) }
    val performa by remember { detailVM.performa }
    val isLoading by remember { detailVM.isLoading}
    val transaksi by remember{detailVM.listTrans}
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Detail Performa",
                        color = Color.White
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
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
        Column() {
            spacerV(height = 16.dp)
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text(text = "Performa Saya", fontSize = 18.sp, fontWeight = FontWeight.W700)
                Text(text = "Detail performa saya", color = SecondaryTextColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                val list = listOf(
                    "Minggu Ini",
                    "Bulan Ini",
                    "Tahun Ini"
                )
                items(list.size) {
                    Button(
                        onClick = {
                            selected = it
                            detailVM.getPerforma(it + 1)
                            detailVM.detailPerformaTrans(page = 1,status = it+1)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = if (selected == it) Color.White else BackgroundColor),
                        elevation = null,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = list[it],
                            color = if (selected == it) MaterialTheme.colors.surface else Color.White
                        )
                    }
                    if (it != list.size - 1) spacerH(width = 4.dp)
                }
            }
            spacerV(height = 16.dp)
            Card(
                shape = RoundedCornerShape(8.dp), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Jumlah Pelanggan", fontSize = 12.sp)
                        Row {
                            Text(
                                text = "${performa?.total ?: 0}",
                                fontWeight = FontWeight.W600
                            )
                            Text(text = " Orang")
                        }
                    }
                    spacerV(height = 12.dp)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        performa?.let {
                            val max = it.total ?: 0
                            it.detail?.forEachIndexed { index, it ->
                                barChart(
                                    max = if (max > 0) max else 1,
                                    jumlah = it.total ?: 0,
                                    color = if ((index + 1) % 2 == 0) GoldColor else PrimaryColor,
                                    hari = it.name.orEmpty()
                                )
                                spacerH(width = 8.dp)
                            }
                        }
                    }
                }
            }
            spacerV(height = 16.dp)
            Text(
                text = "Daftar Private",
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
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
                        }
                    }
                    spacerV(height = 12.dp)
                }
            }
        }
        screenLoading(loading = isLoading)
    }
}