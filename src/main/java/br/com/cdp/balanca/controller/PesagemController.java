package br.com.cdp.balanca.controller;

import br.com.cdp.balanca.db.DbException;
import br.com.cdp.balanca.model.entities.AutorizacaoEntradaSaida;
import br.com.cdp.balanca.model.entities.Veiculo;
import br.com.cdp.balanca.model.services.AutorizacaoEntradaSaidaServices;
import br.com.cdp.balanca.model.services.VeiculoServices;
import br.com.cdp.balanca.utils.Alerts;
import br.com.cdp.balanca.utils.Constraints;
import br.com.cdp.balanca.utils.LeituraPortaCOM;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class PesagemController implements Initializable {

    private AutorizacaoEntradaSaidaServices service;

    private VeiculoServices veiculoServices;

    private Boolean primeiraPesagem;

    @FXML
    private TextField txtVeiculo;

    @FXML
    private TextField txtAutorizacaoEntrada;

    @FXML
    private TextField txtNotaFiscal;

    @FXML
    private TextField txtPesoCheio;

    @FXML
    private TextField txtDataHoraPesagemCheio;

    @FXML
    private TextField txtPesoVazio;

    @FXML
    private TextField txtDataHoraPesagemVazio;

    @FXML
    private TextField txtPesoLiquido;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnPesar;

    @FXML
    private Label lblErrorAutorizacao;

    @FXML
    private Label lblErrorVeiculo;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");

    Veiculo veiculo;

    public void setService(AutorizacaoEntradaSaidaServices service) {
        this.service = service;
    }

    public void setVeiculoServices(VeiculoServices veiculoServices) {
        this.veiculoServices = veiculoServices;
    }

    public void setPrimeiraPesagem(boolean primeiraPesagem){this.primeiraPesagem = primeiraPesagem;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldInteger(txtAutorizacaoEntrada);
        Constraints.setTextFieldInteger(txtVeiculo);
        initializeFocus();
    }

    public void initializeFocus(){
        txtAutorizacaoEntrada.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){

                }else if(!txtAutorizacaoEntrada.getText().equals("")){
                    AutorizacaoEntradaSaida autorizacaoEntradaSaida = service.findById(Integer.parseInt(txtAutorizacaoEntrada.getText()));
                    if(autorizacaoIsValid(autorizacaoEntradaSaida)){
                        if(autorizacaoIsUsed(autorizacaoEntradaSaida)){
                            lblErrorAutorizacao.setStyle("-fx-background-color: green");
                            txtAutorizacaoEntrada.setStyle("-fx-border-color: green");
                            lblErrorAutorizacao.setText("Autorização é Válida");
                        }else {
                            lblErrorAutorizacao.setStyle("-fx-background-color: red");
                            txtAutorizacaoEntrada.setStyle("-fx-border-color: red");
                            lblErrorAutorizacao.setText("Autorização Já Foi Utilizada no dia "+sdf2.format(autorizacaoEntradaSaida.getDataUso()));
                        }
                    }else {
                        lblErrorAutorizacao.setStyle("-fx-background-color: red");
                        txtAutorizacaoEntrada.setStyle("-fx-border-color: red");
                        lblErrorAutorizacao.setText("Autorização Não é Válida");
                    }
                }
            }
        });

        txtVeiculo.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    System.out.println("ganhou");
                }else {
                    buscarVeiculo();
                    if(veiculo != null){
                        lblErrorVeiculo.setStyle("-fx-background-color: green");
                        txtVeiculo.setStyle("-fx-border-color: green");
                        lblErrorVeiculo.setText("PLACA DO VEICULO: "+veiculo.getPlacaVeiculo());
                    } else {
                        lblErrorVeiculo.setStyle("-fx-background-color: red");
                        txtVeiculo.setStyle("-fx-border-color: red");
                        lblErrorVeiculo.setText("VEICULO NÃO ENCONTRADO");
                    }
                }
            }
        });
    }

    private void buscarVeiculo(){
        veiculo = veiculoServices.findById(Integer.parseInt(txtVeiculo.getText()));
    }

    private Boolean autorizacaoIsValid(AutorizacaoEntradaSaida autorizacaoEntradaSaida){
        if(autorizacaoEntradaSaida == null){
            return false;
        }else {
            return true;
        }
    }

    private boolean autorizacaoIsUsed(AutorizacaoEntradaSaida autorizacaoEntradaSaida){

        if(autorizacaoEntradaSaida.autorizacaIsValid()){
            return false;
        } else {
            return true;
        }
    }

    @FXML
    private void onBtActionPesar() {
        Double valorRecuperado = LeituraPortaCOM.leituraPeso();
        if(primeiraPesagem.equals(true)){
            txtPesoCheio.setText(valorRecuperado.toString());
            txtDataHoraPesagemCheio.setText(sdf.format(new Date()));
        }else {
            txtPesoVazio.setText(valorRecuperado.toString());
            txtDataHoraPesagemVazio.setText(sdf.format(new Date()));
        }
    }
}