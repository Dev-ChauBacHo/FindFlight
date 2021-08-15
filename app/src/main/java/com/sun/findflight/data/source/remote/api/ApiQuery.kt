package com.sun.findflight.data.source.remote.api

import android.net.Uri

object ApiQuery {

    private fun createBaseUri() = Uri.Builder()
        .scheme(ApiConstants.SCHEME_HTTPS)
        .authority(ApiConstants.AUTHORITY_API_FLIGHT)

    fun queryToken() = createBaseUri()
        .appendPath(ApiConstants.CONTENT_VER1)
        .appendEncodedPath(ApiConstants.TOKEN)
        .toString()

    fun querySearchNearestAirport(latitude: String, longitude: String) = createBaseUri()
        .appendPath(ApiConstants.CONTENT_VER1)
        .appendEncodedPath(ApiConstants.SEARCH_PLACE)
        .appendPath(ApiConstants.NEAREST_AIRPORT)
        .appendQueryParameter(ApiConstants.LATITUDE, latitude)
        .appendQueryParameter(ApiConstants.LONGITUDE, longitude)
        .appendQueryParameter(ApiConstants.RADIUS, ApiConstants.DEFAULT_RADIUS)
        .appendQueryParameter(
            ApiConstants.MAX_ITEM_PER_PAGE,
            ApiConstants.DEFAULT_MAX_ITEM_PER_PAGE
        )
        .toString()

    fun querySearchPlaces(keyword: String, countryCode: String? = null): String {
        val uri = createBaseUri()
            .appendPath(ApiConstants.CONTENT_VER1)
            .appendEncodedPath(ApiConstants.SEARCH_PLACE)
            .appendQueryParameter(ApiConstants.SUBTYPE, ApiConstants.DEFAULT_SUB_TYPE)
            .appendQueryParameter(ApiConstants.KEYWORD, keyword)
            .appendQueryParameter(
                ApiConstants.MAX_ITEM_PER_PAGE,
                ApiConstants.DEFAULT_MAX_ITEM_PER_PAGE
            )
        countryCode?.let { uri.appendQueryParameter(ApiConstants.COUNTRY_CODE, countryCode) }
        return uri.toString()
    }

    fun queryFlightSearch(
        origin: String,
        destination: String?,
        departureDate: String?,
        oneWay: Boolean?,
    ): String {
        val uri = createBaseUri()
            .appendPath(ApiConstants.CONTENT_VER1)
            .appendPath(ApiConstants.SEARCH_FLIGHT)

        destination?.let {
            uri.appendPath(ApiConstants.FLIGHT_CHEAPEST_DATE)
        } ?: uri.appendPath(ApiConstants.FLIGHT_INSPIRATION)
        uri.appendQueryParameter(ApiConstants.ORIGIN, origin)

        destination?.let { uri.appendQueryParameter(ApiConstants.DESTINATION, it) }
        departureDate?.let { uri.appendQueryParameter(ApiConstants.DEPARTURE_DATE, it) }
        oneWay?.let { uri.appendQueryParameter(ApiConstants.ONEWAY, it.toString()) }
        uri.appendQueryParameter(ApiConstants.NONSTOP, false.toString())

        return uri.toString()
    }

    fun queryFlightDetailSearch(
        originLocationCode: String,
        destinationLocationCode: String,
        departureDate: String,
        returnDate: String?,
        adults: String,
        children: String?,
        infants: String?,
        travelClass: String?,
        nonStop: Boolean?,
        currencyCode: String?,
        maxPrice: String?
    ): String {
        val uri = createBaseUri()
            .appendPath(ApiConstants.CONTENT_VER2)
            .appendPath(ApiConstants.SEARCH_FLIGHT)
            .appendPath(ApiConstants.FLIGHT_OFFER)
            .appendQueryParameter(ApiConstants.ORIGIN_LOCATION_CODE, originLocationCode)
            .appendQueryParameter(ApiConstants.DESTINATION_LOCATION_CODE, destinationLocationCode)
            .appendQueryParameter(ApiConstants.DEPARTURE_DATE, departureDate)
        returnDate?.let { uri.appendQueryParameter(ApiConstants.RETURN_DATE, it) }
        uri.appendQueryParameter(ApiConstants.ADULTS, adults)
            .appendQueryParameter(ApiConstants.MAX_ITEMS, ApiConstants.DEFAULT_MAX_ITEM)

        children?.let { uri.appendQueryParameter(ApiConstants.CHILDREN, it) }
        infants?.let { uri.appendQueryParameter(ApiConstants.INFANTS, it) }
        travelClass?.let { uri.appendQueryParameter(ApiConstants.TRAVEL_CLASS, it) }
        nonStop?.let { uri.appendQueryParameter(ApiConstants.NONSTOP, it.toString()) }
        currencyCode?.let { uri.appendQueryParameter(ApiConstants.CURRENCY_CODE, it) }
        maxPrice?.let { uri.appendQueryParameter(ApiConstants.MAX_PRICE, it) }

        return uri.toString()
    }

    fun queryMostTraveledDestination(originCityCode: String, period: String) = createBaseUri()
        .appendPath(ApiConstants.CONTENT_VER1)
        .appendEncodedPath(ApiConstants.ANALYTICS)
        .appendQueryParameter(ApiConstants.ORIGIN_CITY_CODE, originCityCode)
        .appendQueryParameter(ApiConstants.PERIOD, period)
        .appendQueryParameter(ApiConstants.MAX_ITEMS, ApiConstants.DEFAULT_MAX_ITEM)
        .appendQueryParameter(
            ApiConstants.MAX_ITEM_PER_PAGE,
            ApiConstants.DEFAULT_MIN_ITEM_PER_PAGE
        )
        .toString()

    fun queryImage(countryCode: String, imageSize: String = ApiConstants.IMAGE_SIZE) = Uri.Builder()
        .scheme(ApiConstants.SCHEME_HTTPS)
        .authority(ApiConstants.AUTHORITY_API_IMAGE)
        .appendPath(imageSize)
        .appendPath("${countryCode}${ApiConstants.FILE_EXTENSION}")
        .toString()
}
