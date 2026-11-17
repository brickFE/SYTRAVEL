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
import com.sy.travel.common.PasswordSupport;
import com.sy.travel.dao.SYUserRepository;
import com.sy.travel.dto.login.LoginCheckRequest;
import com.sy.travel.entity.User;

@RunWith(MockitoJUnitRunner.class)
public class SYLoginServiceTest {

	@Mock
	private SYUserRepository syUserRepository;

	@InjectMocks
	private SYLoginService syLoginService;

	@Test
	public void shouldLoginWithLegacyPasswordAndUpgradeToBcrypt() {
		User user = new User("admin", PasswordSupport.legacyEncode("admin", "123456"), "", "0");
		when(syUserRepository.findByUsername("admin")).thenReturn(user);

		LoginCheckRequest request = new LoginCheckRequest();
		request.setUsername("admin");
		request.setPassword("123456");
		AjaxResult<String> result = syLoginService.loginCheck(request);

		assertEquals("success", result.getMsg());
		verify(syUserRepository).save(user);
	}

	@Test
	public void shouldLoginWithBcryptWithoutReSaving() {
		User user = new User("admin", PasswordSupport.hash("123456"), "", "0");
		when(syUserRepository.findByUsername("admin")).thenReturn(user);

		LoginCheckRequest request = new LoginCheckRequest();
		request.setUsername("admin");
		request.setPassword("123456");
		AjaxResult<String> result = syLoginService.loginCheck(request);

		assertEquals("success", result.getMsg());
		verify(syUserRepository, never()).save(user);
	}
}
