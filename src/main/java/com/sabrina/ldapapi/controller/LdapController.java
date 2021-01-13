package com.sabrina.ldapapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sabrina.ldapapi.User;
import com.sabrina.ldapapi.service.ILdapService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Sabrina
 *
 */
@RestController
@Api(value = "LDAP API")
public class LdapController {

	@Autowired
	private ILdapService service;

	/**
	 * <p>
	 * Find all users
	 * </p>
	 * *
	 * 
	 * @return users
	 */
	@ApiOperation(value = "Find all users")
	@GetMapping(value = "/Users", produces = {"application/json; charset=UTF-8"})
	public ResponseEntity<List<User>> findAll() {
		return service.findAll();
	}

	/**
	 * <p>
	 * FFind user by uuid
	 * </p>
	 * 
	 * @param uid
	 * @return user
	 */
	@ApiOperation(value = "Find user by uuid")
	@GetMapping(value = "/Users/{uid}", produces = {"application/json; charset=UTF-8"})
	public ResponseEntity<User> findByUid(@PathVariable String uid) {
		return service.findOne(uid);
	}

	/**
	 * <p>
	 * Create new user
	 * </p>
	 * 
	 * @param user
	 * @return the user details after creating
	 */
	@ApiOperation(value = "Create new user")
	@PostMapping(value = "/Users", produces = {"application/json; charset=UTF-8"})
	public ResponseEntity<User> add(@RequestBody User user) {
		return service.create(user);
	}

	/**
	 * <p>
	 * Delete user
	 * </p>
	 * 
	 * @param uid
	 * @return
	 */
	@ApiOperation(value = "Delete user by uuid")
	@DeleteMapping(value = "/Users/{uid}", produces = {"application/json; charset=UTF-8"})
	public ResponseEntity<String> deleteById(@PathVariable String uid) {
		return service.delete(uid);
	}

}
