package entidades;

import java.io.Serializable;

public class Mesa extends Comanda implements Serializable{
	private static final long serialVersionUID = 715565042528263799L;
	
	private Integer numeroMesa;
	
	public Mesa(int numeroMesa) {
		super("");
		this.numeroMesa = numeroMesa;
	}
	
	public Mesa(String nome, int numeroMesa) {
		super(nome);
		this.numeroMesa = numeroMesa;
	}

	////////////////////////////////////////
	
	@Override
	public String getNome() {
		String texto;
		if (super.getNome().replace("Cliente: ", "").isBlank()) {
			texto = ("Mesa: " + numeroMesa);
		} else {
			texto = ("Mesa: "+ numeroMesa + " (" + super.getNome() + ")");
		}
		System.out.println(texto);
		return texto;
	}
	
	@Override
	public void setNome(String nome) {
		super.nome = nome;
	}
	
	public Integer getNumeroMesa() {
		return numeroMesa;
	}
}
