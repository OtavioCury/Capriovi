package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufpi.capriovi.entidades.cadastros.Animal;

/**
 * Entity implementation class for Entity: Carcaca
 *
 */
@Entity
public class Carcaca implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2885555929182603006L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_animal")
	private Animal animal;
	@Temporal(value=TemporalType.DATE)
	private Date data;

	private Double areaDeOlhoLombo;
	private Double compDeOlhoLombo;
	private Double profDeOlhoLombo;
	private Double marmoDeOlhoLombo;
	private Double espessuraGorduraSub;
	private Double espessuraGorduraBic;
	private Double espessuraGorduraEst;
	
	public Carcaca() {
		super();
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
	public Double getAreaDeOlhoLombo() {
		return areaDeOlhoLombo;
	}
	public void setAreaDeOlhoLombo(Double areaDeOlhoLombo) {
		this.areaDeOlhoLombo = areaDeOlhoLombo;
	}
	public Double getCompDeOlhoLombo() {
		return compDeOlhoLombo;
	}
	public void setCompDeOlhoLombo(Double compDeOlhoLombo) {
		this.compDeOlhoLombo = compDeOlhoLombo;
	}
	public Double getProfDeOlhoLombo() {
		return profDeOlhoLombo;
	}
	public void setProfDeOlhoLombo(Double profDeOlhoLombo) {
		this.profDeOlhoLombo = profDeOlhoLombo;
	}
	public Double getMarmoDeOlhoLombo() {
		return marmoDeOlhoLombo;
	}
	public void setMarmoDeOlhoLombo(Double marmoDeOlhoLombo) {
		this.marmoDeOlhoLombo = marmoDeOlhoLombo;
	}
	public Double getEspessuraGorduraSub() {
		return espessuraGorduraSub;
	}
	public void setEspessuraGorduraSub(Double espessuraGorduraSub) {
		this.espessuraGorduraSub = espessuraGorduraSub;
	}
	public Double getEspessuraGorduraBic() {
		return espessuraGorduraBic;
	}
	public void setEspessuraGorduraBic(Double espessuraGorduraBic) {
		this.espessuraGorduraBic = espessuraGorduraBic;
	}
	public Double getEspessuraGorduraEst() {
		return espessuraGorduraEst;
	}
	public void setEspessuraGorduraEst(Double espessuraGorduraEst) {
		this.espessuraGorduraEst = espessuraGorduraEst;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((animal == null) ? 0 : animal.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		Carcaca other = (Carcaca) obj;
		if (animal == null) {
			if (other.animal != null)
				return false;
		} else if (!animal.equals(other.animal))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
