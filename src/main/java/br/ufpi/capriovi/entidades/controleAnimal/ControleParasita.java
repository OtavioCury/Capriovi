package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Medicamento;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;

/**
 * Entity implementation class for Entity: ControleParazita
 *
 */
@Entity

public class ControleParasita implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_animal")
	private Animal animal;

	@Temporal(value=TemporalType.DATE)
	private Date dataVernifugacao;

	private BigDecimal nivelPre;

	private BigDecimal nivelPos;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_medicamento")
	private Medicamento medicamento;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_rebanho")	
	private Rebanho rebanho;

	private Integer famacha;

	private Double escoreCorporal;

	public String observacao;

	public ControleParasita() {
		this.animal = new Animal();
		this.rebanho = new Rebanho();
		this.medicamento = new Medicamento();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long idControleParazita) {
		this.id = idControleParazita;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Date getDataVernifugacao() {
		return dataVernifugacao;
	}

	public void setDataVernifugacao(Date dataVernifugacao) {
		this.dataVernifugacao = dataVernifugacao;
	}

	public BigDecimal getNivelPre() {
		return nivelPre;
	}

	public void setNivelPre(BigDecimal nivelPre) {
		this.nivelPre = nivelPre;
	}

	public BigDecimal getNivelPos() {
		return nivelPos;
	}

	public void setNivelPos(BigDecimal nivelPos) {
		this.nivelPos = nivelPos;
	}

	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

	public Double getEscoreCorporal() {
		return escoreCorporal;
	}

	public void setEscoreCorporal(Double escoreCorporal) {
		this.escoreCorporal = escoreCorporal;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Rebanho getRebanho() {
		return rebanho;
	}

	public void setRebanho(Rebanho rebanho) {
		this.rebanho = rebanho;
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
		ControleParasita other = (ControleParasita) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getFamacha() {
		return famacha;
	}

	public void setFamacha(Integer famacha) {
		this.famacha = famacha;
	}

}
