package com.amur.pocky.util

import android.graphics.Bitmap
import android.graphics.Color
import com.amur.pocky.data.model.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object BarcodeGenerator {

    fun generate(
        data: String,
        format: BarcodeFormat,
        width: Int = 600,
        height: Int = 300,
    ): Bitmap? {
        if (data.isBlank()) return null

        val zxingFormat = BarcodeFormat.toZxing(format)
        val barcodeHeight = if (format == BarcodeFormat.QR_CODE) width else height

        return try {
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                data,
                zxingFormat,
                width,
                barcodeHeight,
            )
            toBitmap(bitMatrix)
        } catch (_: Exception) {
            null
        }
    }

    private fun toBitmap(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (matrix[x, y]) Color.BLACK else Color.WHITE
            }
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }
}
