/**
 * 
 */
package com.innova4j.api.auth.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innova4j.api.auth.dto.AuthClientDetailsDto;
import com.innova4j.api.auth.services.client.AuthClientDetailsService;
import com.innova4j.api.commons.controllers.BaseController;

/**
 * @author innova4j-team
 *
 */
@RestController
@RequestMapping("/clients")
public class AuthClientController implements BaseController<AuthClientDetailsDto> {

	@Autowired
	private AuthClientDetailsService service;

	@Override
	public AuthClientDetailsDto create(@Valid AuthClientDetailsDto dto) {
		return service.create(dto);
	}

	@Override
	public AuthClientDetailsDto get(String id) {
		return service.get(id);
	}

	@Override
	public List<AuthClientDetailsDto> getAll(@Valid AuthClientDetailsDto dto) {
		return service.getAll(dto);
	}

	@Override
	public AuthClientDetailsDto update(String id, @Valid AuthClientDetailsDto dto) {
		dto.setClientId(id);
		return service.update(dto);
	}

	@Override
	public void delete(String id) {
		service.delete(id);
	}
}