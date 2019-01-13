package am.ik.ldap.user;

import am.ik.ldap.LdapProps;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserInfoController {
	private final UserInfoRepository userInfoRepository;
	private final LdapProps props;

	public UserInfoController(UserInfoRepository userInfoRepository, LdapProps props) {
		this.userInfoRepository = userInfoRepository;
		this.props = props;
	}

	@GetMapping(path = "me")
	public UserInfo get(@AuthenticationPrincipal Authentication authentication) {
		String id = authentication.getName();
		return this.userInfoRepository.findById(id)
				.map(userInfo -> userInfo.withAdmin(this.props.isAdmin(authentication)))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"The given ID (" + id + ") is not found."));
	}

	@PutMapping(path = "me")
	public UserInfo update(@AuthenticationPrincipal Authentication authentication,
			@RequestBody UserInfo userInfo) {
		String id = authentication.getName();
		return this.userInfoRepository.save(userInfo.withId(id));
	}
}
