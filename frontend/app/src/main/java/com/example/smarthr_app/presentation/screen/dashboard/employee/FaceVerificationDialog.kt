package com.example.smarthr_app.presentation.screen.dashboard.employee

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

enum class LivenessStage {
    LOOK_STRAIGHT,
    BLINK,
    SMILE,
    VERIFIED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceVerificationDialog(
    onDismiss: () -> Unit,
    onFaceVerified: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var currentStage by remember { mutableStateOf(LivenessStage.LOOK_STRAIGHT) }
    var statusText by remember { mutableStateOf("Position your face in the circle") }
    var verificationSuccess by remember { mutableStateOf(false) }

    // State for tracking blink sequence
    var eyesOpenedInitially by remember { mutableStateOf(false) }
    var eyesClosedDetected by remember { mutableStateOf(false) }

    // Timer state to check for step timeouts and display tips
    var stageStartTime by remember { mutableStateOf(System.currentTimeMillis()) }

    // Trigger state restart on step change
    LaunchedEffect(currentStage) {
        stageStartTime = System.currentTimeMillis()
        if (currentStage == LivenessStage.LOOK_STRAIGHT) {
            eyesOpenedInitially = false
            eyesClosedDetected = false
        }
    }

    val faceDetector = remember {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build()
        FaceDetection.getClient(options)
    }

    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
            faceDetector.close()
        }
    }

    // Auto-complete flow when verified
    LaunchedEffect(verificationSuccess) {
        if (verificationSuccess) {
            statusText = "Face Verified Successfully!"
            kotlinx.coroutines.delay(1200)
            onFaceVerified()
        }
    }

    // Smooth pulsing animation for the scanning ring
    val infiniteTransition = rememberInfiniteTransition(label = "ringPulse")
    val ringStrokeWidth by infiniteTransition.animateFloat(
        initialValue = 3.5f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "strokeWidth"
    )

    // Dynamic ring color based on liveness stage
    val ringColor = when (currentStage) {
        LivenessStage.LOOK_STRAIGHT -> Color(0xFF7E57C2) // Purple
        LivenessStage.BLINK -> Color(0xFFFF9800) // Orange
        LivenessStage.SMILE -> Color(0xFF2196F3) // Blue
        LivenessStage.VERIFIED -> Color(0xFF4CAF50) // Green
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2E)) // Dark Theme matching CrewHQ
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Face Attendance Scan",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                // Step Indicators
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepItem(
                        stepNumber = 1,
                        label = "Look Straight",
                        isActive = currentStage == LivenessStage.LOOK_STRAIGHT,
                        isCompleted = currentStage > LivenessStage.LOOK_STRAIGHT
                    )
                    StepDivider(isCompleted = currentStage > LivenessStage.LOOK_STRAIGHT)
                    StepItem(
                        stepNumber = 2,
                        label = "Blink",
                        isActive = currentStage == LivenessStage.BLINK,
                        isCompleted = currentStage > LivenessStage.BLINK
                    )
                    StepDivider(isCompleted = currentStage > LivenessStage.BLINK)
                    StepItem(
                        stepNumber = 3,
                        label = "Smile",
                        isActive = currentStage == LivenessStage.SMILE,
                        isCompleted = currentStage > LivenessStage.SMILE
                    )
                }

                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentStage != LivenessStage.VERIFIED) {
                        AndroidView(
                            factory = { ctx ->
                                val previewView = PreviewView(ctx).apply {
                                    scaleType = PreviewView.ScaleType.FILL_CENTER
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                }

                                val mainExecutor = ContextCompat.getMainExecutor(ctx)
                                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                cameraProviderFuture.addListener({
                                    val cameraProvider = cameraProviderFuture.get()

                                    // Preview
                                    val preview = Preview.Builder().build().also {
                                        it.setSurfaceProvider(previewView.surfaceProvider)
                                    }

                                    // Image Analysis
                                    val imageAnalyzer = ImageAnalysis.Builder()
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()
                                        .also {
                                            it.setAnalyzer(cameraExecutor) { imageProxy ->
                                                processImageProxy(
                                                    imageProxy = imageProxy,
                                                    faceDetector = faceDetector,
                                                    onFaceDetected = { face ->
                                                        // Run state checks and updates safely on Main Thread
                                                        mainExecutor.execute {
                                                            if (verificationSuccess) return@execute
                                                            if (face == null) {
                                                                if (currentStage != LivenessStage.VERIFIED) {
                                                                    statusText = "No face detected. Align your face."
                                                                }
                                                                return@execute
                                                            }

                                                            // Check bounding box centering and relative size to prevent spoofing and ensure correct alignment
                                                            val isRotated = imageProxy.imageInfo.rotationDegrees % 180 != 0
                                                            val imgWidth = if (isRotated) imageProxy.height else imageProxy.width
                                                            val imgHeight = if (isRotated) imageProxy.width else imageProxy.height

                                                            val boundingBox = face.boundingBox
                                                            val faceWidth = boundingBox.width()
                                                            val faceHeight = boundingBox.height()

                                                            val faceCenterX = boundingBox.centerX()
                                                            val faceCenterY = boundingBox.centerY()

                                                            val imageCenterX = imgWidth / 2f
                                                            val imageCenterY = imgHeight / 2f

                                                            val faceSizeRatio = faceWidth.toFloat() / imgWidth
                                                            val distFromCenterX = Math.abs(faceCenterX - imageCenterX) / imgWidth.toFloat()
                                                            val distFromCenterY = Math.abs(faceCenterY - imageCenterY) / imgHeight.toFloat()

                                                            // Guiding instructions based on bounding box
                                                            if (currentStage != LivenessStage.VERIFIED) {
                                                                when {
                                                                    faceSizeRatio < 0.28f -> {
                                                                        statusText = "Move closer to the camera"
                                                                        return@execute
                                                                    }
                                                                    faceSizeRatio > 0.72f -> {
                                                                        statusText = "Move slightly back"
                                                                        return@execute
                                                                    }
                                                                    distFromCenterX > 0.20f || distFromCenterY > 0.20f -> {
                                                                        statusText = "Center your face in the circle"
                                                                        return@execute
                                                                    }
                                                                }
                                                            }

                                                            val leftEyeOpen = face.leftEyeOpenProbability ?: -1f
                                                            val rightEyeOpen = face.rightEyeOpenProbability ?: -1f
                                                            val smileProb = face.smilingProbability ?: -1f

                                                            val currentTime = System.currentTimeMillis()
                                                            val stageDuration = currentTime - stageStartTime

                                                            // Reset to LOOK_STRAIGHT if step times out (>15s) to avoid user getting stuck
                                                            if (stageDuration > 15000 && currentStage != LivenessStage.LOOK_STRAIGHT && currentStage != LivenessStage.VERIFIED) {
                                                                currentStage = LivenessStage.LOOK_STRAIGHT
                                                                statusText = "Verification timed out. Try again."
                                                                return@execute
                                                            }

                                                            when (currentStage) {
                                                                LivenessStage.LOOK_STRAIGHT -> {
                                                                    statusText = "Face aligned! Look straight."
                                                                    if (leftEyeOpen > 0.65f && rightEyeOpen > 0.65f) {
                                                                        eyesOpenedInitially = true
                                                                        currentStage = LivenessStage.BLINK
                                                                    }
                                                                }
                                                                LivenessStage.BLINK -> {
                                                                    if (stageDuration > 7000) {
                                                                        statusText = "Tip: Try blinking slowly"
                                                                    } else {
                                                                        statusText = "Blink your eyes"
                                                                    }

                                                                    if (eyesOpenedInitially) {
                                                                        if (leftEyeOpen < 0.25f && rightEyeOpen < 0.25f && leftEyeOpen >= 0f) {
                                                                            eyesClosedDetected = true
                                                                        }
                                                                        if (eyesClosedDetected && leftEyeOpen > 0.65f && rightEyeOpen > 0.65f) {
                                                                            currentStage = LivenessStage.SMILE
                                                                        }
                                                                    } else {
                                                                        if (leftEyeOpen > 0.65f && rightEyeOpen > 0.65f) {
                                                                            eyesOpenedInitially = true
                                                                        }
                                                                    }
                                                                }
                                                                LivenessStage.SMILE -> {
                                                                    if (stageDuration > 7000) {
                                                                        statusText = "Tip: Smile wider and show your teeth"
                                                                    } else {
                                                                        statusText = "Now, smile!"
                                                                    }

                                                                    if (smileProb > 0.65f) {
                                                                        currentStage = LivenessStage.VERIFIED
                                                                        verificationSuccess = true
                                                                    }
                                                                }
                                                                LivenessStage.VERIFIED -> {}
                                                            }
                                                        }
                                                    },
                                                    onError = { e ->
                                                        Log.e("FaceVerification", "Error detecting face", e)
                                                    }
                                                )
                                            }
                                        }

                                    // Select front camera, back camera, or fallback
                                    val cameraSelector = when {
                                        cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) -> {
                                            CameraSelector.DEFAULT_FRONT_CAMERA
                                        }
                                        cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) -> {
                                            CameraSelector.DEFAULT_BACK_CAMERA
                                        }
                                        else -> {
                                            val availableCameras = cameraProvider.availableCameraInfos
                                            if (availableCameras.isNotEmpty()) {
                                                CameraSelector.Builder()
                                                    .addCameraFilter { cameraInfos ->
                                                        if (cameraInfos.isNotEmpty()) listOf(cameraInfos.first()) else emptyList()
                                                    }
                                                    .build()
                                            } else {
                                                CameraSelector.DEFAULT_FRONT_CAMERA
                                            }
                                        }
                                    }

                                    try {
                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            preview,
                                            imageAnalyzer
                                        )
                                    } catch (exc: Exception) {
                                        Log.e("FaceVerification", "Use case binding failed", exc)
                                    }
                                }, ContextCompat.getMainExecutor(ctx))

                                previewView
                            },
                            modifier = Modifier.fillMaxSize(),
                            update = {}
                        )

                        // Circular scanner overlay with pulsing stroke width
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = ringColor,
                                style = Stroke(width = ringStrokeWidth.dp.toPx())
                            )
                        }
                    } else {
                        // Success indicator
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF4CAF50).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓",
                                style = MaterialTheme.typography.displayLarge,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (verificationSuccess) Color(0xFF4CAF50) else Color.LightGray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, Color.Gray),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun StepItem(
    stepNumber: Int,
    label: String,
    isActive: Boolean,
    isCompleted: Boolean
) {
    val contentColor = when {
        isCompleted -> Color(0xFF4CAF50) // Green
        isActive -> Color(0xFF7E57C2) // Purple
        else -> Color.Gray
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    color = if (isCompleted || isActive) contentColor.copy(alpha = 0.15f) else Color.Transparent,
                    shape = CircleShape
                )
                .background(
                    color = if (isCompleted) contentColor else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = if (!isCompleted && !isActive) Color.Gray else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Text("✓", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
            } else {
                Text(
                    text = stepNumber.toString(),
                    color = if (isActive) Color(0xFF7E57C2) else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive || isCompleted) Color.White else Color.Gray,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun RowScope.StepDivider(isCompleted: Boolean) {
    Spacer(
        modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .background(if (isCompleted) Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.3f))
    )
}

@SuppressLint("UnsafeOptInUsageError")
private fun processImageProxy(
    imageProxy: ImageProxy,
    faceDetector: com.google.mlkit.vision.face.FaceDetector,
    onFaceDetected: (com.google.mlkit.vision.face.Face?) -> Unit,
    onError: (Exception) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                onFaceDetected(faces.firstOrNull())
            }
            .addOnFailureListener { e ->
                onError(e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}
