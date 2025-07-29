package com.vic.project.app_maps.presentation.components.html

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.vic.project.app_maps.R
import com.vic.project.app_maps.presentation.theme.Gray_100
import com.vic.project.app_maps.presentation.theme.appTypography
import com.vic.project.app_maps.presentation.theme.primaryMain
import com.vic.project.app_maps.utils.ModifierExtension.clickableSingle
import com.vic.project.app_maps.utils.fromHtml
import kotlin.math.max

private const val SPACING_FIX = 3f

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    html: String,
    textStyle: TextStyle = appTypography.bodyMedium.copy(
        color = Gray_100
    ),
    maxLine: Int = Int.MAX_VALUE,
    onClicked: () -> Unit = {},
    onLinkClicked: ((String?) -> Unit)? = null
) {
    val linkColor = primaryMain.toArgb()
    AndroidView(
        modifier = modifier.then(
            if (onLinkClicked != null) Modifier.clickableSingle {
                onClicked()
            }
            else {
                Modifier
            }
        ),
        update = { it.text = fromHtml(it.context, html) },
        factory = { context ->
            val spacingReady =
                max(textStyle.lineHeight.value - textStyle.fontSize.value - SPACING_FIX, 0f)
            val extraSpacing = spToPx(spacingReady.toInt(), context)
            val gravity = when (textStyle.textAlign) {
                TextAlign.Center -> Gravity.CENTER
                TextAlign.End -> Gravity.END
                else -> Gravity.START
            }
            val fontResId = when (textStyle.fontWeight) {
                FontWeight.Medium -> R.font.inter_medium
                else -> R.font.inter_regular
            }
            val font = ResourcesCompat.getFont(context, fontResId)

            TextView(context).apply {
                // general style
                textSize = textStyle.fontSize.value
                setLineSpacing(extraSpacing, 1f)
                setTextColor(textStyle.color.toArgb())
                setGravity(gravity)
                typeface = font
                maxLines = maxLine
                ellipsize = TextUtils.TruncateAt.END

                // links
                setLinkTextColor(linkColor)
                if (onLinkClicked != null) {
                    movementMethod = DefaultLinkMovementMethod() { link ->
                        onLinkClicked.invoke(link)
                        true
                    }
                }
            }
        }
    )
}

fun spToPx(sp: Int, context: Context): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )