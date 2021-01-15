package com.example.blapoc.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
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
import com.example.blapoc.BluetoothLeService
import com.example.blapoc.MainActivity
import com.example.blapoc.R
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class MainFragment : Fragment() {
    var mActivity: MainActivity? = null
    var writeChara: BluetoothGattCharacteristic? = null
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
                onGattServicesDiscovered(mActivity?.mBluetoothLeService?.supportedGattServices)
            } else if (BluetoothLeService.CHARACTERISTIC_READ == action) {
                val uuid = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_UUID)
                val hexValue =
                    intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_HEX)
                val stringValue =
                    intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_STRING)
                log(uuid, hexValue, stringValue, "-- ON CHARACTERISTIC_READ --")
            } else if (BluetoothLeService.CHARACTERISTIC_CHANGED == action) {
                val uuid = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_UUID)
                val hexValue =
                    intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_HEX)
                val stringValue =
                    intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_STRING)
                log(uuid, hexValue, stringValue, "-- ON CHARACTERISTIC_CHANGED --")
            } else if (BluetoothLeService.DESCRIPTOR_READ == action) {
                val uuid = intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_UUID)
                val hexValue =
                    intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_HEX)
                val stringValue =
                    intent.getStringExtra(BluetoothLeService.EXTRA_CHARACTERISTIC_VALUE_STRING)
                log(uuid, hexValue, stringValue, "-- ON DESCRIPTOR_READ --")
            }
        }

        private fun log(uuid: String?, hexValue: String?, stringValue: String?, title: String) {
            Log.d("ACTION_DATA_AVAILABLE", title)
            Log.d("ACTION_DATA_AVAILABLE", AllGattServices().lookup(UUID.fromString(uuid)))
            Log.d("ACTION_DATA_AVAILABLE", uuid ?: "")
            Log.d("ACTION_DATA_AVAILABLE", hexValue ?: "")
            Log.d("ACTION_DATA_AVAILABLE", stringValue ?: "")
            Log.d("ACTION_DATA_AVAILABLE", "----------------------------")
        }
    }

    private fun onGattServicesDiscovered(supportedGattServices: List<BluetoothGattService>?) {
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
                        //mActivity?.mBluetoothLeService?.readCharacteristic(characteristic)
                        //Thread.sleep(1000)
                        if (characteristic.uuid.toString().contains("a101", true)) {
                            writeChara = characteristic
                            Log.d("displayGattServices", "bingo")
                        }

                        for (descruptor in characteristic.descriptors) {
                           // mActivity?.mBluetoothLeService?.readDescriptor(descruptor)
                           // Thread.sleep(1000)
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
            getReadyLexmanBulbs()
            //mActivity?.mBluetoothLeService?.connect(BulbBle.address)
        }

        bt_off.setOnClickListener {
            //val char: BluetoothGattCharacteristic? = BluetoothGattCharacteristic()
            writeChara?.value =
                BinaryTools().decodeHexString(getOffHex())// byteArrayOf(0x0, 0x0, 0x0, 0x0, 0xF, 0x0, 0x0, 0x1, 0x3, 0x0, 0x0, 0x1)
            mActivity?.mBluetoothLeService?.writeCharacteristic(writeChara)
        }
        bt_on.setOnClickListener {
            writeChara?.value = BinaryTools().decodeHexString(getOnHex())
            mActivity?.mBluetoothLeService?.writeCharacteristic(writeChara)
        }
        bt_custom.setOnClickListener {
            // writeChara?.value = BinaryTools().decodeHexString(getHueSaturationRose())
            writeChara?.value = BinaryTools().decodeHexString(getLowColorTemperature())
            mActivity?.mBluetoothLeService?.writeCharacteristic(writeChara)
        }
    }

    private fun getLowColorTemperature(): String? {
        val tidep = "0000"
        val opcode = "1201" // hue and saturation payload
        val length = "04" // nombre de bytes du payload
        //payload
        val color1 = "00"
        val color2 = "99"
        val transition = "01"
        val delay = "01"
        val s =
            tidep + opcode + length + color1 + color2 + transition + delay //
        return s;
    }

    private fun getOnHex() = "0000100103010001"

    private fun getOffHex() = "0000100103000001"

    private fun getReadyLexmanBulbs() {
        mActivity?.mBluetoothLeService?.lookFor(
            listOf("0000a100-0000-1000-8000-00805f9b34fb"),
            //listOf("0000a1000-1115-1000-0001-617573746f6d"),
            // listOf("0000a101-1115-1000-0001-617573746f6d"),
            object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)
                    mActivity?.mBluetoothLeService?.connect(result?.device?.address)
                    Log.d("getReadyLexmanBulbs", result.toString());
                }

                override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                    super.onBatchScanResults(results)
                }

                override fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                }
            })
    }

    private fun getRandomColor(): String {
        val random = Random()
        val i1 = random.nextInt(6400 - 1500) + 1500
        val i2 = random.nextInt(6400 - 1500) + 1500
        val s = "0000130A06" + i1 + i2 + "0101"
        return s;
    }

    private fun getWhiteColor(): String {
        val random = Random()
        val i1 = "500D"
        val i2 = "543F"
        val s = "0000130A06" + i1 + i2 + "0101" // 0000130A06500D543F0101
        return s;
    }

    private fun getRedColor(): String {
        val random = Random()
        val i1 = "A3D7"
        val i2 = "547A"
        val s = "0000130A06" + i1 + i2 + "0101" // 0000130A06A3D7547A0101
        return s;
    }

    private fun getGreenColor(): String {
        val random = Random()
        val i1 = "4CCC"//"3000"
        val i2 = "9999"//"6000"
        val s = "0000130A06" + i1 + i2 + "0101" // 0000130A064CCC99990101
        return s;
    }

    private fun getBlueColor(): String {
        val random = Random()
        val i1 = "2666"
        val i2 = "0F5C"
        val s = "0000130A06" + i1 + i2 + "3310" // 0000130A0626660F5C0101
        return s;
    }

    private fun getHueSaturationRose(): String {
        // hue : 0.8528957962989807
        // sat : 0.838387668132782
        val random = Random()
        val tidep = "0000"
        val opcode = "1307" // hue and saturation payload
        val length = "04" // nombre de bytes du payload
        //payload
        val hue1 = "DA"
        val sat1 = "D6"
        val transition = "01"
        val delay = "01"
        val s =
            tidep + opcode + length + hue1 + sat1 + transition + delay // 0000130A0626660F5C0101
        return s;
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