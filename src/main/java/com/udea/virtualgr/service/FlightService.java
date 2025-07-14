package com.udea.virtualgr.service;

import com.udea.virtualgr.entity.Flight;
import com.udea.virtualgr.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class FlightService {


    private FlightRepository flightRepository;
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return Optional.ofNullable(flightRepository.findAll())
                .orElseThrow(()-> new RuntimeException("No flight available"));
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Flight not found for id: " + id));
    }

    public Flight addFlight(String flightNumber, int seatsAvailable, String origin, String destination,
                            LocalDateTime departureTime, LocalDateTime arrivalTime) {
        return Optional.of(new Flight())
                .map(flight ->{
                    setIfNotNull(flight::setFlightNumber, flightNumber);
                    setIfNotNull(flight::setSeatsAvailable, seatsAvailable);
                    setIfNotNull(flight::setOrigin, origin);
                    setIfNotNull(flight::setDestination,destination);
                    setIfNotNull(flight::setDepartureTime, departureTime);
                    setIfNotNull(flight::setArrivalTime, arrivalTime);
                    return flightRepository.save(flight);
                })
                .orElseThrow(()-> new RuntimeException("Error creando Vuelo"));
    }

    //Metodo helper generico para asignacion segura de valores
    private <T> void setIfNotNull(Consumer<T> setter, T value) {
        Optional.ofNullable(value).ifPresent(setter);
    }

}
