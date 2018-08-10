package ru.croc.ctp.mts.security;

import org.springframework.security.core.authority.AuthorityUtils;

import ru.croc.ctp.jxfw.security.JxfwUserDetails;
import ru.croc.ctp.mts.domain.User;

/**
 * Class of authentication
 * 
 * @author team1
 *
 */
public class CustomUserDetails extends org.springframework.security.core.userdetails.User implements JxfwUserDetails {

	private static final long serialVersionUID = 8081646706324512027L;

	private User user;

	/**
	 * Method sets login and password for user.
	 * 
	 * @param user
	 * @param password
	 */
	public CustomUserDetails(User user, String password) {
		super(user.getLogin(), password, AuthorityUtils.createAuthorityList("USER"));
		this.user = user;
	}

	/**
	 * Method returns User object.
	 * 
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Method returns Json presentation of object.
	 */
	@Override
	public String toJsonString() {
		return "{\"__metadata\": { " + "\"type\": \"" + user.getClass().getSimpleName() + "\", \"isNew\": "
				+ user.isNew() + " }, " + "\"id\" : " + "\"" + user.getId() + "\", " + "\"firstName\" : " + "\""
				+ user.getFirstName() + "\", " + "\"lastName\" : " + "\"" + user.getLastName() + "\", " + "\"login\" : "
				+ "\"" + user.getLogin() + "\"" + "}";
	}
}
