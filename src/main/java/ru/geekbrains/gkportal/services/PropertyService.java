package ru.geekbrains.gkportal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.entities.Property;
import ru.geekbrains.gkportal.entities.PropertyType;
import ru.geekbrains.gkportal.repository.PropertiesRepository;

@Service
public class PropertyService {

    private PropertiesRepository propertiesRepository;

    @Autowired
    public void setPropertiesRepository(PropertiesRepository propertiesRepository) {
        this.propertiesRepository = propertiesRepository;
    }

    public String getPropertyValue(String propertyName, PropertyType type) {
        Property property = propertiesRepository.findByNameAndPropertyType(propertyName, type);

        return property.getValue();
    }

}