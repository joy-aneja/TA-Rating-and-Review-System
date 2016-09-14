package org.magnum.mobilecloud.integration.test;

import static org.junit.Assert.*;

import java.util.Collection;

import joy.user.client.UserClientApi;
import joy.user.repository.User;

import org.junit.Test;
import org.magnum.mobilecloud.video.TestData;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RequestInterceptor.RequestFacade;
import retrofit.RestAdapter.LogLevel;

public class UserClientApiFindTest {

	private final String URL = "http://localhost:8080";

	/*private RequestInterceptor requestInterceptor = new RequestInterceptor() {
		
		@Override
		public void intercept(RequestFacade request) {
			// TODO Auto-generated method stub
			request.addHeader("Content-Type","plain/text" );
			
		}
	};*/
	private UserClientApi UserService = new RestAdapter.Builder()
	.setEndpoint(URL).setLogLevel(LogLevel.FULL).build()
			.create(UserClientApi.class);

	@Test
	public void testFindUser() throws Exception {
		
		Collection<User> users = UserService.findByEmailIdIgnoreCase("himanshu1449@iiitd.ac.in");
		Collection<User> allUsers = UserService.getUserList();
		assertTrue(allUsers.contains(users));
	}

}
