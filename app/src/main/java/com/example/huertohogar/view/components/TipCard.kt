package com.example.huertohogar.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huertohogar.model.Tip
import com.example.huertohogar.viewmodel.TipViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun getIconForName(iconName: String): ImageVector{
    return when(iconName){
        "LocalOffer" -> Icons.Filled.LocalOffer
        "ThumbUp" -> Icons.Filled.ThumbUp
        "Storefront" -> Icons.Filled.Storefront
        "Call" -> Icons.Filled.Call
        "Error" -> Icons.Filled.Error
        else -> Icons.Filled.Info
    }
}


@Composable
fun TipCard(tip: Tip, modifier: Modifier = Modifier) {

    val progressAnimationDuration = (TipViewModel.TIP_ROTATION_DELAY_MS - 200).toInt()

    var animating by remember() { mutableFloatStateOf(0f) }


    LaunchedEffect(key1 = tip.id) {
        withContext(AndroidUiDispatcher.Main){
            animating = 0f

            delay(50)

            animating = 1f
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = animating,
        animationSpec = if (animating == 1f){
            tween(durationMillis = progressAnimationDuration, easing = LinearEasing)
        } else {
            tween(durationMillis = 0)
        },
        label = "ProgressAnimation"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF388E3C)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            AnimatedContent(
                targetState = tip,
                label = "AnimatedTipText",
                transitionSpec = {
                    fadeIn(animationSpec = tween(600)) togetherWith
                    fadeOut(animationSpec = tween(600)) using
                            SizeTransform(clip = false, sizeAnimationSpec = {_, _ -> tween(0)})
                }
            ) { targetTip ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = getIconForName(targetTip.iconName),
                        contentDescription = "Icono del Tip",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                    Column (modifier = Modifier.weight(1f)) {
                        Text(
                            text = targetTip.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = targetTip.text,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = Color.White.copy(alpha = 0.8f),
                trackColor = Color.White.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}