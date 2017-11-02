package br.ufpi.capriovi.suporte.relatorios;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;

public class ResumoCPM {
	private String fazenda;
	private String entrada;
	private String usuario;
	private Animal animal;
	private DesenvolvimentoPonderal dPonderal;

	public ResumoCPM(Animal animal, DesenvolvimentoPonderal dPonderal) {
		super();
		this.animal = animal;
		this.dPonderal = dPonderal;
	}

	public String getFazenda() {
		return fazenda;
	}

	public void setFazenda(String fazenda) {
		this.fazenda = fazenda;
	}

	public String getEntrada() {
		return entrada;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public DesenvolvimentoPonderal getdPonderal() {
		return dPonderal;
	}

	public void setdPonderal(DesenvolvimentoPonderal dPonderal) {
		this.dPonderal = dPonderal;
	}
}
