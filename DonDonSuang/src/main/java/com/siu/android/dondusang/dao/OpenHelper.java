package com.siu.android.dondusang.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.siu.android.dondusang.dao.model.DaoMaster;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class OpenHelper extends DaoMaster.OpenHelper {

    public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
