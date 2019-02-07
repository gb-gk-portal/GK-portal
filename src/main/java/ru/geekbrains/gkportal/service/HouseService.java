package ru.geekbrains.gkportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.dto.FlatDTO;
import ru.geekbrains.gkportal.dto.Floor;
import ru.geekbrains.gkportal.dto.House;
import ru.geekbrains.gkportal.dto.Porch;
import ru.geekbrains.gkportal.entity.BuildPorchConfig;
import ru.geekbrains.gkportal.entity.Flat;
import ru.geekbrains.gkportal.repository.BuildPorchConfigRepository;
import ru.geekbrains.gkportal.repository.FlatRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HouseService {
    private FlatRepository flatRepository;
    private BuildPorchConfigRepository porchConfigRepository;

    @Autowired
    public void setPorchConfigRepository(BuildPorchConfigRepository porchConfigRepository) {
        this.porchConfigRepository = porchConfigRepository;
    }

    @Autowired
    public void setFlatRepository(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    private Porch makePorch(final BuildPorchConfig porchConfig, final List<Flat> flats) {
        Porch porch = new Porch();
        porch.setDescription(porchConfig);
        for (int i = porchConfig.getFloorsCount(); i >= 1; i--) {
            Floor floor = new Floor();
            int w = i;
            floor.setNumber(w);
            List<Flat> floorFlats = flats.parallelStream().filter(f -> f.getFloor() == w)
                    .filter(f -> f.getPorch().equals(porchConfig.getPorchID()))
                    //.sorted(Comparator.comparing(Flat::getRiser))
                    .collect(Collectors.toList());
            floor.getFlats().addAll(floorFlats);
            //floorFlats.forEach(s-> System.out.println(s.getUuid()));
            flats.removeAll(floorFlats);
            porch.getFloors().add(floor);
        }
        return porch;
    }

    public List<String> getHousingNumList() {
        return porchConfigRepository.findallDistinctHousingID();
    }

    /*public Integer getHousingPorchCount(int housingNum) {
        return porchConfigRepository.getCountporchIDByhousingID(housingNum);
    }*/

    public List<Integer> getHousingPorchNumbers(int housingNumber) {
        return porchConfigRepository.getPorchNumbersByHousingID(housingNumber);
    }


    public House build(int houseNumber) {
        List<BuildPorchConfig> porchConfigs = porchConfigRepository.findAllByhousingID(houseNumber);
        List<Flat> flats = flatRepository.findAllByHouse(houseNumber);
        House house = new House();
        house.setNumber(houseNumber);
        for (BuildPorchConfig config : porchConfigs
        ) {
            house.getPorchList().add(makePorch(config, flats));
        }

        return house;
    }

    public Porch build(int houseNumber, int porchNumber) {
        BuildPorchConfig porchConfig = porchConfigRepository.findAllByhousingIDAndPorchID(houseNumber, porchNumber);
        List<Flat> flats = flatRepository.findAllByHouseAndPorch(houseNumber, porchNumber);

        return makePorch(porchConfig, flats);
    }


    public Flat changeFlat(FlatDTO flatDTO) {
        Optional<Flat> optional = flatRepository.findById(flatDTO.getId());
        if (optional.isPresent()) {
            Flat flat = optional.get();
            flat.setPorch(flatDTO.getPorch());
            flat.setFloor(flatDTO.getFloor());
            flat.setFlatNumber(flatDTO.getFlatNumber());
            flat.setRiser(flatDTO.getRiser());
            flat.setFlatNumberBuild(flatDTO.getFlatNumberBuild());
            return flatRepository.save(flat);
        }
        System.out.println("Ничего не нашлось");
        return null;

    }


}
