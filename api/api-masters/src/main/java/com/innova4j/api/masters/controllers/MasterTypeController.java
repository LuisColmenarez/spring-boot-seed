/**
 * 
 */
package com.innova4j.api.masters.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innova4j.api.commons.controllers.BasePagedController;
import com.innova4j.api.masters.dto.MasterTypeDto;
import com.innova4j.api.masters.services.MasterTypeService;

/**
 * @author innova4j-team
 *
 */
@RestController
@RequestMapping("/master-types")
public class MasterTypeController implements BasePagedController<MasterTypeDto> {

	@Autowired
	private MasterTypeService service;

	@Override
	public MasterTypeDto create(@Valid @RequestBody MasterTypeDto dto) {
		return service.create(dto);
	}

	@Override
	public MasterTypeDto get(@PathVariable String id) {
		return service.get(id);
	}

	@Override
	public List<MasterTypeDto> getAll(@Valid @RequestBody MasterTypeDto dto) {
		return service.getAll(dto);
	}

	@Override
	public MasterTypeDto update(@PathVariable String id, @Valid @RequestBody MasterTypeDto dto) {
		dto.setId(id);

		return service.update(dto);
	}

	@Override
	public void delete(@PathVariable String id) {
		service.delete(id);
	}

	@Override
	public Page<MasterTypeDto> getAll(@Valid @RequestBody MasterTypeDto dto, @NotNull Pageable pageable) {
		return service.getAll(dto, pageable);
	}

}
