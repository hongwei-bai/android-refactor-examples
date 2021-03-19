package com.hongwei.android_refactor_examples.example.hugeclass;

public class DownloadResourceService {
    public void downloadResource(ModuleData moduleData, String exeType) {
        if (null == moduleData) {
            Log.e(TAG, "moduleData = " + moduleData);
            return;
        }

        Log.i(TAG, "exeType = " + exeType);

        if (checkNoUpdate(MainPresenter.getInstance().getConfigManager().getConfigPersistence().getModuleData(), moduleData)) {
            return;
        }

        List<ResourceInfo> list;
        if (Config.MESSAGE_EXETYPE_MODULE_ALL_RES.equals(exeType)) {
            Platform.getInstance().getConfigManager().getConfigPersistence().setModuleData(moduleData);
            list = MainPresenter.getInstance().getConfigManager().getStandbyResourceList();
        } else {
            final long businessId = MainPresenter.getInstance().getConfigManager().getBusinessId();
            final long subjectGroupId = MainPresenter.getInstance().getConfigManager().getSubjectGroupId();
            moduleData.setBusinessSceneId(businessId);
            moduleData.setSubjectGroupId(subjectGroupId);
            Platform.getInstance().getConfigManager().getConfigPersistence().updateModuleData(moduleData);
            list = MainPresenter.getInstance().getConfigManager().getStandbyResourceList();
        }

        final long businessId;
        final long subjectGroupId;
        if (Config.MESSAGE_EXETYPE_MODULE_ALL_RES.equals(exeType)) {
            businessId = moduleData.getBusinessSceneId();
            subjectGroupId = moduleData.getSubjectGroupId();
        } else {
            businessId = MainPresenter.getInstance().getConfigManager().getBusinessId();
            subjectGroupId = MainPresenter.getInstance().getConfigManager().getSubjectGroupId();
        }

        if (null == list || list.isEmpty()) {
            Platform.getInstance().getConfigManager().getConfigPersistence().activeStandbyModuleData(businessId, subjectGroupId);
            CheckDownloadFinish.listener.onAllResourceFinish();
            return;
        }

        List<DownloadInfo> downloadInfos = DownloadPathHelper.batchConvertResourceSavePath(businessId, subjectGroupId, list);

        mTipInterface.showDialog();

        MainPresenter.getInstance().getDynamicResManager().fetchFiles(downloadInfos, new DynamicResListener() {

            @Override
            public void onProgress(String fileName, int soFarFilesCount, int totalFilesCount, long soFarBytes, long totalBytes) {
                mTipInterface.updateProgress(soFarFilesCount, totalFilesCount);
            }

            @Override
            public void onSuccess() {
                mTipInterface.downloadSuccess(true, false);
                Platform.getInstance().getConfigManager().getConfigPersistence().activeStandbyModuleData(businessId, subjectGroupId);

                List<PageId> pageIdList = MainPresenter.getInstance().getConfigManager().getConfigPersistence().getUpdatedPageList();
                if (null != pageIdList && 0 < pageIdList.size()) {
                    CheckDownloadFinish.listener.onIncrementResourceFinish(pageIdList);
                } else {
                    CheckDownloadFinish.listener.onAllResourceFinish();
                }
            }

            @Override
            public void onFailed(String url, String fileName, String cause) {
                mTipInterface.downloadSuccess(false, false);
                Platform.getInstance().getConfigManager().getConfigPersistence().reverseStandbyModuleData();
            }
        });
    }
}

