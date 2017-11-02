package br.ufpi.capriovi.suporte.relatorios;

import br.ufpi.capriovi.entidades.cadastros.Animal;

public class ResumoAnimalIndice {
	
	private Animal animal;
	private Double pesoAjustado;
	private Double idadePrimParto;
	private Double pesoTotCriasNasc;
	private Double pesoTotCriasDesm;
	
	private Double mediaPeso;
	private Double mediaIPP;
	private Double mediaPTCN;
	private Double mediaPTCD;
	
	private Double pesoCorrigido;
	private Double ippCorrigido;
	private Double ptcnCorrigido;
	private Double ptcdCorrigido;
	
	public ResumoAnimalIndice(Animal animal, Double pesoAjustado, 
			Double idadePrimParto, Double pesoTotCriasNasc, Double pesoTotCriasDesm) {
		super();
		this.animal = animal;
		this.pesoAjustado = pesoAjustado;
		this.idadePrimParto = idadePrimParto;
		this.pesoTotCriasNasc = pesoTotCriasNasc;
		this.pesoTotCriasDesm = pesoTotCriasDesm;
	}
	
	public Animal getAnimal() {
		return animal;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	public Double getPesoAjustado() {
		return pesoAjustado;
	}
	public void setPesoAjustado(Double pesoAjustado) {
		this.pesoAjustado = pesoAjustado;
	}
	
	public Double getIdadePrimParto() {
		return idadePrimParto;
	}

	public void setIdadePrimParto(Double idadePrimParto) {
		this.idadePrimParto = idadePrimParto;
	}

	public Double getPesoTotCriasNasc() {
		return pesoTotCriasNasc;
	}
	public void setPesoTotCriasNasc(Double pesoTotCriasNasc) {
		this.pesoTotCriasNasc = pesoTotCriasNasc;
	}
	public Double getPesoTotCriasDesm() {
		return pesoTotCriasDesm;
	}
	public void setPesoTotCriasDesm(Double pesoTotCriasDesm) {
		this.pesoTotCriasDesm = pesoTotCriasDesm;
	}

	public Double getMediaPeso() {
		return mediaPeso;
	}

	public void setMediaPeso(Double mediaPeso) {
		this.mediaPeso = mediaPeso;
	}

	public Double getMediaIPP() {
		return mediaIPP;
	}

	public void setMediaIPP(Double mediaIPP) {
		this.mediaIPP = mediaIPP;
	}

	public Double getMediaPTCN() {
		return mediaPTCN;
	}

	public void setMediaPTCN(Double mediaPTCN) {
		this.mediaPTCN = mediaPTCN;
	}

	public Double getMediaPTCD() {
		return mediaPTCD;
	}

	public void setMediaPTCD(Double mediaPTCD) {
		this.mediaPTCD = mediaPTCD;
	}

	public Double getPesoCorrigido() {
		return pesoCorrigido;
	}

	public void setPesoCorrigido(Double pesoCorrigido) {
		this.pesoCorrigido = pesoCorrigido;
	}

	public Double getIppCorrigido() {
		return ippCorrigido;
	}

	public void setIppCorrigido(Double ippCorrigido) {
		this.ippCorrigido = ippCorrigido;
	}

	public Double getPtcnCorrigido() {
		return ptcnCorrigido;
	}

	public void setPtcnCorrigido(Double ptcnCorrigido) {
		this.ptcnCorrigido = ptcnCorrigido;
	}

	public Double getPtcdCorrigido() {
		return ptcdCorrigido;
	}

	public void setPtcdCorrigido(Double ptcdCorrigido) {
		this.ptcdCorrigido = ptcdCorrigido;
	}

	@Override
	public String toString() {
		return "ResumoAnimalIndice [animal=" + animal.getNomeNumero() + ", pesoAjustado=" + pesoAjustado + ", idadePrimParto="
				+ idadePrimParto + ", pesoTotCriasNasc=" + pesoTotCriasNasc + ", pesoTotCriasDesm=" + pesoTotCriasDesm
				+ ", mediaPeso=" + mediaPeso + ", mediaIPP=" + mediaIPP + ", mediaPTCN=" + mediaPTCN + ", mediaPTCD="
				+ mediaPTCD + ", pesoCorrigido=" + pesoCorrigido + ", ippCorrigido=" + ippCorrigido + ", ptcnCorrigido="
				+ ptcnCorrigido + ", ptcdCorrigido=" + ptcdCorrigido + "]";
	}
	
}
