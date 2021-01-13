package com.sabrina.ldapapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LdapapiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void test1CreateUser() {
		User user = new User();
		user.setUid("sabrina");
		user.setCn("Sabrina");
		user.setSn("Queiroz");

		ResponseEntity<User> postResponse = restTemplate.postForEntity(getRootUrl() + "/Users", user, User.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void test2CreateUserAlredyExists() {
		User user = new User();
		user.setUid("sabrina");
		user.setCn("Sabrina");
		user.setSn("Queiroz");
		try {
			restTemplate.postForEntity(getRootUrl() + "/Users", user, User.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.CONFLICT);
		}
	}

	@Test
	public void test3GetAllUsers() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/Users", HttpMethod.GET, entity,
				String.class);
		assertNotNull(response.getBody());
	}

	@Test
	public void test4GetUserByUid() {
		User user = restTemplate.getForObject(getRootUrl() + "/Users/sabrina", User.class);
		System.out.println(user.getCn());
		assertNotNull(user);
	}

	@Test
	public void test5GetUserByUidNotExists() {
		try {
			restTemplate.getForObject(getRootUrl() + "/Users/teste", User.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

	@Test
	public void test6DeleteUser() {
		String uid = "sabrina";
		User user = restTemplate.getForObject(getRootUrl() + "/Users/" + uid, User.class);
		assertNotNull(user);
		restTemplate.delete(getRootUrl() + "/User/" + uid);
		try {
			user = restTemplate.getForObject(getRootUrl() + "/Users/" + uid, User.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

	@Test
	public void test7DeleteUserNotFound() {
		String uid = "teste";
		try {
			restTemplate.delete(getRootUrl() + "/User/" + uid);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

}
