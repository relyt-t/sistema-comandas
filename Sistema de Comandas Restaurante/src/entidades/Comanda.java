package entidades;

import java.io.Serializable;
import java.util.*;

public class Comanda implements Serializable {
	private static final long serialVersionUID = 3891762330156340846L;
	
	protected String nome;
	private List<Pedido> listaDePedidos;
	private Double precoBruto; //não é necessário inicia-lo no construtor (valor inicial = 0)
	private Float desconto; //não é necessário inicia-lo no construtor (valor inicial = 0)
	private Integer foiConcluidoOuCancelado; //não é necessário inicia-lo no construtor (valor inicial = 0)
	
	public Comanda(String nome) {
		this.nome = nome;
        this.listaDePedidos = new ArrayList<>();
        this.precoBruto = 0.0;
        this.desconto = 0.0f;
        this.foiConcluidoOuCancelado = 0;
	}

	public void adicionarPedido(Pedido pedido) {
		listaDePedidos.add(pedido);
        calculaPreco();
	}
	
	public void removerPedido(Pedido pedido) {
		if (listaDePedidos.remove(pedido)) {
            calculaPreco();
        }
	}
	
	public void removerTodosPedidos(Pedido pedido) {
		do {
			if (!(listaDePedidos.remove(pedido))) {
				calculaPreco();
				break;
			}
		} while(true);
	}
	
	public void calculaPreco() {
		precoBruto = 0.0;
		for(Pedido pedido : listaDePedidos) {
			precoBruto += pedido.getPreco();
		}
	}
	
	public void editarDesconto(float desconto) {
		if (desconto >= 0 && desconto <= 1) {
            this.desconto = desconto;
        } else {
            System.out.println("Desconto inválido! Deve estar entre 0 (0%) e 1 (100%).");
        }
	}
	
	public void concluirComanda() {
		foiConcluidoOuCancelado = 1;
	}
	
	public void cancelarComanda() {
		precoBruto = 0.0;
		listaDePedidos.removeAll(listaDePedidos);
	}
	
	////////////////////////////////////////
	
	public String getNome() {
		return "Cliente: " + nome;
	}

	public void setNome(String nome) {}
	
	public List<Pedido> getListaDePedidos() {
		return listaDePedidos;
	}

	public Double getPrecoBruto() {
		return precoBruto;
	}

	public Float getDesconto() {
		return desconto;
	}

	public Integer getFoiConcluidoOuCancelado() {
		return foiConcluidoOuCancelado;
	}
	
	
}
