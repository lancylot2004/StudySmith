package dev.lancy.studysmith.structures.data

import androidx.compose.ui.graphics.vector.ImageVector

data class StudyGroup(
    val id: String,
    val name: String,
    val members: List<String>,
    val thumbnail: ImageVector,
)
