package com.siu.android.dondusang.dao;

import com.siu.android.dondusang.Application;
import com.siu.android.dondusang.dao.model.DaoMaster;
import com.siu.android.dondusang.dao.model.DaoSession;

/**
 * User: Jonathan TRIBOUHARET
 * Date: 13/04/12
 * Time: 18:40
 */
public class DatabaseHelper {

    public static final String DB_NAME = "dondusang.db";

    private static DatabaseHelper instance;

    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DatabaseHelper(){
    }

    public static DatabaseHelper getInstance() {
        if (null == instance) {
            instance = new DatabaseHelper();
        }

        return instance;
    }

    public DaoMaster getDaoMaster() {
        if (null == daoMaster) {
            daoMaster = new DaoMaster(new OpenHelper(Application.getContext(), DB_NAME, null).getWritableDatabase());
        }

        return daoMaster;
    }

    public DaoSession getDaoSession() {
        if (null == daoSession) {
            daoSession = getDaoMaster().newSession();
        }

        return daoSession;
    }

}
