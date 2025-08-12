package org.example.application;


import org.example.model.entities.Department;
import org.example.model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        Department department = new Department(1, "TI");
        Seller seller = new Seller(1, "Lucas", "luathie@gmail.com", new Date(), 3000.00, department);

        System.out.println(seller);

    }
}