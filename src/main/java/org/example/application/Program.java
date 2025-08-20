package org.example.application;

import org.example.db.DB;
import org.example.model.dao.impl.DepartmentDaoJDBC;
import org.example.model.dao.impl.SellerDaoJDBC;
import org.example.model.entities.Department;
import org.example.model.entities.Seller;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
                            insertSeller(departmentDao, sellerDao);
                            break;
                        case 2:
                            updateSeller(departmentDao, sellerDao);
                            break;
                        case 3:
                            deleteSeller(departmentDao, sellerDao);
                            break;
                        case 4:
                            getByIdSeller(departmentDao, sellerDao);
                            break;
                        case 5:
                            getAllSellers(departmentDao, sellerDao);
                            break;
                        case 6:
                            insertDepartment(departmentDao);
                            break;
                        case 7:
                            updateDepartment(departmentDao);
                            break;
                        case 8:
                            deleteDepartment(departmentDao);
                            break;
                        case 9:
                            getByIdDepartment(departmentDao);
                            break;
                        case 10:
                            getAllDepartments(departmentDao);
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
    public static void insertSeller(DepartmentDaoJDBC departmentDao, SellerDaoJDBC sellerDao) throws ParseException {
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

    public static void updateSeller(DepartmentDaoJDBC departmentDao, SellerDaoJDBC sellerDao) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o id do vendedor: ");
        int sellerId = sc.nextInt();
        sc.nextLine();

        Seller updateSeller = sellerDao.findById(sellerId);
        System.out.println("Vendedor selecionado: \n" + updateSeller);
        System.out.println("Digite o nome do vendedor: ");
        String sellerName = sc.nextLine();
        System.out.println("Digite o email do vendedor: ");
        String sellerEmail = sc.nextLine();
        System.out.println("Digite a data de nascimento do vendedor: ");
        Date sellerNascimento = sdf.parse(sc.next());
        System.out.println("Digite o salário do vendedor: ");
        double sellerSalary = sc.nextDouble();
        System.out.println("Me informe o id do departamento: (caso não possua volte e crie um departamento)");
        int depId = sc.nextInt();
        sc.nextLine();

        Department dep = new Department(depId, null);

        if(dep == null) {
            System.out.println("Departamento não encontrado!");
            return;
        }
        Seller seller = new Seller(null, sellerName, sellerEmail, sellerNascimento, sellerSalary, dep);
        seller.setId(sellerId);
        sellerDao.update(seller);
        System.out.println("Vendedor inserido com sucesso!" + seller);
    }

    public static void deleteSeller(DepartmentDaoJDBC departmentDao, SellerDaoJDBC sellerDao) throws ParseException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite o id do vendedor: ");
        int sellerId = sc.nextInt();
        sc.nextLine();

        sellerDao.deleteById(sellerId);

        System.out.println("Vendedor removido com sucesso!");
    }

    public static void getByIdSeller(DepartmentDaoJDBC departmentDao, SellerDaoJDBC sellerDao) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o id do vendedor: ");
        int sellerId = sc.nextInt();
        sc.nextLine();

        Seller updateSeller = sellerDao.findById(sellerId);
        System.out.println("Vendedor selecionado: \n" + updateSeller);
    }

    public static void getAllSellers(DepartmentDaoJDBC departmentDao, SellerDaoJDBC sellerDao) {
        List<Seller> sellers = sellerDao.findAll();
        System.out.println(sellers);

    }

    public static void insertDepartment(DepartmentDaoJDBC departmentDao) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome do departamento: ");
        String departName = sc.nextLine();

        Department dep = new Department(null, departName);
        departmentDao.insert(dep);
        System.out.println("Departamento inserido com sucesso!");
    }

    public static void updateDepartment(DepartmentDaoJDBC departmentDao) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite o id do departamento: ");
        int departId = sc.nextInt();
        sc.nextLine();

        Department dep = departmentDao.findById(departId);
        System.out.println("Departamento selecionado: \n" + dep);
        System.out.println("Digite o nome do departamento: ");
        String departName = sc.nextLine();

        dep.setName(departName);
        departmentDao.update(dep);
        System.out.println("Departamento atualizado com sucesso!");
    }

    public static void deleteDepartment(DepartmentDaoJDBC departmentDao) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o id do departamento: ");
        int departId = sc.nextInt();
        sc.nextLine();

        departmentDao.deleteById(departId);
        System.out.println("Departamento removido com sucesso!");
    }

    public static void getByIdDepartment(DepartmentDaoJDBC departmentDao) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o id do departamento: ");
        int departId = sc.nextInt();
        sc.nextLine();
        Department dep = departmentDao.findById(departId);
        System.out.println("Departamento selecionado: \n" + dep);
    }

    public static void getAllDepartments(DepartmentDaoJDBC departmentDao) {
        List<Department> departments = departmentDao.findAll();
        System.out.println(departments);
    }
}