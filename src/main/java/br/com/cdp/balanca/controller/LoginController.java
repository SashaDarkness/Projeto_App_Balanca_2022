package br.com.cdp.balanca.controller;

import br.com.cdp.balanca.application.Main;
import br.com.cdp.balanca.db.DB;
import br.com.cdp.balanca.model.dao.DaoFactory;
import br.com.cdp.balanca.model.dao.FuncionarioDAO;
import br.com.cdp.balanca.model.entities.Funcionario;
import br.com.cdp.balanca.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    protected TextField txtLogin;

    @FXML
    protected PasswordField pswSenha;

    @FXML
    protected Button btnEntrar;

    @FXML
    protected Button btnSair;

    FuncionarioDAO funcionarioDAO = DaoFactory.createFuncionarioDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onPressedAction();
    }

    private void onPressedAction(){
        pswSenha.setOnAction(event -> {
                login();
        });
    }

    private boolean validateInput(){
        if(txtLogin.getText().equals("") || pswSenha.getText().equals("")){
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void onLoginAction() {
        login();
    }

    private void login(){
        if (validateInput()) {
            Funcionario func = funcionarioDAO.findByLogin(txtLogin.getText());

            if (func == null) {
                Alerts.showAlert("Error!", "", "Usuário não está cadastrado no sistema", Alert.AlertType.ERROR);
            } else {
                func.setSenha(pswSenha.getText());
                boolean funcAd = DB.atenticacaoUsuarioAd(func);
                if (func.getAtivo() == true) {
                    if(funcAd == true){
                        txtLogin.clear();
                        pswSenha.clear();
                        Main.setDataUser(func);
                        Main.changeScene("Home");
                    }else {
                        Alerts.showAlert("Atenção!", "", "Login ou Senha está Incorreta", Alert.AlertType.ERROR);
                    }
                } else {
                    Alerts.showAlert("Atenção!", "", "Este Perfil Não está ativo no Sistema", Alert.AlertType.ERROR);
                }
            }
        } else {
            Alerts.showAlert("Atenção!", "", "Campos em Branco", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onExitAction() {
        System.exit(1);
    }
}
