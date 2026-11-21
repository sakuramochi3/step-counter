package com.example.cutsteps.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val DAILY_GOAL = 10_000L

@Composable
fun HomeScreen(viewModel: StepViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFE9F2), Pink200, Pink300)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CuteSteps",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = "every step counts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(Modifier.height(28.dp))
            TodayCard(steps = state.todaySteps)
            Spacer(Modifier.height(24.dp))
            WeeklyCard(week = state.week)
            if (!state.hasSensor) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "no step sensor on this device :(",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TodayCard(steps: Long) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TODAY",
                style = MaterialTheme.typography.labelLarge,
                color = Pink500
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "%,d".format(steps),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 38.sp,
                    lineHeight = 46.sp
                ),
                color = Pink600
            )
            Text(
                text = "steps",
                style = MaterialTheme.typography.bodyMedium,
                color = Berry
            )
            Spacer(Modifier.height(18.dp))
            val progress = (steps.toFloat() / DAILY_GOAL.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp),
                color = Pink500,
                trackColor = Pink100,
                strokeCap = StrokeCap.Round
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "goal $DAILY_GOAL",
                style = MaterialTheme.typography.labelSmall,
                color = Pink400
            )
        }
    }
}

@Composable
private fun WeeklyCard(week: List<StepRepository.DaySteps>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "LAST 7 DAYS",
                style = MaterialTheme.typography.labelLarge,
                color = Pink500
            )
            Spacer(Modifier.height(16.dp))
            WeeklyChart(week = week)
            Spacer(Modifier.height(8.dp))
            DayLabels(week = week)
        }
    }
}

@Composable
private fun WeeklyChart(week: List<StepRepository.DaySteps>) {
    val max = week.maxOfOrNull { it.steps }?.coerceAtLeast(1L) ?: 1L
    val labelSpace = 8.dp
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)) {
        val chartHeight = size.height - labelSpace.toPx()
        val slot = size.width / week.size
        val barWidth = slot * 0.55f
        week.forEachIndexed { index, day ->
            val ratio = day.steps.toFloat() / max.toFloat()
            val barHeight = (chartHeight * ratio).coerceAtLeast(6.dp.toPx())
            val left = slot * index + (slot - barWidth) / 2f
            val top = chartHeight - barHeight
            val isToday = index == week.lastIndex
            drawRoundRect(
                brush = if (isToday) {
                    Brush.verticalGradient(listOf(Pink600, Pink400))
                } else {
                    Brush.verticalGradient(listOf(Pink300, Pink200))
                },
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
            )
        }
    }
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
