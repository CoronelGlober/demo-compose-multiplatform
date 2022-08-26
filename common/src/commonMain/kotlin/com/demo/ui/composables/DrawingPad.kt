package com.demo.ui.composables


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import com.demo.data.network.client.ClientConfig
import com.demo.domain.screens.client.mvi.ClientActions
import com.demo.domain.screens.client.mvi.ClientEvents.*
import com.demo.domain.screens.client.mvi.ClientViewModel
import com.demo.expected.getPlatformName
import com.demo.expected.platformDrawingStroke
import com.demo.ui.composables.PathSerializer.deserializeDrawnPath
import com.demo.ui.composables.PathSerializer.serializeDrawnPath
import io.ktor.utils.io.*

/**
 * Helper for centralizing path serialization
 */
internal object PathSerializer {
    // sending to the server all the x,y points of the path, delimited by a * char
    fun serializeDrawnPath(pathPoints: List<Pair<Float, Float>>): String {
        return pathPoints.joinToString("*")
    }

    /**
     * we need x,y points, but they come from the server in tuples delimited by a * character like this
     * "(34,12)*(34,13)*(34,14)....."
     * @param xFactor used for scaling the received points to the current drawing canvas X size
     * @param yFactor used for scaling the received points to the current drawing canvas Y size
     * */
    fun deserializeDrawnPath(path: String, xFactor: Float, yFactor: Float): List<Pair<Float, Float>> {
        return path
            .replace(")", "")
            .replace("(", "")
            .split("*")
            .map { pair ->
                val x = pair.split(",")[0].trim().toFloat()
                val y = pair.split(",")[1].trim().toFloat()
                Pair(x * xFactor, y * yFactor)
            }
    }
}

/**
 * Canvas that draws paths created by local interactions and paths received from the tcp server,
 * each one with its own color defined by the server
 */
@Composable
@Suppress("DEPRECATION")
fun DrawingPad(viewModel: ClientViewModel) {

    /**
     * Paths that are added, this is required to have paths with different options and paths
     *  ith erase to keep over each other
     */
    /**
     * Paths that are added, this is required to have paths with different options and paths
     *  ith erase to keep over each other
     */
    val paths = remember { mutableStateListOf<Pair<Path, PathProperties>>() }

    /**
     * Paths that are undone via button. These paths are restored if user pushes
     * redo button if there is no new path drawn.
     *
     * If new path is drawn after this list is cleared to not break paths after undoing previous
     * ones.
     */
    /**
     * Paths that are undone via button. These paths are restored if user pushes
     * redo button if there is no new path drawn.
     *
     * If new path is drawn after this list is cleared to not break paths after undoing previous
     * ones.
     */
    val pathsUndone = remember { mutableStateListOf<Pair<Path, PathProperties>>() }

    /**
     * Canvas touch state. [MotionEvent.Idle] by default, [MotionEvent.Down] at first contact,
     * [MotionEvent.Move] while dragging and [MotionEvent.Up] when first pointer is up
     */
    /**
     * Canvas touch state. [MotionEvent.Idle] by default, [MotionEvent.Down] at first contact,
     * [MotionEvent.Move] while dragging and [MotionEvent.Up] when first pointer is up
     */
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }

    /**
     * Current position of the pointer that is pressed or being moved
     */
    /**
     * Current position of the pointer that is pressed or being moved
     */
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }

    /**
     * Previous motion event before next touch is saved into this current position.
     */
    /**
     * Previous motion event before next touch is saved into this current position.
     */
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }

    /**
     * Draw mode, erase mode or touch mode to
     */
    /**
     * Draw mode, erase mode or touch mode to
     */
    val drawMode by remember { mutableStateOf(DrawMode.Draw) }

    /**
     * Path that is being drawn between [MotionEvent.Down] and [MotionEvent.Up]. When
     * pointer is up this path is saved to **paths** and new instance is created
     */
    /**
     * Path that is being drawn between [MotionEvent.Down] and [MotionEvent.Up]. When
     * pointer is up this path is saved to **paths** and new instance is created
     */
    var currentPath by remember { mutableStateOf(Path()) }

    /**
     * Properties of path that is currently being drawn between
     * [MotionEvent.Down] and [MotionEvent.Up].
     */
    /**
     * Properties of path that is currently being drawn between
     * [MotionEvent.Down] and [MotionEvent.Up].
     */
    var currentPathProperty by remember { mutableStateOf(PathProperties()) }

    var canvasSize by remember { mutableStateOf(Size(0f, 0f)) }

    fun handleMessageReceived(newEvent: MessageReceived) {
        try {
            // initial greeting, it contains the user color for drawing
            if (newEvent.message.message.startsWith("Welcome")) {
                currentPathProperty.color = Color(newEvent.message.userColor.toLong(16))
            }
            // serialized path to be drawn, lets parse its list of points
            if (newEvent.message.message.first() == '(') {
                val receivedPath = Path()
                val newProperties = PathProperties(
                    strokeWidth = currentPathProperty.strokeWidth,
                    color = Color(newEvent.message.userColor.toLong(16)),
                    strokeCap = currentPathProperty.strokeCap,
                    strokeJoin = currentPathProperty.strokeJoin,
                    eraseMode = currentPathProperty.eraseMode
                )
                val points = deserializeDrawnPath(newEvent.message.message, canvasSize.width, canvasSize.height)
                paths.add(Pair(receivedPath, currentPathProperty))
                if (points.isNotEmpty()) {
                    //initial point
                    receivedPath.moveTo(points.first().first, points.first().second)

                    if (points.size > 1) {
                        //adding middle points
                        for ((index, pointA) in points.slice(0 until (points.size - 2)).withIndex()) {
                            val pointB = points[index + 1]
                            receivedPath.quadraticBezierTo(
                                pointA.first,
                                pointA.second,
                                (pointA.first + pointB.first) / 2,
                                (pointA.second + pointB.second) / 2

                            )
                        }
                        //closing path
                        receivedPath.lineTo(points.last().first, points.last().second)
                    }
                }
                paths.add(Pair(receivedPath, newProperties))
            }
        } catch (ex: Exception) {
            ex.printStack()
        }
    }

    fun handleConnectionChanged(newEvent: ConnectionChanged) {
        if (newEvent.connected) {
            viewModel.dispatchAction(ClientActions.SendMessage(getPlatformName()))
        } else {
            viewModel.dispatchAction(ClientActions.Connect(ClientConfig.ipAddress, ClientConfig.port.toInt()))
        }
    }

    LaunchedEffect(true) {
        viewModel.dispatchAction(ClientActions.Connect(ClientConfig.ipAddress, ClientConfig.port.toInt()))
        // current viewmodel only delivers new messages from server, it won't store them as a list
        // so it is up to each viewmodel consumer to store them and append them
        viewModel.getEventsFlow().collect { newEvent ->
            when (newEvent) {
                is ConnectionChanged -> handleConnectionChanged(newEvent)
                is MessageReceived -> handleMessageReceived(newEvent)
                is ErrorHappened -> println(newEvent.error)
                is LogEventReceived -> Unit
            }
        }
    }
    val pathPoints = mutableListOf<Pair<Float, Float>>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {

        val drawModifier = Modifier
            .padding(8.dp)
            .shadow(1.dp)
            .fillMaxSize()
            .background(Color.White)
            .dragMotionEvent(
                onDragStart = { pointerInputChange ->
                    motionEvent = MotionEvent.Down
                    currentPosition = pointerInputChange.position
                    pointerInputChange.consumeDownChange()

                },
                onDrag = { pointerInputChange ->
                    motionEvent = MotionEvent.Move
                    currentPosition = pointerInputChange.position

                    if (drawMode == DrawMode.Touch) {
                        val change = pointerInputChange.positionChange()
                        paths.forEach { entry ->
                            val path: Path = entry.first
                            path.translate(change)
                        }
                        currentPath.translate(change)
                    }
                    pointerInputChange.consumePositionChange()

                },
                onDragEnd = { pointerInputChange ->
                    motionEvent = MotionEvent.Up
                    pointerInputChange.consumeDownChange()
                }
            )

        Canvas(modifier = drawModifier) {
            canvasSize = size
            when (motionEvent) {

                MotionEvent.Down -> {
                    if (drawMode != DrawMode.Touch) {
                        currentPath.moveTo(currentPosition.x, currentPosition.y)
                    }

                    previousPosition = currentPosition
                    pathPoints.add(
                        Pair(
                            ((previousPosition.x + currentPosition.x) / 2) / size.width,
                            ((previousPosition.y + currentPosition.y) / 2) / size.height
                        )
                    )
                }
                MotionEvent.Move -> {
                    try {
                        if (drawMode != DrawMode.Touch) {
                            currentPath.quadraticBezierTo(
                                previousPosition.x,
                                previousPosition.y,
                                (previousPosition.x + currentPosition.x) / 2,
                                (previousPosition.y + currentPosition.y) / 2

                            )
                        }
                        pathPoints.add(
                            Pair(
                                ((previousPosition.x + currentPosition.x) / 2) / size.width,
                                ((previousPosition.y + currentPosition.y) / 2) / size.height
                            )
                        )
                        previousPosition = currentPosition
                    } catch (ex: Exception) {
                        ex.printStack()
                        println("error getting scroll movement")
                    }
                }

                MotionEvent.Up -> {
                    if (drawMode != DrawMode.Touch) {
                        currentPath.lineTo(currentPosition.x, currentPosition.y)

                        paths.add(Pair(currentPath, currentPathProperty))

                        // Since paths are keys for map, use new one for each key
                        // and have separate path for each down-move-up gesture cycle
                        currentPath = Path()
                        try {
                            viewModel.dispatchAction(ClientActions.SendMessage(serializeDrawnPath(pathPoints)))
                        } catch (e: Exception) {
                            println("error sending drawn path to server: ${e.message}")
                        }
                        pathPoints.clear() // TODO: send the path to server

                        // Create new instance of path properties only for the one currently being drawn
                        currentPathProperty = PathProperties(
                            strokeWidth = currentPathProperty.strokeWidth,
                            color = currentPathProperty.color,
                            strokeCap = currentPathProperty.strokeCap,
                            strokeJoin = currentPathProperty.strokeJoin,
                            eraseMode = currentPathProperty.eraseMode
                        )
                    }

                    // Since new path is drawn no need to store paths to undone
                    pathsUndone.clear()

                    // If we leave this state at MotionEvent.Up it causes current path to draw
                    // line from (0,0) if this composable recomposes when draw mode is changed
                    currentPosition = Offset.Unspecified
                    previousPosition = currentPosition
                    motionEvent = MotionEvent.Idle
                }
                else -> Unit
            }

            with(drawContext.canvas.nativeCanvas) {

                val checkPoint = saveLayer(null, null)

                paths.forEach {

                    val path = it.first
                    val property = it.second

                    if (!property.eraseMode) {
                        drawPath(
                            color = property.color,
                            path = path,
                            style = Stroke(
                                width = property.strokeWidth,
                                cap = property.strokeCap,
                                join = property.strokeJoin
                            )
                        )
                    } else {

                        // Source
                        drawPath(
                            color = Color.Transparent,
                            path = path,
                            style = Stroke(
                                width = currentPathProperty.strokeWidth,
                                cap = currentPathProperty.strokeCap,
                                join = currentPathProperty.strokeJoin
                            ),
                            blendMode = BlendMode.Clear
                        )
                    }
                }

                if (motionEvent != MotionEvent.Idle) {

                    if (!currentPathProperty.eraseMode) {
                        drawPath(
                            color = currentPathProperty.color,
                            path = currentPath,
                            style = Stroke(
                                width = currentPathProperty.strokeWidth,
                                cap = currentPathProperty.strokeCap,
                                join = currentPathProperty.strokeJoin
                            )
                        )
                    } else {
                        drawPath(
                            color = Color.Transparent,
                            path = currentPath,
                            style = Stroke(
                                width = currentPathProperty.strokeWidth,
                                cap = currentPathProperty.strokeCap,
                                join = currentPathProperty.strokeJoin
                            ),
                            blendMode = BlendMode.Clear
                        )
                    }
                }
                restoreToCount(checkPoint)
            }
        }
    }
}

enum class DrawMode {
    Draw, Touch
}

@Suppress("DEPRECATION")
suspend fun AwaitPointerEventScope.awaitDragMotionEvent(
    onTouchEvent: (MotionEvent, PointerInputChange) -> Unit
) {
    // Wait for at least one pointer to press down, and set first contact position
    val down: PointerInputChange = awaitFirstDown()
    onTouchEvent(MotionEvent.Down, down)

    var pointer = down

    // 🔥 Waits for drag threshold to be passed by pointer
    // or it returns null if up event is triggered
    val change: PointerInputChange? =
        awaitTouchSlopOrCancellation(down.id) { change: PointerInputChange, _: Offset ->
            // 🔥🔥 If consumePositionChange() is not consumed drag does not
            // function properly.
            // Consuming position change causes change.positionChanged() to return false.
            change.consumePositionChange()
        }

    if (change != null) {
        // 🔥 Calls  awaitDragOrCancellation(pointer) in a while loop
        drag(change.id) { pointerInputChange: PointerInputChange ->
            pointer = pointerInputChange
            onTouchEvent(MotionEvent.Move, pointer)
        }

        // All of the pointers are up
        onTouchEvent(MotionEvent.Up, pointer)
    } else {
        // Drag threshold is not passed and last pointer is up
        onTouchEvent(MotionEvent.Up, pointer)
    }
}


@Suppress("DEPRECATION")
suspend fun AwaitPointerEventScope.awaitDragMotionEvent(
    onDragStart: (PointerInputChange) -> Unit = {},
    onDrag: (PointerInputChange) -> Unit = {},
    onDragEnd: (PointerInputChange) -> Unit = {}
) {
    // Wait for at least one pointer to press down, and set first contact position
    val down: PointerInputChange = awaitFirstDown()
    onDragStart(down)

    var pointer = down

    // 🔥 Waits for drag threshold to be passed by pointer
    // or it returns null if up event is triggered
    val change: PointerInputChange? =
        awaitTouchSlopOrCancellation(down.id) { change: PointerInputChange, _: Offset ->
            // 🔥🔥 If consumePositionChange() is not consumed drag does not
            // function properly.
            // Consuming position change causes change.positionChanged() to return false.
            change.consumePositionChange()
        }

    if (change != null) {
        // 🔥 Calls  awaitDragOrCancellation(pointer) in a while loop
        drag(change.id) { pointerInputChange: PointerInputChange ->
            pointer = pointerInputChange
            onDrag(pointer)
        }

        // All of the pointers are up
        onDragEnd(pointer)
    } else {
        // Drag threshold is not passed and last pointer is up
        onDragEnd(pointer)
    }
}

fun Modifier.dragMotionEvent(
    onDragStart: (PointerInputChange) -> Unit = {},
    onDrag: (PointerInputChange) -> Unit = {},
    onDragEnd: (PointerInputChange) -> Unit = {}
) = this.then(
    Modifier.pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {
                awaitDragMotionEvent(onDragStart, onDrag, onDragEnd)
            }
        }
    }
)

enum class MotionEvent {
    Idle, Down, Move, Up
}

class PathProperties(
    var strokeWidth: Float = platformDrawingStroke,
    var color: Color = Color.Black,
    var alpha: Float = 1f,
    var strokeCap: StrokeCap = StrokeCap.Round,
    var strokeJoin: StrokeJoin = StrokeJoin.Round,
    var eraseMode: Boolean = false
) {

    fun copyFrom(properties: PathProperties) {
        this.strokeWidth = properties.strokeWidth
        this.color = properties.color
        this.strokeCap = properties.strokeCap
        this.strokeJoin = properties.strokeJoin
        this.eraseMode = properties.eraseMode
    }
}
