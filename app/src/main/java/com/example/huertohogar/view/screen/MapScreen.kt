package com.example.huertohogar.view.screen

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.huertohogar.R
import com.example.huertohogar.viewmodel.StoreViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Calendar

@Composable
fun MapScreen(storeViewModel: StoreViewModel) {
    val context = LocalContext.current
    val stores by storeViewModel.stores.collectAsState()

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
    }

    DisposableEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        onDispose {
            mapView.onDetach()
            Configuration.getInstance().save(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        }
    }

    LaunchedEffect(stores) {
        if (stores.isNotEmpty()) {
            val boundingBox = BoundingBox.fromGeoPoints(stores.map { GeoPoint(it.latitude, it.longitude) })
            mapView.post {
                mapView.zoomToBoundingBox(boundingBox, true, 100) 
            }
        }
    }

    val openIconColor = Color(0xFF00C853) // Verde
    val closedIconColor = Color(0xFFD50000) // Rojo

    val openIcon = remember { createTintedDrawable(context, R.drawable.tienda, openIconColor) }
    val closedIcon = remember { createTintedDrawable(context, R.drawable.tienda, closedIconColor) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
        ) { currentMapView ->
            currentMapView.overlays.removeAll { it is Marker }

            stores.forEach { store ->
                val (statusText, isOpen) = checkStoreStatus(store.description)
                val markerIcon = if (isOpen) openIcon else closedIcon
                val snippetText = "$statusText\n${store.description}\nDirección: ${store.address}\nTeléfono: ${store.phone}"
                
                addMarker(
                    mapView = currentMapView,
                    geoPoint = GeoPoint(store.latitude, store.longitude),
                    title = store.name,
                    snippet = snippetText,
                    icon = markerIcon
                )
            }
            currentMapView.invalidate()
        }

        // --- LEYENDA MEJORADA EN LA ESQUINA SUPERIOR DERECHA ---
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd) // Movida a la esquina superior derecha
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Tiendas: ${stores.size}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.tienda),
                        contentDescription = "Icono tienda abierta",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(openIconColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Abierto")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.tienda),
                        contentDescription = "Icono tienda cerrada",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(closedIconColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrado")
                }
            }
        }
    }
}

private fun createTintedDrawable(context: Context, drawableResId: Int, color: Color): Drawable? {
    return ContextCompat.getDrawable(context, drawableResId)?.mutate()?.apply {
        setColorFilter(color.toArgb(), PorterDuff.Mode.SRC_ATOP)
    }
}

private fun checkStoreStatus(description: String): Pair<String, Boolean> {
    val regex = """(\d{1,2}):(\d{2})[^0-9]+(\d{1,2}):(\d{2})""".toRegex()
    val matchResult = regex.find(description)

    if (matchResult != null) {
        try {
            val (openHour, openMinute, closeHour, closeMinute) = matchResult.destructured.toList().map { it.toInt() }

            val cal = Calendar.getInstance()
            val currentTotalMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
            val openTimeInMinutes = openHour * 60 + openMinute
            val closeTimeInMinutes = closeHour * 60 + closeMinute

            val isOpen = currentTotalMinutes in openTimeInMinutes until closeTimeInMinutes
            return if (isOpen) "Abierto" to true else "Cerrado" to false
        } catch (e: Exception) {
            return "Horario no disponible" to false
        }
    }
    return "Horario no disponible" to false
}

private fun addMarker(mapView: MapView, geoPoint: GeoPoint, title: String, snippet: String, icon: Drawable?) {
    val marker = Marker(mapView)
    marker.position = geoPoint
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    marker.title = title
    marker.snippet = snippet
    marker.icon = icon
    mapView.overlays.add(marker)
}
