package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.suporte.tiposEnum.TipoMotivoSaidaEnum;

/**
 * Entity implementation class for Entity: MovimentacaoAnimal
 *
 */
@Entity

public class MovimentacaoAnimal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_animal")
	private Animal animal;

	@Temporal(value=TemporalType.DATE)
	private Date data;

	private BigDecimal peso;

	private String observacao;
	
	@Column(name = "tipo_motivo")
	private TipoMotivoSaidaEnum motivoSaida;
		
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_rebanho")	
	private Rebanho rebanho;
	
	public MovimentacaoAnimal() {
		this.animal = new Animal();
		this.rebanho = new Rebanho();
	}

	public Rebanho getRebanho() {
		return rebanho;
	}


	public void setRebanho(Rebanho rebanho) {
		this.rebanho = rebanho;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long idMovimentacaoAnimal) {
		this.id = idMovimentacaoAnimal;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public TipoMotivoSaidaEnum getMotivoSaida() {
		return motivoSaida;
	}

	public void setMotivoSaida(TipoMotivoSaidaEnum motivoSaida) {
		this.motivoSaida = motivoSaida;
	}

	public Integer getTipoSaida() {
		if (motivoSaida != null)
			return motivoSaida.getCodigo();
		return -1;
	}

	public void setTipoSaida(Integer codigo) {
		this.motivoSaida = TipoMotivoSaidaEnum.getEnumByCodigo(codigo);
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovimentacaoAnimal other = (MovimentacaoAnimal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
