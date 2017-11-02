package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Medicamento;
import br.ufpi.capriovi.entidades.cadastros.Usuario;

/**
 * Entity implementation class for Entity: Vacinacao
 *
 */
@Entity

public class Vacinacao implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany(fetch = FetchType.EAGER,
	        cascade =
	        {	                
	                CascadeType.MERGE,
	                CascadeType.REFRESH	                
	        },
	        targetEntity = Animal.class)
	@JoinTable(name = "animal_vacinacao",
	        inverseJoinColumns = @JoinColumn(name = "vacinacao_id",
	                nullable = false,
	                updatable = false),
	        joinColumns = @JoinColumn(name = "animal_id",
	                nullable = false,
	                updatable = false),
	        foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
	        inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
	private List<Animal> animais;	

	@Temporal(value=TemporalType.DATE)
	private Date data;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})	
	@JoinColumn(name = "id_medicamento")
	private Medicamento medicamento;

	private String observacao;

	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;

	public Vacinacao() {
		this.medicamento = new Medicamento();
		animais = new ArrayList<Animal>();		
		usuario = new Usuario();
	}

	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long idVacinacao) {
		this.id = idVacinacao;
	}	

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
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
		Vacinacao other = (Vacinacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
