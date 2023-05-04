package com.driver.repositories;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {

    // passengerId, Passenger object
    Map<Integer, Passenger> passengerMap = new HashMap<>();


    // flightId, Flight Object
    Map<Integer, Flight> flightMap = new HashMap<>();


    // airportName, Airport object
    Map<String, Airport> airportMap = new HashMap<>();


    // flightId, passengerId map --> all passengers on a flight
    Map<Integer, List<Integer>> flightPassengerMap = new HashMap<>();


    // passengerId, flightId map --> all flights a passenger has booked
    Map<Integer, List<Integer>> passengerFlightMap = new HashMap<>();


    // Other logical operations below

    public void addAirport(Airport airport) {

        // check if the airport already exists in airportMap
        String key = airport.getAirportName();
        if (airportMap.containsKey(key) == false) {

            airportMap.put(key, airport);
        }

    }

    public List<Airport> getAllAirports() {

        // return all airports
        // return airportMap.values().stream().toList();

        List<Airport> airportList = new ArrayList<>();

        for (Airport airport : airportMap.values()) {
            airportList.add(airport);
        }

        return airportList;
    }

    public List<Flight> getAllFlights() {

        // return all flights
        // return flightMap.values().stream().toList();

        List<Flight> flightList = new ArrayList<>();

        for (Flight flight : flightMap.values()) {
            flightList.add(flight);
        }

        return flightList;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {

        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight

        // 1. get Airport from airportName
        // 2. get City from airport
        // 3. get all flights list
        // 4. iterate and match with date & fromCity or toCity,
            // if matched, increase count

        Airport airport = airportMap.get(airportName);

        City city = airport.getCity();

        // get all flights list
        List<Flight> flightList = getAllFlights();

        int peopleCount = 0;
        // iterate
        for (Flight flight : flightList) {
            // 1. now match if date is same,
            // 2. and then, takeOff or takeOn city is same too
            if (flight.getFlightDate().equals(date) && (flight.getFromCity().equals(city) || flight.getToCity().equals(city))) {
                peopleCount++;
            }
        }
        return peopleCount;
    }

    public int calculateFlightFare(Integer flightId) {

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price

        // price = 3000 + hashmap.size * 50

        int fare = 3000 + flightPassengerMap.size() * 50;

        return fare;

    }

    public String bookATicket(Integer flightId, Integer passengerId) {

        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"

        Flight flight = flightMap.get(flightId);

        if (flight.getMaxCapacity() == flightPassengerMap.get(flightId).size() || passengerFlightMap.get(passengerId).contains(flightId)) {
            return "FAILURE";
        } else {
            // do booking
            // 1. add passenger against flight
            // 2. add flight against passenger

            List<Integer> passengerList = flightPassengerMap.getOrDefault(flightId, new ArrayList<>());
            passengerList.add(passengerId);
            flightPassengerMap.put(flightId, passengerList);

            List<Integer> flightList = passengerFlightMap.getOrDefault(passengerId, new ArrayList<>());
            flightList.add(flightId);
            passengerFlightMap.put(passengerId, flightList);

            return "SUCCESS";
        }

    }

    public String cancelATicket(Integer flightId, Integer passengerId) {

        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId

        List<Integer> flightList = passengerFlightMap.getOrDefault(passengerId, new ArrayList<>());

        if (flightList.contains(flightId)) {
            // 1. remove flightId
            // 2. remove passenger in passenger list of flightId
            flightList.remove(flightId);

            flightPassengerMap.get(flightId).remove(passengerId);

            return "SUCCESS";
        }
        else {
            return "FAILURE";
        }

    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {

        return passengerFlightMap.get(passengerId).size();
    }

    public void addFlight(Flight flight) {

        // add a flight
        int key = flight.getFlightId();

        flightMap.put(key, flight);

        // also initialise passenger list in filght-passenger-map
        flightPassengerMap.put(key, new ArrayList<>());
    }

    public String getAirportNameFromFlightId(Integer flightId) {

        //We need to get the starting airportName from where the flight will be taking off (Hint think of City variable if that can be of some use)
        //return null incase the flightId is invalid or you are not able to find the airportName

        // 1. get flight object with flightId
        // 2. get city name from flight.fromCity
        // 3. Match this city name with every airport city name in airport map

        Flight flight = flightMap.get(flightId);

        City city = flight.getFromCity();

        for (Airport airport : airportMap.values()) {

            if (airport.getCity().equals(city)) {
                return airport.getAirportName();
            }
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {

        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight

        // base price of ticket --> 3000 + 50 on every ticket sale
        // means 3000 + n ( n + 1) / 2 * 50

        List<Integer> passengerList = flightPassengerMap.getOrDefault(flightId, new ArrayList<>());

        if (passengerList.isEmpty()) {
            return 0;
        }
        else {
            int n = passengerList.size() - 1;
            return 3000 + n * ( n + 1) / 2 * 50;
        }

    }

    public String addPassenger(Passenger passenger) {

        // add a passenger
        int key = passenger.getPassengerId();

        // add passenger in passengerMap
        if (passengerMap.containsKey(key) == false) {
            passengerMap.put(key, passenger);
        }

        // add passenger as key and initialise flights list in passenger-flight-map
        if (!passengerFlightMap.containsKey(key)) {
            passengerFlightMap.put(key, new ArrayList<>());
        }
        return "SUCCESS";
    }
}
