package com.sun.findflight.data.source.local.dao

import com.sun.findflight.data.model.BasicFlight

interface BasicFlightDao {
    fun getBasicFlight(): BasicFlight?
    fun updateBasicFlight(basicFlight: BasicFlight): Boolean
}
