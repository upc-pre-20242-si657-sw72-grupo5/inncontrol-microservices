package com.inncontrol.profile;

import com.inncontrol.profile.domain.model.aggregates.Profile;
import com.inncontrol.profile.domain.model.commands.CreateProfileCommand;
import com.inncontrol.profile.domain.model.queries.GetAllProfilesQuery;
import com.inncontrol.profile.domain.model.queries.GetProfileByIdQuery;
import com.inncontrol.profile.domain.services.ProfileCommandService;
import com.inncontrol.profile.domain.services.ProfileQueryService;
import com.inncontrol.profile.interfaces.rest.ProfilesController;
import com.inncontrol.profile.interfaces.rest.resources.CreateProfileResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProfilesController.class)
class ProfileServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProfileCommandService profileCommandService;

	@MockBean
	private ProfileQueryService profileQueryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void createProfile_ShouldReturnBadRequestForInvalidData() throws Exception {
		mockMvc.perform(post("/api/v1/profiles")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"\",\"lastName\":\"\",\"email\":\"invalid-email\",\"phoneNumber\":\"\",\"userId\":null}"))
				.andExpect(status().isBadRequest());
	}



	@Test
	void getProfileById_ShouldReturnNotFoundForNonExistentProfile() throws Exception {
		when(profileQueryService.handle(any(GetProfileByIdQuery.class)))
				.thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/profiles/{profileId}", 999L)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}


	@Test
	void getAllProfiles_ShouldReturnEmptyListWhenNoProfiles() throws Exception {
		when(profileQueryService.handle(any(GetAllProfilesQuery.class)))
				.thenReturn(List.of());

		mockMvc.perform(get("/api/v1/profiles")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
	}
}