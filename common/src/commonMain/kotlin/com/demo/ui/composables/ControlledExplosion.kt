package com.demo.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.pow
import kotlin.random.Random
import androidx.compose.ui.Modifier

/**
 * Content from the wonderful post by Omkar Tenkale on
 * https://proandroiddev.com/creating-a-particle-explosion-animation-in-jetpack-compose-4ee42022bbfa
 */
@Composable
fun ControlledExplosion() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var enabled by remember { mutableStateOf(true) }
        var resetting by remember { mutableStateOf(false) }
        val progress by animateFloatAsState(
            targetValue = if (enabled) 0f else 1f,
            // Configure the animation duration and easing.
            animationSpec = if (resetting) snap() else tween(durationMillis = 1000, easing = FastOutLinearInEasing)
        ) {
            resetting = true
            enabled = true
        }
        Explosion(progress)

        Button({
            resetting = false
            enabled = false
        }, enabled = progress == 0f) {
            Text("Boom!")
        }
    }
}

@Composable
fun Explosion(progress: Float) {
    val density = LocalDensity.current.density
    val sizeDp = 200.dp
    val sizePx = sizeDp.toPx(density)
    val sizePxHalf = sizePx / 2
    val particles = remember {
        List(150) {
            Particle(
                color = Color(listOf(0xffea4335, 0xff4285f4, 0xfffbbc05, 0xff34a853).random()),
                startXPosition = sizePxHalf.toInt(),
                startYPosition = sizePxHalf.toInt(),
                maxHorizontalDisplacement = sizePx * randomInRange(-0.9f, 0.9f),
                maxVerticalDisplacement = sizePx * randomInRange(0.2f, 0.38f),
                density = density
            )
        }
    }
    particles.forEach { it.updateProgress(progress) }

    Canvas(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0x26000000))
            .size(sizeDp)
    ) {
        drawLine(
            color = Color.Black,
            start = Offset(sizePxHalf, 0f),
            end = Offset(sizePxHalf, sizePx),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.Black,
            start = Offset(0f, sizePxHalf),
            end = Offset(sizePx, sizePxHalf),
            strokeWidth = 2.dp.toPx()
        )
        particles.forEach { particle ->
            drawCircle(
                alpha = particle.alpha,
                color = particle.color,
                radius = particle.currentRadius,
                center = Offset(particle.currentXPosition, particle.currentYPosition),
            )
        }
    }
}

class Particle(
    val color: Color,
    val startXPosition: Int,
    val startYPosition: Int,
    val maxHorizontalDisplacement: Float,
    val maxVerticalDisplacement: Float,
    val density: Float
) {
    val velocity = 4 * maxVerticalDisplacement
    val acceleration = -2 * velocity
    var currentXPosition = 0f
    var currentYPosition = 0f

    var visibilityThresholdLow = randomInRange(0f, 0.14f)
    var visibilityThresholdHigh = randomInRange(0f, 0.4f)

    val initialXDisplacement = 10.dp.toPx(density) * randomInRange(-1f, 1f)
    val initialYDisplacement = 10.dp.toPx(density) * randomInRange(-1f, 1f)

    var alpha = 0f
    var currentRadius = 0f
    val startRadius = 2.dp.toPx(density)
    val endRadius = if (randomBoolean(trueProbabilityPercentage = 20)) {
        randomInRange(startRadius, 7.dp.toPx(density))
    } else {
        randomInRange(1.5.dp.toPx(density), startRadius)
    }

    fun updateProgress(explosionProgress: Float) {
        val trajectoryProgress =
            if (explosionProgress < visibilityThresholdLow || (explosionProgress > (1 - visibilityThresholdHigh))) {
                alpha = 0f; return
            } else (explosionProgress - visibilityThresholdLow).mapInRange(
                0f,
                1f - visibilityThresholdHigh - visibilityThresholdLow,
                0f,
                1f
            )
        alpha = if (trajectoryProgress < 0.7f) 1f else (trajectoryProgress - 0.7f).mapInRange(
            0f,
            0.3f,
            1f,
            0f
        )
        currentRadius = startRadius + (endRadius - startRadius) * trajectoryProgress
        val currentTime = trajectoryProgress.mapInRange(0f, 1f, 0f, 1.4f)
        val verticalDisplacement =
            (currentTime * velocity + 0.5 * acceleration * currentTime.toDouble()
                .pow(2.0)).toFloat()
        currentYPosition = startXPosition + initialXDisplacement - verticalDisplacement
        currentXPosition = startYPosition + initialYDisplacement + maxHorizontalDisplacement * trajectoryProgress
    }
}


fun Float.mapInRange(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
    return outMin + (((this - inMin) / (inMax - inMin)) * (outMax - outMin))
}

fun Dp.toPx(density: Float) = value.dpToPx(density)

fun Float.dpToPx(density: Float) = this * density


private val random = Random
fun Float.randomTillZero() = this * random.nextFloat()
fun randomInRange(min: Float, max: Float): Float = min + (max - min).randomTillZero()
fun randomBoolean(trueProbabilityPercentage: Int): Boolean = random.nextFloat() < trueProbabilityPercentage / 100f