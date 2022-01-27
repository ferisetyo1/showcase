package com.llc.thelegionpt.fitur.chatting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.llc.thelegionpt.MainViewModel
import com.llc.thelegionpt.R
import com.llc.thelegionpt.data.network.response.GroupMsg
import com.llc.thelegionpt.data.network.response.TransaksiPrivate
import com.llc.thelegionpt.ui.helper.dividerSmall
import com.llc.thelegionpt.ui.helper.editText
import com.llc.thelegionpt.ui.helper.spacerH
import com.llc.thelegionpt.ui.helper.spacerV
import com.llc.thelegionpt.ui.theme.SecondaryTextColor
import com.llc.thelegionpt.utils.showToast
import com.llc.thelegionpt.utils.toFormattedDate
import com.llc.thelegionpt.utils.toFormattedString
import java.util.*

@Composable
fun roomPesanScreen(
    navController: NavHostController,
    vm: MainViewModel,
    roomchatVM: RoomChatViewModel = hiltViewModel()
) {
    val dataTransaksi by remember {
        mutableStateOf(
            navController.previousBackStackEntry?.savedStateHandle?.get<TransaksiPrivate>(
                "transaksi"
            )
        )
    }
    LaunchedEffect(key1 = dataTransaksi) {
        dataTransaksi?.let { roomchatVM.initPesan(it) }
    }
    val listPesan by remember {
        roomchatVM.listChat
    }
    var input by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = dataTransaksi?.customer?.name.orEmpty(),
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

        Column(Modifier.fillMaxSize()) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    reverseLayout = true,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { spacerV(height = 16.dp) }
                    listPesan.groupBy { it.tgl.orEmpty() }.map {
                        val isHariini = Date().toFormattedString("yyyy-MM-dd") == it.key
                        val cal = Calendar.getInstance()
                        cal.add(Calendar.DATE, -1)
                        val isKemarin = cal.time.toFormattedString("yyyy-MM-dd") == it.key
                        GroupMsg(
                            it.value,
                            if (isKemarin) "Kemarin" else if (isHariini) "Hari ini" else it.key.toFormattedDate(
                                "yyyy-MM-dd",
                                "dd MMM yyyy"
                            )
                        )
                    }.forEach {
                        items(it.pesan.orEmpty().sortedByDescending { it.date }) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (it.userRole == "trainer") Arrangement.End else Arrangement.Start
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(0.7f),
                                    horizontalArrangement = if (it.userRole == "trainer") Arrangement.End else Arrangement.Start
                                ) {
                                    Card(
                                        shape = RoundedCornerShape(
                                            topStart = if (it.userRole == "trainer") 8.dp else 0.dp,
                                            bottomStart = 8.dp,
                                            bottomEnd = 8.dp,
                                            topEnd = if (it.userRole == "trainer") 0.dp else 8.dp
                                        ),
                                    ) {
                                        Row(
                                            Modifier
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.Bottom,
                                        ) {
                                            Row(modifier = Modifier.weight(1f,false)){ Text(text = it.body.orEmpty()) }
                                            spacerH(width = 12.dp)
                                            Text(
                                                text = Date(it.date ?: 0).toFormattedString("HH:mm"),
                                                color = SecondaryTextColor,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                }

                            }
                            spacerV(height = 12.dp)
                        }
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                dividerSmall()
                                it.tgl?.let { it1 ->
                                    Text(
                                        text = it1,
                                        modifier = Modifier
                                            .background(color = MaterialTheme.colors.surface)
                                            .padding(horizontal = 8.dp, vertical = 8.dp),
                                    )
                                }
                            }

                        }
                    }

                }
            }
            dividerSmall()
            spacerV(height = 10.dp)
            Row(Modifier.padding(start = 16.dp, end = 4.dp)) {
                Row(Modifier.weight(1f)) {
                    editText(
                        label = "",
                        hint = "Tulis pesan anda...",
                        input = input,
                        onChange = { text -> input = text })
                }
                IconButton(onClick = {
                    if (input.isNotEmpty()) roomchatVM.sendMessage(input,
                        { input = "" },
                        { context.showToast(it) })
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_fi_send),
                        contentDescription = ""
                    )
                }
            }
            spacerV(height = 16.dp)
        }
    }
}
