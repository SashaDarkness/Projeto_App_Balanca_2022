package br.com.cdp.balanca.controller;

import br.com.cdp.balanca.listeners.DataChangeListeners;
import br.com.cdp.balanca.model.entities.Funcionario;
import br.com.cdp.balanca.model.services.FuncionarioServices;
import br.com.cdp.balanca.utils.Alerts;
import br.com.cdp.balanca.utils.ResourceStage;
import br.com.cdp.balanca.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FuncionarioFormController implements Initializable {

    private Funcionario funcionario;
    private FuncionarioServices funcionarioServices;
    private List<DataChangeListeners> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtLoginRede;

    @FXML
    private TextField txtLoginScap;

    @FXML
    private CheckBox checkAdministrador;

    @FXML
    private CheckBox checkAtivo;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnLimpar;

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setFuncionarioServices(FuncionarioServices funcionarioServices) {
        this.funcionarioServices = funcionarioServices;
    }

    public void autenticar_usuario(){
        if (funcionario == null){
            throw new IllegalStateException("Funcionário é null");
        }
        try {

        } catch (RuntimeException exceptionMsg) {

        }
    }


    @FXML
    private void btnOnActionSalvar(ActionEvent event) {
        if (funcionario == null) {
            throw new IllegalStateException("Funcionário as null");
        }
        if (funcionarioServices == null) {
            throw new IllegalStateException("Service as null");
        }
        try {
            funcionario = getFormData();
            funcionarioServices.insertOrUpdate(funcionario);
            notifyDataChangeListener();
            ResourceStage.currentStage(event).close();
        } catch (RuntimeException exceptionMsg) {
            Alerts.showAlert("Erro em Salvar Funcionário", null, exceptionMsg.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void btnOnActionLimpar(ActionEvent event) {
        try {
            getFormClear();
            notifyDataChangeListener();
        } catch (RuntimeException exceptionMsg) {
            Alerts.showAlert("Erro ao limpar campo", null, exceptionMsg.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnOnActionCancelar(ActionEvent event) {
        ResourceStage.currentStage(event).close();
    }

    public void subscribeDataChangeListener(DataChangeListeners listener) {
        dataChangeListeners.add(listener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void notifyDataChangeListener() {
        for (DataChangeListeners listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Funcionario getFormData() {
        Funcionario obj = new Funcionario();
        obj.setId(Utils.tryParseToLong(txtId.getText()));
        obj.setNome(txtNome.getText());
        obj.setLoginRede(txtLoginRede.getText());
        obj.setLoginScap(txtLoginScap.getText());
        obj.setAdministrador(checkAdministrador.isSelected());
        obj.setAtivo(checkAtivo.isSelected());
        return obj;
    }

    private void getFormClear() {
        if (funcionario == null) {
            throw new IllegalStateException("Funcionário é null");
        }
        if (funcionarioServices == null) {
            throw new IllegalStateException("Service é null");
        }
        txtNome.setText("");
        txtLoginRede.setText("");
        txtLoginScap.setText("");
        checkAdministrador.setSelected(false);
        checkAtivo.setSelected(false);
    }

    public void updateFormData() {
        if (funcionario == null) {
            throw new IllegalStateException("Funcionário é Null");
        }
        txtId.setText(String.valueOf(funcionario.getId() == null ? "" : funcionario.getId()));
        txtNome.setText(funcionario.getNome());
        txtLoginRede.setText(funcionario.getLoginRede());
        txtLoginScap.setText(funcionario.getLoginScap());
    //    checkAdministrador.setSelected(funcionario.getAdministrador() == null ? false : funcionario.getAdministrador());
        checkAdministrador.setSelected(funcionario.getAdministrador() != null && funcionario.getAdministrador());
    //    checkAtivo.setSelected(funcionario.getAtivo() == null ? false : funcionario.getAtivo());
        checkAtivo.setSelected(funcionario.getAtivo() != null && funcionario.getAtivo());
    }


}
