package com.sun.findflight.ui.basicflightslist

import com.sun.findflight.R
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.Flight
import com.sun.findflight.data.repository.FlightRepository
import com.sun.findflight.data.source.ultils.OnDataCallBack
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import java.lang.Exception

class BasicFlightsListPresenterTest {

    private val view = mockk<BasicFlightsListContract.View>(relaxed = true)
    private val repository = mockk<FlightRepository>()
    private val callBack = slot<OnDataCallBack<List<Flight>>>()
    private val presenter = BasicFlightsListPresenter(view, repository)
    private val exception = Exception()

    @Test
    fun `getFlights favoriteCallBack return onSuccess, callback return onSuccess`() {
        val basicFlight = createBasicFlight()
        val flights = mutableListOf(createFlight())
        val favoriteCallBack = slot<OnDataCallBack<Unit>>()

        every {
            repository.getFlights(basicFlight, capture(callBack))
        } answers {
            callBack.captured.onSuccess(flights)
        }

        every {
            repository.isFavorite(flights, capture(favoriteCallBack))
        } answers {
            favoriteCallBack.captured.onSuccess(Unit)
        }

        presenter.getFlights(basicFlight)
        verify {
            view.showFlights(flights)
        }
    }

    @Test
    fun `getFlights favoriteCallBack return onFailure, callback return onSuccess`() {
        val basicFlight = createBasicFlight()
        val flights = mutableListOf(createFlight())
        val favoriteCallBack = slot<OnDataCallBack<Unit>>()

        every {
            repository.getFlights(basicFlight, capture(callBack))
        } answers {
            callBack.captured.onSuccess(flights)
        }

        every {
            repository.isFavorite(flights, capture(favoriteCallBack))
        } answers {
            favoriteCallBack.captured.onFailure(exception)
        }

        presenter.getFlights(basicFlight)
        verify {
            view.showFlights(flights)
        }
    }

    @Test
    fun `getFlights favoriteCallBack return onSuccess, callback return onFailure`() {
        val basicFlight = createBasicFlight()
        val flights = mutableListOf(createFlight())
        val favoriteCallBack = slot<OnDataCallBack<Unit>>()

        every {
            repository.getFlights(basicFlight, capture(callBack))
        } answers {
            callBack.captured.onFailure(exception)
        }

        every {
            repository.isFavorite(flights, capture(favoriteCallBack))
        } answers {
            favoriteCallBack.captured.onSuccess(Unit)
        }

        presenter.getFlights(basicFlight)
        verify {
            view.showMessage(R.string.error_cant_find_place)
        }
    }

    @Test
    fun `getFlights favoriteCallBack return onFailure, callback return onFailure`() {
        val basicFlight = createBasicFlight()
        val flights = mutableListOf(createFlight())
        val favoriteCallBack = slot<OnDataCallBack<Unit>>()

        every {
            repository.getFlights(basicFlight, capture(callBack))
        } answers {
            callBack.captured.onFailure(exception)
        }

        every {
            repository.isFavorite(flights, capture(favoriteCallBack))
        } answers {
            favoriteCallBack.captured.onFailure(exception)
        }

        presenter.getFlights(basicFlight)
        verify {
            view.showMessage(R.string.error_cant_find_place)
        }
    }

    @Test
    fun `insertFlightFavourite insertCallback return onSuccess valid data`() {
        val flight = createFlight()
        val insertCallback = slot<OnDataCallBack<String>>()

        every {
            repository.insertFlight(flight, capture(insertCallback))
        } answers {
            insertCallback.captured.onSuccess("3")
        }
        presenter.insertFlightFavourite(flight)
        verify {
            view.showMessage(R.string.text_added_to_favourite)
        }
    }

    @Test
    fun `insertFlightFavourite insertCallback return onSuccess invalid data`() {
        val flight = createFlight()
        val insertCallback = slot<OnDataCallBack<String>>()

        every {
            repository.insertFlight(flight, capture(insertCallback))
        } answers {
            insertCallback.captured.onSuccess("-1")
        }
        presenter.insertFlightFavourite(flight)
        verify {
            view.showMessage(R.string.error_common)
            flight.id = "0"
        }
    }

    @Test
    fun `insertFlightFavourite insertCallback return onFailure`() {
        val flight = createFlight()
        val insertCallback = slot<OnDataCallBack<String>>()

        every {
            repository.insertFlight(flight, capture(insertCallback))
        } answers {
            insertCallback.captured.onFailure(exception)
        }
        presenter.insertFlightFavourite(flight)
        verify {
            view.showMessage(R.string.error_common)
        }
    }

    @Test
    fun `deleteFlightFavourite deleteCallback return onSuccess true`() {
        val flight = createLocalFlight()
        val deleteCallback = slot<OnDataCallBack<Boolean>>()

        every {
            repository.deleteFlight(flight.id, capture(deleteCallback))
        } answers {
            deleteCallback.captured.onSuccess(true)
        }
        presenter.deleteFlightFavourite(flight)
        verify {
            view.showMessage(R.string.text_deleted_from_favourite)
        }
    }

    @Test
    fun `deleteFlightFavourite deleteCallback return onSuccess false`() {
        val flight = createLocalFlight()
        val deleteCallback = slot<OnDataCallBack<Boolean>>()

        every {
            repository.deleteFlight(flight.id, capture(deleteCallback))
        } answers {
            deleteCallback.captured.onSuccess(false)
        }
        presenter.deleteFlightFavourite(flight)
        verify {
            view.showMessage(R.string.error_common)
        }
    }

    @Test
    fun `deleteFlightFavourite deleteCallback return onFailure`() {
        val flight = createLocalFlight()
        val deleteCallback = slot<OnDataCallBack<Boolean>>()

        every {
            repository.deleteFlight(flight.id, capture(deleteCallback))
        } answers {
            deleteCallback.captured.onFailure(exception)
        }
        presenter.deleteFlightFavourite(flight)
        verify {
            view.showMessage(R.string.error_common)
        }
    }

    private fun createFlight() = Flight(
        originCode = "MAD",
        destinationCode = "MUC",
        departureDate = "2020-07-29",
        returnDate = "2020-07-30",
        flightLink = "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=MAD&destinationLocationCode=MUC&departureDate=2020-07-29&returnDate=2020-07-30&adults=1&nonStop=false"
    )

    private fun createLocalFlight() = Flight(
        id = "3",
        originCode = "MAD",
        destinationCode = "MUC",
        departureDate = "2020-07-29",
        returnDate = "2020-07-30",
        flightLink = "https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=MAD&destinationLocationCode=MUC&departureDate=2020-07-29&returnDate=2020-07-30&adults=1&nonStop=false"
    )

    private fun createBasicFlight() = BasicFlight(
        originCode = "MAD",
        destinationCode = "MUC",
        adult = "1",
        child = "0",
        infant = "0",
        travelClass = "ECONOMY",
        currencyCode = "VND"
    )
}
