package com.sy.travel.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.sy.travel.common.AjaxResult;
import com.sy.travel.dao.SYUserRepository;
import com.sy.travel.dto.user.UserCreateRequest;
import com.sy.travel.dto.user.UserUpdateRequest;
import com.sy.travel.entity.User;

@RunWith(MockitoJUnitRunner.class)
public class SYUserServiceTest {

	@Mock
	private SYUserRepository syUserRepository;

	@Mock
	private SYLoggerService syLoggerService;

	@InjectMocks
	private SYUserService syUserService;

	@Test
	public void addShouldFailWhenUsernameBlank() {
		UserCreateRequest request = new UserCreateRequest();
		request.setOperator("admin");
		request.setUsername(" ");
		request.setPassword("123456");
		request.setPermission("0");

		AjaxResult<String> result = syUserService.add(request);

		assertEquals("failed", result.getMsg());
		verify(syUserRepository, never()).save(org.mockito.ArgumentMatchers.any(User.class));
	}

	@Test
	public void updateShouldFailWhenUserNotFound() {
		UserUpdateRequest request = new UserUpdateRequest();
		request.setOperator("admin");
		request.setId(99);
		when(syUserRepository.findOne(99)).thenReturn(null);

		AjaxResult<String> result = syUserService.update(request);

		assertEquals("failed", result.getMsg());
	}
}
