package com.sy.travel.service.support;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.sy.travel.common.Commons;
import com.sy.travel.dao.SYUserRepository;
import com.sy.travel.entity.User;

@RunWith(MockitoJUnitRunner.class)
public class PermissionGuardTest {

	@Mock
	private SYUserRepository syUserRepository;

	@InjectMocks
	private PermissionGuard permissionGuard;

	@Test
	public void shouldReturnEmptyWhenAdmin() {
		User user = new User("admin", "pwd", "", "0");
		when(syUserRepository.findByUsername("admin")).thenReturn(user);
		assertEquals("", permissionGuard.requireAdmin("admin"));
	}

	@Test
	public void shouldReturnNoPermissionWhenNotAdmin() {
		User user = new User("pm", "pwd", "", "3");
		when(syUserRepository.findByUsername("pm")).thenReturn(user);
		assertEquals(Commons.USER_OPERATOR_NOT_PERMISSION, permissionGuard.requireAdmin("pm"));
	}
}
