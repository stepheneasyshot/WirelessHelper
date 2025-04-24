package com.stephen.wirelesshelper

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import network.chaintech.cmp_bluetooth_manager.BluetoothManager
import network.chaintech.cmp_bluetooth_manager.BluetoothPeripheral
import network.chaintech.cmp_bluetooth_manager.modle.BluetoothConnectionState
import network.chaintech.cmp_bluetooth_manager.modle.BluetoothScanState
import network.chaintech.cmp_bluetooth_manager.modle.BluetoothState

class MainStateHolder : ViewModel() {

    val bluetoothManager = BluetoothManager()

    private val _btDeviceListStateFlow = MutableStateFlow(mutableStateListOf<BluetoothPeripheral>())
    val btDeviceListState = _btDeviceListStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            bluetoothManager.bluetoothState.collectLatest { (state, device) ->
                when (state) {
                    BluetoothState.IDLE -> println("Bluetooth Idle")
                    BluetoothState.BLUETOOTH_ENABLED -> println("Bluetooth Enabled ")
                    BluetoothState.BLUETOOTH_DISABLED -> println("Bluetooth Disabled ")
                    BluetoothState.DEVICE_CONNECTED -> println("Device Connected ${device?.name}")
                    BluetoothState.DEVICE_DISCONNECTED -> println("Device Disconnected ${device?.name}")
                }
            }
        }
    }

    fun startScan() {
        _btDeviceListStateFlow.value.clear()
        bluetoothManager.startScan(
            onDeviceFound = { device ->
                println("Device Found -> name: ${device.name}, address: ${device.address}")
                _btDeviceListStateFlow.value.add(device)
            },
            onScanStateChanged = { state ->
                when (state) {
                    BluetoothScanState.IDLE -> {
                        println("Scan Idle")
                    }
                    BluetoothScanState.SCANNING -> {
                        println("Scanning...")
                    }
                    BluetoothScanState.STOPPED -> {
                        println("Scan Stopped")
                    }
                }
            },
            onError = { error -> println("Error: $error") }
        )
    }

    fun stopScan() {
        bluetoothManager.stopScan()
    }

    fun connectDevice() {
        bluetoothManager.connectToDevice(
            device = BluetoothPeripheral(null), // Bluetooth Device
            uuid = (12345).toString(),       // Optional
            onConnectionStateChanged = { state ->
                when (state) {
                    BluetoothConnectionState.DISCONNECTED -> println("Device Disconnected")
                    BluetoothConnectionState.CONNECTING -> println("Connecting...")
                    BluetoothConnectionState.CONNECTED -> println("Device Connected")
                    BluetoothConnectionState.ERROR -> println("Connection Error")
                }
            },
            onError = { error -> println("Connection error: $error") }
        )
    }

    fun disconnectDevice() {
        bluetoothManager.disconnectDevice(
            device = BluetoothPeripheral(null),    // Bluetooth Device ,
            onSuccess = { println("Device disconnected successfully") },
            onError = { error -> println("Error disconnecting: $error") }
        )
    }
}
