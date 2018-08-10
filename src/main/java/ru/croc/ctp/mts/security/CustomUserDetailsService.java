package ru.croc.ctp.mts.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.croc.ctp.mts.domain.QUser;
import ru.croc.ctp.mts.domain.User;
import ru.croc.ctp.mts.domain.repo.UserRepository;

/**
 * Class implements logic of user's creating.
 * @author team1
 *
 */
@Service("userDetailsService")
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Method encrypts user's password.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final QUser user = QUser.user;
		final User empl = userRepository.findOne(user.login.eq(username));
		if (empl == null) {
			throw new UsernameNotFoundException("user with username " + username + " not found");
		}
		return new CustomUserDetails(empl, passwordEncoder.encode(empl.getPassword()));
	}
}
