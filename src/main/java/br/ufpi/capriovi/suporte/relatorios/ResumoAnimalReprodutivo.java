package br.ufpi.capriovi.suporte.relatorios;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.controleAnimal.TamanhoCorporal;
/**
 * Usado para os relatórios de femeas em idade reprodutiva eCorberturas por reprodutor. 
 * @author thasciano
 *
 */
public class ResumoAnimalReprodutivo {
	private Animal animal;
	private int idadeMeses;
	private double idadeAnos;
	private int soma;
	private double media;
	private TamanhoCorporal tamanhoCorporal;
	
	public ResumoAnimalReprodutivo(Animal animal) {
		super();
		this.animal = animal;
	}
	public Animal getAnimal() {
		return animal;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	public int getIdadeMeses() {
		return idadeMeses;
	}
	public void setIdadeMeses(int idadeMeses) {
		this.idadeMeses = idadeMeses;
	}
	public double getIdadeAnos() {
		return idadeAnos;
	}
	public void setIdadeAnos(double idadeAnos) {
		this.idadeAnos = idadeAnos;
	}
	public int getSoma() {
		return soma;
	}
	public void setSoma(int soma) {
		this.soma = soma;
	}
	public TamanhoCorporal getTamanhoCorporal() {
		return tamanhoCorporal;
	}
	public void setTamanhoCorporal(TamanhoCorporal tamanhoCorporal) {
		this.tamanhoCorporal = tamanhoCorporal;
	}
	public double getMedia() {
		return media;
	}
	public void setMedia(double media) {
		this.media = media;
	}
	
}
