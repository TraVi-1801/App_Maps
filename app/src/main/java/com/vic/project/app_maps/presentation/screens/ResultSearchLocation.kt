package com.vic.project.app_maps.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vic.project.app_maps.data.model.LocationData
import com.vic.project.app_maps.presentation.theme.Gray_10
import com.vic.project.app_maps.presentation.theme.Gray_100
import com.vic.project.app_maps.presentation.theme.Gray_20
import com.vic.project.app_maps.presentation.theme.Gray_40
import com.vic.project.app_maps.presentation.theme.Gray_80
import com.vic.project.app_maps.presentation.theme.appTypography
import com.vic.project.app_maps.presentation.theme.primaryMain
import com.vic.project.app_maps.presentation.theme.warningMain
import com.vic.project.app_maps.utils.ModifierExtension.clickableSingle
import com.vic.project.app_maps.utils.ModifierExtension.shadowCustom
import kotlin.collections.ifEmpty


@Composable
fun ResultSearchLocation(
    title: String,
    iLoad: Boolean,
    list: List<LocationData>,
    onClose: () -> Unit,
    onDirections: (LocationData) -> Unit,
) {

    val shimmerColors = listOf(
        Gray_20,
        primaryMain,
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        AnimatedVisibility(iLoad) {
            Spacer(
                Modifier
                    .padding(bottom = 8.dp)
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(brush)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = appTypography.titleLarge,
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
                    .background(Gray_40, CircleShape)
                    .clickableSingle {
                        onClose.invoke()
                    }
                    .padding(8.dp)
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Spacer(Modifier.height(12.dp))
            }
            if (list.isEmpty()){
                item {
                    Text(
                        text = "No data",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.W500,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    )
                }
            }
            items(list) {
                Column(
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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = it.name,
                        style = appTypography.titleMedium,
                        fontWeight = FontWeight.W700,
                        color = Gray_100
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
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
                        Spacer(
                            Modifier
                                .size(2.dp)
                                .clip(CircleShape)
                                .background(Gray_80, CircleShape)
                        )
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

                    Text(
                        text = "Directions",
                        style = appTypography.labelLarge,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center,
                        color = Gray_10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableSingle {
                                onDirections.invoke(it)
                            }
                            .background(primaryMain, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    )
                }
            }

            item {
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}