package am.ik.ldap;

import am.ik.ldap.user.UserInfoRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LdapSimpleUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LdapSimpleUiApplication.class, args);
	}

	@Bean
	CommandLineRunner clr(UserInfoRepository userInfoRepository) {
		return x -> userInfoRepository.findAll().forEach(y -> System.out.println(y));
	}

}
