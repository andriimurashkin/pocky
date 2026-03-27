package com.amur.pocky.ui.addcard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amur.pocky.ui.components.BarcodeImage

private val cardColors = listOf(
    Color(0xFFFF6B6B),
    Color(0xFFFFBE76),
    Color(0xFF6BCB77),
    Color(0xFF4D96FF),
    Color(0xFF9B59B6),
    Color(0xFFFF9FF3),
    Color(0xFF00CEC9),
    Color(0xFFFF7675),
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddCardScreen(
    onNavigateBack: () -> Unit,
    scannedData: String? = null,
    scannedFormat: String? = null,
    cardId: Long? = null,
    viewModel: AddCardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(scannedData) {
        viewModel.initFromScan(scannedData, scannedFormat)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEditing) "Редагувати" else "Нова картка") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveCard() },
                        enabled = uiState.isValid,
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Назва картки") },
                placeholder = { Text("напр. Сільпо, АТБ, Rozetka") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.cardNumber,
                onValueChange = viewModel::onCardNumberChange,
                label = { Text("Номер картки") },
                placeholder = { Text("Цифри зі штрихкоду картки") },
                singleLine = true,
                isError = uiState.cardNumberError != null,
                supportingText = uiState.cardNumberError?.let { error ->
                    { Text(error) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.note,
                onValueChange = viewModel::onNoteChange,
                label = { Text("Нотатка (необов'язково)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Color picker
            Text("Колір картки", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                cardColors.forEach { color ->
                    val isSelected = uiState.color == color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                else Modifier
                            )
                            .clickable {
                                viewModel.onColorChange(
                                    if (isSelected) null else color.toArgb()
                                )
                            },
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Live barcode preview
            if (uiState.cardNumber.isNotBlank()) {
                Text("Попередній перегляд", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(8.dp))
                BarcodeImage(
                    cardNumber = uiState.cardNumber,
                    format = uiState.barcodeFormat,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
