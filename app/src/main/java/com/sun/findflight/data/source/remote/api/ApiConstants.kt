package com.sun.findflight.data.source.remote.api

object ApiConstants {
    const val SCHEME_HTTPS = "https"
    const val AUTHORITY_API_FLIGHT = "api.amadeus.com"
    const val CONTENT_VER1 = "v1"
    const val CONTENT_VER2 = "v2"

    const val TOKEN = "security/oauth2/token"
    const val SEARCH_PLACE = "reference-data/locations"
    const val SEARCH_FLIGHT = "shopping"
    const val ANALYTICS = "travel/analytics/air-traffic/traveled"
    const val NEAREST_AIRPORT = "airports"
    const val FLIGHT_INSPIRATION = "flight-destinations"
    const val FLIGHT_CHEAPEST_DATE = "flight-dates"
    const val FLIGHT_OFFER = "flight-offers"

    const val MAX_ITEM_PER_PAGE = "page[limit]"
    const val DEFAULT_MAX_ITEM_PER_PAGE = "50"
    const val DEFAULT_MIN_ITEM_PER_PAGE = "3"
    const val MAX_ITEMS = "max"
    const val DEFAULT_MAX_ITEM = "50"
    const val PAGE_INDEX = "page[offset]"
    const val SORT = "sort"
    const val ORIGIN = "origin"
    const val DESTINATION = "destination"
    const val DEPARTURE_DATE = "departureDate"
    const val RETURN_DATE = "returnDate"
    const val ONEWAY = "oneWay"
    const val DURATION = "duration"
    const val NONSTOP = "nonStop"
    const val MAX_PRICE = "maxPrice"
    const val VIEW_BY = "viewBy"

    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val RADIUS = "radius"
    const val DEFAULT_RADIUS = "100"

    const val SUBTYPE = "subType"
    const val DEFAULT_SUB_TYPE = "AIRPORT,CITY"
    const val KEYWORD = "keyword"
    const val COUNTRY_CODE = "countryCode"

    const val ORIGIN_LOCATION_CODE = "originLocationCode"
    const val DESTINATION_LOCATION_CODE = "destinationLocationCode"
    const val ADULTS = "adults"
    const val CHILDREN = "children"
    const val INFANTS = "infants"
    const val TRAVEL_CLASS = "travelClass"
    const val CURRENCY_CODE = "currencyCode"

    const val ORIGIN_CITY_CODE = "originCityCode"
    const val PERIOD = "period"

    const val SORT_BY_FLIGHTS_SCORE = "analytics.flights.score"
    const val SORT_BY_TRAVELERS_SCORE = "analytics.travelers.score"

    const val AUTHORITY_API_IMAGE = "flagcdn.com"
    const val IMAGE_SIZE = "112x84"
    const val FILE_EXTENSION = ".png"
}
