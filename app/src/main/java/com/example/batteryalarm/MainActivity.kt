package com.example.batteryalarm

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.batteryalarm.ui.theme.BatteryAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BatteryAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    BatteryScreen()
                }
            }
        }
    }
}

@Composable
fun BatteryScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isBatteryFull by remember { mutableStateOf(true) }

    showBatteryStatus(
        context = context,
        isBatteryFull = { isFull -> isBatteryFull = isFull },
    )

    val batteryImage = when {
        isBatteryFull -> R.drawable.battery_full
        else -> R.drawable.battery_low
    }

    Battery(
        batteryImageWithStatus = batteryImage,
    )

}

@Composable
fun Battery(batteryImageWithStatus: Int, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = batteryImageWithStatus),
            modifier = modifier.size(240.dp),
            contentScale = ContentScale.Fit,
            contentDescription = "Battery"
        )
    }

}

fun showBatteryStatus(
    context: Context,
    isBatteryFull: (Boolean) -> Unit,
): () -> Unit {
    val batteryReceiver = BatteryReceiver(isBatteryFull)
    val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    context.registerReceiver(batteryReceiver, filter)

    return {
        context.unregisterReceiver(batteryReceiver)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun BatteryScreenPreview() {
    BatteryScreen()
}