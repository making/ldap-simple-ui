package am.ik.ldap.user;

import java.util.List;
import java.util.Optional;

import javax.naming.directory.Attributes;
import javax.naming.directory.ModificationItem;

import am.ik.ldap.LdapProps;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Component
public class UserInfoRepository {
	private final LdapTemplate ldapTemplate;
	private final UserInfoMapper userInfoMapper;
	private final LdapProps props;

	public UserInfoRepository(LdapTemplate ldapTemplate, UserInfoMapper userInfoMapper,
			LdapProps props) {
		this.ldapTemplate = ldapTemplate;
		this.userInfoMapper = userInfoMapper;
		this.props = props;
	}

	public List<UserInfo> findAll() {
		return this.ldapTemplate.search(query().where("objectClass").is("person"),
				userInfoMapper);
	}

	public Optional<UserInfo> findById(String id) {
		UserInfo userInfo = this.ldapTemplate.executeReadWrite(ctx -> {
			String dn = this.props.dn(id);
			Attributes attributes = ctx.getAttributes(dn);
			String fullDn = this.props.fullDn(id, ctx);
			return userInfoMapper.mapFromAttributes(attributes).withDn(fullDn);
		});
		return Optional.ofNullable(userInfo);
	}

	public UserInfo save(UserInfo updated) {
		String dn = this.props.dn(updated.getId());
		Optional<UserInfo> byId = this.findById(updated.getId());
		ModificationItem[] modificationItems = byId
				.map(current -> this.userInfoMapper.toModificationItems(current, updated))
				.orElseGet(() -> this.userInfoMapper.toModificationItems(updated));
		this.ldapTemplate.modifyAttributes(dn, modificationItems);
		return updated;
	}

	public void delete(String id) {
		String dn = this.props.dn(id);
		this.ldapTemplate.unbind(dn);
	}
}
