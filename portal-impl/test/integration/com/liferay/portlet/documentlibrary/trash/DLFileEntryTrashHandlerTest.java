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

package com.liferay.portlet.documentlibrary.trash;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashHandlerRegistryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.WorkflowedModel;
import com.liferay.portal.repository.liferayrepository.model.LiferayFileEntry;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileRank;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileRankLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.trash.BaseTrashHandlerTestCase;
import com.liferay.portlet.trash.service.TrashEntryServiceUtil;

import java.io.File;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alexander Chow
 * @author Julio Camarero
 * @author Eudaldo Alonso
 */
@ExecutionTestListeners(listeners = {EnvironmentExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class DLFileEntryTrashHandlerTest extends BaseTrashHandlerTestCase {

	@Test
	@Transactional
	public void testTrashFileRank() throws Exception {
		trashFileRank();
	}

	@Test
	@Transactional
	public void testTrashFileVersionAndDelete() throws Exception {
		trashDLFileEntry(true);
	}

	@Test
	@Transactional
	public void testTrashFileVersionAndRestore() throws Exception {
		trashDLFileEntry(false);
	}

	@Override
	protected BaseModel<?> addBaseModel(
			BaseModel<?> parentBaseModel, boolean approved,
			ServiceContext serviceContext)
		throws Exception {

		serviceContext = (ServiceContext)serviceContext.clone();

		serviceContext.setWorkflowAction(WorkflowConstants.ACTION_SAVE_DRAFT);

		if (approved) {
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
		}

		String content = "Content: Enterprise. Open Source.";

		File file = FileUtil.createTempFile(content.getBytes());

		DLFolder dlFolder = (DLFolder)parentBaseModel;

		FileEntry fileEntry = DLAppServiceUtil.addFileEntry(
			dlFolder.getRepositoryId(), dlFolder.getFolderId(),
			ServiceTestUtil.randomString() + ".txt", ContentTypes.TEXT_PLAIN,
			getSearchKeywords(), StringPool.BLANK, StringPool.BLANK, file,
			serviceContext);

		LiferayFileEntry liferayFileEntry = (LiferayFileEntry)fileEntry;

		return liferayFileEntry.getDLFileEntry();
	}

	protected int getActiveDLFileRanksCount(long groupId, long fileEntryId)
		throws Exception {

		List<DLFileRank> dlFileRanks = DLFileRankLocalServiceUtil.getFileRanks(
			groupId, TestPropsValues.getUserId());

		int count = 0;

		for (DLFileRank dlFileRank : dlFileRanks) {
			if (dlFileRank.getFileEntryId() == fileEntryId) {
				count++;
			}
		}

		return count;
	}

	@Override
	protected BaseModel<?> getBaseModel(long primaryKey) throws Exception {
		return DLFileEntryLocalServiceUtil.getDLFileEntry(primaryKey);
	}

	@Override
	protected Class<?> getBaseModelClass() {
		return DLFileEntry.class;
	}

	@Override
	protected String getBaseModelName(ClassedModel classedModel) {
		DLFileEntry dlFileEntry = (DLFileEntry)classedModel;

		return dlFileEntry.getTitle();
	}

	@Override
	protected int getBaseModelsNotInTrashCount(BaseModel<?> parentBaseModel)
		throws Exception {

		DLFolder dlFolder = (DLFolder)parentBaseModel;

		return DLFileEntryServiceUtil.getFileEntriesCount(
			dlFolder.getGroupId(), dlFolder.getFolderId(),
			WorkflowConstants.STATUS_ANY);
	}

	@Override
	protected BaseModel<?> getParentBaseModel(
			Group group, ServiceContext serviceContext)
		throws Exception {

		return DLFolderLocalServiceUtil.addFolder(
			TestPropsValues.getUserId(), group.getGroupId(), group.getGroupId(),
			false, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			ServiceTestUtil.randomString(), StringPool.BLANK, serviceContext);
	}

	protected Class<?> getParentBaseModelClass() {
		return DLFolder.class;
	}

	@Override
	protected String getSearchKeywords() {
		return "Title";
	}

	@Override
	protected WorkflowedModel getWorkflowedModel(ClassedModel baseModel)
		throws Exception {

		DLFileEntry dlFileEntry = (DLFileEntry)baseModel;

		return dlFileEntry.getFileVersion();
	}

	@Override
	protected boolean isInTrashFolder(ClassedModel classedModel)
		throws Exception {

		DLFileEntry dlFileEntry = (DLFileEntry)classedModel;

		DLFileVersion dlFileVersion = dlFileEntry.getFileVersion();

		return dlFileVersion.isInTrashFolder();
	}

	@Override
	protected BaseModel<?> moveBaseModelFromTrash(
			ClassedModel classedModel, Group group,
			ServiceContext serviceContext)
		throws Exception {

		BaseModel<?> parentBaseModel = getParentBaseModel(
			group, serviceContext);

		DLAppServiceUtil.moveFileEntryFromTrash(
			(Long)classedModel.getPrimaryKeyObj(),
			(Long)parentBaseModel.getPrimaryKeyObj(), serviceContext);

		return parentBaseModel;
	}

	@Override
	protected void moveBaseModelToTrash(long primaryKey) throws Exception {
		DLAppServiceUtil.moveFileEntryToTrash(primaryKey);
	}

	@Override
	protected void moveParentBaseModelToTrash(long primaryKey)
			throws Exception {

		DLAppServiceUtil.moveFolderToTrash(primaryKey);
	}

	protected void trashDLFileEntry(boolean delete) throws Exception {
		Group group = ServiceTestUtil.addGroup();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		serviceContext.setScopeGroupId(group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			group, serviceContext);

		int initialBaseModelsCount = getBaseModelsNotInTrashCount(
				parentBaseModel);
		int initialBaseModelsSearchCount = searchBaseModelsCount(
			getBaseModelClass(), group.getGroupId());
		int initialTrashEntriesCount = getTrashEntriesCount(group.getGroupId());
		int initialTrashEntriesSearchCount = searchTrashEntriesCount(
			getSearchKeywords(), serviceContext);

		BaseModel<?> baseModel = addBaseModel(
			parentBaseModel, true, serviceContext);

		updateBaseModel((Long)baseModel.getPrimaryKeyObj(), serviceContext);

		Assert.assertEquals(
			initialBaseModelsCount + 1,
			getBaseModelsNotInTrashCount(parentBaseModel));
		Assert.assertEquals(
			initialTrashEntriesCount, getTrashEntriesCount(group.getGroupId()));

		Assert.assertTrue(isAssetEntryVisible(baseModel));
		Assert.assertEquals(
			initialBaseModelsSearchCount + 1,
			searchBaseModelsCount(getBaseModelClass(), group.getGroupId()));

		Assert.assertEquals(
			initialTrashEntriesSearchCount,
			searchTrashEntriesCount(getSearchKeywords(), serviceContext));

		moveBaseModelToTrash((Long)baseModel.getPrimaryKeyObj());

		Assert.assertEquals(
			initialBaseModelsCount,
			getBaseModelsNotInTrashCount(parentBaseModel));
		Assert.assertEquals(
			initialTrashEntriesCount + 1,
			getTrashEntriesCount(group.getGroupId()));
		Assert.assertFalse(isAssetEntryVisible(baseModel));
		Assert.assertEquals(
			initialBaseModelsSearchCount,
			searchBaseModelsCount(getBaseModelClass(), group.getGroupId()));
		Assert.assertEquals(
			initialTrashEntriesSearchCount + 1,
			searchTrashEntriesCount(getSearchKeywords(), serviceContext));

		if (delete) {
			TrashEntryServiceUtil.deleteEntries(group.getGroupId());

			Assert.assertEquals(
				initialBaseModelsCount,
				getBaseModelsNotInTrashCount(parentBaseModel));
			Assert.assertNull(fetchAssetEntry(baseModel));
			Assert.assertEquals(
				initialBaseModelsSearchCount,
				searchBaseModelsCount(getBaseModelClass(), group.getGroupId()));

			Assert.assertEquals(0, getTrashEntriesCount(group.getGroupId()));
			Assert.assertEquals(
				0,
				searchTrashEntriesCount(getSearchKeywords(), serviceContext));
		}
		else {
			DLAppServiceUtil.restoreFileEntryFromTrash(
				(Long)baseModel.getPrimaryKeyObj());

			Assert.assertEquals(
				initialBaseModelsCount + 1,
				getBaseModelsNotInTrashCount(parentBaseModel));

			Assert.assertTrue(isAssetEntryVisible(baseModel));
			Assert.assertEquals(
				initialBaseModelsSearchCount + 1,
				searchBaseModelsCount(getBaseModelClass(), group.getGroupId()));

			Assert.assertEquals(
				initialTrashEntriesCount,
				getTrashEntriesCount(group.getGroupId()));
			Assert.assertEquals(
				initialTrashEntriesSearchCount,
				searchTrashEntriesCount(getSearchKeywords(), serviceContext));
		}
	}

	protected void trashFileRank() throws Exception {
		Group group = ServiceTestUtil.addGroup();

		ServiceContext serviceContext = ServiceTestUtil.getServiceContext();

		serviceContext.setScopeGroupId(group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			group, serviceContext);

		BaseModel<?> baseModel = addBaseModel(
			parentBaseModel, true, serviceContext);

		DLAppLocalServiceUtil.addFileRank(
			group.getGroupId(), TestPropsValues.getCompanyId(),
			TestPropsValues.getUserId(), (Long)baseModel.getPrimaryKeyObj(),
			serviceContext);

		Assert.assertEquals(
			1,
			getActiveDLFileRanksCount(
				group.getGroupId(), (Long)baseModel.getPrimaryKeyObj()));

		moveBaseModelToTrash((Long)baseModel.getPrimaryKeyObj());

		Assert.assertEquals(
			0,
			getActiveDLFileRanksCount(
				group.getGroupId(), (Long)baseModel.getPrimaryKeyObj()));

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			getBaseModelClassName());

		trashHandler.restoreTrashEntry(getTrashClassPK(baseModel));

		Assert.assertEquals(
			1,
			getActiveDLFileRanksCount(
				group.getGroupId(), (Long)baseModel.getPrimaryKeyObj()));
	}

	protected BaseModel<?> updateBaseModel(
			long primaryKey, ServiceContext serviceContext)
		throws Exception {

		String content = "Content: Enterprise. Open Source. For Life.";

		FileEntry fileEntry = DLAppServiceUtil.updateFileEntry(
			primaryKey, ServiceTestUtil.randomString() + ".txt",
			ContentTypes.TEXT_PLAIN, getSearchKeywords(), StringPool.BLANK,
			StringPool.BLANK, false, content.getBytes(), serviceContext);

		LiferayFileEntry liferayFileEntry = (LiferayFileEntry)fileEntry;

		return liferayFileEntry.getDLFileEntry();
	}

}