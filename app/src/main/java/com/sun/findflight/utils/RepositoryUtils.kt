package com.sun.findflight.utils

import android.content.Context
import com.sun.findflight.data.repository.*
import com.sun.findflight.data.source.local.BasicFlightLocalDataSource
import com.sun.findflight.data.source.local.FlightLocalDataSource
import com.sun.findflight.data.source.local.dao.BasicFlightDaoImpl
import com.sun.findflight.data.source.local.dao.FlightDaoImpl
import com.sun.findflight.data.source.local.db.AppDatabase
import com.sun.findflight.data.source.remote.*

object RepositoryUtils {

    fun getTokenRepository(): TokenRepository {
        val remote = TokenRemoteDataSource.getInstance()
        return TokenRepository.getInstance(remote)
    }

    fun getPlaceRepository(): PlaceRepository {
        val remote = PlaceRemoteDataSource.getInstance()
        return PlaceRepository.getInstance(remote)
    }

    fun getFlightRepository(context: Context): FlightRepository {
        val database = AppDatabase.getInstance(context)
        val remote = FlightRemoteDataSource.getInstance()
        val local = FlightLocalDataSource.getInstance(FlightDaoImpl.getInstance(database))
        return FlightRepository.getInstance(remote, local)
    }

    fun getFlightOfferRepository(): FlightOfferRepository {
        val remote = FlightOfferRemoteDataSource.getInstance()
        return FlightOfferRepository.getInstance(remote)
    }

    fun getFlightInfoRepository(): FlightInfoRepository {
        val remote = FlightInfoRemoteDataSource.getInstance()
        return FlightInfoRepository.getInstance(remote)
    }

    fun getBasicFlightRepository(context: Context): BasicFlightRepository {
        val database = AppDatabase.getInstance(context)
        val local = BasicFlightLocalDataSource.getInstance(BasicFlightDaoImpl.getInstance(database))
        return BasicFlightRepository.getInstance(local)
    }
}
