package org.example.application;


import org.example.db.DB;
import org.example.model.dao.DaoFactory;
import org.example.model.dao.DepartmentDao;
import org.example.model.dao.SellerDao;
import org.example.model.dao.impl.DepartmentDaoJDBC;
import org.example.model.dao.impl.SellerDaoJDBC;
import org.example.model.entities.Department;
import org.example.model.entities.Seller;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            try(Connection connection = DB.getConnection();) {
                DepartmentDaoJDBC departmentDao = new DepartmentDaoJDBC(connection);
                SellerDaoJDBC sellerDao = new SellerDaoJDBC(connection);


                int i = 1;
                while (i != 0) {
                    System.out.println("Boa tarde, selecione uma das opções abaixo: \n " +
                            "Cadastrar um vendedor - 1\n" +
                            "Atualizar um vendedor - 2\n" +
                            "Excluir um vendedor - 3\n" +
                            "Procurar um vendedor - 4\n" +
                            "Lista de todos os vendedores - 5\n" +
                            "Cadastrar um departamento - 6\n" +
                            "Atualizar um departamento - 7\n" +
                            "Excluir um departamento - 8\n" +
                            "Procurar um departamento - 9\n" +
                            "Listar de todos os departamentos - 10\n" +
                            "Sair - 0");
                    i = sc.nextInt();
                    sc.nextLine();
                    switch (i) {

                        case 1:
                            InsertSeller(departmentDao, sellerDao);
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:
                            break;
                        case 8:
                            break;
                        case 9:
                            break;
                        case 10:
                            break;
                        case 0:
                            System.out.println("Programa finalizado!");
                            i = 0;
                        default:
                            System.out.println("Opção escolhida inexistente");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
        } catch (ParseException e) {
                throw new RuntimeException(e);
            }
    }
    public static void InsertSeller(DepartmentDaoJDBC departmentDao, SellerDaoJDBC sellerDao) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Scanner sc = new Scanner(System.in);
        System.out.println("Me informe o id do departamento: (caso não possua volte e crie um departamento)");
        int depId = sc.nextInt();
        sc.nextLine();

        Department dep = new Department(depId, null);

        if(dep == null) {
            System.out.println("Departamento não encontrado!");
            return;
        }

        System.out.println("Digite o nome do vendedor: ");
        String sellerName = sc.nextLine();
        System.out.println("Digite o email do vendedor: ");
        String sellerEmail = sc.nextLine();
        System.out.println("Digite a data de nascimento do vendedor: ");
        Date nascimento = sdf.parse(sc.next());
        System.out.println("Digite o salário do vendedor: ");
        double baseSalary = sc.nextDouble();

        Seller seller = new Seller(null, sellerName, sellerEmail, nascimento, baseSalary, dep);
        sellerDao.insert(seller);
        System.out.println("Vendedor inserido com sucesso!");
    }
}