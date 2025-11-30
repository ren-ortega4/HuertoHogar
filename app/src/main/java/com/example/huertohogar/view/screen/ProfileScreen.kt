package com.example.huertohogar.view.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.huertohogar.R
import com.example.huertohogar.view.components.ImagenInteligente
import com.example.huertohogar.viewmodel.UserViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    navController: NavController
) {
    val estado by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val currentUser = estado.currentUser
    val isDark = isSystemInDarkTheme()

    var showCerrarSesionDialog by remember { mutableStateOf(false) }
    var showEliminarCuentaDialog by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var isLoggingOut by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()



    //muestra error de conexion a internet por ahora solo funciona con eliminar cuenta
    if (estado.errores.errorLoginGeneral != null) {
        AlertDialog(
            onDismissRequest = { viewModel.limpiarErrorGeneral() },
            title = { Text("Error") },
            text = { Text(estado.errores.errorLoginGeneral ?: "") },
            confirmButton = {
                Button(onClick = { viewModel.limpiarErrorGeneral() }) {
                    Text("OK")
                }
            }
        )
    }

    LaunchedEffect(currentUser) {
        android.util.Log.d("ProfileScreen", "currentUser = $currentUser")
    }

    LaunchedEffect(estado) {
        android.util.Log.d("ProfileScreen", "estado completo = $estado")
    }

    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    // --- Launchers para Galería y Cámara ---
    // Función para copiar la imagen seleccionada a la carpeta privada
    fun copiarImagenAGaleriaPrivada(context: Context, uri: Uri): Uri? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val nombreArchivo = "profile_${System.currentTimeMillis()}.jpg"
            val directorio = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val archivo = java.io.File(directorio, nombreArchivo)
            val outputStream = java.io.FileOutputStream(archivo)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Uri.fromFile(archivo)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val uriPersistente = copiarImagenAGaleriaPrivada(context, it)
            uriPersistente?.let { persistUri ->
                viewModel.actualizarFotoPerfil(persistUri)
            }
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            cameraUri?.let { viewModel.actualizarFotoPerfil(it) }
        }
    }

    fun createImageUri(context: Context): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        return FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
    }

    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            cameraUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    // Contenedor principal para el fondo de pantalla
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(if (isDark) R.drawable.fondooscuro else R.drawable.fondoblanco),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenedor que permite scroll y centra el contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ){
            if (currentUser != null) {
                // --- VISTA CON SESIÓN INICIADA ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CenterAlignedTopAppBar(
                            title = { Text("PERFIL DE USUARIO", fontWeight = FontWeight.Bold) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        ImagenInteligente(currentUser.fotopefil ?: "")

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { pickImageLauncher.launch("image/*") },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("GALERÍA")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                        val uri = createImageUri(context)
                                        cameraUri = uri
                                        takePictureLauncher.launch(uri)
                                    } else {
                                        requestCameraPermission.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("CÁMARA")
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = currentUser.nombre,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentUser.correo,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        // boton de eliminar cuenta
                        Button(
                            onClick = {showEliminarCuentaDialog=true},
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD32F2F),
                                contentColor= Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("ELIMINAR CUENTA")
                        }
                        DialogConfirmacion(
                            show = showEliminarCuentaDialog,
                            titulo = "Eliminar cuenta",
                            mensaje = "¿Desea eliminar Esta Cuenta?",
                            textoConfirmar = "Eliminar",
                            colorConfirmar = Color(0xFFD32F2F),
                            onConfirm = {
                                scope.launch {
                                    showEliminarCuentaDialog=false
                                    isDeleting=true
                                    delay(3000)
                                    viewModel.eliminarCuenta()
                                    isDeleting=false
                                }
                            }, onCancel = {showEliminarCuentaDialog=false}
                        )
                        DialogCargando(show = isDeleting, mensaje = "Eliminando Cuenta...")





                        // boton de cerrar sesión
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { showCerrarSesionDialog=true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF5722),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("CERRAR SESIÓN")
                        }

                        DialogConfirmacion(
                            show = showCerrarSesionDialog,
                            titulo = "Cerrar Sesión",
                            mensaje = "¿Desea Cerrar Sesión?",
                            textoConfirmar = "Cerrar",
                            colorConfirmar = Color(0xFFFF5722),
                            onConfirm = {
                                scope.launch {
                                    showCerrarSesionDialog=false
                                    isLoggingOut=true
                                    delay(3000)
                                    viewModel.logout()
                                    isLoggingOut=false
                                }
                            }, onCancel = {showCerrarSesionDialog=false}
                        )
                        DialogCargando(show = isLoggingOut, mensaje = "Cerrando Sesión...")
                    }
                }
            } else {
                // --- VISTA SIN SESIÓN INICIADA ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Bienvenido",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        ImagenInteligente(imagenUri = "")
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.navigate("InicioSesion") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E8B57),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("INICIAR SESIÓN", fontWeight = FontWeight.Bold)
                        }
                        TextButton(
                            onClick = { navController.navigate("FormularioRegistro") }
                        ) {
                            Text(
                                buildAnnotatedString {
                                    append("Si NO TIENES CUENTA, ")
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E8B57)
                                        )
                                    ) {
                                        append("REGISTRATE AQUI")
                                    }
                                },
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}


// composable reutilizable para diálogos de confirmación
@Composable
fun DialogConfirmacion(
    show: Boolean,
    titulo: String,
    mensaje: String,
    textoConfirmar: String,
    colorConfirmar: Color,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(titulo) },
            text = { Text(mensaje) },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorConfirmar,
                        contentColor = Color.White
                    )
                ) {
                    Text(textoConfirmar)
                }
            },
            dismissButton = {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// funcion para una carga reutilizable
@Composable
fun DialogCargando(show: Boolean, mensaje: String) {
    if (show) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(mensaje) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Por favor espera")
                }
            },
            confirmButton = {}
        )
    }
}