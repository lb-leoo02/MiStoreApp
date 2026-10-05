package com.bonfigli.storeapp.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val DarkGlassColorScheme = darkColorScheme(
    primary = Color(0xFF3B82F6),              // Azul eléctrico moderno
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E293B),     // Azul pizarra contenedor
    onPrimaryContainer = Color(0xFF93C5FD),   // Azul brillante
    secondary = Color(0xFF94A3B8),           // Gris neutro secundario
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF272D3B),  // Gris contenedor
    onSecondaryContainer = Color(0xFFE2E8F0),
    background = Color(0xFF0B0D12),          // Fondo muy oscuro
    onBackground = Color(0xFFF8FAFC),        // Texto principal
    surface = Color(0xFF1A1E29),             // Superficie base
    onSurface = Color(0xFFF8FAFC),           // Texto en superficie
    surfaceVariant = Color(0xFF11141C),      // Contenedores de fotos
    onSurfaceVariant = Color(0xFF94A3B8),    // Texto secundario / descripciones
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF2E3545),      // Bordes
    error = Color(0xFFEF4444),
    onError = Color.White
)
