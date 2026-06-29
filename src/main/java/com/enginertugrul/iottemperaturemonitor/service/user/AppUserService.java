package com.enginertugrul.iottemperaturemonitor.service.user;

import com.enginertugrul.iottemperaturemonitor.entity.user.AppUser;
import com.enginertugrul.iottemperaturemonitor.entity.user.PreferredLanguage;
import com.enginertugrul.iottemperaturemonitor.entity.user.TemperatureUnit;

public interface AppUserService {

    AppUser createUser(
            String email,
            String passwordHash,
            PreferredLanguage preferredLanguage,
            TemperatureUnit preferredTemperatureUnit,
            String preferredTimezone
    );



}
