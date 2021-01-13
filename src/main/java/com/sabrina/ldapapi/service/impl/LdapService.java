package com.sabrina.ldapapi.service.impl;

import java.util.List;

import javax.naming.Name;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.NameAlreadyBoundException;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
//import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import com.sabrina.ldapapi.User;
import com.sabrina.ldapapi.exception.UserAlredyExistsException;
import com.sabrina.ldapapi.exception.UserNotFoundException;
import com.sabrina.ldapapi.service.ILdapService;

/**
 * @author Sabrina
 *
 */

@SuppressWarnings("deprecation")
@Service
public class LdapService implements ILdapService {
	
	private static final Logger logger = LogManager.getLogger(LdapService.class);
	
	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public ResponseEntity<User> create(User user) {
		
		logger.info("executing {create}");
		
		Name dn = LdapNameBuilder.newInstance().add("ou", "Users").add("uid", user.getUid()).build();
		DirContextAdapter context = new DirContextAdapter(dn);

		context.setAttributeValues("objectclass",
				new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });

		context.setAttributeValue("uid", user.getUid());
		context.setAttributeValue("cn", user.getCn());
		context.setAttributeValue("sn", user.getSn());
		try {
			ldapTemplate.bind(context);
			
			logger.info("user created "+user.getCn());
			
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (NameAlreadyBoundException e) {
			
			logger.error("User not found : " + user.getUid());
			throw new UserAlredyExistsException("user not found : " + user.getUid());
		}
		
	}

	@Override
	public ResponseEntity<List<User>> findAll() {
		
		logger.info("executing {findAll}");

		EqualsFilter filter = new EqualsFilter("objectclass", "person");
		List<User> userList = ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), new UserContextMapper());
		
		logger.info("Users -> " + userList);
				
		return new ResponseEntity<>(userList, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<User> findOne(String uid) {
		
		logger.info("executing {findOne}");
		
		User user = findByUid(uid);
		if (user == null) {
			throw new UserNotFoundException("User not found : " + uid);
		}
		
		logger.info("user "+user.getCn());

		return new ResponseEntity<User>(user, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<String> delete(String uid) {
		
		logger.info("executing {delete}");

		User user = findByUid(uid);
		if (user == null) {
			logger.error("user not found "+uid);
			throw new UserNotFoundException("User not found");
		}
		
		logger.info("user deleted "+user.getCn());
		
		return new ResponseEntity<>(delete(user), HttpStatus.OK);

	}

	private User findByUid(String uid) {

		EqualsFilter f = new EqualsFilter("uid", uid);
		List<User> result = ldapTemplate.search(DistinguishedName.EMPTY_PATH, f.toString(), new UserContextMapper());

		if (result.size() != 1) {
			return null;
		}

		return result.get(0);
	}

	private String delete(User user) {
		JSONObject jsonObject = new JSONObject();
		try {
			ldapTemplate.unbind(buildDn(user.getUid()));
			jsonObject.put("message", "User deleted successfully");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();

	}

	private Name buildDn(String uid) {
		return LdapNameBuilder.newInstance().add("ou", "Users").add("uid", uid).build();
	}

	private static class UserContextMapper extends AbstractContextMapper<User> {
		public User doMapFromContext(DirContextOperations context) {
			User user = new User();
			user.setUid(context.getStringAttribute("uid"));
			user.setCn(context.getStringAttribute("cn"));
			user.setSn(context.getStringAttribute("sn"));
			return user;
		}
	}

}
