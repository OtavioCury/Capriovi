package br.ufpi.capriovi.entidades.administracao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ControlePagamento
 *
 */
@Entity

public class ControlePagamento implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	private Pecuarista pecuarista;
	private Double valorPagarUnidade;
	private int competencia;
	private boolean status;
	
	@OneToMany(mappedBy = "controlePagamento", cascade = CascadeType.REMOVE)
	private Set<HistoricoPagamento> historico;

	public Long getId() {
		return id;
	}

	public void setId(Long idControlePagamento) {
		this.id = idControlePagamento;
	}

//	public Pecuarista getPecuarista() {
//		return pecuarista;
//	}
//
//	public void setPecuarista(Pecuarista pecuarista) {
//		this.pecuarista = pecuarista;
//	}

	public ControlePagamento() {
		this.historico = new HashSet<HistoricoPagamento>();
	}

	public Double getValorPagarUnidade() {
		return valorPagarUnidade;
	}

	public void setValorPagarUnidade(Double valorPagarUnidade) {
		this.valorPagarUnidade = valorPagarUnidade;
	}

	public int getCompetencia() {
		return competencia;
	}

	public void setCompetencia(int competencia) {
		this.competencia = competencia;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<HistoricoPagamento> getHistorico() {
		return historico;
	}

	public void setHistorico(Set<HistoricoPagamento> historico) {
		this.historico = historico;
	}
   
}
