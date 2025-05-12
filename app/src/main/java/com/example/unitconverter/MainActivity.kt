package com.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unitconverter.ui.theme.UnitConverterTheme
import kotlin.math.roundToInt

/**
 * Enumeration made to represent the units we are converting to-and-from. Our base unit is
 * meters, so conversion factors are based on that.
 */
enum class Units(
    val string: String,
    val conversion: Double
) {
    METERS("Meters", 1.0),
    CENTIMETERS("Centimeters", 0.01),
    MILLIMETERS("Millimeters", 0.001),
    FEET("Feet", 0.3048),
}

/**
 * Simple MainActivity class for the Unit Converter. Will adjust if needed to be more scalable, but
 * for the simple application it is; just planning on running everything from main.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnitConverterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Modifier.padding(innerPadding)
                    UnitConverter()
                }
            }
        }
    }
}

/**
 * Main UI for the simple unit converter application.
 */
@Composable
fun UnitConverter() {

    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf(Units.METERS) }
    var outputUnit by remember { mutableStateOf(Units.METERS) }
    var iConversionFactor by remember { mutableDoubleStateOf(Units.METERS.conversion) }
    var oConversionFactor by remember { mutableDoubleStateOf(Units.METERS.conversion) }

    /**
     * Handles conversion of the user-selected units. Should be put into a domain package to handle
     * business-logic, but figured with how small the app is, it is fine to handle this here.
     */
    fun convertUnits() {
        val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result = (inputValueDouble * iConversionFactor * 100.0 / oConversionFactor).roundToInt() / 100.0
        outputValue = result.toString()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unit Converter",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                convertUnits()
                            },
            label = { Text("Enter Value") }
        )
        Spacer(Modifier.height(16.dp))

        Row {
            UnitDropdownMenu(inputUnit) {
                inputUnit = it
                iConversionFactor = it.conversion
                convertUnits()
            }
            Spacer(Modifier.width(16.dp))
            UnitDropdownMenu(outputUnit) {
                outputUnit = it
                oConversionFactor = it.conversion
                convertUnits()
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Result: $outputValue",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

/**
 * Separate composable for the dropdown menus to not duplicate logic.
 * @param unit The unit to be shown when dropdown is not expanded.
 * @param selectedUnit The new unit selected that will set text and conversion factor.
 */
@Composable
fun UnitDropdownMenu(
    unit: Units,
    selectedUnit: (Units) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(unit.string)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(Units.METERS.string) },
                onClick  = {
                    expanded = false
                    selectedUnit(Units.METERS)
                }
            )
            DropdownMenuItem(
                text = { Text(Units.CENTIMETERS.string) },
                onClick = {
                    expanded = false
                    selectedUnit(Units.CENTIMETERS)
                }
            )
            DropdownMenuItem(
                text = { Text(Units.MILLIMETERS.string) },
                onClick = {
                    expanded = false
                    selectedUnit(Units.MILLIMETERS)
                }
            )
            DropdownMenuItem(
                text = { Text(Units.FEET.string) },
                onClick = {
                    expanded = false
                    selectedUnit(Units.FEET)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverter()
}
