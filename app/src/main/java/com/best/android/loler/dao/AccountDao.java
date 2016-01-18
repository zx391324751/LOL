package com.best.android.loler.dao;

import android.util.Log;

import com.best.android.loler.LOLApplication;
import com.best.android.loler.model.Account;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by BL06249 on 2015/12/17.
 */
public class AccountDao {

    private Dao<Account, Long> mDao;

    public AccountDao() {
        try {
            mDao = LOLApplication.getInstance().getDatabaseHelper().getDao(Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Account, Long> getDao() {
        return mDao;
    }

    /**
     * 增加一个历史记录
     * @param record
     */
    public void add(Account record) {
        try {
            mDao.create(record);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有的历史记录
     * @return
     */
    public List<Account> queryAllRecord() {
        try {
            return mDao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除某条记录
     * @param account
     */
    public void deleteAccount(Account account){
        try {
            mDao.delete(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新数据
     * @param account
     */
    public void updateAccount(Account account){
        try {
            mDao.update(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getSelectedAccount(){
        try {
            List<Account> list = mDao.queryBuilder().where().eq("isSelect", true).query();
            if(list != null && list.size() > 0)
                return list.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
