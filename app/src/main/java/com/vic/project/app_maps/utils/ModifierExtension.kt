package com.vic.project.app_maps.utils

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ModifierExtension {
    internal fun interface MultipleEventsCutter {
        fun processEvent(event: () -> Unit)

        companion object
    }

    @SuppressLint("RememberReturnType", "ModifierFactoryUnreferencedReceiver")
    fun Modifier.clickableSingle(
        enabled: Boolean = true,
        onClickLabel: String? = null,
        role: Role? = null,
        indicated: Boolean = false,
        onClick: () -> Unit
    ) = composed(
        inspectorInfo = debugInspectorInfo {
            name = "clickable"
            properties["enabled"] = enabled
            properties["onClickLabel"] = onClickLabel
            properties["role"] = role
            properties["onClick"] = onClick
        }
    ) {
        val multipleEventsCutter = remember { MultipleEventsCutter.get() }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        Modifier.clickable(
            enabled = enabled,
            onClickLabel = onClickLabel,
            onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                multipleEventsCutter.processEvent { onClick() }
            },
            role = role,
            indication = if (indicated) LocalIndication.current else null,
            interactionSource = remember { MutableInteractionSource() }
        )
    }
    internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
        MultipleEventsCutterImpl()

    private class MultipleEventsCutterImpl : MultipleEventsCutter {
        private val now: Long
            get() = System.currentTimeMillis()

        private var lastEventTimeMs: Long = 0

        override fun processEvent(event: () -> Unit) {
            if (now - lastEventTimeMs >= 300L) {
                event.invoke()
            }
            lastEventTimeMs = now
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    fun Modifier.clickOutSideToHideKeyBoard() = composed {
        val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        clickableSingle(indicated = false) {
            localSoftwareKeyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    fun Modifier.shadowCustom(
        color: Color = Color.Black,
        borderRadius: Dp = 0.dp,
        blurRadius: Dp = 0.dp,
        offsetY: Dp = 0.dp,
        offsetX: Dp = 0.dp,
        spread: Dp = 0f.dp,
        modifier: Modifier = Modifier
    ) = this.then(modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    })

    fun DrawScope.drawQrBorderCanvas(
        borderColor: Color = Color.White,
        curve: Dp,
        strokeWidth: Dp,
        capSize: Dp,
        gapAngle: Int = 20,
        shadowSize: Dp = strokeWidth * 2,
        cap: StrokeCap = StrokeCap.Square,
        lineCap: StrokeCap = StrokeCap.Round,
    ) {

        val curvePx = curve.toPx()

        val mCapSize = capSize.toPx()

        val width = size.width
        val height = size.height

        val sweepAngle = 90 / 2 - gapAngle / 2f

        strokeWidth.toPx().toInt()
        for (i in 4..shadowSize.toPx().toInt() step 2) {
            drawRoundRect(
                color = Color.Black.copy(0f),
                size = size,
                topLeft = Offset(0f, 0f),
                style = Stroke(width = i * 1f),
                cornerRadius = CornerRadius(
                    x = curvePx,
                    y = curvePx
                ),
            )
        }

        val mCurve = curvePx * 2

        drawArc(
            color = borderColor,
            style = Stroke(strokeWidth.toPx(), cap = cap),
            startAngle = 0f,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(mCurve, mCurve),
            topLeft = Offset(
                width - mCurve, height - mCurve
            )
        )
        drawArc(
            color = borderColor,
            style = Stroke(strokeWidth.toPx(), cap = cap),
            startAngle = 90 - sweepAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(mCurve, mCurve),
            topLeft = Offset(
                width - mCurve, height - mCurve
            )
        )

        drawArc(
            color = borderColor,
            style = Stroke(strokeWidth.toPx(), cap = cap),
            startAngle = 90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(mCurve, mCurve),
            topLeft = Offset(
                0f, height - mCurve
            )
        )
        drawArc(
            color = borderColor,
            style = Stroke(strokeWidth.toPx(), cap = cap),
            startAngle = 180 - sweepAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(mCurve, mCurve),
            topLeft = Offset(
                0f, height - mCurve
            )
        )

        drawArc(
            color = borderColor,
            style = Stroke(strokeWidth.toPx(), cap = cap),
            startAngle = 180f,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(mCurve, mCurve),
            topLeft = Offset(
                0f, 0f
            )
        )

        drawArc(
            color = borderColor,
            style = Stroke(strokeWidth.toPx(), cap = cap),
            startAngle = 270 - sweepAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(mCurve, mCurve),
            topLeft = Offset(
                0f, 0f
            )
        )


        drawArc(
            color = borderColor,
            style = Stroke(strokeWidth.toPx(), cap = cap),
            startAngle = 270f,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(mCurve, mCurve),
            topLeft = Offset(
                width - mCurve, 0f
            )
        )

        drawArc(
            color = borderColor,
            style = Stroke(strokeWidth.toPx(), cap = cap),
            startAngle = 360 - sweepAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(mCurve, mCurve),
            topLeft = Offset(
                width - mCurve, 0f
            )
        )


        drawLine(
            SolidColor(borderColor),
            Offset(width, height - mCapSize),
            Offset(width, height - curvePx),
            strokeWidth.toPx(),
            lineCap,
        )

        drawLine(
            SolidColor(borderColor),
            Offset(width - mCapSize, height),
            Offset(width - curvePx, height),
            strokeWidth.toPx(),
            lineCap,
        )

        drawLine(
            SolidColor(borderColor), Offset(mCapSize, height), Offset(curvePx, height),
            strokeWidth.toPx(), lineCap,
        )

        drawLine(
            SolidColor(borderColor), Offset(0f, height - curvePx), Offset(0f, height - mCapSize),
            strokeWidth.toPx(), lineCap
        )

        drawLine(
            SolidColor(borderColor), Offset(0f, curvePx), Offset(0f, mCapSize),
            strokeWidth.toPx(), lineCap,
        )

        drawLine(
            SolidColor(borderColor), Offset(curvePx, 0f), Offset(mCapSize, 0f),
            strokeWidth.toPx(), lineCap,
        )

        drawLine(
            SolidColor(borderColor), Offset(width - curvePx, 0f), Offset(width - mCapSize, 0f),
            strokeWidth.toPx(), lineCap,
        )

        drawLine(
            SolidColor(borderColor), Offset(width, curvePx), Offset(width, mCapSize),
            strokeWidth.toPx(), lineCap
        )

    }
}