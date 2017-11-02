/**
 * 
 */
package br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util;

import java.util.ArrayList;

import br.ufpi.capriovi.entidades.cadastros.Animal;

/**
 * 
 * @author thasciano
 *
 */
public class AnimalSelectionSolution {
	
	/** */
	private Double resultObjective;//max ganho
	private Double resultObjective2;//min endogamia
	private Double resultObjective3;//max DEP
	/** */
	private ArrayList<Animal> animaisSelecionados;
	
	/**
	 * @param resultObjective
	 */
	public AnimalSelectionSolution(Double resultObjective) {
		this.resultObjective = resultObjective;
		this.animaisSelecionados = new ArrayList<Animal>();
	}
	/**
	 * @param resultObjective
	 */
	public AnimalSelectionSolution(Double resultObjective, Double resultObjective2) {
		this.resultObjective = resultObjective;
		this.resultObjective2 = resultObjective2;
		this.animaisSelecionados = new ArrayList<Animal>();
	}
	/**
	 * @param resultObjective
	 */
	public AnimalSelectionSolution(Double resultObjective, Double resultObjective2, Double resultObjective3) {
		this.resultObjective = resultObjective;
		this.resultObjective2 = resultObjective2;
		this.resultObjective3 = resultObjective3;
		this.animaisSelecionados = new ArrayList<Animal>();
	}
	/**
	 * @param animalSelecionado
	 */
	public void addAnimalSelecionado(Animal animalSelecionado){
		this.animaisSelecionados.add(animalSelecionado);
	}
	
	/**
	 * @return the resultObjective
	 */
	public Double getResultObjective() {
		return resultObjective;
	}
	/**
	 * @param resultObjective the resultObjective to set
	 */
	public void setResultObjective(Double resultObjective) {
		this.resultObjective = resultObjective;
	}
	
	public Double getResultObjective2() {
		return resultObjective2;
	}
	public void setResultObjective2(Double resultObjective2) {
		this.resultObjective2 = resultObjective2;
	}
	
	public Double getResultObjective3() {
		return resultObjective3;
	}
	public void setResultObjective3(Double resultObjective3) {
		this.resultObjective3 = resultObjective3;
	}
	/**
	 * @return the animaisSelecionados
	 */
	public ArrayList<Animal> getAnimaisSelecionados() {
		return animaisSelecionados;
	}
	/**
	 * @param animaisSelecionados the animaisSelecionados to set
	 */
	public void setAnimaisSelecionados(ArrayList<Animal> animaisSelecionados) {
		this.animaisSelecionados = animaisSelecionados;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnimalSelectionSolution [resultObjective=" + resultObjective + ", animaisSelecionados=" + animaisSelecionados + "]";
	}
}
