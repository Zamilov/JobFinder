package ru.zamilov.jobfinder.util

import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import org.xml.sax.XMLReader

fun formatText(text: String): CharSequence {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY, null, TagHandler())
    } else {
        Html.fromHtml(text, null, TagHandler())
    }
}

class TagHandler : Html.TagHandler {
    private var start = 0
    override fun handleTag(
        opening: Boolean,
        tag: String,
        output: Editable,
        xmlReader: XMLReader,
    ) {
        if (tag == "highlighttext") {
            if (opening) {
                start = output.length
            } else {
                val end = output.length
                output.setSpan(
                    BackgroundColorSpan(Color.YELLOW),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                output.setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
}
