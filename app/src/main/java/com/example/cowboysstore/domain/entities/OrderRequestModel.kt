package com.example.cowboysstore.domain.entities

data class OrderRequestModel(
    var ProductId : String? = null,
    var Quantity : Int? = null,
    var Size : String? = null,
    var House : String? = null,
    var Apartment : String? = null,
    var Etd : String? = null
)