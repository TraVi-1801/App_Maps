package com.vic.project.app_maps.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vic.project.app_maps.R
import com.vic.project.app_maps.data.model.LocationData
import com.vic.project.app_maps.presentation.components.InputText
import com.vic.project.app_maps.presentation.theme.Gray_10
import com.vic.project.app_maps.presentation.theme.Gray_40
import com.vic.project.app_maps.presentation.theme.appTypography
import com.vic.project.app_maps.presentation.theme.dangerMain
import com.vic.project.app_maps.presentation.theme.primaryMain
import com.vic.project.app_maps.utils.ModifierExtension.clickableSingle
import com.vic.project.app_maps.utils.ModifierExtension.shadowCustom

@Composable
fun SearchLocation(
    search : String,
    onFocus : Boolean,
    isGpsEnabled : Boolean,
    isDirection : Boolean,
    startLocation: LocationData?,
    currentChoose: LocationData?,
    list: List<LocationData>,
    listHistory : List<String>,
    listRecommend : List<String>,
    onFocusChanged: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onDirections: (Boolean,LocationData) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isDirection,) {
        if (isGpsEnabled.not() && startLocation == null && isDirection){
            focusManager.clearFocus()
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    var inputStart by remember (startLocation){
        mutableStateOf(startLocation?.formatted_address)
    }
    var isStart by remember{
        mutableStateOf(false)
    }
    var inputEnd by remember (currentChoose){
        mutableStateOf(currentChoose?.formatted_address)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedVisibility(isDirection) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .shadowCustom(
                        color = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                        offsetY = 0.dp,
                        offsetX = 3.dp,
                        spread = 0.dp,
                        blurRadius = 4.dp,
                        borderRadius = 8.dp
                    )
                    .background(Gray_10, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ){
                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Spacer(Modifier.size(4.dp).border(2.dp,Gray_40, CircleShape))
                    Spacer(Modifier.weight(1f).width(1.dp).background(primaryMain))
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "",
                        tint = dangerMain,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Column (
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    InputText(
                        modifier = Modifier
                            .then(
                                if (isDirection){
                                    Modifier.focusRequester(focusRequester)
                                }else{
                                    Modifier
                                }
                            ),
                        string = inputStart?:"",
                        textHint = if (isGpsEnabled && startLocation?.formatted_address.isNullOrBlank()) "Your Location" else "Enter location...",
                        onSearch = { onSearch.invoke(search) },
                    ) {
                        isStart = true
                        inputStart = it
                        onValueChange.invoke(it)
                    }
                    Spacer(Modifier.fillMaxWidth().height(1.dp).background(Gray_40))
                    InputText(
                        string = inputEnd?:"",
                        textHint = "Enter location...",
                        onSearch = { onSearch.invoke(search) },
                    ) {
                        isStart = false
                        inputEnd = it
                        onValueChange.invoke(it)
                    }
                }
            }

        }
        if (isDirection.not()) {
            InputText(
                string = search,
                textHint = "Enter location...",
                imgTrailing = R.drawable.ic_search,
                imeAction = ImeAction.Search,
                onSearch = { onSearch.invoke(search) },
                onFocusChanged = {
                    onFocusChanged.invoke(it.hasFocus)
                },
                onValueChange = {
                    onValueChange.invoke(it)
                })
        }

        AnimatedVisibility(
            visible = list.isNotEmpty() && isDirection,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadowCustom(
                        color = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                        offsetY = 0.dp,
                        offsetX = 3.dp,
                        spread = 0.dp,
                        blurRadius = 4.dp,
                        borderRadius = 8.dp
                    )
                    .background(Gray_10, RoundedCornerShape(8.dp))
                    .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search recommend",
                        style = appTypography.labelMedium,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                list.forEach {
                    Text(
                        text = it.formatted_address?:"",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableSingle {
                                onDirections.invoke(isStart,it)
                            }
                            .padding(vertical = 6.dp))
                }
            }
        }

        AnimatedVisibility(
            visible = onFocus && (listHistory.isNotEmpty() || listRecommend.isNotEmpty()),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadowCustom(
                        color = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                        offsetY = 0.dp,
                        offsetX = 3.dp,
                        spread = 0.dp,
                        blurRadius = 4.dp,
                        borderRadius = 8.dp
                    )
                    .background(Gray_10, RoundedCornerShape(8.dp))
                    .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (listRecommend.isNotEmpty()) "Search recommend" else "Search history",
                        style = appTypography.labelMedium,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (listRecommend.isEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                listRecommend.ifEmpty { listHistory }.forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableSingle {
                                onSearch.invoke(it)
                            }
                            .padding(vertical = 6.dp))
                }
            }
        }
    }
}