package com.example.dora.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.dora.R
import com.example.dora.common.Location
import com.example.dora.ui.composable.AlertDialogComposable
import com.example.dora.ui.composable.SnackBarComposable
import com.example.dora.ui.navigation.DoraScreen
import com.example.dora.ui.theme.DoraTheme
import com.example.dora.viewmodel.MainActivityViewModel
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>
    private var requestingLocationUpdates = false
    private var showSnackBar = mutableStateOf(false)
    private var showAlertDialog = mutableStateOf(false)
    private val location = mutableStateOf(Location())
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = createFusedLocationProviderClient()
        locationPermissionRequest = createLocationPermissionRequest()
        locationRequest = createLocationRequest()
        locationCallback = createLocationCallback()

        lifecycleScope.launch {
            val isUserSignedIn = mainActivityViewModel.isUserSignedIn()

            setContent {
                val theme by mainActivityViewModel.theme.collectAsState(initial = "")
                DoraTheme(darkTheme = theme == getString(R.string.dark_theme)) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val snackbarHostState = remember { SnackbarHostState() }
                        val context = LocalContext.current

                        DoraApplication(
                            startDestination =
                                if (isUserSignedIn) DoraScreen.Home.name
                                else DoraScreen.SignIn.name,
                            location = location,
                            startLocationUpdates = ::startLocationUpdates
                        )

                        if (showSnackBar.value) {
                            SnackBarComposable(snackbarHostState, context, showSnackBar)
                        }
                        if (showAlertDialog.value) {
                            AlertDialogComposable(context, showAlertDialog)
                        }
                    }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        requestingLocationUpdates = true
        val permission = Manifest.permission.ACCESS_COARSE_LOCATION

        when {
            ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED -> {
                val gpsEnabled = checkGPS()
                if (gpsEnabled) {
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                } else {
                    showAlertDialog.value = true
                }
            }
            shouldShowRequestPermissionRationale(permission) -> showSnackBar.value = true
            else -> locationPermissionRequest.launch(permission)
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun createFusedLocationProviderClient() =
        LocationServices.getFusedLocationProviderClient(this)

    private fun createLocationPermissionRequest() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                showSnackBar.value = true
            }
        }

    private fun createLocationRequest() =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .apply { setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL) }
            .build()

    private fun createLocationCallback() =
        object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                location.value =
                    Location(p0.locations.last().latitude, p0.locations.last().longitude)
                stopLocationUpdates()
                requestingLocationUpdates = false
            }
        }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkGPS(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
