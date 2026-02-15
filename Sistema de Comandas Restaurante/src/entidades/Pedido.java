package entidades;

import java.io.Serializable;

public class Pedido implements Serializable {
	private static final long serialVersionUID = -4569960759572322975L;
	
	private String nome;
	private Double preco;
	private String categoria;
	private Boolean estaNoCardapio;
	private Boolean jaFoiPedido;
	
	public Pedido(String nome, Double preco, String categoria) {
		this.nome = nome;
        this.preco = preco;
        this.categoria = categoria;
        this.estaNoCardapio = true;
        this.jaFoiPedido = false;
	}
	
	//implementei o método de alterar o nome com a condição de não permitir alteração para um nome vazio, como uma String: ""
	public boolean alterarNome(String nome) {
		if(!nome.isEmpty()) {
			this.nome = nome;
            return true;
        }
        return false;
	}
	
	//implementei o método que ativa ou desativa o respectivo pedido da comanda "estaNoCardapio"
	public void alterarNoCardapio() {
		this.estaNoCardapio = !this.estaNoCardapio;
	}
	
	//implementei o método que altera o preço caso não seja alterado para um valor negativo
	public boolean alterarPreco(Double preco) {
		if(preco >= 0) {
            this.preco = preco;
            return true;
        }
        return false;
	}
	
	//implementei o método que aciona a variável jaFoiPedido como true após um pedido ser atribuido a uma comanda
	public void foiPedido() {
		this.jaFoiPedido = true;
	}
	
	////////////////////////////////////////
	
	public String getNome() {
		return nome;
	}
	
	public Double getPreco() {
		return preco;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public Boolean getEstaNoCardapio() {
		return estaNoCardapio;
	}

	public Boolean getJaFoiPedido() {
		return jaFoiPedido;
	}
}
