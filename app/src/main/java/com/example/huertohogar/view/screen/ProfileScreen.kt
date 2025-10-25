package com.example.huertohogar.view.screen

import android.Manifest
import android.R.attr.shape
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
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
import com.example.huertohogar.view.components.ImagenInteligente
import com.example.huertohogar.viewmodel.ProfileViewModel
import com.example.huertohogar.viewmodel.UserViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: UserViewModel, profileViewModel: ProfileViewModel, navController: NavController){
    val estado by viewModel.estado.collectAsState()
    val context = LocalContext.current
    val imagenUri by profileViewModel.imagenUri.collectAsState(initial = null)

    // --- Launchers para Galería y Cámara ---
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileViewModel.setImage(it) }
    }

    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            cameraUri?.let { profileViewModel.setImage(it) }
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


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PERFIL DE USUARIO") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E8B57),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (estado.isLoggedIn) {
                // --- VISTA CON SESIÓN INICIADA ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ImagenInteligente(imagenUri) // Componente de imagen de perfil

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Botones para cambiar imagen ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            pickImageLauncher.launch("image/*") },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("GALERÍA")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = {
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) -> {
                                    val uri = createImageUri(context)
                                    cameraUri = uri
                                    takePictureLauncher.launch(uri)
                                }
                                else -> {
                                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                                }
                            }
                        }, shape = RoundedCornerShape(10.dp)) {
                            Text("CÁMARA")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = estado.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = estado.correo,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { viewModel.logout() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.8f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("CERRAR SESIÓN")
                    }
                }
            } else {
                // --- VISTA SIN SESIÓN INICIADA ---
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ImagenInteligente(imagenUri)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate("InicioSesion") },
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF2E8B57),
                            Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("INICIAR SESIÓN") }
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