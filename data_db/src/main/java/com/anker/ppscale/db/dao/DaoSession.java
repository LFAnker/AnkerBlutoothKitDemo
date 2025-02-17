package com.anker.ppscale.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.anker.ppscale.db.dao.DeviceModel;

import com.anker.ppscale.db.dao.DeviceModelDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig deviceModelDaoConfig;

    private final DeviceModelDao deviceModelDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        deviceModelDaoConfig = daoConfigMap.get(DeviceModelDao.class).clone();
        deviceModelDaoConfig.initIdentityScope(type);

        deviceModelDao = new DeviceModelDao(deviceModelDaoConfig, this);

        registerDao(DeviceModel.class, deviceModelDao);
    }
    
    public void clear() {
        deviceModelDaoConfig.clearIdentityScope();
    }

    public DeviceModelDao getDeviceModelDao() {
        return deviceModelDao;
    }

}
