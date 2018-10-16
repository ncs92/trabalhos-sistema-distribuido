/*
 *  Learning Project.
 */
package representacaoexternaSerializacao;

import java.io.Serializable;

/**
 *
 * @author rodrigo
 */

public class Gerenciamento implements Serializable {
    private String titulo;
    private String autor;
    private String cantor;
    private int ano;
    private int num_paginas;
    private String isbn;
    private double peso;
    private Dimensao dimensao;
    private String album;
    private String data_lancamento;

    public Gerenciamento(String titulo, String autor, String cantor, int ano, int num_paginas, String isbn, double peso, Dimensao dimensao, String album, String data_lancamento) {
        this.titulo = titulo;
        this.autor = autor;
        this.cantor = cantor;
        this.ano = ano;
        this.num_paginas = num_paginas;
        this.isbn = isbn;
        this.peso = peso;
        this.dimensao = dimensao;
        this.album = album;
        this.data_lancamento = data_lancamento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCantor() {
        return cantor;
    }

    public void setCantor(String cantor) {
        this.cantor = cantor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getNum_paginas() {
        return num_paginas;
    }

    public void setNum_paginas(int num_paginas) {
        this.num_paginas = num_paginas;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Dimensao getDimensao() {
        return dimensao;
    }

    public void setDimensao(Dimensao dimensao) {
        this.dimensao = dimensao;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getData_lancamento() {
        return data_lancamento;
    }

    public void setData_lancamento(String data_lancamento) {
        this.data_lancamento = data_lancamento;
    }

    @Override
    public String toString() {
        return "Gerenciamento{" + "titulo=" + titulo + ", autor=" + autor + ", cantor=" + cantor + ", ano=" + ano + ", num_paginas=" + num_paginas + ", isbn=" + isbn + ", peso=" + peso + ", dimensao=" + dimensao + ", album=" + album + ", data_lancamento=" + data_lancamento + '}';
    }
    
    
    
}
