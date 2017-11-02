package br.ufpi.capriovi.entidades.administracao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: HistoricoPagamento
 *
 */
@Entity

public class HistoricoPagamento implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double valorPagarUnidade;
	private Double valorPagarTotal;
	private int quantUnidade;
	
	@Temporal(value=TemporalType.DATE)
	private Date dataPagamento;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_controlePagamento")
	private ControlePagamento controlePagamento;
	private boolean status;

	public Long getId() {
		return id;
	}

	public Double getValorPagarUnidade() {
		return valorPagarUnidade;
	}

	public void setValorPagarUnidade(Double valorPagarUnidade) {
		this.valorPagarUnidade = valorPagarUnidade;
	}

	public Double getValorPagarTotal() {
		return valorPagarTotal;
	}

	public void setValorPagarTotal(Double valorPagarTotal) {
		this.valorPagarTotal = valorPagarTotal;
	}

	public int getQuantUnidade() {
		return quantUnidade;
	}

	public void setQuantUnidade(int quantUnidade) {
		this.quantUnidade = quantUnidade;
	}

	public void setId(Long idHistorico) {
		this.id = idHistorico;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public ControlePagamento getControlePagamento() {
		return controlePagamento;
	}

	public void setControlePagamento(ControlePagamento controlePagamento) {
		this.controlePagamento = controlePagamento;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
