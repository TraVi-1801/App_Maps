package com.vic.project.app_maps.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vic.project.app_maps.data.model.LocationData
import com.vic.project.app_maps.presentation.theme.Gray_10
import com.vic.project.app_maps.presentation.theme.Gray_100
import com.vic.project.app_maps.presentation.theme.Gray_40
import com.vic.project.app_maps.presentation.theme.Gray_80
import com.vic.project.app_maps.presentation.theme.appTypography
import com.vic.project.app_maps.presentation.theme.warningMain
import com.vic.project.app_maps.utils.ModifierExtension.clickableSingle
import com.vic.project.app_maps.utils.ModifierExtension.shadowCustom


@Composable
fun ResultSearchLocation(
    title: String,
    list: List<LocationData>,
    onItemClick: (LocationData) -> Unit,
    onClose: () -> Unit,
    onStart: (LocationData) -> Unit,
    onDirections: (LocationData) -> Unit,
) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Text(
                    text = title,
                    style = appTypography.titleMedium,
                    fontWeight = FontWeight.W700,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    color = Gray_100,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    tint = Gray_100,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Gray_40,CircleShape)
                        .clickableSingle{
                            onClose.invoke()
                        }
                        .padding(8.dp)
                )
            }
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){

                item {
                    Spacer(Modifier.height(12.dp))
                }
                items(list){
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadowCustom(
                                color = Gray_100.copy(0.12f),
                                offsetX = 0.dp,
                                offsetY = 2.dp,
                                blurRadius = 4.dp,
                                borderRadius = 16.dp,
                                spread = 0.dp
                            )
                            .background(
                                Gray_10,
                                RoundedCornerShape(16.dp)
                            )
                            .clickableSingle{onItemClick.invoke(it)}
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        Text(
                            text = it.name,
                            style = appTypography.titleMedium,
                            fontWeight = FontWeight.W700,
                            color = Gray_100
                        )
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ){
                            if (it.rating != null && it.rating > 0.0f) {
                                Text(
                                    text = it.rating.toString(),
                                    style = appTypography.labelMedium,
                                    fontWeight = FontWeight.W400,
                                    color = Gray_80
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "",
                                    tint = warningMain,
                                    modifier = Modifier.size(14.dp)
                                )

                                Text(
                                    text = "(${it.user_ratings_total})",
                                    style = appTypography.labelMedium,
                                    fontWeight = FontWeight.W400,
                                    color = Gray_80
                                )

                            } else {
                                Text(
                                    text = "No Riews",
                                    style = appTypography.labelMedium,
                                    fontWeight = FontWeight.W400,
                                    color = Gray_80
                                )
                            }
                            Spacer(Modifier.size(2.dp).clip(CircleShape).background(Gray_80,CircleShape))
                            Text(
                                text = it.types.firstOrNull() ?: "",
                                style = appTypography.labelMedium,
                                fontWeight = FontWeight.W400,
                                color = Gray_80
                            )
                        }
                        Text(
                            text = it.formatted_address,
                            style = appTypography.labelLarge,
                            fontWeight = FontWeight.W400,
                            color = Gray_80
                        )
                    }
                }
            }
        }
}