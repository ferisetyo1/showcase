package com.llc.thelegionpt.fitur.main.privatetrainer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.llc.thelegionpt.data.network.response.Trainer
import com.llc.thelegionpt.ui.helper.spacerV
import com.llc.thelegionpt.ui.helper.star
import com.llc.thelegionpt.ui.theme.GrayColor
import com.llc.thelegionpt.utils.Constant
import com.llc.thelegionpt.utils.toCurrencyK

@Composable
fun trainerCardHorizontal(
    navController: NavController,
    modifier: Modifier = Modifier,
    trainer: Trainer? = null
) {
    trainer?.let {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Constant.Route.detailTrainer+"/"+trainer.ptId) }) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .aspectRatio(1f)
                            .background(color = Color.White)
                    ) {
                        Image(
                            painter = rememberImagePainter(it.ptImage.orEmpty()),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = it.ptNama.orEmpty(),
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "",
                                tint = GrayColor,
                                modifier = Modifier
                                    .width(10.dp)
                                    .height(10.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = it.ptKota.orEmpty(),
                                color = GrayColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.W400,
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        if ((it.ptStarCount ?: 0.0) > 0.0) star(value = it.ptStarCount ?: 0.0)
                        Spacer(modifier = Modifier.height(4.dp))
                        it.ptDefaultHarga?.let {
                            Text(
                                text = (it.htHarga
                                    ?: 0.0).toCurrencyK() + "k / ${it.htWaktu} Menit",
                                fontWeight = FontWeight.W500,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                    }
                }

            }
        }
    }

}