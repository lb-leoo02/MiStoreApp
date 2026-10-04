package com.bonfigli.storeapp.ui

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // En iOS la navegación se gestiona mediante la UI (botón de regresar en la barra)
}
