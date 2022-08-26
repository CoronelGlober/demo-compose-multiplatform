package com.demo.ui.utils.chat

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Filled.Close_face: ImageVector
    get() {
        if (_close_face != null) {
            return _close_face!!
        }
        _close_face = ImageVector.Builder(
            name = "Close_face",
            defaultWidth = 48.0.dp,
            defaultHeight = 48.0.dp,
            viewportWidth = 48.0F,
            viewportHeight = 48.0F,
        ).path(
            fill = SolidColor(Color(0xFFFFFFFF)),
            fillAlpha = 1.0F,
            strokeAlpha = 1.0F,
            strokeLineWidth = 0.0F,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0F,
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(24.0F, 27.15F)
            quadTo(20.65F, 27.15F, 17.925F, 29.025F)
            quadTo(15.2F, 30.9F, 13.9F, 34.0F)
            horizontalLineTo(34.1F)
            quadTo(32.85F, 30.9F, 30.1F, 29.025F)
            quadTo(27.35F, 27.15F, 24.0F, 27.15F)

            moveTo(14.85F, 23.55F)
            lineTo(17.35F, 21.3F)
            lineTo(19.6F, 23.55F)
            lineTo(21.15F, 21.75F)
            lineTo(18.9F, 19.5F)
            lineTo(21.15F, 17.25F)
            lineTo(19.6F, 15.45F)
            lineTo(17.35F, 17.7F)
            lineTo(14.85F, 15.45F)
            lineTo(13.3F, 17.25F)
            lineTo(15.55F, 19.5F)
            lineTo(13.3F, 21.75F)

            moveTo(28.45F, 23.55F)
            lineTo(30.65F, 21.3F)
            lineTo(33.2F, 23.55F)
            lineTo(34.75F, 21.75F)
            lineTo(32.5F, 19.5F)
            lineTo(34.75F, 17.25F)
            lineTo(33.2F, 15.45F)
            lineTo(30.65F, 17.7F)
            lineTo(28.45F, 15.45F)
            lineTo(26.9F, 17.25F)
            lineTo(29.1F, 19.5F)
            lineTo(26.9F, 21.75F)

            moveTo(24.0F, 44.0F)
            quadTo(19.9F, 44.0F, 16.25F, 42.425F)
            quadTo(12.6F, 40.85F, 9.875F, 38.125F)
            quadTo(7.15F, 35.4F, 5.575F, 31.75F)
            quadTo(4.0F, 28.1F, 4.0F, 24.0F)
            quadTo(4.0F, 19.85F, 5.575F, 16.2F)
            quadTo(7.15F, 12.55F, 9.875F, 9.85F)
            quadTo(12.6F, 7.15F, 16.25F, 5.575F)
            quadTo(19.9F, 4.0F, 24.0F, 4.0F)
            quadTo(28.15F, 4.0F, 31.8F, 5.575F)
            quadTo(35.45F, 7.15F, 38.15F, 9.85F)
            quadTo(40.85F, 12.55F, 42.425F, 16.2F)
            quadTo(44.0F, 19.85F, 44.0F, 24.0F)
            quadTo(44.0F, 28.1F, 42.425F, 31.75F)
            quadTo(40.85F, 35.4F, 38.15F, 38.125F)
            quadTo(35.45F, 40.85F, 31.8F, 42.425F)
            quadTo(28.15F, 44.0F, 24.0F, 44.0F)

            moveTo(24.0F, 24.0F)
            quadTo(24.0F, 24.0F, 24.0F, 24.0F)
            quadTo(24.0F, 24.0F, 24.0F, 24.0F)
            quadTo(24.0F, 24.0F, 24.0F, 24.0F)
            quadTo(24.0F, 24.0F, 24.0F, 24.0F)
            quadTo(24.0F, 24.0F, 24.0F, 24.0F)
            quadTo(24.0F, 24.0F, 24.0F, 24.0F)
            quadTo(24.0F, 24.0F, 24.0F, 24.0F)
            quadTo(24.0F, 24.0F, 24.0F, 24.0F)

            moveTo(24.0F, 41.0F)
            quadTo(31.1F, 41.0F, 36.05F, 36.025F)
            quadTo(41.0F, 31.05F, 41.0F, 24.0F)
            quadTo(41.0F, 16.9F, 36.05F, 11.95F)
            quadTo(31.1F, 7.0F, 24.0F, 7.0F)
            quadTo(16.95F, 7.0F, 11.975F, 11.95F)
            quadTo(7.0F, 16.9F, 7.0F, 24.0F)
            quadTo(7.0F, 31.05F, 11.975F, 36.025F)
            quadTo(16.95F, 41.0F, 24.0F, 41.0F)
            close()
        }.build()
        return _close_face!!
    }
private var _close_face: ImageVector? = null