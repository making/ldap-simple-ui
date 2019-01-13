package am.ik.ldap.password;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Password {
	private final String currentPassword;
	private final String newPassword;

	public Password(@JsonProperty("currentPassword") String currentPassword,
			@JsonProperty("newPassword") String newPassword) {
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}
}
