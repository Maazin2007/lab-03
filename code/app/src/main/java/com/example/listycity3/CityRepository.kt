package com.example.listycity3
import androidx.compose.runtime.mutableStateListOf

class CityRepository {
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    val cities: List<City>
        get() = _cities

    // adding an add City function
    fun addCity(city: City) {
        _cities.add(city)
    }

    // update City on a index
    fun updateCity(index: Int, updatedCity: City) {
        // if the index is less than 0 or greater than the size of the list we return
        if (index < 0 || index >= _cities.size) {
            return
        }
        // updating the city at the given index
        _cities[index] = updatedCity
    }

}