package com.hongwei.android_refactor_examples.example.exceptional;

public class AdResourceManager {
    private String getAdResourceVersion(String key) {
        if (null != key) {
            DaoSession daoSession = ((App) Platform.getInstance().getApplicationContext()).getDaoSession();
            ADResourceDao adResourceDao = daoSession.getADResourceDao();
            if (null != adResourceDao && 0 < adResourceDao.count()) {
                List<ADResource> adResourceList = adResourceDao.queryBuilder().where(ADResourceDao.Properties.AdKey.eq(key)).build().list();
                if (null != adResourceList && 0 < adResourceList.size()) {
                    String version = null;
                    for (ADResource resource1 : adResourceList) {
                        String ver = resource1.getMaterialBean().getVersion();
                        if (null == version && null != ver) {
                            version = ver;
                        } else if (null != version && null != ver && 0 > version.compareTo(ver)) {
                            version = ver;
                        }
                    }
                    return version;
                }
            }
            return null;
        }
        return null;
    }

    private String getAdResourceVersionB(String key, DaoSession daoSession) {
        if (key == null) {
            return;
        }

        DaoSession daoSession = ((App) Platform.getInstance().getApplicationContext()).getDaoSession();

        ADResourceDao adResourceDao = daoSession.getADResourceDao();
        if (adResourceDao == null || adResourceDao.count() == 0) {
            return;
        }

        List<ADResource> adResourceList = adResourceDao.queryBuilder().where(ADResourceDao.Properties.AdKey.eq(key)).build().list();
        if (adResourceList == null || adResourceList.size() == 0) {
            return;
        }

        String version = null;
        for (ADResource resource1 : adResourceList) {
            String ver = resource1.getMaterialBean().getVersion();
            if (null == version && null != ver) {
                version = ver;
            } else if (null != version && null != ver && 0 > version.compareTo(ver)) {
                version = ver;
            }
        }
        return version;
    }
}
