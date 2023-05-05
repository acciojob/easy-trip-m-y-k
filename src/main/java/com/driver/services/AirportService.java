package com.driver.services;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AirportService {


    AirportRepository airportRepository = new AirportRepository();


    public void addAirport(Airport airport) {

        airportRepository.addAirport(airport);
    }

    public String getLargestAirportName() {

        // get all airport list
        // filter them on terminal size and
        // return the airport with highest no of terminals

        List<Airport> airportList = airportRepository.getAllAirports();

        String airportName = null;
        int noOfTerminals = 0;

        // iterate over airport list
        for (Airport airport : airportList) {

            if (noOfTerminals < airport.getNoOfTerminals()) {
                noOfTerminals = airport.getNoOfTerminals();
                airportName = airport.getAirportName();
            } else if (noOfTerminals == airport.getNoOfTerminals() && airportName.compareTo(airport.getAirportName()) < 0) {
                noOfTerminals = airport.getNoOfTerminals();
                airportName = airport.getAirportName();
            }
        }

        return airportName;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {

        // get list of all flights
        // then, search over them and match
        // if match with source and destination cities, return duration of flight
        // else return -1

        List<Flight> flightList = airportRepository.getAllFlights();

        double shortestDurationOfFlight = Double.MAX_VALUE;

        for (Flight flight : flightList) {

            if (flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)) {

                if (shortestDurationOfFlight > flight.getDuration()) {
                    shortestDurationOfFlight = flight.getDuration();
                }
            }

        }
        return shortestDurationOfFlight;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {

        return airportRepository.getNumberOfPeopleOn(date, airportName);
    }

    public int calculateFlightFare(Integer flightId) {

        return airportRepository.calculateFlightFare(flightId);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {

        return airportRepository.bookATicket(flightId, passengerId);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {

        return airportRepository.cancelATicket(flightId, passengerId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {

        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public void addFlight(Flight flight) {

        airportRepository.addFlight(flight);
    }

    public String getAirportNameFromFlightId(Integer flightId) {

        return airportRepository.getAirportNameFromFlightId(flightId);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {

        return airportRepository.calculateRevenueOfAFlight(flightId);
    }

    public String addPassenger(Passenger passenger) {

        return airportRepository.addPassenger(passenger);
    }
}
