package com.example.listycity3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.listycity3.ui.theme.ListyCity3Theme
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCity3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = cityRepository::addCity,
                        updateCityAtIndex = cityRepository::updateCity,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// need to make the composable function to make the CityList Screen
@Composable
fun CityListScreen(cities: List<City>, onAddCity: (City) -> Unit, updateCityAtIndex: (Int, City) -> Unit, modifier: Modifier = Modifier) {
    var newCityName by rememberSaveable { mutableStateOf("") } // store the city name
    var newProvinceName by rememberSaveable { mutableStateOf("") } // store the province name
    var showAddCityFields by rememberSaveable { mutableStateOf(false) } // state for the dynamic add button
    // to store index of the cityRow has been clicked
    var selectedCityIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    Column(modifier = modifier) {
        // Row to store the dynamic addition button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = {
                    showAddCityFields = !showAddCityFields
                    selectedCityIndex = null
                    newCityName = ""
                    newProvinceName = ""
                }
            ) {
                Text("+")
            }
        }
        if (showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // text field for the city name
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text(if (selectedCityIndex == null) "City" else "Updated City")},
                    modifier = Modifier.weight(1f)
                )

                // adding 8dp spacer
                Spacer(modifier = Modifier.width(8.dp))

                // adding a text field for the province name
                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text(if (selectedCityIndex == null) "Province" else "Updated Province")},
                    modifier = Modifier.weight(1f)
                )

                // adding 8dp spacer
                Spacer(modifier = Modifier.width(8.dp))

                // adding a button to add the city
                // if the updateCity index is null show the Add city button otherwise show the update button
                Button(
                    onClick = {
                        // if the text boxes are not empty
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            // create a city object
                            val newCity = City(newCityName, newProvinceName)
                            // add the city to the list
                            // if the selectedCityIndex is null add the city otherwise update the city
                            if (selectedCityIndex == null) {
                                onAddCity(newCity)
                            } else {
                                updateCityAtIndex(selectedCityIndex ?: -1, newCity)
                                selectedCityIndex = null
                            }
                            // clear the text boxes
                            newCityName = ""
                            newProvinceName = ""
                            showAddCityFields = false
                        }
                    }
                ) {
                    Text(if (selectedCityIndex == null) "ADD CITY" else "UPDATE CITY")
                }
            }
        }

        // main column to hold the text fields and the list
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                CityRow(city = city,
                    modifier = Modifier.clickable(onClick = {
                        newCityName = city.name
                        newProvinceName = city.province
                        selectedCityIndex = index
                        showAddCityFields = true
                    })
                )
                // this adds a horizontal divider between the rows except for the last one
                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }

    }
}