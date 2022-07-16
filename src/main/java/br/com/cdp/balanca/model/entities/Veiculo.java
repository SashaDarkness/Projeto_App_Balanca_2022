package br.com.cdp.balanca.model.entities;

import java.sql.Timestamp;

public class Veiculo {

    private Integer idVeiculo;
    private Integer idPessoa; //motorista
    private String placaVeiculo;
    private Float pesoTara;

    public Veiculo(){

    }

    public Veiculo(Integer idVeiculo, Integer idPessoa, String placaVeiculo, Float pesoTara) {
        this.idVeiculo = idVeiculo;
        this.idPessoa = idPessoa;
        this.placaVeiculo = placaVeiculo;
        this.pesoTara = pesoTara;
    }

    public Integer getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(Integer idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public Float getPesoTara() {
        return pesoTara;
    }

    public void setPesoTara(Float pesoTara) {
        this.pesoTara = pesoTara;
    }
}
