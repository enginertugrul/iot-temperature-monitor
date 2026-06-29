package com.enginertugrul.iottemperaturemonitor.service.user;

import com.enginertugrul.iottemperaturemonitor.entity.user.AppUser;
import com.enginertugrul.iottemperaturemonitor.entity.user.PreferredLanguage;
import com.enginertugrul.iottemperaturemonitor.entity.user.TemperatureUnit;
import com.enginertugrul.iottemperaturemonitor.repository.user.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }


    @Override
    @Transactional
    public AppUser createUser(
            String email,
            String passwordHash,
            PreferredLanguage preferredLanguage,
            TemperatureUnit preferredTemperatureUnit,
            String preferredTimezone
    ) {


        String normalizedEmail = AppUser.normalizeEmail(email);

        ensureEmailIsAvailable(normalizedEmail);


        AppUser appUser = new AppUser(
                normalizedEmail,
                passwordHash,
                preferredLanguage,
                preferredTemperatureUnit,
                preferredTimezone
        );

        return appUserRepository.save(appUser);
    }



    @Transactional(readOnly = true)
    protected void ensureEmailIsAvailable(String normalizedEmail) {
        if (appUserRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }
    }




}

