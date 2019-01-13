package am.ik.ldap.password;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.naming.ldap.ExtendedRequest;
import javax.naming.ldap.ExtendedResponse;
import javax.naming.ldap.LdapContext;

import am.ik.ldap.LdapProps;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
public class PasswordService {
	private final LdapTemplate ldapTemplate;
	private final LdapProps props;

	public PasswordService(LdapTemplate ldapTemplate, LdapProps props) {
		this.ldapTemplate = ldapTemplate;
		this.props = props;
	}

	@SuppressWarnings("deprecation")
	public void changePassword(String userId, Password password) {
		this.ldapTemplate.executeReadWrite(dirCtx -> {
			LdapContext ctx = (LdapContext) dirCtx;
			String userIdentity = this.props.fullDn(userId, ctx);
			PasswordModifyRequest request = new PasswordModifyRequest(userIdentity,
					password.getCurrentPassword(), password.getNewPassword());
			try {
				return ctx.extendedOperation(request);
			}
			catch (javax.naming.AuthenticationException e) {
				throw new BadCredentialsException(
						"Authentication for password change failed.");
			}
		});
	}

	/**
	 * An implementation of the
	 * <a target="_blank" href="https://tools.ietf.org/html/rfc3062"> LDAP Password Modify
	 * Extended Operation </a> client request.
	 *
	 * Can be directed at any LDAP server that supports the Password Modify Extended
	 * Operation.
	 *
	 * @author Josh Cummings
	 * @since 4.2.9
	 */
	private static class PasswordModifyRequest implements ExtendedRequest {
		private static final byte SEQUENCE_TYPE = 48;

		private static final String PASSWORD_MODIFY_OID = "1.3.6.1.4.1.4203.1.11.1";
		private static final byte USER_IDENTITY_OCTET_TYPE = -128;
		private static final byte OLD_PASSWORD_OCTET_TYPE = -127;
		private static final byte NEW_PASSWORD_OCTET_TYPE = -126;

		private final ByteArrayOutputStream value = new ByteArrayOutputStream();

		public PasswordModifyRequest(String userIdentity, String oldPassword,
				String newPassword) {
			ByteArrayOutputStream elements = new ByteArrayOutputStream();

			if (userIdentity != null) {
				berEncode(USER_IDENTITY_OCTET_TYPE, userIdentity.getBytes(), elements);
			}

			if (oldPassword != null) {
				berEncode(OLD_PASSWORD_OCTET_TYPE, oldPassword.getBytes(), elements);
			}

			if (newPassword != null) {
				berEncode(NEW_PASSWORD_OCTET_TYPE, newPassword.getBytes(), elements);
			}

			berEncode(SEQUENCE_TYPE, elements.toByteArray(), this.value);
		}

		@Override
		public String getID() {
			return PASSWORD_MODIFY_OID;
		}

		@Override
		public byte[] getEncodedValue() {
			return this.value.toByteArray();
		}

		@Override
		public ExtendedResponse createExtendedResponse(String id, byte[] berValue,
				int offset, int length) {
			return null;
		}

		/**
		 * Only minimal support for <a target="_blank" href=
		 * "https://www.itu.int/ITU-T/studygroups/com17/languages/X.690-0207.pdf"> BER
		 * encoding </a>; just what is necessary for the Password Modify request.
		 *
		 */
		private void berEncode(byte type, byte[] src, ByteArrayOutputStream dest) {
			int length = src.length;

			dest.write(type);

			if (length < 128) {
				dest.write(length);
			}
			else if ((length & 0x0000_00FF) == length) {
				dest.write((byte) 0x81);
				dest.write((byte) (length & 0xFF));
			}
			else if ((length & 0x0000_FFFF) == length) {
				dest.write((byte) 0x82);
				dest.write((byte) ((length >> 8) & 0xFF));
				dest.write((byte) (length & 0xFF));
			}
			else if ((length & 0x00FF_FFFF) == length) {
				dest.write((byte) 0x83);
				dest.write((byte) ((length >> 16) & 0xFF));
				dest.write((byte) ((length >> 8) & 0xFF));
				dest.write((byte) (length & 0xFF));
			}
			else {
				dest.write((byte) 0x84);
				dest.write((byte) ((length >> 24) & 0xFF));
				dest.write((byte) ((length >> 16) & 0xFF));
				dest.write((byte) ((length >> 8) & 0xFF));
				dest.write((byte) (length & 0xFF));
			}

			try {
				dest.write(src);
			}
			catch (IOException e) {
				throw new IllegalArgumentException(
						"Failed to BER encode provided value of type: " + type);
			}
		}
	}
}
