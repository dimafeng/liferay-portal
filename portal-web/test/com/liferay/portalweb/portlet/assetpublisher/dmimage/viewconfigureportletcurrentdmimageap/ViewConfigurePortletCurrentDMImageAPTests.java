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

package com.liferay.portalweb.portlet.assetpublisher.dmimage.viewconfigureportletcurrentdmimageap;

import com.liferay.portalweb.portal.BaseTestSuite;
import com.liferay.portalweb.portal.util.TearDownPageTest;
import com.liferay.portalweb.portlet.assetpublisher.dmimage.addnewdmfolderimageapactions.AddDMFolderTest;
import com.liferay.portalweb.portlet.assetpublisher.dmimage.addnewdmfolderimageapactions.AddNewDMFolderImageAPActionsTest;
import com.liferay.portalweb.portlet.assetpublisher.dmimage.viewconfigureportletavailabledmimageap.ViewConfigurePortletAvailableDMImageAPTest;
import com.liferay.portalweb.portlet.assetpublisher.portlet.addportletap.AddPageAPTest;
import com.liferay.portalweb.portlet.assetpublisher.portlet.addportletap.AddPortletAPTest;
import com.liferay.portalweb.portlet.assetpublisher.portlet.configureportletavailabledmdocument.ConfigurePortletAvailableDMDocumentTest;
import com.liferay.portalweb.portlet.assetpublisher.portlet.configureportletcurrentdmdocument.ConfigurePortletCurrentDMDocumentTest;
import com.liferay.portalweb.portlet.documentsandmedia.dmdocument.adddmdocument.TearDownDMDocumentTest;
import com.liferay.portalweb.portlet.documentsandmedia.portlet.addportletdm.AddPageDMTest;
import com.liferay.portalweb.portlet.documentsandmedia.portlet.addportletdm.AddPortletDMTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class ViewConfigurePortletCurrentDMImageAPTests extends BaseTestSuite {
	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTestSuite(AddPageAPTest.class);
		testSuite.addTestSuite(AddPortletAPTest.class);
		testSuite.addTestSuite(AddPageDMTest.class);
		testSuite.addTestSuite(AddPortletDMTest.class);
		testSuite.addTestSuite(AddDMFolderTest.class);
		testSuite.addTestSuite(AddNewDMFolderImageAPActionsTest.class);
		testSuite.addTestSuite(ConfigurePortletAvailableDMDocumentTest.class);
		testSuite.addTestSuite(ViewConfigurePortletAvailableDMImageAPTest.class);
		testSuite.addTestSuite(ConfigurePortletCurrentDMDocumentTest.class);
		testSuite.addTestSuite(ViewConfigurePortletCurrentDMImageAPTest.class);
		testSuite.addTestSuite(TearDownDMDocumentTest.class);
		testSuite.addTestSuite(TearDownPageTest.class);

		return testSuite;
	}
}