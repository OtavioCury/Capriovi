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

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;

/**
 * Entity implementation class for Entity: OcorrenciaClinica
 *
 */
@Entity

public class OcorrenciaClinica implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_animal")
	private Animal animal;
	
	@Temporal(value=TemporalType.DATE)
	private Date data;
		
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_doenca")	
	private Doenca doenca;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_rebanho")	
	private Rebanho rebanho;

	private String observacao;

	public OcorrenciaClinica() {
		this.animal = new Animal();
		this.rebanho = new Rebanho();
		this.doenca = new Doenca();
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

	public void setId(Long idOcorrenciasClinicas) {
		this.id = idOcorrenciasClinicas;
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

	public Doenca getDoenca() {
		return doenca;
	}

	public void setDoenca(Doenca doenca) {
		this.doenca = doenca;
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
		OcorrenciaClinica other = (OcorrenciaClinica) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
   
	
}
