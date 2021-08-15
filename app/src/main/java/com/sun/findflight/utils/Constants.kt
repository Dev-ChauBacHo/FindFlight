package com.sun.findflight.utils

object BaseConst {
    const val DATA = "data"
    const val DICTIONARY = "dictionaries"
    const val LOCATION = "locations"
    const val CARRIERS = "carriers"
    const val DETAILED_NAME = "detailedName"
    const val COUNTRY_CODE = "countryCode"
    const val AIRCRAFT = "aircraft"
    const val ANALYTICS = "analytics"
    const val FLIGHTS = "flights"
    const val TRAVELERS = "travelers"
    const val SCORE = "score"
    const val ORIGIN = "origin"
    const val DESTINATION = "destination"
    const val DEPARTURE_DATE = "departureDate"
    const val RETURN_DATE = "returnDate"
    const val PRICE = "price"
    const val TOTAL = "total"
    const val LINK = "links"
    const val FLIGHT_OFFERS = "flightOffers"
    const val IATA_CODE = "iataCode"
    const val CARRIER_CODE = "carrierCode"
    const val SECONDS_VALUE = 1000
}

object FlightModelConst {
    const val LAST_TICKETING_DATE = "lastTicketingDate"
    const val ONEWAY = "oneWay"
    const val BOOKABLE_SEATS = "numberOfBookableSeats"
    const val CURRENCY = "currency"
    const val TRAVELER_PRICING = "travelerPricings"
    const val VALIDATE_AIRLINE = "validatingAirlineCodes"
    const val ITINERARIES = "itineraries"
    const val SEGMENTS = "segments"
    const val DURATION = "duration"
    const val INDEX_ZERO = 0
}

object PlaceModelConst {
    const val PLACE_TYPE = "type"
    const val PLACE_SUBTYPE = "subType"
    const val PLACE_NAME = "name"
    const val PLACE_DETAILED_NAME = "detailedName"
    const val TIME_ZONE = "timeZoneOffset"
    const val CITY_NAME = "cityName"
    const val COUNTRY_NAME = "countryName"
    const val COUNTRY_CODE = "countryCode"
    const val ADDRESS = "address"
}

object SegmentModelConst {
    const val DEPARTURE = "departure"
    const val TERMINAl = "terminal"
    const val AT_TIME = "at"
    const val ARRIVAL = "arrival"
    const val CARRIER_NUMBER = "number"
    const val AIRCRAFT = "aircraft"
    const val AIRCRAFT_CODE = "code"
}

object SegmentFareDetailModel {
    const val CABIN = "cabin"
    const val FARE_BASIS = "fareBasis"
    const val FARE_CLASS = "class"
    const val CHECKED_BAG = "includedCheckedBags"
    const val WEIGHT = "weight"
}

object TravelerModelConst {
    const val FARE_OPTION = "fareOption"
    const val TRAVELER_TYPE = "travelerType"
    const val FARE_DETAILS = "fareDetailsBySegment"
}

object NetworkConst {
    const val GET = "GET"
    const val POST = "POST"
    const val GRANT_TYPE = "grant_type=client_credentials"
    const val AUTHORIZATION = "Authorization"
    const val BASIC = "Basic"
    const val CONTENT_TYPE = "Content-Type"
    const val DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded"
    const val ACCEPT = "Accept"
    const val RESPONSE_TYPE = "application/json"
    const val BEARER = "Bearer"
    const val ACCESS_TOKEN = "access_token"
    const val EXPIRES_IN = "expires_in"
}
