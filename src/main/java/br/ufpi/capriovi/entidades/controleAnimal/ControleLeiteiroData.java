package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ControleLeiteiroData
 *
 */
@Entity

public class ControleLeiteiroData implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idControleLeiteiroData;
	@Temporal(value=TemporalType.DATE)
	private Date dataPesagem;
	private BigDecimal pesoMatriz;
	private BigDecimal indiceGordura;
	private BigDecimal proteina;
	private BigDecimal celulasSomatica;
	private BigDecimal producao1;
	private BigDecimal producao2;
	private BigDecimal producaoDia;
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_controleLeiteiro")
	private ControleLeiteiro controleLeiteiro;

	public Long getIdControleLeiteiroData() {
		return idControleLeiteiroData;
	}

	public void setIdControleLeiteiroData(Long idControleLeiteiroData) {
		this.idControleLeiteiroData = idControleLeiteiroData;
	}

	public Date getDataPesagem() {
		return dataPesagem;
	}

	public void setDataPesagem(Date dataPesagem) {
		this.dataPesagem = dataPesagem;
	}

	public BigDecimal getPesoMatriz() {
		return pesoMatriz;
	}

	public void setPesoMatriz(BigDecimal pesoMatriz) {
		this.pesoMatriz = pesoMatriz;
	}

	public BigDecimal getIndiceGordura() {
		return indiceGordura;
	}

	public void setIndiceGordura(BigDecimal indiceGordura) {
		this.indiceGordura = indiceGordura;
	}

	public BigDecimal getProteina() {
		return proteina;
	}

	public void setProteina(BigDecimal proteina) {
		this.proteina = proteina;
	}

	public BigDecimal getCelulasSomatica() {
		return celulasSomatica;
	}

	public void setCelulasSomatica(BigDecimal celulasSomatica) {
		this.celulasSomatica = celulasSomatica;
	}

	public BigDecimal getProducao1() {
		return producao1;
	}

	public void setProducao1(BigDecimal producao1) {
		this.producao1 = producao1;
	}

	public BigDecimal getProducao2() {
		return producao2;
	}

	public void setProducao2(BigDecimal producao2) {
		this.producao2 = producao2;
	}

	public BigDecimal getProducaoDia() {
		return producaoDia;
	}

	public void setProducaoDia(BigDecimal producaoDia) {
		this.producaoDia = producaoDia;
	}

	public ControleLeiteiro getControleLeiteiro() {
		return controleLeiteiro;
	}

	public void setControleLeiteiro(ControleLeiteiro controleLeiteiro) {
		this.controleLeiteiro = controleLeiteiro;
	}
}
