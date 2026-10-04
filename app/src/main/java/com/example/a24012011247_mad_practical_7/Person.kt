package com.example.a24012011247_mad_practical_7

import java.io.Serializable

data class Person(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
) : Serializable