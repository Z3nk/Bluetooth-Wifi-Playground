package com.example.blapoc

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blapoc.bluetooth.BluetoothLeService
import com.example.blapoc.wifi.WifiFragment

class MainActivity : AppCompatActivity() {
    var mBluetoothLeService: BluetoothLeService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBluetooth()
        supportFragmentManager.beginTransaction().replace(R.id.container, WifiFragment()).commit()
    }

    private fun initBluetooth() {
        val serviceConnectionListener: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mBluetoothLeService = (service as BluetoothLeService.LocalBinder).service
                if (mBluetoothLeService?.initialize() == false) {
                    Log.e(MainActivity::class.java.simpleName, "Unable to initialize Bluetooth")
                    finish()
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                mBluetoothLeService = null
            }
        }
        bindService(Intent(this, BluetoothLeService::class.java), serviceConnectionListener, BIND_AUTO_CREATE)
        // Verification de la disponibilité du BLE
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}