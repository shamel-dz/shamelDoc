package com.logicaldoc.webservice.soap.endpoint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.logicaldoc.util.Context;
import com.logicaldoc.webservice.AbstractWebserviceTestCase;

@RunWith(MockitoJUnitRunner.class)
public class SoapAuthServiceTest extends AbstractWebserviceTestCase {

	@Mock
	private Message currentMessage;

	@Mock
	private HttpServletRequest httpRequest;

	// Instance under test
	private SoapAuthService soapAuthServiceImpl;

	@Before
	public void setUp() throws FileNotFoundException, IOException, SQLException {
		super.setUp();

		currentMessage = mock(Message.class, Answers.RETURNS_DEEP_STUBS);
		when(currentMessage.get(AbstractHTTPDestination.HTTP_REQUEST)).thenReturn(httpRequest);

		// Make sure that this is a SoapAuthService instance
		soapAuthServiceImpl = new SoapAuthService();
		soapAuthServiceImpl.setCurrentMessage(currentMessage);
	}

	@Test
	public void testLogin() {
		String sid = soapAuthServiceImpl.login("author", "admin");
		assertNotNull(sid);
	}

	@Test
	public void testLogout() {
		String sid = soapAuthServiceImpl.login("author", "admin");
		assertNotNull(sid);
		soapAuthServiceImpl.logout(sid);
	}

	@Test
	public void testValid() {
		String sid = soapAuthServiceImpl.login("author", "admin");
		assertNotNull(sid);
		boolean isValid = soapAuthServiceImpl.valid(sid);
		assertTrue(isValid);

		Context.get().getProperties().setProperty("webservice.enabled", "false");
		isValid = soapAuthServiceImpl.valid(sid);
		assertFalse(isValid);

		Context.get().getProperties().setProperty("webservice.enabled", "true");

		// Now using an invalid sid
		sid = "106a106d-a440-43b9-a3fd-4acb85543a0e";
		isValid = soapAuthServiceImpl.valid(sid);
		assertFalse(isValid);
	}

	@Test
	public void testRenew() throws InterruptedException {
		String sid = soapAuthServiceImpl.login("author", "admin");
		System.err.println(sid);
		assertNotNull(sid);
		waiting();
		soapAuthServiceImpl.renew(sid);
	}

	private void waiting() throws InterruptedException {
		final int secondsToWait = 5;
		CountDownLatch lock = new CountDownLatch(1);
		lock.await(secondsToWait, TimeUnit.SECONDS);
	}
}
