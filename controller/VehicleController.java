package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Vehicle;
import com.example.demo.repository.VehicleRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    // GET all vehicles
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // GET vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST create new vehicle
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }

    // PUT update vehicle
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle updatedVehicle) {
        return vehicleRepository.findById(id).map(vehicle -> {
            vehicle.setMake(updatedVehicle.getMake());
            vehicle.setModel(updatedVehicle.getModel());
            vehicle.setColor(updatedVehicle.getColor());
            vehicle.setYear(updatedVehicle.getYear());
            return ResponseEntity.ok(vehicleRepository.save(vehicle));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE vehicle
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        return vehicleRepository.findById(id).map(vehicle -> {
            vehicleRepository.delete(vehicle);
            return ResponseEntity.ok().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // SEARCH by make and/or model
    @GetMapping("/search")
    public List<Vehicle> searchVehicles(@RequestParam(required = false) String make,
                                        @RequestParam(required = false) String model) {
        if (make != null && model != null) {
            return vehicleRepository.findByMakeAndModel(make, model);
        } else if (make != null) {
            return vehicleRepository.findByMake(make);
        } else if (model != null) {
            return vehicleRepository.findByModel(model);
        } else {
            return vehicleRepository.findAll();
        }
    }

    // FILTER by year range
    @GetMapping("/year-range")
    public List<Vehicle> getVehiclesByYearRange(@RequestParam int start, @RequestParam int end) {
        return vehicleRepository.findByYearBetween(start, end);
    }
}
