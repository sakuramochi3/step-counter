package com.example.cutsteps

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cutsteps.ui.HomeScreen
import com.example.cutsteps.ui.StepViewModel
import com.example.cutsteps.ui.theme.Pink200
import com.example.cutsteps.ui.theme.Pink300
import com.example.cutsteps.ui.theme.Pink500
import com.example.cutsteps.ui.theme.Pink600

@Composable
fun CuteStepsApp(viewModel: StepViewModel = viewModel()) {
    val needsPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    val granted by viewModel.permissionGranted.collectAsStateWithLifecycle()
    var asked by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result -> viewModel.onPermissionResult(result) }

    LaunchedEffect(Unit) {
        if (needsPermission && !granted && !asked) {
            asked = true
            launcher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }

    if (needsPermission && !granted) {
        PermissionScreen(onAllow = { launcher.launch(Manifest.permission.ACTIVITY_RECOGNITION) })
    } else {
        HomeScreen(viewModel)
    }
}

@Composable
private fun PermissionScreen(onAllow: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFE9F2), Pink200, Pink300)))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Pink600
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "CuteSteps needs permission to count your steps",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onAllow,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Pink500)
        ) {
            Text(
                text = "allow",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}
