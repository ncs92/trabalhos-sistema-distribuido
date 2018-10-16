/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representacaoexternaSerializacao;

import java.io.Serializable;

/**
 *
 * @author Elaine
 */
public class Dimensao implements Serializable{
    private double altura;
    private double largura;
    private double comprimento;

    public Dimensao(double altura, double largura, double comprimento) {
        this.altura = altura;
        this.largura = largura;
        this.comprimento = comprimento;
    }
    
    public Dimensao() {
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getLargura() {
        return largura;
    }

    public void setLargura(double largura) {
        this.largura = largura;
    }

    public double getComprimento() {
        return comprimento;
    }

    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
    }

    @Override
    public String toString() {
        return "Dimensao{" + "altura=" + altura + ", largura=" + largura + ", comprimento=" + comprimento + '}';
    }
    
    
}

