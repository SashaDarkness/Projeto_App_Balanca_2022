package br.com.cdp.balanca.model.services;

import br.com.cdp.balanca.db.DB;
import br.com.cdp.balanca.model.dao.DaoFactory;
import br.com.cdp.balanca.model.dao.FuncionarioDAO;
import br.com.cdp.balanca.model.entities.Funcionario;

import java.util.List;

public class FuncionarioServices {
    FuncionarioDAO funcionarioDao = DaoFactory.createFuncionarioDao();

    public List<Funcionario> findAll() {
        return funcionarioDao.findAll();
    }

    public List<Funcionario> findByNameOrLogin(String str) {
        return funcionarioDao.find(str); //conectado com FUNCIONARIODaoJDBC e FuncionarioDAO
    }

    public void insertOrUpdate(Funcionario obj) {
        if (obj.getId() != null) {
            funcionarioDao.update(obj);
        } else {
            funcionarioDao.insert(obj);
        }
    }

    //USAR PARA AUTENTICACAO ADD NO FUNCIONARIO_FORM_CONTROLLER
    public boolean loginAdConfirmation(Funcionario obj) {
        return DB.atenticacaoUsuarioAd(obj);
    }


    public Funcionario getFuncionario(String login) {
        return funcionarioDao.findByLogin(login);
    }

    public Funcionario getNameAndLoginScap(String cpf) {
        return funcionarioDao.findLoginByCpf(cpf);
    }
}
