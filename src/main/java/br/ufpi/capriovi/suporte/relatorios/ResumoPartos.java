package br.ufpi.capriovi.suporte.relatorios;

import br.ufpi.capriovi.entidades.cadastros.Animal;

public class ResumoPartos {
	private Animal animal;
	private Double intervaloPartos;
	private int idadePrimeiroParto;
	private Double mediaCioPosParto;
	private int partoSimples;
	private int partoDuplo;
	private int partoTriplo;
	private int partoQuadr;
	private int total;
	
	public ResumoPartos(Animal animal) {
		super();
		this.animal = animal;
	}
	public Animal getAnimal() {
		return animal;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	public Double getIntervaloPartos() {
		return intervaloPartos;
	}
	public void setIntervaloPartos(Double intervaloPartos) {
		this.intervaloPartos = intervaloPartos;
	}
	public int getIdadePrimeiroParto() {
		return idadePrimeiroParto;
	}
	public void setIdadePrimeiroParto(int idadePrimeiroParto) {
		this.idadePrimeiroParto = idadePrimeiroParto;
	}
	public Double getMediaCioPosParto() {
		return mediaCioPosParto;
	}
	public void setMediaCioPosParto(Double mediaCioPosParto) {
		this.mediaCioPosParto = mediaCioPosParto;
	}
	public int getPartoSimples() {
		return partoSimples;
	}
	public void setPartoSimples(int partoSimples) {
		this.partoSimples = partoSimples;
	}
	public int getPartoDuplo() {
		return partoDuplo;
	}
	public void setPartoDuplo(int partoDuplo) {
		this.partoDuplo = partoDuplo;
	}
	public int getPartoTriplo() {
		return partoTriplo;
	}
	public void setPartoTriplo(int partotriplo) {
		this.partoTriplo = partotriplo;
	}
	public int getPartoQuadr() {
		return partoQuadr;
	}
	public void setPartoQuadr(int partoQuadr) {
		this.partoQuadr = partoQuadr;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}	
}
