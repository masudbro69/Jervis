package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MakimaBloodRed
import com.example.ui.theme.MakimaCrimson
import com.example.ui.theme.MakimaDarkRed
import com.example.ui.theme.MakimaGold
import com.example.ui.theme.MakimaObsidianDark
import com.example.ui.theme.MakimaRose
import com.example.ui.theme.MakimaTextPrimary
import com.example.ui.theme.MakimaTextSecondary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 3D Animated Makima character panel with floating particles,
 * glowing eyes, and dynamic aura effects.
 */
@Composable
fun MakimaCharacterPanel(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "makima")

    // Breathing / floating animation
    val breathe by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    // Aura rotation
    val auraRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auraRotation"
    )

    // Eye glow pulse
    val eyeGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eyeGlow"
    )

    // Chain link floating
    val chainFloat by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chainFloat"
    )

    // Particle shimmer
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MakimaObsidianDark,
                        Color(0xFF0D0510),
                        MakimaBloodRed.copy(alpha = 0.3f),
                        MakimaObsidianDark
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MakimaCrimson.copy(alpha = 0.6f),
                        MakimaGold.copy(alpha = 0.3f),
                        MakimaCrimson.copy(alpha = 0.6f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background aura / particle canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(auraRotation)
        ) {
            drawMakimaAura(shimmer, this)
        }

        // Character silhouette canvas
        Canvas(
            modifier = Modifier
                .size(200.dp, 240.dp)
                .scale(breathe)
                .offset(y = chainFloat.dp)
        ) {
            drawMakimaSilhouette(eyeGlow, this)
        }

        // Overlay info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Glowing status dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MakimaCrimson)
                        .border(1.dp, MakimaGold, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MAKIMA",
                    color = MakimaTextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    letterSpacing = 3.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CONTROL DEVIL",
                    color = MakimaGold,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Autonomous Agent OS • Chain of Command Active",
                color = MakimaTextSecondary,
                fontSize = 11.sp
            )
        }

        // Top-right power indicator
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "POWER",
                color = MakimaGold.copy(alpha = 0.7f),
                fontSize = 9.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "∞",
                color = MakimaCrimson,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

/**
 * Draws rotating aura particles around the character
 */
private fun DrawScope.drawMakimaAura(shimmer: Float, scope: DrawScope) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val maxRadius = size.width * 0.45f

    // Outer glow rings
    for (ring in 0..2) {
        val radius = maxRadius * (0.5f + ring * 0.2f)
        val alpha = (0.08f - ring * 0.02f) * shimmer
        drawCircle(
            color = MakimaCrimson.copy(alpha = alpha),
            radius = radius,
            center = Offset(centerX, centerY)
        )
    }

    // Floating chain particles
    val particleCount = 18
    for (i in 0 until particleCount) {
        val angle = (2.0 * PI / particleCount * i) + (shimmer * 2.0 * PI)
        val radiusVariation = maxRadius * (0.6f + 0.35f * sin(angle * 2).toFloat())
        val px = centerX + (cos(angle) * radiusVariation).toFloat()
        val py = centerY + (sin(angle) * radiusVariation).toFloat()
        val particleAlpha = (0.3f + 0.4f * sin(shimmer * PI + i).toFloat()).coerceIn(0f, 1f)

        // Chain link dots
        drawCircle(
            color = MakimaGold.copy(alpha = particleAlpha * 0.6f),
            radius = 2.5f,
            center = Offset(px, py)
        )

        // Connecting lines between particles (chain effect)
        if (i > 0) {
            val prevAngle = (2.0 * PI / particleCount * (i - 1)) + (shimmer * 2.0 * PI)
            val prevRadius = maxRadius * (0.6f + 0.35f * sin(prevAngle * 2).toFloat())
            val prevPx = centerX + (cos(prevAngle) * prevRadius).toFloat()
            val prevPy = centerY + (sin(prevAngle) * prevRadius).toFloat()

            drawLine(
                color = MakimaCrimson.copy(alpha = particleAlpha * 0.15f),
                start = Offset(prevPx, prevPy),
                end = Offset(px, py),
                strokeWidth = 1f
            )
        }
    }
}

/**
 * Draws Makima's stylized silhouette with glowing red eyes and long hair
 */
private fun DrawScope.drawMakimaSilhouette(eyeGlow: Float, scope: DrawScope) {
    val w = size.width
    val h = size.height
    val centerX = w / 2

    // Hair — long flowing lines
    val hairColor = MakimaDarkRed.copy(alpha = 0.7f)
    val hairPath = Path().apply {
        // Left hair strand
        moveTo(centerX - 35f, h * 0.12f)
        cubicTo(centerX - 50f, h * 0.3f, centerX - 65f, h * 0.55f, centerX - 55f, h * 0.85f)
        // Right hair strand
        moveTo(centerX + 35f, h * 0.12f)
        cubicTo(centerX + 50f, h * 0.3f, centerX + 65f, h * 0.55f, centerX + 55f, h * 0.85f)
    }
    drawPath(hairPath, hairColor, style = Stroke(width = 8f, cap = StrokeCap.Round))

    // Additional hair volume
    for (i in -3..3) {
        val offset = i * 7f
        val strandPath = Path().apply {
            moveTo(centerX + offset - 25f, h * 0.1f)
            cubicTo(
                centerX + offset - 40f, h * 0.25f,
                centerX + offset - 55f, h * 0.5f,
                centerX + offset - 45f + (i * 3f), h * 0.82f
            )
        }
        drawPath(strandPath, hairColor.copy(alpha = 0.3f), style = Stroke(width = 4f, cap = StrokeCap.Round))
    }

    // Head / face silhouette
    drawOval(
        color = Color(0xFF1A1218).copy(alpha = 0.9f),
        topLeft = Offset(centerX - 30f, h * 0.08f),
        size = androidx.compose.ui.geometry.Size(60f, 75f)
    )

    // Neck
    drawRect(
        color = Color(0xFF1A1218).copy(alpha = 0.8f),
        topLeft = Offset(centerX - 12f, h * 0.18f),
        size = androidx.compose.ui.geometry.Size(24f, 30f)
    )

    // Suit / collar (Makima's iconic look)
    val suitPath = Path().apply {
        moveTo(centerX - 40f, h * 0.28f)
        lineTo(centerX - 15f, h * 0.22f)
        lineTo(centerX, h * 0.28f)
        lineTo(centerX + 15f, h * 0.22f)
        lineTo(centerX + 40f, h * 0.28f)
        lineTo(centerX + 50f, h * 0.55f)
        lineTo(centerX - 50f, h * 0.55f)
        close()
    }
    drawPath(suitPath, Color(0xFF1E1520).copy(alpha = 0.85f))

    // Tie
    drawLine(
        color = MakimaCrimson.copy(alpha = 0.6f),
        start = Offset(centerX, h * 0.25f),
        end = Offset(centerX, h * 0.42f),
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )

    // Glowing red eyes — Makima's iconic spiral/ring eyes
    val eyeY = h * 0.145f
    val eyeSpacing = 14f

    for (side in listOf(-1f, 1f)) {
        val eyeX = centerX + side * eyeSpacing

        // Outer glow
        drawCircle(
            color = MakimaCrimson.copy(alpha = eyeGlow * 0.3f),
            radius = 10f,
            center = Offset(eyeX, eyeY)
        )

        // Eye ring
        drawCircle(
            color = MakimaCrimson.copy(alpha = eyeGlow),
            radius = 5f,
            center = Offset(eyeX, eyeY),
            style = Stroke(width = 2.5f)
        )

        // Inner pupil
        drawCircle(
            color = MakimaCrimson.copy(alpha = eyeGlow),
            radius = 2f,
            center = Offset(eyeX, eyeY)
        )

        // Spiral detail inside eye
        val spiralPath = Path().apply {
            val spiralRadius = 3.5f
            for (t in 0..90 step 5) {
                val angle = t * PI / 45
                val r = spiralRadius * t / 90f
                val sx = eyeX + (cos(angle) * r).toFloat()
                val sy = eyeY + (sin(angle) * r).toFloat()
                if (t == 0) moveTo(sx, sy) else lineTo(sx, sy)
            }
        }
        drawPath(
            spiralPath,
            MakimaCrimson.copy(alpha = eyeGlow * 0.7f),
            style = Stroke(width = 1f)
        )
    }

    // Collar chain decoration (golden)
    val chainY = h * 0.27f
    for (i in -4..4) {
        val cx = centerX + i * 10f
        drawCircle(
            color = MakimaGold.copy(alpha = 0.5f),
            radius = 2.5f,
            center = Offset(cx, chainY)
        )
        if (i < 4) {
            drawLine(
                color = MakimaGold.copy(alpha = 0.3f),
                start = Offset(cx + 2.5f, chainY),
                end = Offset(cx + 7.5f, chainY),
                strokeWidth = 1.5f
            )
        }
    }
}
