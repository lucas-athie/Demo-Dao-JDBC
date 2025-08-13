package org.example.model.dao;

import org.example.model.dao.impl.SellerDaoJDBC;
import org.example.model.entities.Seller;

public class DaoFactory {
    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC();
    }
}
