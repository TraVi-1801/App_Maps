package com.vic.project.app_maps.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.vic.project.app_maps.R
import com.vic.project.app_maps.presentation.theme.Gray_10
import com.vic.project.app_maps.presentation.theme.Gray_100
import com.vic.project.app_maps.presentation.theme.Gray_40
import com.vic.project.app_maps.presentation.theme.Gray_70
import com.vic.project.app_maps.presentation.theme.appTypography
import com.vic.project.app_maps.presentation.theme.dangerMain
import com.vic.project.app_maps.presentation.theme.primaryMain
import com.vic.project.app_maps.utils.ModifierExtension.clickableSingle
import com.vic.project.app_maps.utils.rememberImeState

@Composable
fun InputText(
    modifier: Modifier = Modifier,
    leadingTitle: String = "",
    string: String,
    textHint: String,
    imgTrailing: Int? = null,
    imgTrailingTint: Color? = null,
    leadingIcon: Int? = null,
    leadingIconTint: Color? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    errorMessage: String = "",
    lines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    isPassword: Boolean = false,
    error: Boolean = false,
    isShowIconPassword: Boolean = isPassword,
    keyType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    onClick: () -> Unit = {},
    onSearch: () -> Unit = {},
    disabledContainerColor: Color = Gray_10,
    onFocusChanged: (FocusState) -> Unit = {},
    onValueChange: (String) -> Unit
) {
    val imeState = rememberImeState()
    val check = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val showPassword = remember { mutableStateOf(false) }

    val focusInputOne = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            check.value = true
        }
        if (!imeState.value && check.value) {
            focusManager.clearFocus()
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = string,
            enabled = enabled,
            onValueChange = {
                onValueChange.invoke(it)
            },
            minLines = lines,
            maxLines = maxLines,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    focusInputOne.value = focusState.hasFocus
                    onFocusChanged.invoke(focusState)
                }
                .border(
                    if (focusInputOne.value) 2.dp else 1.dp,
                    when {
                        errorMessage.isNotEmpty() || error -> dangerMain
                        focusInputOne.value -> primaryMain
                        else -> Gray_40
                    },
                    RoundedCornerShape(8.dp)
                )
                .background(disabledContainerColor, RoundedCornerShape(8.dp))
                .clickableSingle { onClick.invoke() },
            textStyle = appTypography.bodySmall.copy(
                fontWeight = FontWeight.W500,
                color = if (enabled) Gray_100 else Gray_70,
            ),
            visualTransformation = if (isPassword) {
                if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation()
            } else VisualTransformation.None,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyType,
                imeAction = imeAction
            ),
            cursorBrush = SolidColor(primaryMain),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                },
                onSearch = {
                    focusManager.clearFocus()
                    onSearch.invoke()
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            painter = painterResource(id = leadingIcon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(18.dp),
                            tint = leadingIconTint ?: Gray_100
                        )
                    }
                    if (leadingTitle.isNotEmpty()){
                        Text(
                            text = leadingTitle,
                            style = appTypography.labelLarge,
                            fontWeight = FontWeight.W400,
                            color = Gray_100
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (string.isEmpty()) {
                            Text(
                                text = textHint,
                                style = appTypography.bodySmall,
                                fontWeight = FontWeight.W400,
                                color = Gray_100
                            )
                        }
                        innerTextField()
                    }
                    if (imgTrailing != null) {
                        Icon(
                            painter = painterResource(id = imgTrailing),
                            contentDescription = "",
                            modifier = Modifier
                                .size(18.dp)
                                .clickableSingle{
                                    onSearch.invoke()
                                },
                            tint = imgTrailingTint ?: Gray_100
                        )
                    }
                    if (isShowIconPassword) {
                        val icon = if (showPassword.value) {
                            R.drawable.ic_eye
                        } else {
                            R.drawable.ic_eyeclosed
                        }

                        IconButton(
                            modifier = Modifier.size(16.dp),
                            onClick = { showPassword.value = !showPassword.value }) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = "Visibility",
                                tint = Gray_100
                            )
                        }
                    }
                }
            },
        )
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = dangerMain,
                style = appTypography.labelSmall,
                modifier = Modifier.semantics { contentDescription = "ConfirmPasswordMessage" },
            )
        }
    }
}