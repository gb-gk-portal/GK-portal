package ru.geekbrains.gkportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.gkportal.entity.Property;
import ru.geekbrains.gkportal.entity.PropertyType;
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

    public void setPropertyValue(String propertyName, String value, PropertyType type) {
        Property property = propertiesRepository.findByNameAndPropertyType(propertyName, type);
        property.setValue(value);
        propertiesRepository.save(property);
    }

}