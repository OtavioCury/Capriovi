package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.suporte.tiposEnum.TipoDesenvolvimentoPonderal;

/**
 * Entity implementation class for Entity: DesenvolvimentoPonderal
 *
 */
@Entity

public class DesenvolvimentoPonderal implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_animal")
	private Animal animal;

	@Temporal(value=TemporalType.DATE)
	private Date data;
	private Double peso;

	private double cpm;

	private Integer conformidade;

	private Integer precocidade;

	private Integer musculosidade;

	private Double escoreCondicaoCorporal;

	@Column(name="tipo_desenvolvimento")
	private TipoDesenvolvimentoPonderal tipoDesenvolvimento;


	public DesenvolvimentoPonderal() {
		super();
		this.conformidade = 0;
		this.precocidade = 0;
		this.musculosidade = 0;
	}

	public void setEscoreCondicaoCorporal(Double escoreCondicaoCorporal) {
		this.escoreCondicaoCorporal = escoreCondicaoCorporal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getEscoreCondicaoCorporal() {
		return escoreCondicaoCorporal;
	}

	public Integer getConformidade() {
		return conformidade;
	}

	public void setConformidade(Integer conformidade) {
		this.conformidade = conformidade;
	}

	public Integer getPrecocidade() {
		return precocidade;
	}

	public void setPrecocidade(Integer precocidade) {
		this.precocidade = precocidade;
	}

	public Integer getMusculosidade() {
		return musculosidade;
	}

	public void setMusculosidade(Integer musculosidade) {
		this.musculosidade = musculosidade;
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
		DesenvolvimentoPonderal other = (DesenvolvimentoPonderal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public double getCpm() {
		return cpm;
	}

	public void setCpm(double cpm) {
		this.cpm = cpm;
	}

	public TipoDesenvolvimentoPonderal getTipoDesenvolvimento() {
		return tipoDesenvolvimento;
	}

	public void setTipoDesenvolvimento(TipoDesenvolvimentoPonderal tipoDesenvolvimento) {
		this.tipoDesenvolvimento = tipoDesenvolvimento;
	}

}
