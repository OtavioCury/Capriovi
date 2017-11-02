package br.ufpi.capriovi.suporte.relatorios;

public class ResumoGanhoGeneticoEsp {

	private String Rebanho;
	private Double diferencialSel;
	private int pesoEscolhido;
	private Double propMachos;
	private Double propFemeas;
	private int totalAniVivosReb;
	private int totalAnimaisAptos;
	private int MachosSel;
	private int FemeasSel;
	private Double herdabilidade;
	private Double intensidadeMacho;
	private Double intensidadeFemea;
	private Double DesvioPadraoCarac;
	private Double ganhoGenEsp;
	
	public ResumoGanhoGeneticoEsp() {
		super();
		// TODO Auto-generated constructor stub
	}
	public double intensidadeMedia() {
		return (intensidadeMacho + intensidadeFemea)/2;
	}
	public String getRebanho() {
		return Rebanho;
	}

	public void setRebanho(String rebanho) {
		Rebanho = rebanho;
	}

	public Double getDiferencialSel() {
		return diferencialSel;
	}

	public void setDiferencialSel(Double diferencialSel) {
		this.diferencialSel = diferencialSel;
	}

	public Double getIntensidadeMacho() {
		return intensidadeMacho;
	}

	public void setIntensidadeMacho(Double intensidadeMacho) {
		this.intensidadeMacho = intensidadeMacho;
	}

	public Double getIntensidadeFemea() {
		return intensidadeFemea;
	}

	public void setIntensidadeFemea(Double intensidadeFemea) {
		this.intensidadeFemea = intensidadeFemea;
	}

	public Double getDesvioPadraoCarac() {
		return DesvioPadraoCarac;
	}

	public void setDesvioPadraoCarac(Double desvioPadraoCarac) {
		DesvioPadraoCarac = desvioPadraoCarac;
	}

	public Double getGanhoGenEsp() {
		return ganhoGenEsp;
	}

	public void setGanhoGenEsp(Double ganhoGenEsp) {
		this.ganhoGenEsp = ganhoGenEsp;
	}

	public int getPesoEscolhido() {
		return pesoEscolhido;
	}

	public void setPesoEscolhido(int pesoEscolhido) {
		this.pesoEscolhido = pesoEscolhido;
	}

	public Double getPropMachos() {
		return propMachos;
	}

	public void setPropMachos(Double propMachos) {
		this.propMachos = propMachos;
	}

	public Double getPropFemeas() {
		return propFemeas;
	}

	public void setPropFemeas(Double propFemeas) {
		this.propFemeas = propFemeas;
	}

	public int getTotalAniVivosReb() {
		return totalAniVivosReb;
	}

	public void setTotalAniVivosReb(int totalAniVivosReb) {
		this.totalAniVivosReb = totalAniVivosReb;
	}

	public int getTotalAnimaisAptos() {
		return totalAnimaisAptos;
	}

	public void setTotalAnimaisAptos(int totalAnimaisAptos) {
		this.totalAnimaisAptos = totalAnimaisAptos;
	}

	public int getMachosSel() {
		return MachosSel;
	}

	public void setMachosSel(int machosSel) {
		MachosSel = machosSel;
	}

	public int getFemeasSel() {
		return FemeasSel;
	}

	public void setFemeasSel(int femeasSel) {
		FemeasSel = femeasSel;
	}

	public Double getHerdabilidade() {
		return herdabilidade;
	}

	public void setHerdabilidade(Double herdabilidade) {
		this.herdabilidade = herdabilidade;
	}
	
}
