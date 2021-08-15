package com.sun.findflight.data.source.remote

import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.FlightDetail
import com.sun.findflight.data.source.FlightOfferDataSource
import com.sun.findflight.data.source.remote.api.ApiQuery
import com.sun.findflight.data.source.remote.ultils.getNetworkData
import com.sun.findflight.data.source.ultils.DownloaderTask
import com.sun.findflight.data.source.ultils.OnDataCallBack
import com.sun.findflight.utils.BaseConst
import com.sun.findflight.utils.parseJsonArrayToObjects
import org.json.JSONObject

class FlightOfferRemoteDataSource private constructor() : FlightOfferDataSource.Remote {

    override fun getOfferFlights(
        basicFlight: BasicFlight,
        callBack: OnDataCallBack<List<FlightDetail>>
    ) {
        DownloaderTask<Unit, List<FlightDetail>>(callBack) {
            getOfferFlights(basicFlight)
        }.execute(Unit)
    }

    private fun getOfferFlights(basicFlight: BasicFlight): List<FlightDetail> {
        val flightData = getJSONData(basicFlight) ?: return listOf()
        val flights = flightData
            .getString(BaseConst.DATA)
            .parseJsonArrayToObjects<FlightDetail>()
        val dictionary = flightData.getJSONObject(BaseConst.DICTIONARY)
        val locations = dictionary.getJSONObject(BaseConst.LOCATION)
        val carriers = dictionary.getJSONObject(BaseConst.CARRIERS)
        val aircrafts = dictionary.getJSONObject(BaseConst.AIRCRAFT)

        flights.forEach { flight ->
            flight.segments.forEach {
                it.departure.countryCode = locations
                    .getJSONObject(it.departure.placeCode)
                    .getString(BaseConst.COUNTRY_CODE)
                it.arrival.countryCode = locations
                    .getJSONObject(it.arrival.placeCode)
                    .getString(BaseConst.COUNTRY_CODE)
                it.carrierName = carriers.getString(it.carrierCode)
                it.aircraftName = aircrafts.getString(it.aircraftNumber)
            }
            flight.basicFlight = basicFlight
        }
        return flights
    }

    private fun getJSONData(basicFlight: BasicFlight) = with(basicFlight) {
        val desCode = basicFlight.destinationCode
        val departDate = basicFlight.departureDate
        if (desCode != null && departDate != null) {
            JSONObject(
                getNetworkData(
                    ApiQuery.queryFlightDetailSearch(
                        originLocationCode = originCode,
                        destinationLocationCode = desCode,
                        departureDate = departDate,
                        returnDate = returnDate,
                        adults = adult,
                        children = child,
                        infants = infant,
                        travelClass = travelClass,
                        nonStop = null,
                        currencyCode = currencyCode,
                        maxPrice = null
                    )
                )
            )
        } else {
            null
        }
    }

    companion object {
        private var instance: FlightOfferRemoteDataSource? = null

        fun getInstance() = instance ?: FlightOfferRemoteDataSource().also { instance = it }
    }
}
