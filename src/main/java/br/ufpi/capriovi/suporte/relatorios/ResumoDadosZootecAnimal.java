package br.ufpi.capriovi.suporte.relatorios;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufpi.capriovi.entidades.cadastros.Animal;

/**
 * Resumo usado para o relatório de dados zootecnicos de animal.
 * 
 * @author thasciano
 *
 */
public class ResumoDadosZootecAnimal {
	private Animal animal;
	private int quantFilhosMachos;
	private int quantFilhosFemeas;
	private int quantCoberturas;
	private double peso60;
	private double peso120;
	private double peso180;
	private int limiteCurva;
	private ArrayList<String> genealogia;
	HashMap<String, ArrayList<Animal>> descendentes;
	ArrayList<String> ordemFilhos;
	public ResumoDadosZootecAnimal(Animal animal) {
		super();
		this.animal = animal;
		this.peso60 = 0;
		this.peso120 = 0;
		this.peso180 = 0;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public int getQuantFilhosMachos() {
		return quantFilhosMachos;
	}

	public void setQuantFilhosMachos(int quantFilhosMachos) {
		this.quantFilhosMachos = quantFilhosMachos;
	}

	public int getQuantFilhosFemeas() {
		return quantFilhosFemeas;
	}

	public void setQuantFilhosFemeas(int quantFilhosFemeas) {
		this.quantFilhosFemeas = quantFilhosFemeas;
	}

	public int getQuantCoberturas() {
		return quantCoberturas;
	}

	public void setQuantCoberturas(int quantCoberturas) {
		this.quantCoberturas = quantCoberturas;
	}

	public double getPeso60() {
		return peso60;
	}

	public void setPeso60(double peso60) {
		this.peso60 = peso60;
	}

	public double getPeso120() {
		return peso120;
	}

	public void setPeso120(double peso120) {
		this.peso120 = peso120;
	}

	public double getPeso180() {
		return peso180;
	}

	public void setPeso180(double peso180) {
		this.peso180 = peso180;
	}

	public int getLimiteCurva() {
		return limiteCurva;
	}

	public void setLimiteCurva(int limiteCurva) {
		this.limiteCurva = limiteCurva;
	}

	public ArrayList<String> getGenealogia() {
		return genealogia;
	}

	public void setGenealogia(ArrayList<String> genealogia) {
		this.genealogia = genealogia;
	}

	public HashMap<String, ArrayList<Animal>> getDescendentes() {
		return descendentes;
	}

	public void setDescendentes(HashMap<String, ArrayList<Animal>> descendentes) {
		this.descendentes = descendentes;
	}

	public ArrayList<String> getOrdemFilhos() {
		return ordemFilhos;
	}

	public void setOrdemFilhos(ArrayList<String> ordemFilhos) {
		this.ordemFilhos = ordemFilhos;
	}
	
}
