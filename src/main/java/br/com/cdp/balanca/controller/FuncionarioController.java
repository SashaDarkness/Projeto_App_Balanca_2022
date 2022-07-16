package br.com.cdp.balanca.controller;

import br.com.cdp.balanca.listeners.DataChangeListeners;
import br.com.cdp.balanca.model.entities.Funcionario;
import br.com.cdp.balanca.model.services.FuncionarioServices;
import br.com.cdp.balanca.utils.DialogForm;
import br.com.cdp.balanca.utils.ResourceStage;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FuncionarioController implements Initializable, DataChangeListeners {

    FuncionarioServices service;

    @FXML
    private TableView<Funcionario> tabelaFuncionario;

    @FXML
    private TableColumn<Funcionario, Long> columnId;

    @FXML
    private TableColumn<Funcionario, String> columnNome;

    @FXML
    private TableColumn<Funcionario, String> columnLoginScap;

    @FXML
    private TableColumn<Funcionario, String> columnLoginRede;

    @FXML
    private TableColumn<Funcionario, Boolean> columnAdministrador;

    @FXML
    private TableColumn<Funcionario, Boolean> columnAtivo;

    @FXML
    private TableColumn<Funcionario, Funcionario> columnEdit;

    @FXML
    private Button btnCadastrarUsuario;

    @FXML
    private TextField txtPesquisar;

    @FXML
    private Button btnPesquisar;

    private ObservableList<Funcionario> obsList;

    public void setService(FuncionarioServices service) {
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialNodes();
    }

    public void updateTableView() {
        List<Funcionario> list;

        list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tabelaFuncionario.setItems(obsList);
        initEditButtons();
    }

    //AÇAO DE EDITAR OS USERS JÁ CADASTRADOS COMO ADM'S
    private void initEditButtons() {
        columnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        columnEdit.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() {
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(Funcionario obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialog(obj, ResourceStage.currentStage(event), "/br/com/cdp/balanca/view/funcionarioForm.fxml", "Alterar Usuário da Balança"));
            }
        });
    }

    private void searchTableView(){
        List<Funcionario> list;
        list = service.findByNameOrLogin(txtPesquisar.getText());
        obsList = FXCollections.observableArrayList(list);
        tabelaFuncionario.setItems(obsList);
        initEditButtons();
    }

    private void createDialog(Funcionario obj, Stage parentStage, String url, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            Pane pane = loader.load();

            FuncionarioFormController controller = loader.getController();

            controller.setFuncionarioServices(new FuncionarioServices());
            controller.setFuncionario(obj);
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            DialogForm.createDialogForm(pane, title, parentStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialNodes() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        columnLoginRede.setCellValueFactory(new PropertyValueFactory<>("LoginRede"));
        columnLoginScap.setCellValueFactory(new PropertyValueFactory<>("LoginScap"));
        columnAdministrador.setCellValueFactory(new PropertyValueFactory<>("Administrador"));
        columnAtivo.setCellValueFactory(new PropertyValueFactory<>("Ativo"));
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    @FXML
    private void onBtActionPesquisar() {
        searchTableView();
    }

    @FXML
    private void onBtnActionCadastrar(ActionEvent event) {
        Funcionario obj = new Funcionario();
        createDialog(obj, ResourceStage.currentStage(event), "/br/com/cdp/balanca/view/funcionarioForm.fxml", "Cadastro De Usuário da Balança");
    }

}
