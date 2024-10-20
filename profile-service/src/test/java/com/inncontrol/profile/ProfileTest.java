package com.inncontrol.profile;

import com.inncontrol.profile.domain.model.aggregates.Profile;
import com.inncontrol.profile.domain.model.commands.CreateProfileCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sharon Antuanet Ivet Barrial Marin - u202114900
 * @version 1.0
 */

class ProfileTest {

    @Test
    void testUpdateName() {
        Profile profile = new Profile("John", "Doe", "john.doe@example.com", 1L);
        profile.updateName("Jane", "Smith");

        assertEquals("Jane Smith", profile.getFullName());
    }

    @Test
    void testUpdateEmail() {
        Profile profile = new Profile("John", "Doe", "john.doe@example.com", 1L);
        profile.updateEmail("jane.smith@example.com");

        assertEquals("jane.smith@example.com", profile.getEmailAddress());
    }

    @Test
    void testUpdatePhoneNumber() {
        Profile profile = new Profile("John", "Doe", "john.doe@example.com", 1L);
        profile.setPhoneNumber("0987654321");

        assertEquals("0987654321", profile.getPhoneNumber());
    }

}