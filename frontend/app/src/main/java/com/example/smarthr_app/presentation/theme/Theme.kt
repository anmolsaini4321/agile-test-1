package com.example.smarthr_app.presentation.theme

import android.app.Activity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    secondary = SecondaryPurple,
    tertiary = Pink80,
    background = Color.Transparent, // Make background transparent to let CrewHQBackground show
    surface = CrewHQCardBg,         // Set card surface to CrewHQCardBg
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    secondary = SecondaryPurple,
    tertiary = Pink40,
    background = BackgroundLight,
    surface = CardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun CrewHQTheme(
    darkTheme: Boolean = true, // Force dark theme to match the premium dark gradient theme
    dynamicColor: Boolean = false, // Disabled to maintain consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color(0xFF020E17).toArgb() // Match top gradient color
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun CrewHQBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundGradientStart,
                        BackgroundGradientEnd
                    )
                )
            )
    ) {
        // Draw custom canvas for premium glowing teal background + sparkles + hexagons
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Light radial glow in top right
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GlowCyan.copy(alpha = 0.5f), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.15f),
                    radius = size.width * 0.9f
                ),
                center = Offset(size.width * 0.8f, size.height * 0.15f),
                radius = size.width * 0.9f
            )
            
            // Light radial glow in bottom left
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GlowCyan.copy(alpha = 0.3f), Color.Transparent),
                    center = Offset(size.width * 0.2f, size.height * 0.8f),
                    radius = size.width * 0.8f
                ),
                center = Offset(size.width * 0.2f, size.height * 0.8f),
                radius = size.width * 0.8f
            )

            // Draw a subtle hexagon outline in the background
            val hexPath = Path().apply {
                val hexRadius = size.width * 0.45f
                val hexCenter = Offset(size.width * 0.7f, size.height * 0.25f)
                for (i in 0..5) {
                    val angle = (i * 60 - 30) * Math.PI / 180.0
                    val x = (hexCenter.x + hexRadius * Math.cos(angle)).toFloat()
                    val y = (hexCenter.y + hexRadius * Math.sin(angle)).toFloat()
                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }
            drawPath(
                path = hexPath,
                color = Color(0xFF0A3C5C).copy(alpha = 0.12f),
                style = Stroke(width = 1.dp.toPx())
            )

            // Draw another hexagon in the bottom left
            val hexPath2 = Path().apply {
                val hexRadius = size.width * 0.35f
                val hexCenter = Offset(size.width * 0.2f, size.height * 0.75f)
                for (i in 0..5) {
                    val angle = (i * 60) * Math.PI / 180.0
                    val x = (hexCenter.x + hexRadius * Math.cos(angle)).toFloat()
                    val y = (hexCenter.y + hexRadius * Math.sin(angle)).toFloat()
                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }
            drawPath(
                path = hexPath2,
                color = Color(0xFF0A3C5C).copy(alpha = 0.08f),
                style = Stroke(width = 1.dp.toPx())
            )

            // Draw faint 4-pointed stars (sparkles) in the background
            val starColor = Color(0xFF00E5FF).copy(alpha = 0.25f)
            
            fun drawSparkle(center: Offset, scale: Float) {
                val sparklePath = Path().apply {
                    moveTo(center.x, center.y - 12.dp.toPx() * scale)
                    quadraticTo(center.x, center.y, center.x + 12.dp.toPx() * scale, center.y)
                    quadraticTo(center.x, center.y, center.x, center.y + 12.dp.toPx() * scale)
                    quadraticTo(center.x, center.y, center.x - 12.dp.toPx() * scale, center.y)
                    quadraticTo(center.x, center.y, center.x, center.y - 12.dp.toPx() * scale)
                }
                drawPath(path = sparklePath, color = starColor)
            }

            drawSparkle(Offset(size.width * 0.35f, size.height * 0.28f), 0.4f)
            drawSparkle(Offset(size.width * 0.85f, size.height * 0.45f), 0.3f)
            drawSparkle(Offset(size.width * 0.15f, size.height * 0.65f), 0.5f)
            drawSparkle(Offset(size.width * 0.55f, size.height * 0.78f), 0.35f)
            drawSparkle(Offset(size.width * 0.8f, size.height * 0.92f), 0.6f)
        }

        content()
    }
}