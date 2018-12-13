/**
 * 
 */
package com.innova4j.api.auth.granters.password;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import com.google.common.collect.ImmutableMap;
import com.innova4j.api.auth.AuthConstants;
import com.innova4j.api.auth.domain.AuthPasswordToken;
import com.innova4j.api.auth.domain.AuthUser;
import com.innova4j.api.auth.domain.CustomUserDetails;
import com.innova4j.api.auth.dto.AuthUserDto;
import com.innova4j.api.auth.services.encoder.HashEncoder;
import com.innova4j.api.auth.services.token.AuthPasswordTokenService;
import com.innova4j.api.auth.services.user.AuthUserService;

/**
 * @author innova4j-team
 *
 */
public class PasswordGranter extends AbstractTokenGranter {

	private static final String AUTH_PASSWORD_GRANT_TYPE = "auth_password";
	private static final String PASSWORD_TOKEN_PARAM = "password_token";

	private AuthPasswordTokenService passwordTokenService;
	private AuthUserService userService;
	private HashEncoder encoder;

	public PasswordGranter(@NotNull AuthorizationServerTokenServices tokenServices,
			@NotNull ClientDetailsService clientDetailsService, @NotNull OAuth2RequestFactory requestFactory) {
		super(tokenServices, clientDetailsService, requestFactory, AUTH_PASSWORD_GRANT_TYPE);
	}

	/**
	 * @param passwordService the passwordService to set
	 */
	public void setPasswordTokenService(@NotNull AuthPasswordTokenService passwordTokenService) {
		this.passwordTokenService = passwordTokenService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(@NotNull AuthUserService userService) {
		this.userService = userService;
	}

	/**
	 * @param encoder the encoder to set
	 */
	public void setEncoder(@NotNull HashEncoder encoder) {
		this.encoder = encoder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.oauth2.provider.token.AbstractTokenGranter#
	 * getOAuth2Authentication(org.springframework.security.oauth2.provider.
	 * ClientDetails, org.springframework.security.oauth2.provider.TokenRequest)
	 */
	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		Map<String, String> params = tokenRequest.getRequestParameters();

		String token = params.containsKey(PASSWORD_TOKEN_PARAM) ? params.get(PASSWORD_TOKEN_PARAM) : StringUtils.EMPTY;
		AuthPasswordToken passwordToken = passwordTokenService.customGet(
				ImmutableMap.<String, Object>builder().put(AuthConstants.TOKEN, encoder.encode(token)).build());
		if (passwordToken.isExpired()) {
			throw new UnauthorizedUserException("Authorization code has expired.");
		}

		AuthUserDto user = userService.customGet(ImmutableMap.<String, Object>builder()
				.put(AuthUser.NICKNAME, passwordToken.getId().getNickname()).build());
		CustomUserDetails userDetails = new CustomUserDetails(user);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
				userDetails.getPassword(), userDetails.getAuthorities());
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(tokenRequest.createOAuth2Request(client),
				authentication);
		return oAuth2Authentication;
	}

}
