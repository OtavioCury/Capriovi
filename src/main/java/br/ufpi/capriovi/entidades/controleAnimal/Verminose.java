package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufpi.capriovi.entidades.cadastros.Animal;

/**
 * Entity implementation class for Entity: Verminose
 *
 */
@Entity

public class Verminose implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_animal")
	private Animal animal;

	private Integer ovosPorGramaDeFazes;

	private Integer famacha;

	private Date data;
	
	private Boolean pelosArrepiados;

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

	public Integer getOvosPorGramaDeFazes() {
		return ovosPorGramaDeFazes;
	}

	public void setOvosPorGramaDeFazes(Integer ovosPorGramaDeFazes) {
		this.ovosPorGramaDeFazes = ovosPorGramaDeFazes;
	}

	public Integer getFamacha() {
		return famacha;
	}

	public void setFamacha(Integer famacha) {
		this.famacha = famacha;
	}

	public Date getData() {
		return data;
	}

	public Boolean getPelosArrepiados() {
		return pelosArrepiados;
	}

	public void setPelosArrepiados(Boolean pelosArrepiados) {
		this.pelosArrepiados = pelosArrepiados;
	}

	public void setData(Date data) {
		this.data = data;
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
		Verminose other = (Verminose) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
