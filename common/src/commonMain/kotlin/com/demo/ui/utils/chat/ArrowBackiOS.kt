package com.demo.ui.utils.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.IosArrow: ImageVector
    get() {
        if (_iosArrow != null) {
            return _iosArrow!!
        }
        _iosArrow = materialIcon(name = "IosArrow") {
            materialPath(
                fillAlpha = 1.0F,
                strokeAlpha = 1.0F,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(17.77F, 3.77F)
                lineToRelative(-1.77F, -1.77F)
                lineToRelative(-10.0F, 10.0F)
                lineToRelative(10.0F, 10.0F)
                lineToRelative(1.77F, -1.77F)
                lineToRelative(-8.23F, -8.23F)
                close()
            }
        }
        return _iosArrow!!
    }

private var _iosArrow: ImageVector? = null

