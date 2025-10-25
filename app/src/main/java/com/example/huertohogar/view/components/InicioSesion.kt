package com.example.huertohogar.view.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.huertohogar.view.screen.Screen // Importar Screen
import com.example.huertohogar.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioSesion(navController: NavController,viewModel : UserViewModel){
    val estado by viewModel.estado.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.limpiarFormulario()
        }
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("Inicio de Sesión")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E8B57),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ){
            innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            // icono de inicio de sesión
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "icono de inicio de sesión",
                modifier = Modifier.size(128.dp),
                tint = Color(0xFF2E8B57)
            )
            // Formulario de correo
            OutlinedTextField(
                value = estado.loginCorreo, // Usar el campo de login
                onValueChange = viewModel::onLoginCorreoChange, // Usar la función de login
                label = { Text("Correo") },
                isError = estado.errores.errorLoginCorreo != null, // Usar el error de login
                supportingText = {
                    estado.errores.errorLoginCorreo?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Formulario de clave
            OutlinedTextField(
                value = estado.loginClave, // Usar el campo de login
                onValueChange = viewModel::onLoginClaveChange, // Usar la función de login
                label = { Text("Clave") },
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.errorLoginClave != null, // Usar el error de login
                supportingText = {
                    estado.errores.errorLoginClave?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            // guardar datos del formulario de inicio de sesion
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    checked = estado.recordarUsuario,
                    onCheckedChange =viewModel::onRecordarUsuarioChange
                )
                Text("recordar usuario")
            }



            // boton de inicio de sesion por defecto me esta llevando a al pantalla pricipal
            Button(
                onClick = {
                    if (viewModel.validarLogin()) {
                        navController.navigate(Screen.Home.route) // Navega a la pantalla de inicio si la validación es exitosa
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E8B57),
                    contentColor = Color.White
                )
            ) {
                Text("Ingresar")
            }
        }
    }

}