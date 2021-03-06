/**
 * 
 */
package com.co.app.auth.services.token.impl;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.co.app.commons.exception.RegisterNotFoundException;
import com.co.app.auth.dao.AuthPasswordTokenRepository;
import com.co.app.auth.domain.AuthPasswordToken;
import com.co.app.auth.domain.AuthPasswordTokenId;
import com.co.app.auth.services.token.AuthPasswordTokenService;

/**
 * @author alobaton
 *
 */
@Service
public class AuthPasswordTokenServiceImpl implements AuthPasswordTokenService {

	@Autowired
	private AuthPasswordTokenRepository repository;

	@Override
	public AuthPasswordToken create(AuthPasswordToken dto) {
		return repository.save(dto);
	}

	@Override
	public AuthPasswordToken get(AuthPasswordTokenId id) {
		return repository.getOne(id);
	}

	@Override
	public AuthPasswordToken customGet(@NotNull AuthPasswordToken dto) {
		Example<AuthPasswordToken> example = Example.of(dto);

		return repository.findOne(example).orElseThrow(
				() -> new RegisterNotFoundException(AuthPasswordToken.class, Strings.EMPTY, dto.toString()));
	}

	@Override
	public List<AuthPasswordToken> getAll() {
		return repository.findAll();
	}

	@Override
	public List<AuthPasswordToken> getAll(AuthPasswordToken dto) {
		Example<AuthPasswordToken> example = Example.of(dto);

		return repository.findAll(example);
	}

	@Override
	public AuthPasswordToken update(AuthPasswordToken dto) {
		return repository.save(dto);
	}

	@Override
	public AuthPasswordToken customUpdate(Map<String, Object> dto) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AuthPasswordToken delete(AuthPasswordTokenId id) {
		AuthPasswordToken domain = get(id);

		repository.deleteById(id);

		return domain;
	}

	@Override
	public boolean exists(AuthPasswordTokenId id) {
		return repository.existsById(id);
	}

}
