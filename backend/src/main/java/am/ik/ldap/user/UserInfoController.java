package am.ik.ldap.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserInfoController {
	private final UserInfoRepository userInfoRepository;

	public UserInfoController(UserInfoRepository userInfoRepository) {
		this.userInfoRepository = userInfoRepository;
	}

	@GetMapping(path = "me")
	public UserInfo get(@AuthenticationPrincipal OidcUser user) {
		String id = user.getName();
		return this.userInfoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"The given ID (" + id + ") is not found."));
	}

	@PutMapping(path = "me")
	public UserInfo update(@AuthenticationPrincipal OidcUser user,
			@RequestBody UserInfo userInfo) {
		String id = user.getName();
		return this.userInfoRepository.save(userInfo.withId(id));
	}
}
