package br.ufpi.capriovi.entidades.cadastros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufpi.capriovi.entidades.controleAnimal.Vacinacao;



/**
 * Entity implementation class for Entity: PrincipioAtivo
 *
 */
@Entity

public class Medicamento implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String descricao;
	
	@JsonIgnore
	@OneToMany(mappedBy = "medicamento", cascade = CascadeType.REMOVE)
	private List<Vacinacao> vacinacao;
		
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private Usuario usuario;	

	public Medicamento() {
		this.vacinacao = new ArrayList<Vacinacao>();
		usuario = new Usuario();
	}

	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public List<Vacinacao> getVacinacao() {
		return vacinacao;
	}

	public void setVacinacao(List<Vacinacao> vacinacao) {
		this.vacinacao = vacinacao;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long idPrincipioAtivo) {
		this.id = idPrincipioAtivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		Medicamento other = (Medicamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



}
