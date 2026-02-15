package entidades;

import java.io.Serializable;
import java.util.*;

public class Historico implements Serializable {
	private static final long serialVersionUID = 884298767781774916L;
	
	private Date dataDoPedido;
	private Comanda comanda;
	private String dadosProdutos; //preço dos produtos podem mudar, então tem que se manter assim
	
	public Historico(Comanda comanda) {
		this.dataDoPedido = new Date();
		this.comanda = comanda;
		this.dadosProdutos = imprimirPedidosESeusValores();
	}
	
	public String obterNomeDaComanda() {
		return comanda.getNome();
	}
	
	public Double obterPreco() {
		if (comanda.getDesconto() != null && comanda.getDesconto() > 0) {
			float desconto = comanda.getDesconto();
			double bruto = comanda.getPrecoBruto();
			return bruto - (bruto * (desconto));
		} else {
			return obterPrecoBruto();
		}
	}
	
	public Float obterDesconto() {
		return comanda.getDesconto();
	}
	
	public Double obterPrecoBruto() {
		return comanda.getPrecoBruto();
	}
	
	private String imprimirPedidosESeusValores() {
		StringBuilder sb = new StringBuilder();
		for (Pedido pedido : comanda.getListaDePedidos()) {
			sb.append(" - ")
			  .append(pedido.getNome())
			  .append(" (")
			  .append(pedido.getCategoria())
			  .append("): R$ ")
			  .append(String.format("%.2f", pedido.getPreco()))
			  .append("\n");
			pedido.foiPedido();
			// - nome (categoria): R$ 9.99\n
		}
		return sb.toString();
	}

	////////////////////////////////////////
	
	public Date getDataDoPedido() {
		return dataDoPedido;
	}

	public Comanda getComanda() {
		return comanda;
	}

	public String getDadosProdutos() {
		return dadosProdutos;
	}
}
