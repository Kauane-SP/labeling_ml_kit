package com.example.mlkitlabeler.model

data class DetectedLabel(
    val text: String,
    val confidence: Float,
    val displayConfidence: String
)