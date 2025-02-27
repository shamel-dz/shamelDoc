package com.logicaldoc.web.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.logicaldoc.gui.common.client.ServerException;
import com.logicaldoc.gui.common.client.beans.GUIEmailSettings;
import com.logicaldoc.gui.common.client.beans.GUIParameter;
import com.logicaldoc.web.AbstractWebappTestCase;

public class SettingServiceImplTest extends AbstractWebappTestCase {

	private static Logger log = LoggerFactory.getLogger(SettingServiceImplTest.class);

	// Instance under test
	private SettingServiceImpl service = new SettingServiceImpl();

	@Before
	public void setUp() throws FileNotFoundException, IOException, SQLException {
		super.setUp();
	}

	@Test
	public void testSaveEmailSettings() throws ServerException {
		GUIEmailSettings emailSettings = new GUIEmailSettings();
		emailSettings.setSmtpServer("smtp.logicalobjects.it");
		emailSettings.setPort(8080);
		emailSettings.setUsername("admin");
		emailSettings.setPwd("pippo");
		emailSettings.setConnSecurity(GUIEmailSettings.SECURITY_TLS);
		emailSettings.setSecureAuth(true);
		emailSettings.setSenderEmail("mario@acme.com");

		String notThrownTest = null;
		try {
			service.saveEmailSettings(emailSettings);
			notThrownTest = "ok";
		} catch (Exception t) {
			t.printStackTrace();
		}
		Assert.assertNotNull(notThrownTest);
	}

	@Test
	public void testSaveSettings() throws ServerException {
		List<GUIParameter> params = new ArrayList<>();
		for (int i = 0; i < 50; i++)
			params.add(new GUIParameter("param" + i + "_name", "Value " + i));

		String notThrownTest = null;
		try {
			service.saveSettings(params);
			notThrownTest = "ok";
		} catch (Exception t) {
			log.error(t.getMessage(), t);
		}
		Assert.assertNotNull(notThrownTest);
	}

	@Test
	public void testSaveWSSettings() throws ServerException {
		GUIParameter wsSettings = new GUIParameter();
		wsSettings.setName("webservice.enabled");
		wsSettings.setValue("true");

		GUIParameter wdSettings = new GUIParameter();
		wsSettings.setName("webdav.enabled");
		wsSettings.setValue("true");

		String notThrownTest = null;
		try {
			service.saveSettings(List.of(wsSettings, wdSettings));
			notThrownTest = "ok";
		} catch (Exception t) {
			log.error(t.getMessage(), t);
		}
		Assert.assertNotNull(notThrownTest);
	}
}