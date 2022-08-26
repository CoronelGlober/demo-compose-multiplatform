package com.demo.ui.utils.chat

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Filled.Max_Screen: ImageVector
    get() {
        if (_max_Screen != null) {
            return _max_Screen!!
        }
        _max_Screen = ImageVector.Builder(
            name = "Max_Screen",
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
            moveTo(24.0F, 34.95F)
            quadTo(27.3F, 34.95F, 30.075F, 33.175F)
            quadTo(32.85F, 31.4F, 34.1F, 28.35F)
            horizontalLineTo(13.9F)
            quadTo(15.2F, 31.4F, 17.95F, 33.175F)
            quadTo(20.7F, 34.95F, 24.0F, 34.95F)

            moveTo(15.1F, 21.35F)
            lineTo(17.35F, 19.1F)
            lineTo(19.6F, 21.35F)
            lineTo(21.4F, 19.55F)
            lineTo(17.35F, 15.5F)
            lineTo(13.3F, 19.55F)

            moveTo(28.45F, 21.35F)
            lineTo(30.7F, 19.1F)
            lineTo(32.95F, 21.35F)
            lineTo(34.75F, 19.55F)
            lineTo(30.7F, 15.5F)
            lineTo(26.65F, 19.55F)

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
        return _max_Screen!!
    }
private var _max_Screen: ImageVector? = null