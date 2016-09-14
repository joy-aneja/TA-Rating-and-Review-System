package org.magnum.mobilecloud.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import joy.user.client.UserClientApi;
import joy.user.repository.User;

import org.junit.Test;
import org.magnum.mobilecloud.video.TestData;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;


public class UserClientApiTest {

	private final String TEST_URL = "http://localhost:8080";

	private UserClientApi UserService = new RestAdapter.Builder()
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(UserClientApi.class);

	private User user = TestData.randomUser();
	
	
	@Test
	public void testVideoAddAndList() throws Exception {
		
		// Add the user
		UserService.addUser(user);
		
		// We should get back the user that we added above
		Collection<User> users = UserService.getUserList();
		assertTrue(users.contains(user));
	}

}
