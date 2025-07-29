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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vic.project.app_maps.R
import com.vic.project.app_maps.data.model.DirectionData
import com.vic.project.app_maps.presentation.theme.Gray_10
import com.vic.project.app_maps.presentation.theme.Gray_100
import com.vic.project.app_maps.presentation.theme.Gray_20
import com.vic.project.app_maps.presentation.theme.Gray_40
import com.vic.project.app_maps.presentation.theme.Gray_80
import com.vic.project.app_maps.presentation.theme.appTypography
import com.vic.project.app_maps.presentation.theme.primaryMain
import com.vic.project.app_maps.utils.ModifierExtension.clickableSingle

@Composable
fun DirectionLocation(
    iLoad: Boolean,
    directionDataTW: DirectionData?,
    directionDataDrive: DirectionData?,
    directionDataWalk: DirectionData?,
    currentModel: ModelDirection,
    onClose: () -> Unit,
    onChangeModel: (ModelDirection) -> Unit,
    onStart: () -> Unit,
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
                text = currentModel.name,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            directionDataDrive?.let {
                Row (
                    modifier = Modifier
                        .weight(1f)
                        .clickableSingle {
                        onChangeModel.invoke(ModelDirection.Driving)
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_car),
                        contentDescription = "",
                        tint = if (currentModel == ModelDirection.Driving) primaryMain else Gray_80,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = it.legs?.firstOrNull()?.duration?.text ?: "NA",
                        style = appTypography.labelLarge,
                        fontWeight = FontWeight.W400,
                        color = if (currentModel == ModelDirection.Driving) primaryMain else Gray_80,
                    )
                }

            }
            directionDataTW?.let {

                Row (
                    modifier = Modifier
                        .weight(1f)
                        .clickableSingle {
                            onChangeModel.invoke(ModelDirection.Two_wheeler)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bicycle),
                        contentDescription = "",
                        tint = if (currentModel == ModelDirection.Two_wheeler) primaryMain else Gray_80,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = it.legs?.firstOrNull()?.duration?.text ?: "NA",
                        style = appTypography.labelLarge,
                        fontWeight = FontWeight.W400,
                        color = if (currentModel == ModelDirection.Two_wheeler) primaryMain else Gray_80,
                    )
                }
            }
            directionDataWalk?.let {
                Row (
                    modifier = Modifier
                        .weight(1f)
                        .clickableSingle {
                            onChangeModel.invoke(ModelDirection.Walking)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_walk),
                        contentDescription = "",
                        tint = if (currentModel == ModelDirection.Walking) primaryMain else Gray_80,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = it.legs?.firstOrNull()?.duration?.text ?: "NA",
                        style = appTypography.labelLarge,
                        fontWeight = FontWeight.W400,
                        color = if (currentModel == ModelDirection.Walking) primaryMain else Gray_80,
                    )
                }
            }

        }

        Text(
            text = when (currentModel) {
                ModelDirection.Driving -> {
                    "Distance: ${directionDataDrive?.legs?.firstOrNull()?.duration?.text ?: "NA"} (${directionDataDrive?.legs?.firstOrNull()?.distance?.text ?: "NA"})"
                }

                ModelDirection.Walking -> {
                    "Distance: ${directionDataWalk?.legs?.firstOrNull()?.duration?.text ?: "NA"} (${directionDataWalk?.legs?.firstOrNull()?.distance?.text ?: "NA"})"
                }

                ModelDirection.Two_wheeler -> {
                    "Distance: ${directionDataTW?.legs?.firstOrNull()?.duration?.text ?: "NA"} (${directionDataTW?.legs?.firstOrNull()?.distance?.text ?: "NA"})"
                }
            },
            style = appTypography.bodyMedium,
            fontWeight = FontWeight.W500,
            color = Gray_100,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Text(
            text = "Start",
            style = appTypography.labelLarge,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center,
            color = Gray_10,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickableSingle {
                    onStart.invoke()
                }
                .background(primaryMain, RoundedCornerShape(16.dp))
                .padding(12.dp)
        )
    }
}

enum class ModelDirection(val model: String) {
    Driving("driving"),
    Walking("walking"),
    Two_wheeler("two_wheeler")
}