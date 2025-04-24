package com.stephen.wirelesshelper

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import network.chaintech.cmp_bluetooth_manager.RequestBluetoothPermission
import network.chaintech.cmp_bluetooth_manager.isBluetoothPermissionGranted
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        RequestPermissions()
        LaunchedEffect(Unit) {
            val isPermissionGranted = isBluetoothPermissionGranted()
            if (isPermissionGranted) {
                println("Bluetooth permission is already granted")
            } else {
                println("Bluetooth permission is required")
            }
        }

        val mainViewModel = MainStateHolder()
        val deviceList = mainViewModel.btDeviceListState.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize(1f),
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Row {
                Button(onClick = {
                    println("Button clicked")
                    mainViewModel.startScan()
                }) {
                    Text(text = "触发扫描")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    println("Button clicked")
                    mainViewModel.stopScan()
                }) {
                    Text(text = "停止扫描")
                }
            }

            LazyColumn {
                items(deviceList.value.toList()) {
                    DeviceItemView(it.name, it.address)
                }
            }
        }
    }
}

@Composable
fun DeviceItemView(name: String?, addr: String?) {
    Column(modifier = Modifier.padding(bottom = 8.dp).padding(2.dp)) {
        Text(
            text = "Device Name: $name",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Text(text = "Device Address: $addr", fontSize = 12.sp)
    }
}

@Composable
fun RequestPermissions() {
    RequestBluetoothPermission { isGranted ->
        if (isGranted) {
            println("Bluetooth permission granted")
        } else {
            println("Bluetooth permission denied")
        }
    }
}