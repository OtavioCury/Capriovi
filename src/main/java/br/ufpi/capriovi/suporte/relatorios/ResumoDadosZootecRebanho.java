package br.ufpi.capriovi.suporte.relatorios;

import java.util.LinkedHashMap;

import br.ufpi.capriovi.entidades.cadastros.Rebanho;

/**
 * Resumo usado para o relatório de dados zootecnicos de rebanho.
 * 
 * @author thasciano
 *
 */
public class ResumoDadosZootecRebanho {

	private Rebanho rebanho;
	private int quantAni;
	private int quantMachosV;
	private int quantMachosM;
	private int quantFemeasV;
	private int quantFemeasM;
	private double mediaPeso60;
	private double mediaPeso120;
	private double mediaPeso180;
	private double escoreMedio;
	private LinkedHashMap<String, Integer> natalidade;
	private LinkedHashMap<String, Integer> mortalidade;
	private int limiteGraf;
	private int limiteCurvaPesos;
	private int venda, morte, roubo, alimentacao, emprestimo;
	private boolean ativaGrafs = true;

	public ResumoDadosZootecRebanho(Rebanho rebanho) {
		super();
		this.rebanho = rebanho;
		this.mediaPeso60 = 0;
		this.mediaPeso120 = 0;
		this.mediaPeso180 = 0;
		this.natalidade = new LinkedHashMap<String, Integer>();
		this.mortalidade = new LinkedHashMap<String, Integer>();
	}

	public int quantMovAni() {
		return venda + morte + roubo + alimentacao + emprestimo;
	}

	public int getQuantAni() {
		return quantAni;
	}

	public void setQuantAni(int quantAni) {
		this.quantAni = quantAni;
	}

	public Rebanho getRebanho() {
		return rebanho;
	}

	public void setRebanho(Rebanho rebanho) {
		this.rebanho = rebanho;
	}

	public int getQuantMachosV() {
		return quantMachosV;
	}

	public void setQuantMachosV(int quantMachosV) {
		this.quantMachosV = quantMachosV;
	}

	public int getQuantMachosM() {
		return quantMachosM;
	}

	public void setQuantMachosM(int quantMachosM) {
		this.quantMachosM = quantMachosM;
	}

	public int getQuantFemeasV() {
		return quantFemeasV;
	}

	public void setQuantFemeasV(int quantFemeasV) {
		this.quantFemeasV = quantFemeasV;
	}

	public int getQuantFemeasM() {
		return quantFemeasM;
	}

	public void setQuantFemeasM(int quantFemeasM) {
		this.quantFemeasM = quantFemeasM;
	}

	public double getMediaPeso60() {
		return mediaPeso60;
	}

	public void setMediaPeso60(double mediaPeso60) {
		this.mediaPeso60 = mediaPeso60;
	}

	public double getMediaPeso120() {
		return mediaPeso120;
	}

	public void setMediaPeso120(double mediaPeso120) {
		this.mediaPeso120 = mediaPeso120;
	}

	public double getMediaPeso180() {
		return mediaPeso180;
	}

	public void setMediaPeso180(double mediaPeso180) {
		this.mediaPeso180 = mediaPeso180;
	}

	public double getEscoreMedio() {
		return escoreMedio;
	}

	public void setEscoreMedio(double escoreMedio) {
		this.escoreMedio = escoreMedio;
	}

	public LinkedHashMap<String, Integer> getNatalidade() {
		return natalidade;
	}

	public void setNatalidade(LinkedHashMap<String, Integer> natalidade) {
		this.natalidade = natalidade;
	}

	public LinkedHashMap<String, Integer> getMortalidade() {
		return mortalidade;
	}

	public void setMortalidade(LinkedHashMap<String, Integer> mortalidade) {
		this.mortalidade = mortalidade;
	}

	public int getLimiteGraf() {
		return limiteGraf;
	}

	public void setLimiteGraf(int limiteGraf) {
		this.limiteGraf = limiteGraf;
	}

	public int getLimiteCurvaPesos() {
		return limiteCurvaPesos;
	}

	public void setLimiteCurvaPesos(int limiteCurvaPesos) {
		this.limiteCurvaPesos = limiteCurvaPesos;
	}

	public int getVenda() {
		return venda;
	}

	public void setVenda(int venda) {
		this.venda = venda;
	}

	public int getMorte() {
		return morte;
	}

	public void setMorte(int morte) {
		this.morte = morte;
	}

	public int getRoubo() {
		return roubo;
	}

	public void setRoubo(int roubo) {
		this.roubo = roubo;
	}

	public int getAlimentacao() {
		return alimentacao;
	}

	public void setAlimentacao(int alimentacao) {
		this.alimentacao = alimentacao;
	}

	public int getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(int emprestimo) {
		this.emprestimo = emprestimo;
	}

	public boolean isAtivaGrafs() {
		return ativaGrafs;
	}

	public void setAtivaGrafs(boolean ativaGrafs) {
		this.ativaGrafs = ativaGrafs;
	}

}
