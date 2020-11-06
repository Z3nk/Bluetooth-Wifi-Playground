package com.example.blapoc

import android.bluetooth.BluetoothGattService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class MainFragment : Fragment() {
    var mActivity: MainActivity? = null


    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    val mGattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothLeService.ACTION_GATT_CONNECTED == action) {
                tv_information.text = "connected"
                // mConnected = true
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED == action) {
                tv_information.text = "DISconnected"
                //mConnected = false
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED == action) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mActivity?.mBluetoothLeService?.supportedGattServices)
            } else if (BluetoothLeService.CHARACTERISTIC_READ == action) {
                val uuid = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_UUID)
                val hexValue = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_HEX)
                val stringValue = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_STRING)
                log(uuid, hexValue, stringValue, "-- ON CHARACTERISTIC_READ --")
            } else if (BluetoothLeService.CHARACTERISTIC_CHANGED == action) {
                val uuid = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_UUID)
                val hexValue = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_HEX)
                val stringValue = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_STRING)
                log(uuid, hexValue, stringValue, "-- ON CHARACTERISTIC_CHANGED --")
            } else if (BluetoothLeService.DESCRIPTOR_READ == action) {
                val uuid = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_UUID)
                val hexValue = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_HEX)
                val stringValue = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_STRING)
                log(uuid, hexValue, stringValue, "-- ON DESCRIPTOR_READ --")
            }
        }

        private fun log(uuid: String?, hexValue: String?, stringValue: String?, title: String) {
            Log.d("ACTION_DATA_AVAILABLE", title)
            Log.d("ACTION_DATA_AVAILABLE", AllGattServices().lookup(UUID.fromString(uuid)))
            Log.d("ACTION_DATA_AVAILABLE", uuid)
            Log.d("ACTION_DATA_AVAILABLE", hexValue)
            Log.d("ACTION_DATA_AVAILABLE", stringValue)
            Log.d("ACTION_DATA_AVAILABLE", "----------------------------")
        }
    }

    private fun displayGattServices(supportedGattServices: List<BluetoothGattService>?) {
        Log.d("MainFragment", "gatt services detected")
        val gateServiceUtilities = AllGattServices()
        supportedGattServices?.let {
            AsyncTask.execute {
                for (currentService in supportedGattServices) {
                    val name = gateServiceUtilities.lookup(currentService.uuid)
                    activity?.runOnUiThread {
                        tv_information.text = (tv_information.text.toString() + "\n $name")
                    }
                    for (characteristic in currentService.characteristics) {
                        mActivity?.mBluetoothLeService?.readCharacteristic(characteristic)
                        Thread.sleep(1000)

                        for(descruptor in characteristic.descriptors){
                            mActivity?.mBluetoothLeService?.readDescriptor(descruptor)
                            Thread.sleep(1000)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mActivity = activity as MainActivity
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_pair.setOnClickListener {
            mActivity?.mBluetoothLeService?.connect(Eglo27.address)
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(BluetoothLeService.CHARACTERISTIC_READ)
        intentFilter.addAction(BluetoothLeService.CHARACTERISTIC_CHANGED)
        intentFilter.addAction(BluetoothLeService.DESCRIPTOR_READ)
        mActivity?.registerReceiver(mGattUpdateReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        mActivity?.unregisterReceiver(mGattUpdateReceiver);

    }
}