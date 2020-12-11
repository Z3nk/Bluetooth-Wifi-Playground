package com.example.blapoc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_wifi.*


class WifiFragment : Fragment() {

    val TAG = "WifiFragment"
    var wifiManager: WifiManager? = null
    var mActivity: MainActivity? = null
    var mWifiConnectionReceiver: WifiConnectionReceiver? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mActivity = activity as MainActivity
        return inflater.inflate(R.layout.fragment_wifi, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wifiManager = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val havePermission = checkSelfPermission(
            context!!,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        rv_wifi.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = WifiAdapter(ArrayList()) { wifi ->
                val networkSSID = wifi.SSID
                val networkPass = "pixeloupilou"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //suggestNetwork(networkSSID, networkPass)
                    connectToWifi10(networkSSID, networkPass)

                } else {
                    val wifiManager = context?.getSystemService(WIFI_SERVICE) as WifiManager?
                    if (havePermission) {
                        var found = false
                        for (config in wifiManager?.configuredNetworks!!) {
                            if (config.SSID == String.format("\"%s\"", wifi.SSID)) {
                                wifiManager?.enableNetwork(config.networkId, true)
                                found = true
                                break
                            }
                        }

                        if (!found) {
                            val wifiConfiguration = WifiConfiguration()
                            wifiConfiguration.SSID = String.format("\"%s\"", networkSSID)
                            wifiConfiguration.preSharedKey = String.format("\"%s\"", networkPass)
//                            wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
//                            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.OPEN)
//                            wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.AuthAlgorithm.SHARED);
                            val wifiID = wifiManager?.addNetwork(wifiConfiguration)
                            if (wifiID != null) {
                                wifiManager?.enableNetwork(wifiID, true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun connectToWifi10(
        networkSSID: String,
        networkPass: String
    ) {
        val wifiNetworkSpecifier: WifiNetworkSpecifier?

        if (networkPass.isEmpty()) {
            wifiNetworkSpecifier = WifiNetworkSpecifier.Builder().setSsid(networkSSID).build()
        } else {
            wifiNetworkSpecifier = WifiNetworkSpecifier.Builder().setSsid(networkSSID).setWpa2Passphrase(networkPass).build()
        }

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(wifiNetworkSpecifier)
            .build()

        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                super.onUnavailable()
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)

            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                connectivityManager?.bindProcessToNetwork(network)
            }

            override fun onLost(network: Network) {
                super.onLost(network)

            }
        }
        connectivityManager?.requestNetwork(networkRequest, networkCallback)
    }

    private fun suggestNetwork(networkSSID: String, networkPass: String) {
        val networkSuggestion1 = WifiNetworkSuggestion.Builder()
            .setSsid(networkSSID)
            .setWpa2Passphrase(networkPass)
            .build()

        val suggestionsList = listOf(networkSuggestion1)
        var status = wifiManager?.addNetworkSuggestions(suggestionsList)
        Log.i("WifiNetworkSuggestion", "Adding Network suggestions status is $status")
        if (status == WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE) {
            Log.d(TAG, "Suggestion Update Needed")
            status = wifiManager?.removeNetworkSuggestions(suggestionsList)
            Log.i("WifiNetworkSuggestion", "Removing Network suggestions status is $status")
            status = wifiManager?.addNetworkSuggestions(suggestionsList)
        }
        if (status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            Log.d(TAG, "Suggestion Added")
        }
    }

    override fun onResume() {
        super.onResume()

        val wifiScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent) {
                val success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false
                )
                if (success) {
                    scanSuccess()
                } else {
                    // scan failure handling
                    scanFailure()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context!!.registerReceiver(wifiScanReceiver, intentFilter)

        val success = wifiManager?.startScan()
        if (!success!!) {
            // scan failure handling
            scanFailure()
        }
    }

    private fun scanFailure() {
        Log.d("WifiFfragment", "scan failure")
    }

    private fun scanSuccess() {
        wifiManager?.scanResults?.let {
            (rv_wifi.adapter as WifiAdapter).refreshListWith(it)
        }
    }
}