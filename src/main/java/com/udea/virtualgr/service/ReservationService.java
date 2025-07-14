package com.udea.virtualgr.service;

import com.udea.virtualgr.entity.Reservation;
import com.udea.virtualgr.repository.FlightRepository;
import com.udea.virtualgr.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;
    public ReservationService(ReservationRepository reservationRepository, FlightRepository flightRepository) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
    }

    public Reservation reserveFlight(Long flightId, String passengerName, String seatNumber ) {
        return flightRepository.findById(flightId)
                .map(flight -> {
                    if(flight.getSeatsAvailable()<=0){
                        throw new RuntimeException("No seats available");
                    }
                    //actualizo el total de sillas del vuelo
                    flight.setSeatsAvailable(flight.getSeatsAvailable()-1);
                    Reservation reservation = new Reservation();
                    reservation.setPassengerName(passengerName);
                    reservation.setSeatNumber(seatNumber);
                    reservation.setFlight(flight);
                    reservation.setReservationCode(generateReservationCode(flight.getFlightNumber()));
                    return reservationRepository.save(reservation);

                })

                .orElseThrow(()-> new RuntimeException("flight not found"));

    }

    private String generateReservationCode(String flightNumber){
        return Optional.ofNullable(flightNumber)
                .map(number-> number + "-" + UUID.randomUUID().toString().substring(0,8).toUpperCase())
                .orElseThrow(()-> new IllegalArgumentException("flight number cannot be null"));
    }

}
