package am.ik.ldap;

import java.util.Optional;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.LdapUtils;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ldap")
public class LdapProps {
	private String idAttribute = "cn";
	private String firstNameAttribute = "givenName";
	private String lastNameAttribute = "sn";
	private String emailAttribute = "mail";
	private String passwordAttribute = "userPassword";
	private String userRole = "users";
	private String adminRole = "administrators";

	public String getIdAttribute() {
		return idAttribute;
	}

	public void setIdAttribute(String idAttribute) {
		this.idAttribute = idAttribute;
	}

	public String getFirstNameAttribute() {
		return firstNameAttribute;
	}

	public void setFirstNameAttribute(String firstNameAttribute) {
		this.firstNameAttribute = firstNameAttribute;
	}

	public String getLastNameAttribute() {
		return lastNameAttribute;
	}

	public void setLastNameAttribute(String lastNameAttribute) {
		this.lastNameAttribute = lastNameAttribute;
	}

	public String getEmailAttribute() {
		return emailAttribute;
	}

	public void setEmailAttribute(String emailAttribute) {
		this.emailAttribute = emailAttribute;
	}

	public String getPasswordAttribute() {
		return passwordAttribute;
	}

	public void setPasswordAttribute(String passwordAttribute) {
		this.passwordAttribute = passwordAttribute;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getAdminRole() {
		return adminRole;
	}

	public void setAdminRole(String adminRole) {
		this.adminRole = adminRole;
	}

	public boolean isAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream()
				.anyMatch(authority -> ("ROLE_" + this.adminRole)
						.equalsIgnoreCase(authority.getAuthority()));
	}

	public String dn(String id) {
		return String.format("%s=%s", this.idAttribute, id);
	}

	@SuppressWarnings("deprecation")
	public String fullDn(String id, Context ctx) throws NamingException {
		String dn = this.dn(id);
		return LdapUtils.getFullDn(new DistinguishedName(dn), ctx).toCompactString();
	}

	public Optional<String> id(Attributes attributes) throws NamingException {
		return this.asString(attributes, this.idAttribute);
	}

	public Optional<String> firstName(Attributes attributes) throws NamingException {
		return this.asString(attributes, this.firstNameAttribute);
	}

	public Optional<String> lastName(Attributes attributes) throws NamingException {
		return this.asString(attributes, this.lastNameAttribute);
	}

	public Optional<String> email(Attributes attributes) throws NamingException {
		return this.asString(attributes, this.emailAttribute);
	}

	private Optional<String> asString(Attributes attributes, String name)
			throws NamingException {
		Attribute attribute = attributes.get(name);
		if (attribute == null) {
			return Optional.empty();
		}
		return Optional.of(attribute.get().toString());
	}
}
