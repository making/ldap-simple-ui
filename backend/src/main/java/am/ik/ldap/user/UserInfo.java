package am.ik.ldap.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserInfo {
	private final String id;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String dn;
	private final boolean admin;

	public UserInfo(@JsonProperty("id") String id,
			@JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName,
			@JsonProperty("email") String email, @JsonProperty("dn") String dn,
			@JsonProperty("admin") Boolean admin) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.dn = dn;
		this.admin = admin != null ? admin : false;
	}

	@Override
	public String toString() {
		return "UserInfo{" + "id='" + id + '\'' + ", firstName='" + firstName + '\''
				+ ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + '}';
	}

	public String getId() {
		return this.id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public String getDn() {
		return dn;
	}

	public boolean isAdmin() {
		return admin;
	}

	public UserInfo withId(String id) {
		return new UserInfo(id, this.firstName, this.lastName, this.email, this.dn,
				this.admin);
	}

	public UserInfo withDn(String dn) {
		return new UserInfo(this.id, this.firstName, this.lastName, this.email, dn,
				this.admin);
	}

	public UserInfo withAdmin(boolean admin) {
		return new UserInfo(this.id, this.firstName, this.lastName, this.email, this.dn,
				admin);
	}
}
