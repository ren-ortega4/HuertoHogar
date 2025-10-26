package com.example.huertohogar.view.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogar.viewmodel.NotificacionesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    viewModel: NotificacionesViewModel,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFFF8FFF7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Barra superior con botón de volver
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Volver al menú",
                        tint = Color(0xFF388E3C)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Notificaciones",
                    fontSize = 24.sp,
                    color = Color(0xFF388E3C),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val notificaciones by viewModel.notificaciones.collectAsState()

            notificaciones.forEach { notif ->
                val cardColor by animateColorAsState(
                    if (notif.leido) Color(0xFFE0E0E0) else Color(0xFFE7F6D7),
                    label = "cardColor"
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .shadow(4.dp, RoundedCornerShape(18.dp))
                        .clickable { if (!notif.leido) viewModel.marcarComoLeida(notif.id) },
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Box {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                tint = if (notif.leido) Color.Gray else Color(0xFF388E3C),
                                modifier = Modifier.size(32.dp)
                            )
                            if (!notif.leido) {
                                Badge(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                ) {
                                    Text("Nuevo", fontSize = 10.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                notif.mensaje,
                                fontSize = 18.sp,
                                color = if (notif.leido) Color.Gray else Color.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if (!notif.leido) {
                                Text(
                                    "Toca para marcar como leído",
                                    fontSize = 12.sp,
                                    color = Color(0xFF388E3C),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onClose,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Cerrar")
            }
        }
    }
}