package com.example.mlkitlabeler

data class DetectedLabel(
    val text: String,
    val confidence: Float,
    val displayConfidence: String
)
