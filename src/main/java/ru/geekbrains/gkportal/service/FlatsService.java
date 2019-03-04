package ru.geekbrains.gkportal.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.dto.FlatDTO;
import ru.geekbrains.gkportal.dto.FlatRegDTO;
import ru.geekbrains.gkportal.dto.SystemAccount;
import ru.geekbrains.gkportal.entity.Flat;
import ru.geekbrains.gkportal.exception.FlatNotFoundException;
import ru.geekbrains.gkportal.repository.FlatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class FlatsService {

    private static final Logger logger = Logger.getLogger(FlatsService.class);

    private FlatRepository flatRepository;

    @Autowired
    public void setFlatRepository(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }


    public Flat createFlat(FlatRegDTO flatRegDTO) throws Throwable {

        return flatRepository.findByHouseAndPorchAndFloorAndFlatNumber(
                flatRegDTO.getHousingNumber(),
                flatRegDTO.getPorchNumber(),
                flatRegDTO.getFloorNumber(),
                flatRegDTO.getFlatNumber()
        ).orElseThrow((Supplier<Throwable>) () -> new FlatNotFoundException(flatRegDTO));
    }

    public List<Flat> createFlats(SystemAccount systemAccount) throws Throwable {
        List<Flat> flatArrayList = new ArrayList<>();
        for (FlatRegDTO flatDTO : systemAccount.getFlats()) {
            flatArrayList.add(createFlat(flatDTO));
        }
        return flatArrayList;
    }

    public Flat getFlatByHouseAndFlatNum(int houseNum, int flatNum, boolean isBiuldNum) {
        return isBiuldNum ?
                flatRepository.findByHouseBuildAndFlatNumberBuild(houseNum, flatNum)
                : flatRepository.findByHouseAndFlatNumber(houseNum, flatNum);
    }

    private void fillFlatDto(List<FlatDTO> flatDtos, List<Flat> flatList) {

        for (Flat flat : flatList) {

            FlatDTO flatDto = new FlatDTO();

            flatDto.setId(flat.getUuid());
            flatDto.setFlatNumber(flat.getFlatNumber());
            flatDto.setHouse(flat.getHouse());
            flatDto.setPorch(flat.getPorch());
            flatDto.setFloor(flat.getFloor());

            flatDtos.add(flatDto);
        }

    }

    public List<FlatDTO> getFlatContactsByHouseByPorhByFloor(int houseNum, int porch, int floor, int accountCount, int flatNumber) {

        List<FlatDTO> flatDtos = new ArrayList<>();

        List<Flat> flatList = flatRepository.findAllByHouseAndPorchAndFloorAndAccountCountAndFlatNumberNot(houseNum, porch,
                floor, accountCount, flatNumber);

        fillFlatDto(flatDtos, flatList);

        return flatDtos;
    }

    public List<FlatDTO> getFlatContactsByHouseByPorh(int houseNum, int porch, int accountCount, int flatNumber) {

        List<FlatDTO> flatDtos = new ArrayList<>();

        List<Flat> flatList = flatRepository.findAllByHouseAndPorchAndAccountCountAndFlatNumberNot(houseNum, porch,
                accountCount, flatNumber);

        fillFlatDto(flatDtos, flatList);

        return flatDtos;
    }

    public List<FlatDTO> getFlatContactByHouseByRiser(int houseNum, int riser, int accountCount, int flatNumber) {
        List<FlatDTO> flatDtos = new ArrayList<>();

        List<Flat> flatList = flatRepository.findAllByHouseAndRiserAndAccountCountAndFlatNumberNot(houseNum, riser,
                accountCount, flatNumber);

        fillFlatDto(flatDtos, flatList);

        return flatDtos;
    }

    public List<FlatDTO> getFlatContactsByHouse(int houseNum, int accountCount, int flatNumber) {

        List<FlatDTO> flatDtos = new ArrayList<>();

        List<Flat> flatList = flatRepository.findAllByHouseAndAccountCountAndFlatNumberNot(houseNum,
                accountCount, flatNumber);

        fillFlatDto(flatDtos, flatList);

        return flatDtos;
    }

    public Flat getById(String id) {

        return flatRepository.findByUuid(id);
    }
}
