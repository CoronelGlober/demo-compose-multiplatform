package com.demo.ui.utils.chat


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.CameraAlt: ImageVector
    get() {
        if (_cameraAlt != null) {
            return _cameraAlt!!
        }
        _cameraAlt = materialIcon(name = "Filled.CameraAlt") {
            materialPath {
                moveTo(12.0f, 12.0f)
                moveToRelative(-3.2f, 0.0f)
                arcToRelative(3.2f, 3.2f, 0.0f, true, true, 6.4f, 0.0f)
                arcToRelative(3.2f, 3.2f, 0.0f, true, true, -6.4f, 0.0f)
            }
            materialPath {
                moveTo(9.0f, 2.0f)
                lineTo(7.17f, 4.0f)
                lineTo(4.0f, 4.0f)
                curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
                verticalLineToRelative(12.0f)
                curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                horizontalLineToRelative(16.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                lineTo(22.0f, 6.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                horizontalLineToRelative(-3.17f)
                lineTo(15.0f, 2.0f)
                lineTo(9.0f, 2.0f)
                close()
                moveTo(12.0f, 17.0f)
                curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
                reflectiveCurveToRelative(2.24f, -5.0f, 5.0f, -5.0f)
                reflectiveCurveToRelative(5.0f, 2.24f, 5.0f, 5.0f)
                reflectiveCurveToRelative(-2.24f, 5.0f, -5.0f, 5.0f)
                close()
            }
        }
        return _cameraAlt!!
    }

private var _cameraAlt: ImageVector? = null
