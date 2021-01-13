package com.sabrina.ldapapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sabrina.ldapapi.User;

/**
 * @author Sabrina
 *
 */
public interface ILdapService {
	
	ResponseEntity<User> create(User user);

	ResponseEntity<List<User>> findAll();

	ResponseEntity<User> findOne(String uid);

	ResponseEntity<String> delete(String uid);
	
}
