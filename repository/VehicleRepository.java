package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByMake(String make);
    List<Vehicle> findByModel(String model);
    List<Vehicle> findByMakeAndModel(String make, String model);
    List<Vehicle> findByYearBetween(int start, int end);
}
