package ru.geekbrains.gkportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.gkportal.entity.Flat;

import java.util.List;

import java.util.Optional;

@Repository
public interface FlatRepository extends JpaRepository<Flat, String> {
    List<Flat> findAllByHouse(int house);

    List<Flat> findAllByHouseAndPorch(int house, int porch);

    Optional<Flat> findByHouseAndPorchAndFloorAndFlatNumber(int house, int porch, int floor, int flatNumber);
}
