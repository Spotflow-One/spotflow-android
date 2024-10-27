package com.spotflow.models


open class BaseModel(val name: String)


class City(name: String) : BaseModel(name)

class Bank(name: String,val code: String) : BaseModel(name)

class BankResponse (val banks: List<Bank>)

class Country(name: String, val states: List<CountryState>) : BaseModel(name)


class CountryState(name: String, val cities: List<City>) : BaseModel(name)
