package br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util;

import javax.ejb.Stateless;


@Stateless
public class InputNsgaII {
	
	private int tipo;
	
	private double herdabilidade;
	private double idadeAcasalamentoMachos;
	private double idadeAcasalamentoFemeas;
	private int diasPesagem;
	private double percentualMachos;
	private double percentualFemeas;
	private double proporcaoMachoFemea;
	private double mediaCaracteristica;
	private double desvioPadraoCaracteristica;
	private double intensidadeSelecaoMachos;
	private double intensidadeSelecaoFemeas;
	private int quantCarac;
	
	public InputNsgaII(int tipo, double herdabilidade,
			double idadeAcasalamentoMachos, double idadeAcasalamentoFemeas,
			int diasPesagem, double percentualMachos,
			double percentualFemeas, double proporcaoMachoFemea,
			double mediaCaracteristica, double desvioPadraoCaracteristica,
			double intensidadeSelecaoMachos, double intensidadeSelecaoFemeas) {
		super();
		this.tipo = tipo;
		this.herdabilidade = herdabilidade;
		this.idadeAcasalamentoMachos = idadeAcasalamentoMachos;
		this.idadeAcasalamentoFemeas = idadeAcasalamentoFemeas;
		this.diasPesagem = diasPesagem;
		this.percentualMachos = percentualMachos;
		this.percentualFemeas = percentualFemeas;
		this.proporcaoMachoFemea = proporcaoMachoFemea;
		this.mediaCaracteristica = mediaCaracteristica;
		this.desvioPadraoCaracteristica = desvioPadraoCaracteristica;
		this.intensidadeSelecaoMachos = intensidadeSelecaoMachos;
		this.intensidadeSelecaoFemeas = intensidadeSelecaoFemeas;
	}
	

	public InputNsgaII() {
		super();
	}


	public InputNsgaII(Double idadeMinimaM, Double idadeMinimaF,
			Double herdabilidade2) {
	}

	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public double getHerdabilidade() {
		return herdabilidade;
	}
	public void setHerdabilidade(double herdabilidade) {
		this.herdabilidade = herdabilidade;
	}
	public double getIdadeAcasalamentoMachos() {
		return idadeAcasalamentoMachos;
	}
	public void setIdadeAcasalamentoMachos(double idadeAcasalamentoMachos) {
		this.idadeAcasalamentoMachos = idadeAcasalamentoMachos;
	}
	public double getIdadeAcasalamentoFemeas() {
		return idadeAcasalamentoFemeas;
	}
	public void setIdadeAcasalamentoFemeas(double idadeAcasalamentoFemeas) {
		this.idadeAcasalamentoFemeas = idadeAcasalamentoFemeas;
	}
	public int getDiasPesagem() {
		return diasPesagem;
	}
	public void setDiasPesagem(int diasPesagem) {
		this.diasPesagem = diasPesagem;
	}
	public double getPercentualMachos() {
		return percentualMachos;
	}
	public void setPercentualMachos(double percentualMachos) {
		this.percentualMachos = percentualMachos;
	}
	public double getPercentualFemeas() {
		return percentualFemeas;
	}
	public void setPercentualFemeas(double percentualFemeas) {
		this.percentualFemeas = percentualFemeas;
	}
	public double getProporcaoMachoFemea() {
		return proporcaoMachoFemea;
	}
	public void setProporcaoMachoFemea(double proporcaoMachoFemea) {
		this.proporcaoMachoFemea = proporcaoMachoFemea;
	}
	public double getMediaCaracteristica() {
		return mediaCaracteristica;
	}
	public void setMediaCaracteristica(double mediaCaracteristica) {
		this.mediaCaracteristica = mediaCaracteristica;
	}
	public double getDesvioPadraoCaracteristica() {
		return desvioPadraoCaracteristica;
	}
	public void setDesvioPadraoCaracteristica(double desvioPadraoCaracteristica) {
		this.desvioPadraoCaracteristica = desvioPadraoCaracteristica;
	}
	public double getIntensidadeSelecaoMachos() {
		return intensidadeSelecaoMachos;
	}
	public void setIntensidadeSelecaoMachos(double intensidadeSelecaoMachos) {
		this.intensidadeSelecaoMachos = intensidadeSelecaoMachos;
	}
	public double getIntensidadeSelecaoFemeas() {
		return intensidadeSelecaoFemeas;
	}
	public void setIntensidadeSelecaoFemeas(double intensidadeSelecaoFemeas) {
		this.intensidadeSelecaoFemeas = intensidadeSelecaoFemeas;
	}
	public int getQuantCarac() {
		return quantCarac;
	}
	public void setQuantCarac(int quantCarac) {
		this.quantCarac = quantCarac;
	}
}

