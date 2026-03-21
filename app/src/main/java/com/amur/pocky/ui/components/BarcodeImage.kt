package com.amur.pocky.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.amur.pocky.data.model.BarcodeFormat
import com.amur.pocky.util.BarcodeGenerator

@Composable
fun BarcodeImage(
    cardNumber: String,
    format: BarcodeFormat,
    modifier: Modifier = Modifier,
) {
    val bitmap: Bitmap? = remember(cardNumber, format) {
        BarcodeGenerator.generate(cardNumber, format)
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Barcode",
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp, max = 200.dp),
            contentScale = ContentScale.Fit,
            filterQuality = FilterQuality.None,
        )
    }
}
