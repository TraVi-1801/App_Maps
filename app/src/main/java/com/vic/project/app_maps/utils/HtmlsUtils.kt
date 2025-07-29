package com.vic.project.app_maps.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.vic.project.app_maps.R

fun fromHtml(context: Context, html: String): Spannable = parse(html).apply {
    removeLinksUnderline()
    styleBold(context)
}
 fun parse(html: String): Spannable =
    (HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) as Spannable)

 fun Spannable.removeLinksUnderline() {
    for (s in getSpans(0, length, URLSpan::class.java)) {
        setSpan(object : UnderlineSpan() {
            override fun updateDrawState(tp: TextPaint) {
                tp.isUnderlineText = false
            }
        }, getSpanStart(s), getSpanEnd(s), 0)
    }
}

 fun Spannable.styleBold(context: Context) {
    val bold = ResourcesCompat.getFont(context, R.font.inter_bold)!!
    for (s in getSpans(0, length, StyleSpan::class.java)) {
        if (s.style == Typeface.BOLD) {
            setSpan(ForegroundColorSpan(Color.BLACK), getSpanStart(s), getSpanEnd(s), 0)
            setSpan(bold.getTypefaceSpan(), getSpanStart(s), getSpanEnd(s), 0)
        }
    }
}

fun Typeface.getTypefaceSpan(): MetricAffectingSpan =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        typefaceSpanCompatV28(this)
    } else {
        CustomTypefaceSpan(this)
    }

@TargetApi(Build.VERSION_CODES.P)
private fun typefaceSpanCompatV28(typeface: Typeface) = TypefaceSpan(typeface)

private class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {

    override fun updateDrawState(paint: TextPaint) {
        paint.typeface = typeface
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = typeface
    }
}