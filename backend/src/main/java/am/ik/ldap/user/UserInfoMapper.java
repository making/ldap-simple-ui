package am.ik.ldap.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.ModificationItem;

import am.ik.ldap.LdapProps;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.NameAwareAttribute;
import org.springframework.stereotype.Component;

import static javax.naming.directory.DirContext.ADD_ATTRIBUTE;
import static javax.naming.directory.DirContext.REPLACE_ATTRIBUTE;

@Component
public class UserInfoMapper implements AttributesMapper<UserInfo> {
	private final LdapProps props;

	public UserInfoMapper(LdapProps props) {
		this.props = props;
	}

	@Override
	public UserInfo mapFromAttributes(Attributes attributes) throws NamingException {
		return new UserInfo(this.props.id(attributes).orElse(null),
				this.props.firstName(attributes).orElse(null),
				this.props.lastName(attributes).orElse(null),
				this.props.email(attributes).orElse(null), null, false);
	}

	public ModificationItem[] toModificationItems(UserInfo current, UserInfo updated) {
		List<ModificationItem> items = new ArrayList<>();
		this.toModOps(current.getId(), updated.getId()).ifPresent(
				ops -> items.add(new ModificationItem(ops, new NameAwareAttribute(
						this.props.getIdAttribute(), updated.getId()))));

		this.toModOps(current.getFirstName(), updated.getFirstName())
				.ifPresent(ops -> items.add(new ModificationItem(ops,
						new NameAwareAttribute(this.props.getFirstNameAttribute(),
								updated.getFirstName()))));

		this.toModOps(current.getLastName(), updated.getLastName())
				.ifPresent(ops -> items.add(new ModificationItem(ops,
						new NameAwareAttribute(this.props.getLastNameAttribute(),
								updated.getLastName()))));

		this.toModOps(current.getEmail(), updated.getEmail()).ifPresent(
				ops -> items.add(new ModificationItem(ops, new NameAwareAttribute(
						this.props.getEmailAttribute(), updated.getEmail()))));
		return items.toArray(new ModificationItem[0]);
	}

	public ModificationItem[] toModificationItems(UserInfo created) {
		return new ModificationItem[] {
				new ModificationItem(ADD_ATTRIBUTE,
						new NameAwareAttribute(this.props.getIdAttribute(),
								created.getId())),
				new ModificationItem(ADD_ATTRIBUTE,
						new NameAwareAttribute(this.props.getFirstNameAttribute(),
								created.getFirstName())),
				new ModificationItem(ADD_ATTRIBUTE,
						new NameAwareAttribute(this.props.getLastNameAttribute(),
								created.getLastName())),
				new ModificationItem(ADD_ATTRIBUTE, new NameAwareAttribute(
						this.props.getEmailAttribute(), created.getEmail())) };
	}

	private OptionalInt toModOps(String current, String updated) {
		if (Objects.equals(current, updated)) {
			return OptionalInt.empty();
		}
		if (current == null) {
			return OptionalInt.of(ADD_ATTRIBUTE);
		}
		if (updated == null) {
			return OptionalInt.empty();
		}
		return OptionalInt.of(REPLACE_ATTRIBUTE);
	}
}
