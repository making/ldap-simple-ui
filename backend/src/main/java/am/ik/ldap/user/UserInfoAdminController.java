package am.ik.ldap.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserInfoAdminController {

	private final UserInfoRepository userInfoRepository;

	public UserInfoAdminController(UserInfoRepository userInfoRepository) {
		this.userInfoRepository = userInfoRepository;
	}

	@GetMapping(path = "users")
	public List<UserInfo> getAll() {
		return this.userInfoRepository.findAll();
	}

	@GetMapping(path = "users/{id}")
	public UserInfo get(@PathVariable String id) {
		return this.userInfoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"The given ID (" + id + ") is not found."));
	}

	@PostMapping(path = "users")
	@ResponseStatus(HttpStatus.CREATED)
	public UserInfo create(@RequestBody UserInfo userInfo) {
		return this.userInfoRepository.save(userInfo);
	}

	@PutMapping(path = "users/{id}")
	public UserInfo update(@PathVariable String id, @RequestBody UserInfo userInfo) {
		return this.userInfoRepository.save(userInfo.withId(id));
	}

	@DeleteMapping(path = "users/{id}")
	public void update(@PathVariable String id) {
		this.userInfoRepository.delete(id);
	}
}
