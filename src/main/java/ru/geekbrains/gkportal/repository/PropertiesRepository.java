package ru.geekbrains.gkportal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.gkportal.entity.Property;
import ru.geekbrains.gkportal.entity.PropertyType;

@Repository
public interface PropertiesRepository extends CrudRepository<Property, String> {

    Property findByNameAndPropertyType(String name, PropertyType type);

}
