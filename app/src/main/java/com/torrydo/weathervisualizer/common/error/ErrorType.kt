package com.torrydo.weathervisualizer.common.error

sealed class ErrorType(val reason: String? = null)

class DefaultError(reason: String) : ErrorType(reason)

class LocationNotFoundError(reason: String? = "location not found") : ErrorType(reason)

class NoInternetError(reason: String? = "no internet") : ErrorType(reason)

