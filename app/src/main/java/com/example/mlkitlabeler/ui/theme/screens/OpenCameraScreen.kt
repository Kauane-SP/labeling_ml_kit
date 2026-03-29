package com.example.mlkitlabeler.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.mlkitlabeler.R
import com.example.mlkitlabeler.ui.theme.gradientPurple
import com.example.mlkitlabeler.ui.theme.titleLarge
import com.example.mlkitlabeler.ui.theme.viewModel.CameraViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OpenCamera(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: CameraViewModel = koinViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val context = LocalContext.current
    val applicationContext = context.applicationContext

    LaunchedEffect(lifecycleOwner) {
        viewModel.bindToCamera(applicationContext, lifecycleOwner)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "CAMERA IA",
                    style = titleLarge.copy(
                        brush = Brush.horizontalGradient(gradientPurple),
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Black
                    )
                )
                Text(
                    text = "Reconhecimento Inteligente",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .drawBehind {
                        drawCircle(
                            Brush.radialGradient(
                                colors = listOf(
                                    gradientPurple.first().copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            radius = size.maxDimension / 1.2f
                        )
                    }
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = gradientPurple.map { it.copy(alpha = 0.1f) }
                        )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            bounded = true,
                            radius = 16.dp,
                            color = gradientPurple.first()
                        ),
                        onClick = { navController.navigate("Camera") }
                    ),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .graphicsLayer(alpha = 0.2f)
                        .drawBehind {
                            drawCircle(
                                Brush.sweepGradient(gradientPurple),
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                            )
                        }
                )

                Icon(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "Abrir Câmera",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    Brush.verticalGradient(gradientPurple),
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        },
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "clique para acessar a câmera",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
        }
    }
}