package org.jailbreak.service.tests.managers;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.University;
import org.jailbreak.service.ServiceConfiguration;
import org.jailbreak.service.base.TeamsManagerImpl;
import org.jailbreak.service.config.JailbreakFactory;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.db.dao.TeamsDAO;
import org.jailbreak.service.helpers.DistanceHelper;
import org.junit.Before;
import org.junit.Test;

import com.github.slugify.Slugify;
import com.google.common.base.Optional;

public class TeamsManagerTest {

	private final static int SAVE_ID = 1;
	private final static int PATCH_ID = 2;
	private final static String TEAM_NAME = "Super & Awesome Team";

	private TeamsDAO dao;
	private CheckinsManager checkinsManager;
	private Slugify slg;
	private DistanceHelper distanceHelper;
	private ServiceConfiguration config;
	private JailbreakFactory jbSettings;
	private final double START_LAT = 53.348857;
	private final double START_LON = -6.285844;
	
	private final Team team_unsaved = Team.newBuilder()
			.setTeamNumber(5)
			.setTeamName(TEAM_NAME)
			.setNames("Kevin and Jack")
			.setAvatar("http://test.com/avatar.jpg")
			.setTagLine("Lorem ispum...")
			.setUniversity(University.TCD)
			.build();

	private final Team team_after_creation = team_unsaved.toBuilder()
			.setId(SAVE_ID)
			.setSlug("super-and-awesome-team")
			.build();

	private final Team team_pre_patch = Team.newBuilder()
			.setId(PATCH_ID)
			.setTeamNumber(5)
			.setTeamName("Original Name")
			.setNames("Kevin and Jack")
			.setAvatar("http://test.com/avatar.jpg")
			.setTagLine("Lorem ispum...")
			.setUniversity(University.TCD)
			.build();

	private final Team team_patch = Team.newBuilder()
			.setId(PATCH_ID)
			.setTeamName("Super New Name")
			.setNames("Kevin and Killian")
			.setAvatar("http://test.com/new_avatar.jpg")
			.setTagLine("Lorem ispum....")
			.build();

	private final Team team_after_patch = Team.newBuilder().setId(PATCH_ID)
			.setTeamNumber(5)
			.setTeamName("Super New Name")
			.setNames("Kevin and Killian")
			.setAvatar("http://test.com/new_avatar.jpg")
			.setTagLine("Lorem ispum....")
			.setUniversity(University.TCD)
			.build();

	@Before
	public void setup() {
		dao = mock(TeamsDAO.class);
		checkinsManager = mock(CheckinsManager.class);
		slg = mock(Slugify.class);
		distanceHelper = mock(DistanceHelper.class);
		config = mock(ServiceConfiguration.class);
		jbSettings = mock(JailbreakFactory.class);
		
		when(dao.insert(any(Team.class))).thenReturn(SAVE_ID);
		when(dao.getTeam(eq(SAVE_ID))).thenReturn(Optional.of(team_after_creation));
		when(dao.getTeam(eq(PATCH_ID))).thenReturn(Optional.of(team_pre_patch));
		when(slg.slugify(eq(TEAM_NAME))).thenReturn("super-and-awesome-team");
		when(config.getJailbreakSettings()).thenReturn(jbSettings);
		when(jbSettings.getStartLat()).thenReturn(START_LAT);
		when(jbSettings.getStartLon()).thenReturn(START_LON);
	}

	@Test
	public void testAddTeam() {
		// test that addTeam sets save id and sets current lat/lon
		TeamsManagerImpl manager = new TeamsManagerImpl(dao, checkinsManager, slg, distanceHelper, config);
		Team result = manager.addTeam(team_unsaved);
		assertThat(result).isEqualTo(team_after_creation);
	}

	@Test
	public void testPatchTeam() {
		TeamsManagerImpl manager = new TeamsManagerImpl(dao, checkinsManager, slg, distanceHelper, config);
		Optional<Team> result = manager.patchTeam(team_patch);
		assertThat(result.get()).isEqualTo(team_after_patch);
	}

}
