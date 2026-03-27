package com.amur.pocky.ui.home

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amur.pocky.data.model.Card
import com.amur.pocky.BuildConfig
import com.amur.pocky.data.model.BrandRegistry
import com.amur.pocky.ui.components.BarcodeImage
import com.amur.pocky.ui.components.CardTile
import com.amur.pocky.ui.components.EmptyState
import com.amur.pocky.util.BrightnessManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEditCardClick: (Long) -> Unit,
    onAddCardClick: () -> Unit,
    onScanClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val cards by viewModel.cards.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val barcodeCardId by viewModel.barcodeCardId.collectAsStateWithLifecycle()
    val barcodeCard = barcodeCardId?.let { id -> cards.find { it.id == id } }
    var menuCard by remember { mutableStateOf<Card?>(null) }

    Scaffold(
        topBar = {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onQueryChange = viewModel::onSearchQueryChange,
                        onSearch = {},
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = { Text("Search cards...") },
                        trailingIcon = {
                            IconButton(onClick = onScanClick) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Scan barcode")
                            }
                        },
                    )
                },
                expanded = false,
                onExpandedChange = {},
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {}
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCardClick) {
                Icon(Icons.Default.Add, contentDescription = "Add card")
            }
        },
        bottomBar = {
            Text(
                text = "v${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 4.dp),
            )
        },
    ) { padding ->
        if (cards.isEmpty() && searchQuery.isBlank()) {
            EmptyState(modifier = Modifier.padding(padding))
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp,
            ) {
                items(cards, key = { it.id }) { card ->
                    Box {
                        CardTile(
                            card = card,
                            onClick = { viewModel.showBarcode(card.id) },
                            onLongClick = { menuCard = card },
                        )

                        if (menuCard?.id == card.id) {
                            DropdownMenu(
                                expanded = true,
                                onDismissRequest = { menuCard = null },
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(if (card.isFavorite) "Unfavorite" else "Favorite")
                                    },
                                    onClick = {
                                        viewModel.toggleFavorite(card)
                                        menuCard = null
                                    },
                                    leadingIcon = {
                                        Icon(
                                            if (card.isFavorite) Icons.Filled.Star
                                            else Icons.Outlined.StarBorder,
                                            contentDescription = null,
                                        )
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    onClick = {
                                        val cardId = card.id
                                        menuCard = null
                                        onEditCardClick(cardId)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Edit, contentDescription = null)
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete") },
                                    onClick = {
                                        viewModel.deleteCard(card)
                                        menuCard = null
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Barcode fullscreen overlay
    if (barcodeCard != null) {
        val card = barcodeCard
        val activity = LocalContext.current as? Activity
        val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        val brand = card.brandId?.let { BrandRegistry.findById(it) }
        val cardColor = card.color?.let { Color(it) }
        val bgBrush = if (brand != null) {
            Brush.verticalGradient(
                listOf(brand.primaryColor, brand.secondaryColor),
            )
        } else null
        val bgColor = cardColor ?: MaterialTheme.colorScheme.surface
        val textColor = if (brand != null) brand.textColor
            else if (cardColor != null) Color.White
            else MaterialTheme.colorScheme.onSurface
        val subtextColor = if (brand != null) brand.textColor.copy(alpha = 0.8f)
            else if (cardColor != null) Color.White.copy(alpha = 0.8f)
            else MaterialTheme.colorScheme.onSurfaceVariant

        DisposableEffect(Unit) {
            activity?.let { BrightnessManager.setMaxBrightness(it) }
            onDispose {
                activity?.let { BrightnessManager.restoreBrightness(it) }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (bgBrush != null) Modifier.background(bgBrush)
                    else Modifier.background(bgColor)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { viewModel.hideBarcode() },
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (isLandscape) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(Color.White)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    BarcodeImage(
                        cardNumber = card.cardNumber,
                        format = card.barcodeFormat,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (brand != null) {
                        Image(
                            painter = painterResource(brand.logoRes),
                            contentDescription = brand.displayName,
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        text = card.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.White)
                            .padding(16.dp),
                    ) {
                        BarcodeImage(
                            cardNumber = card.cardNumber,
                            format = card.barcodeFormat,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = card.cardNumber,
                        style = MaterialTheme.typography.titleMedium,
                        color = subtextColor,
                    )
                }
            }
        }
    }
}
