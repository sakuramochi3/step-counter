package com.example.cutsteps.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cutsteps.data.StepRepository
import com.example.cutsteps.ui.theme.Berry
import com.example.cutsteps.ui.theme.Pink100
import com.example.cutsteps.ui.theme.Pink200
import com.example.cutsteps.ui.theme.Pink300
import com.example.cutsteps.ui.theme.Pink400
import com.example.cutsteps.ui.theme.Pink500
import com.example.cutsteps.ui.theme.Pink600
import com.example.cutsteps.ui.theme.StarYellow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

private const val DAILY_GOAL = 10_000L
private val WindowShape = RoundedCornerShape(20.dp)
private val WindowBorder = 3.dp

@Composable
fun HomeScreen(viewModel: StepViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(Pink100)
    ) {
        PolkaDots(modifier = Modifier.matchParentSize())
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Pink300,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-18).dp, y = 90.dp)
                .size(34.dp)
        )
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = StarYellow,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 22.dp, y = 160.dp)
                .size(22.dp)
        )
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Pink400,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 14.dp, y = (-30).dp)
                .size(26.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleBar()
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Pink500,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "every step counts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Berry
                )
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Pink500,
                    modifier = Modifier.size(12.dp)
                )
            }
            Spacer(Modifier.height(14.dp))
            TodayWindow(steps = state.todaySteps)
            Spacer(Modifier.height(16.dp))
            WeekWindow(week = state.week)
            if (!state.hasSensor) {
                Spacer(Modifier.height(14.dp))
                Text(
                    text = "no step sensor on this device :(",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Berry
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PolkaDots(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val gap = 56.dp.toPx()
        val radius = 5.dp.toPx()
        var y = gap / 2
        var row = 0
        while (y < size.height) {
            var x = if (row % 2 == 0) gap / 2 else gap
            while (x < size.width) {
                drawCircle(Pink200, radius = radius, center = Offset(x, y))
                x += gap
            }
            y += gap
            row++
        }
    }
}

@Composable
private fun TitleBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(WindowBorder, Pink600, WindowShape)
            .background(Pink500, WindowShape)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "CuteSteps",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White
        )
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .border(2.dp, Pink600, CircleShape)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun WindowCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(WindowBorder, Pink600, WindowShape)
            .background(Color.White, WindowShape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Pink500, RoundedCornerShape(topStart = 17.dp, topEnd = 17.dp))
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp)
            )
        }
        Column(modifier = Modifier.padding(18.dp), content = content)
    }
}

@Composable
private fun TodayWindow(steps: Long) {
    WindowCard(title = "today") {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%,d".format(steps),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 40.sp,
                        lineHeight = 48.sp
                    ),
                    color = Pink600
                )
                Text(
                    text = "steps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Berry
                )
            }
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = StarYellow,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        val progress = (steps.toFloat() / DAILY_GOAL.toFloat()).coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
                .border(2.dp, Berry, RoundedCornerShape(12.dp))
                .padding(3.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = Pink500,
                trackColor = Pink100,
                strokeCap = StrokeCap.Round
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "goal $DAILY_GOAL steps",
            style = MaterialTheme.typography.labelSmall,
            color = Pink400,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun WeekWindow(week: List<StepRepository.DaySteps>) {
    WindowCard(title = "last 7 days") {
        WeeklyChart(week = week)
        Spacer(Modifier.height(6.dp))
        DayLabels(week = week)
    }
}

@Composable
private fun WeeklyChart(week: List<StepRepository.DaySteps>) {
    val max = week.maxOfOrNull { it.steps }?.coerceAtLeast(1L) ?: 1L
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        val starSpace = 20.dp.toPx()
        val labelSpace = 6.dp.toPx()
        val chartHeight = size.height - starSpace - labelSpace
        val slot = size.width / week.size
        val barWidth = slot * 0.55f
        week.forEachIndexed { index, day ->
            val ratio = day.steps.toFloat() / max.toFloat()
            val barHeight = (chartHeight * ratio).coerceAtLeast(8.dp.toPx())
            val left = slot * index + (slot - barWidth) / 2f
            val top = chartHeight - barHeight
            val isToday = index == week.lastIndex
            drawRoundRect(
                color = if (isToday) Pink600 else Pink300,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
            )
            drawRoundRect(
                color = if (isToday) Berry else Pink500,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f),
                style = Stroke(width = 2.5.dp.toPx())
            )
            if (isToday) {
                drawStar(
                    center = Offset(left + barWidth / 2f, starSpace / 2f),
                    outer = 9.dp.toPx(),
                    inner = 4.dp.toPx(),
                    color = StarYellow
                )
            }
        }
    }
}

private fun DrawScope.drawStar(center: Offset, outer: Float, inner: Float, color: Color) {
    val path = Path().apply {
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) outer else inner
            val angle = -Math.PI / 2 + i * Math.PI / 5
            val x = (center.x + r * cos(angle)).toFloat()
            val y = (center.y + r * sin(angle)).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    drawPath(path, color)
}

@Composable
private fun DayLabels(week: List<StepRepository.DaySteps>) {
    val dayFormat = remember { SimpleDateFormat("EEE", Locale.getDefault()) }
    Row(modifier = Modifier.fillMaxWidth()) {
        week.forEach { day ->
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -day.offsetFromToday)
            val label = dayFormat.format(cal.time).first().uppercase()
            val isToday = day.offsetFromToday == 0
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = if (isToday) Pink600 else Pink400,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
