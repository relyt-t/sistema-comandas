package entidades;

import java.util.*;
import java.io.Serializable;

public class Cardapio implements Serializable {
	private static final long serialVersionUID = 3234625979594024580L;
	
	private List<Pedido> listaItensCardapio;
	private List<String> categorias;
	
	//implementei construtor padrão das listas com new ArrayList<>()
	public Cardapio() {
		this.listaItensCardapio = new ArrayList<>();
        this.categorias = new ArrayList<>();
	}
	
	//implementei o método que adiciona um item na List<Pedido> listaItensCardapio
	public void adicionarItemNoCardapio(Pedido item) {
        listaItensCardapio.add(item);
	}
	
	//implementei o método de remoção do item especifico da List<Pedido> listaItensCardapio
	public void removerItemNoCardapio(Pedido item) {
		if(item.getJaFoiPedido() == false) {
			listaItensCardapio.remove(item);
		}
	}
	
	//implementei o método que adiciona uma categoria na List<String> categorias
	public void adicionarCategoria(String categoria) {
        categorias.add(categoria);
	}

	////////////////////////////////////////
	
	public List<Pedido> getListaItensCardapio() {
		return listaItensCardapio;
	}

	public List<String> getCategorias() {
		return categorias;
	}
}
