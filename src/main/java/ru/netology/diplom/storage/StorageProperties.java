package ru.netology.diplom.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("storage")
public class StorageProperties {

    private String rootLocation = "storage";

    public String getRootLocation() {
        return rootLocation;
    }
    public void setRootLocation(String rootLocation) {
        this.rootLocation = rootLocation;
    }
}
