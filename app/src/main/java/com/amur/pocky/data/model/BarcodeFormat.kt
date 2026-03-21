package com.amur.pocky.data.model

enum class BarcodeFormat(val displayName: String) {
    EAN_13("EAN-13"),
    EAN_8("EAN-8"),
    CODE_128("Code 128"),
    CODE_39("Code 39"),
    UPC_A("UPC-A"),
    QR_CODE("QR Code");

    companion object {
        fun fromMlKit(mlKitFormat: Int): BarcodeFormat? = when (mlKitFormat) {
            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_13 -> EAN_13
            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_8 -> EAN_8
            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_128 -> CODE_128
            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_39 -> CODE_39
            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_UPC_A -> UPC_A
            com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE -> QR_CODE
            else -> null
        }

        fun toZxing(format: BarcodeFormat): com.google.zxing.BarcodeFormat = when (format) {
            EAN_13 -> com.google.zxing.BarcodeFormat.EAN_13
            EAN_8 -> com.google.zxing.BarcodeFormat.EAN_8
            CODE_128 -> com.google.zxing.BarcodeFormat.CODE_128
            CODE_39 -> com.google.zxing.BarcodeFormat.CODE_39
            UPC_A -> com.google.zxing.BarcodeFormat.UPC_A
            QR_CODE -> com.google.zxing.BarcodeFormat.QR_CODE
        }
    }
}
