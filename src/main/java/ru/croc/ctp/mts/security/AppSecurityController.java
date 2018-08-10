package ru.croc.ctp.mts.security;

import java.util.UUID;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.croc.ctp.jxfw.core.facade.webclient.DomainResult;
import ru.croc.ctp.jxfw.core.facade.webclient.DomainTo;

import ru.croc.ctp.jxfw.security.facade.webclient.XSecurityControllerBase;
import ru.croc.ctp.mts.domain.User;
import ru.croc.ctp.mts.domain.facade.webclient.UserToService;
import ru.croc.ctp.mts.domain.repo.UserRepository;
import ru.croc.ctp.mts.security.CustomUserDetails;

/**
 * Class of identifying.
 * 
 * @author team1
 *
 */
@RestController
public class AppSecurityController extends XSecurityControllerBase {

	@Autowired
	private UserToService serviceTO;

	@Override
	@RequestMapping(value = "/currentUser", method = RequestMethod.GET)
	public ResponseEntity<DomainResult<DomainTo>> getCurrentUser() {
		final SecurityContext context = SecurityContextHolder.getContext();
		final Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			final Object principal = authentication.getPrincipal();
			if (principal == null || principal instanceof String) {
				return new ResponseEntity<>(HttpStatus.OK);
			}

			final CustomUserDetails currentUser = (CustomUserDetails) principal;
			DomainTo dto = serviceTO.toTo(currentUser.getUser());
			return buildResponseOk(dto);
		}

	}

	@Autowired
	private UserRepository userRepository;

	/**
	 * Method initializes user.
	 * 
	 * @return bean
	 */
	@Bean
	public InitializingBean adminUsers() {
		return () -> {
			User user = new User();
			user.setId(UUID.randomUUID().toString());
			user.setLogin("user1");
			user.setFirstName("John");
			user.setMiddleName("Mark");
			user.setPassword("password");
			user.setLastName("Constantine");
			user.setIsAdmin(true);
			userRepository.save(user);

		};
	}

	/**
	 * Method initializes user.
	 * 
	 * @return bean
	 */
	@Bean
	public InitializingBean notAdminUsers() {
		return () -> {
			User user = new User();
			user.setId(UUID.randomUUID().toString());
			user.setLogin("user2");
			user.setFirstName("Clark");
			user.setMiddleName("Donald");
			user.setPassword("password");
			user.setLastName("Kent");
			user.setIsAdmin(false);
			userRepository.save(user);

		};
	}

}
