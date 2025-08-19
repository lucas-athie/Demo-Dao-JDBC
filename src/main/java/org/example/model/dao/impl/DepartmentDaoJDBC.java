package org.example.model.dao.impl;

import org.example.model.dao.DepartmentDao;
import org.example.model.entities.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private final Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert (Department department) {
        String sql = "INSERT INTO department (Name)" + "VALUES (?) RETURNING Id";

        try(PreparedStatement st = connection.prepareStatement(sql)){

            st.setString(1, department.getName());

            try(ResultSet rs = st.executeQuery()){
                if(rs.next()){
                    department.setId(rs.getInt("Id"));
                }
            }

        }catch(SQLException e){
            throw new RuntimeException("Erro ao inserir Departamento" + e.getMessage());
        }

    }
    @Override
    public void update (Department department) {
        if (department.getId() == null) {
            throw new IllegalArgumentException("id do department não pode ser nulo");
        }

        String sql = "UPDATE department" + "SET Name=?" + "WHERE Id=?";

        try(PreparedStatement st = connection.prepareStatement(sql)){

            st.setString(1, department.getName());
            st.setInt(2, department.getId());

            int rowsAffected = st.executeUpdate();
            if(rowsAffected == 0){
                throw new IllegalArgumentException("Erro ao atualizar departamento" + department);
            }
        }catch(SQLException e){
            throw new RuntimeException("Erro ao atualizar Departamento" + e.getMessage());
        }

    }
    @Override
    public void deleteById (Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id não pode ser nulo para a exclusão");
        }

        String sql = "DELETE FROM department WHERE Id = ?";

        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();
            if(rowsAffected == 0){
                throw new IllegalArgumentException("Erro ao excluir departamento" + id);
            }
        }catch (SQLException e){
            String sqlState = e.getSQLState();
            if("23503".equals(sqlState)){
                throw new RuntimeException("não foi possivel excluir o departamento, ele está vinculado a outros registros (violação de chave estrangeira)", e);
            }
            throw new RuntimeException("Erro ao excluir departamento" + e.getMessage());
        }

    }
    @Override
    public Department findById(Integer id){
        String sql = "SELECT * FROM department WHERE Id = ?";

        try(PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, id);
            try(ResultSet rs = st.executeQuery()){
                if(rs.next()){
                    return instantiateDepartment(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar departamento" + e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        String sql = "SELECT * FROM department";

        try(PreparedStatement st = connection.prepareStatement(sql)){
            ResultSet rs = st.executeQuery();

            List<Department> departments = new ArrayList<>();

            while(rs.next()){
                departments.add(instantiateDepartment(rs));
            }
            return departments;

        }catch (SQLException e) {
            throw new RuntimeException("Erro ao listar departamentos: " + e.getMessage(), e);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }
}
