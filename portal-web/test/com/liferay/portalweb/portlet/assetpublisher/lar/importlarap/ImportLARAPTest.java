/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portalweb.portlet.assetpublisher.lar.importlarap;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class ImportLARAPTest extends BaseTestCase {
	public void testImportLARAP() throws Exception {
		selenium.selectWindow("null");
		selenium.selectFrame("relative=top");
		selenium.open("/web/guest/home/");
		selenium.clickAt("link=Asset Publisher Test Page",
			RuntimeVariables.replace("Asset Publisher Test Page"));
		selenium.waitForPageToLoad("30000");
		selenium.waitForElementPresent(
			"//nav[@class='site-breadcrumbs aui-helper-hidden lfr-hudcrumbs']");
		selenium.waitForVisible("//span[@title='Options']/ul/li/strong/a");
		assertEquals(RuntimeVariables.replace("Options"),
			selenium.getText("//span[@title='Options']/ul/li/strong/a"));
		selenium.clickAt("//span[@title='Options']/ul/li/strong/a",
			RuntimeVariables.replace("Options"));
		selenium.waitForVisible(
			"//div[@class='lfr-component lfr-menu-list']/ul/li[3]/a");
		assertEquals(RuntimeVariables.replace("Export / Import"),
			selenium.getText(
				"//div[@class='lfr-component lfr-menu-list']/ul/li[3]/a"));
		selenium.clickAt("//div[@class='lfr-component lfr-menu-list']/ul/li[3]/a",
			RuntimeVariables.replace("Export / Import"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Import"),
			selenium.getText("link=Import"));
		selenium.clickAt("link=Import", RuntimeVariables.replace("Import"));
		selenium.waitForPageToLoad("30000");
		selenium.uploadFile("//input[@id='_86_importFileName']",
			RuntimeVariables.replace(
				"L:\\portal\\build\\portal-web\\test\\com\\liferay\\portalweb\\portlet\\assetpublisher\\dependencies\\Asset_Publisher-Selenium.portlet.lar"));
		selenium.clickAt("//input[@value='Import']",
			RuntimeVariables.replace("Import"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace(
				"Your request completed successfully."),
			selenium.getText("//div[@class='portlet-msg-success']"));
	}
}