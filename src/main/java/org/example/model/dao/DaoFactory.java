package org.example.model.dao;

import org.example.model.dao.impl.SellerDaoJDBC;
import org.example.db.DB;

public class DaoFactory {
    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }
}
