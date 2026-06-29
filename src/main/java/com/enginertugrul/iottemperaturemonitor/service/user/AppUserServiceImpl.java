package com.enginertugrul.iottemperaturemonitor.service.user;

import com.enginertugrul.iottemperaturemonitor.dto.auth.RegisterUserForm;
import com.enginertugrul.iottemperaturemonitor.entity.user.AppUser;
import com.enginertugrul.iottemperaturemonitor.repository.user.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;


    public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AppUser createUser(RegisterUserForm registerUserForm) {


        String normalizedEmail = AppUser.normalizeEmail(registerUserForm.getEmail());

        String passwordHash = passwordEncoder.encode(registerUserForm.getPassword());

        ensureEmailIsAvailable(normalizedEmail);


        AppUser appUser = new AppUser(
                normalizedEmail,
                passwordHash,
                registerUserForm.getPreferredLanguage(),
                registerUserForm.getPreferredTemperatureUnit(),
                registerUserForm.getPreferredTimezone()
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

