package am.ik.ldap.security;

import java.util.List;
import java.util.stream.Stream;

import am.ik.ldap.LdapProps;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static java.util.stream.Collectors.toSet;

@Configuration
@ConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final LdapProps props;
	private String uaaUrl;
	private String groupsClaim = "roles";

	public SecurityConfig(LdapProps props) {
		this.props = props;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/favicon.ico", "/static/**/*");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() //
				.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll() //
				.mvcMatchers("/me").hasRole(this.props.getUserRole().toUpperCase()) //
				.mvcMatchers("/users/**").hasRole(this.props.getAdminRole().toUpperCase()) //
				.mvcMatchers("/").fullyAuthenticated() //
				.anyRequest().authenticated() //
				.and() //
				.logout() //
				.logoutSuccessUrl(this.uaaUrl + "/logout.do") //
				.and() //
				.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //
				.and() //
				.oauth2Login() //
				.userInfoEndpoint() //
				.userAuthoritiesMapper(authorities -> authorities.stream().flatMap(x -> {
					if (x instanceof OidcUserAuthority) {
						OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) x;
						OidcIdToken idToken = oidcUserAuthority.getIdToken();
						List<String> roles = idToken
								.getClaimAsStringList(this.groupsClaim);
						return roles.stream() //
								.map(role -> new SimpleGrantedAuthority(
										"ROLE_" + role.toUpperCase()));
					}
					return Stream.of(x);
				}).collect(toSet()));
	}

	public String getUaaUrl() {
		return uaaUrl;
	}

	public void setUaaUrl(String uaaUrl) {
		this.uaaUrl = uaaUrl;
	}

	public String getGroupsClaim() {
		return groupsClaim;
	}

	public void setGroupsClaim(String groupsClaim) {
		this.groupsClaim = groupsClaim;
	}
}
