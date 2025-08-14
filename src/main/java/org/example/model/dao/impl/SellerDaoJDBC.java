package org.example.model.dao.impl;

import org.example.model.dao.SellerDao;
import org.example.model.entities.Department;
import org.example.model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private final Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        String sql = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING Id";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setTimestamp(3, new Timestamp(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    seller.setId(rs.getInt("Id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir vendedor: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Seller seller) {
        if(seller.getId() == null){
            throw new IllegalArgumentException("Id do seller não pode ser nulo para a atualização: ");
        }
        if (seller.getDepartment()==null || seller.getDepartment().getId() == null){
            throw new IllegalArgumentException("Department e DepartmentId não podem ser nulos");
        }
        String sql = "UPDATE seller " +
                "SET Name = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                "WHERE Id = ?";

        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setTimestamp(3, new Timestamp(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Erro ao atualizar vendedor: " + seller);
            }
        }catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar vendedor: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        if(id == null){
            throw new IllegalArgumentException("Id não pode ser nulo para a exclusão.");
        }

        String sql = "DELETE FROM seller WHERE Id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, id);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Erro ao excluir vendedor: " + id);
            }
        }catch (SQLException e) {
            String sqlState = e.getSQLState();
            if("23503".equals(sqlState)){
                throw new RuntimeException("não foi possivel excluir o vendedor, ele está vinculado a outros registros (violação de chave estrangeira)", e);
            }
            throw new RuntimeException("Erro ao excluir vendedor: " + e.getMessage());
        }
    }

    @Override
    public Seller findById(Integer id) {
        String sql = "SELECT s.*, d.Name AS DepName " +
                "FROM seller s " +
                "INNER JOIN department d ON s.DepartmentId = d.Id " +
                "WHERE s.Id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Department dep = instantiateDepartment(rs);
                    Seller seller = instantiateSeller(rs, dep);

                    return seller;
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vendedor por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Seller> findAll() {
        String sql = "SELECT s.*, d.Name AS DepName " +
                "FROM seller s " +
                "INNER JOIN department d ON s.DepartmentId = d.Id " +
                "ORDER BY s.Name";

        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> depCache = new HashMap<>();

            while (rs.next()) {
                int depId = rs.getInt("DepartmentId");

                Department dep = depCache.get(depId);
                if (dep == null) {
                    dep = new Department();
                    dep.setId(depId);
                    dep.setName(rs.getString("DepName"));
                    depCache.put(depId, dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                list.add(seller);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendedores: " + e.getMessage(), e);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getTimestamp("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(dep);

        return seller;
    }
}