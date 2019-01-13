package am.ik.ldap.password;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {
	private final PasswordService passwordService;

	public PasswordController(PasswordService passwordService) {
		this.passwordService = passwordService;
	}

	@PutMapping(path = "me/password")
	public Object updatePassword(@AuthenticationPrincipal OidcUser user,
			@RequestBody Password password) {
		String userId = user.getName();
		this.passwordService.changePassword(userId, password);
		return password;
	}

	@PutMapping(path = "users/{userId}/password")
	public Object updatePassword(@PathVariable("userId") String userId,
			@RequestBody Password password) {
		this.passwordService.changePassword(userId, password);
		return password;
	}
}
