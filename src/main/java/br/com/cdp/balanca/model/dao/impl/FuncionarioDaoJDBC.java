package br.com.cdp.balanca.model.dao.impl;

import br.com.cdp.balanca.db.DB;
import br.com.cdp.balanca.model.dao.FuncionarioDAO;
import br.com.cdp.balanca.model.entities.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDaoJDBC implements FuncionarioDAO {

    private Connection conn;

    public FuncionarioDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Funcionario func) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "INSERT INTO dbo.funcionario_balanca (login_rede,login_scap,nome_funcionario,administrador,ativo) VALUES (?,?,?,?,?)");
            st.setString(1, func.getLoginRede());
            st.setString(2, func.getLoginScap());
            st.setString(3, func.getNome());
            st.setBoolean(4, func.getAdministrador());
            st.setBoolean(5, func.getAtivo());

            st.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }finally {
            DB.closeStatment(st);
        }
    }

    @Override
    public void update(Funcionario func) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "UPDATE dbo.funcionario_balanca SET login_rede = ?, nome_funcionario = ?, login_scap = ? , administrador = ?, ativo = ? WHERE id_funcionario = ?");

            st.setString(1, func.getLoginRede());
            st.setString(2, func.getNome());
            st.setString(3, func.getLoginScap());
            st.setBoolean(4, func.getAdministrador());
            st.setBoolean(5, func.getAtivo());
            st.setLong(6, func.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatment(st);
        }
    }

    @Override
    public Funcionario findById(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM dbo.funcionario_balanca WHERE id_funcionario = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Funcionario func = instantiateFuncionario(rs);
                return func;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Funcionario findByLogin(String loginRede) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * FROM dbo.funcionario_balanca WHERE login_rede = ?");
            st.setString(1, loginRede);
            rs = st.executeQuery();

            if (rs.next()) {
                Funcionario func = instantiateFuncionario(rs);
                return func;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Funcionario> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Funcionario> list = new ArrayList<>();

        try {
            st = conn.prepareStatement("SELECT * FROM dbo.funcionario_balanca");
            rs = st.executeQuery();

            while (rs.next()) {
                list.add(instantiateFuncionario(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Funcionario> find(String pesquisa) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Funcionario> list = new ArrayList<>();
        //
        try {
            //st = conn.prepareStatement("select * from funcionario_balanca where login_rede = ? or nome_funcionario = ?");
            st = conn.prepareStatement("EXECUTE dbo.pr_Balanca_Buscar_Funcionario ?");
            st.setString(1, pesquisa);
            rs = st.executeQuery();

            while (rs.next()) {
                list.add(instantiateFuncionario(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Funcionario findLoginByCpf(String cpf) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("select p1.nm_pessoa as nome_funcionario, u1.nm_usuario as login_scap from "
                    + "pessoa p1 inner join usuario u1 on u1.nsu_pessoa = p1.nsu_pessoa "
                    + "where p1.no_cpf = ?");
            st.setString(1, cpf);
            rs = st.executeQuery();

            if (rs.next()) {
                Funcionario func = new Funcionario();
                func.setNome(rs.getString("nome_funcionario"));
                func.setLoginScap(rs.getString("login_scap"));
                return func;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            DB.closeStatment(st);
            DB.closeResultSet(rs);
        }
    }

    private Funcionario instantiateFuncionario(ResultSet rs) throws SQLException {
        Funcionario func = new Funcionario();
        func.setId(rs.getLong("id_funcionario"));
        func.setLoginRede(rs.getString("login_rede"));
        func.setNome(rs.getString("nome_funcionario"));
        func.setLoginScap(rs.getString("login_scap"));
        func.setAdministrador(rs.getBoolean("administrador"));
        func.setAtivo(rs.getBoolean("ativo"));
        return func;
    }
}
