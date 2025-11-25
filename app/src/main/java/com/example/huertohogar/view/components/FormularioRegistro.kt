package com.example.huertohogar.view.components

import android.os.Message
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.huertohogar.R
import com.example.huertohogar.viewmodel.UserViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    viewModel: UserViewModel
){
    val scope = rememberCoroutineScope()
    val isDark = isSystemInDarkTheme()
    val estado by  viewModel.uiState.collectAsState()
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // variable para el manejo de estado
    var showDialog by remember {  mutableStateOf(false)}
    var dialogMessage by remember { mutableStateOf("") }

    if (showDialog){
        LoadingDialog(dialogMessage = dialogMessage)
    }


    // esto es para que se limpie el formulario una vez que se sale de la pantalla de registro o inicio de sesion
    DisposableEffect(Unit) {
        onDispose {
            viewModel.limpiarFormulario()
        }
    }

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

        // 3. --- Colocamos todo el formulario dentro de una Card semitransparente ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp) // Padding vertical para que no toque los bordes
                .verticalScroll(rememberScrollState()), // Hacemos la tarjeta scrollable
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                // Color semitransparente para que "flote" sobre el fondo
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                // Botón de volver flotante
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = if (isDark) Color.White else Color.Black // Color dinámico para el icono
                    )
                }
            }



            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp), // Padding interno de la tarjeta
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaciado entre elementos
            ) {
                Text(
                    text = "Crea tu Cuenta",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // formulario de nombre
                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = viewModel::onNombreChange,
                    label = { Text("Nombre") },
                    leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    isError = estado.errores.nombre != null,
                    supportingText = {
                        estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // formulario de apellido
                OutlinedTextField(
                    value = estado.apellido,
                    onValueChange = viewModel::onApellidoChange,
                    label = { Text("Apellido") },
                    leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                    isError = estado.errores.apellido != null,
                    supportingText = {
                        estado.errores.apellido?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // formulario de correo
                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = viewModel::onCorreoChange,
                    label = { Text("Correo Electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    isError = estado.errores.correo != null,
                    supportingText = {
                        estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // formulario de clave (CORREGIDO)
                OutlinedTextField(
                    value = estado.clave,
                    onValueChange = viewModel::onClaveChange,
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.contrasena != null,
                    supportingText = {
                        estado.errores.contrasena?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // formulario de clave confirmar (CORREGIDO)
                OutlinedTextField(
                    value = estado.confirmarClave,
                    onValueChange = viewModel::onConfirmarClaveChange,
                    label = { Text("Confirmar Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.confirmarContrasena != null,
                    supportingText = {
                        estado.errores.confirmarContrasena?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // seleccionar region
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = estado.region,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Región") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        isError = estado.errores.region != null,
                        supportingText = {
                            estado.errores.region?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        viewModel.regiones.forEach { region ->
                            DropdownMenuItem(
                                text = { Text(region) },
                                onClick = {
                                    viewModel.onRegionChange(region)
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // acepto de terminos
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = estado.aceptaTerminos,
                        onCheckedChange = viewModel::onAceptarTerminosChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF2E8B57),
                            uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Acepto términos y condiciones", color = MaterialTheme.colorScheme.onSurface)
                }

                // Botón de Registro
                Button(
                    onClick = {
                        if (viewModel.validarFormularioRegistro()){
                            scope.launch {
                                // muestra el dialogo de registro
                                dialogMessage="Guardando...."
                                showDialog=true

                                // inicia el registro con un tiempo de 3 segundos
                                val registrojob = async { viewModel.registrarUsuario() }
                                val delayJob = async { delay(3000) }

                                // espera a que ambos terminen
                                val registroExitoso = registrojob.await()
                                delayJob.await()

                                if (registroExitoso){
                                    dialogMessage = "Usuario Registrado Con Exito !"
                                    delay(1500)

                                    showDialog=false
                                    navController.navigate("InicioSesion")
                                }else {
                                    showDialog =false

                                }
                            }
                        }
                    }, modifier = Modifier.fillMaxWidth()


                ) {
                    Text("Registar")
                }


                // Botón para ir a inicio de sesión
                TextButton(onClick = { navController.navigate("InicioSesion") }) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }
        }

    }
}
@Composable
fun LoadingDialog(dialogMessage: String){
    AlertDialog(
        onDismissRequest = {},
        containerColor = Color.DarkGray,
        title = {
            Text(
                "Procesando registro",
                color = Color(0xFF2E8B57)
            )
        },
        text = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ){
                CircularProgressIndicator(color = Color(0xFF2E8B57))
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = dialogMessage,
                    color = Color(0xFF2E8B57)
                )
            }
        }, confirmButton = {}
    )
}
