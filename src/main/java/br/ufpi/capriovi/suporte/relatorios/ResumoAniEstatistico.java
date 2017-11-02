package br.ufpi.capriovi.suporte.relatorios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufpi.capriovi.entidades.cadastros.Animal;

public class ResumoAniEstatistico {

	private Animal animal;
	private List<Integer> quantIdade;
	private List<Double> media;
	private List<Double> desvioPadrao;
	private List<Date> ultimoRegistro;
	private List<Double> total;
	private List<String> tiposEnum;
	
	public ResumoAniEstatistico(Animal animal) {
		super();
		this.animal = animal;
		this.quantIdade = new ArrayList<Integer>();
		this.media = new ArrayList<Double>();
		this.ultimoRegistro = new ArrayList<Date>(); 
		this.total = new ArrayList<Double>(); 
	}
	public ResumoAniEstatistico(Animal animal, List<Integer> quantidade) {
		super();
		this.animal = animal;
		this.quantIdade = quantidade;
	}
	public ResumoAniEstatistico(Animal animal, List<Integer> quantidade,
			List<Double> media, List<Double> desvioPadrao,
			List<Date> ultimoRegistro, List<Double> total) {
		super();
		this.animal = animal;
		this.quantIdade = quantidade;
		this.media = media;
		this.desvioPadrao = desvioPadrao;
		this.ultimoRegistro = ultimoRegistro;
		this.total = total;
	}
	
	public ResumoAniEstatistico(Animal animal, List<Integer> quantidade,
			List<Double> media, List<Date> ultimoRegistro, List<Double> total) {
		super();
		this.animal = animal;
		this.quantIdade = quantidade;
		this.media = media;
		this.ultimoRegistro = ultimoRegistro;
		this.total = total;
	}
	
	public ResumoAniEstatistico(Animal animal, Integer quantidade,
			Double media, Date ultimoRegistro, Double total) {
		super();
		this.animal = animal;
		this.quantIdade = new ArrayList<Integer>();
		this.quantIdade.add(quantidade);
		this.media = new ArrayList<Double>();
		this.media.add(media);
		this.ultimoRegistro = new ArrayList<Date>(); 
		this.ultimoRegistro.add(ultimoRegistro);
		this.total = new ArrayList<Double>(); 
		this.total.add(total);
	}
	
	public ResumoAniEstatistico(Animal animal, Date ultimoRegistro, String tiposEnum) {
		super();
		this.animal = animal;
		this.ultimoRegistro = new ArrayList<Date>(); 
		this.ultimoRegistro.add(ultimoRegistro);
		this.tiposEnum = new ArrayList<String>(); 
		this.tiposEnum.add(tiposEnum);
	}
	
	public Animal getAnimal() {
		return animal;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	public List<Integer> getQuantIdade() {
		return quantIdade;
	}
	public void setQuantIdade(List<Integer> quantIdade) {
		this.quantIdade = quantIdade;
	}
	public List<Double> getMedia() {
		return media;
	}
	public void setMedia(List<Double> media) {
		this.media = media;
	}
	public List<Double> getDesvioPadrao() {
		return desvioPadrao;
	}
	public void setDesvioPadrao(List<Double> desvioPadrao) {
		this.desvioPadrao = desvioPadrao;
	}
	public List<Date> getUltimoRegistro() {
		return ultimoRegistro;
	}
	public void setUltimoRegistro(List<Date> ultimoRegistro) {
		this.ultimoRegistro = ultimoRegistro;
	}
	public List<Double> getTotal() {
		return total;
	}
	public void setTotal(List<Double> total) {
		this.total = total;
	}
	public List<String> getTiposEnum() {
		return tiposEnum;
	}
	public void setTiposEnum(List<String> tiposEnum) {
		this.tiposEnum = tiposEnum;
	}
}
