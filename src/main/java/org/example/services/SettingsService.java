package org.example.services;

import org.example.repositories.SettingsRepository;

import java.util.List;

public class SettingsService {

    SettingsRepository settingsRepository = new SettingsRepository();

    public void dropAllTables() {
        settingsRepository.dropAllTables();
    }

    public void createDummyData() {
        settingsRepository.createDummyData();
    }

    public List<Object> getEverything() {
        return settingsRepository.getEverything();
    }
}
