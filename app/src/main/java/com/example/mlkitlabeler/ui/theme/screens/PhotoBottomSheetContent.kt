package com.example.mlkitlabeler.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mlkitlabeler.ui.theme.viewModel.CameraViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PhotoBottonSheetContent(
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = koinViewModel(),
) {
    val listPhotos by viewModel.imageList.collectAsState()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        items(listPhotos) { uri ->
            AsyncImage(model = uri, contentDescription = "")
        }
    }
}