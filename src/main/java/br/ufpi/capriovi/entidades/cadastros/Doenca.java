package br.ufpi.capriovi.entidades.cadastros;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;

/**
 * Entity implementation class for Entity: Doenca
 *
 */
@Entity
public class Doenca implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private boolean geral; //se a doença foi cadastrada por um usuário ou ela tem que aparecer para todos

	@Lob
	@Column(length=512)
	private String causa;
		
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Lob
	@Column(length=512)
	private String sintomas;

	@Lob
	@Column(length=1000)
	private String profilaxia;

	@Lob
	@Column(length=512)
	private String tratamento;
	
	@JsonIgnore
	@OneToMany(mappedBy = "doenca", cascade = CascadeType.REMOVE)
	private Set<OcorrenciaClinica> ocorrenciaClinica;

	public Doenca() {
		this.ocorrenciaClinica = new HashSet<OcorrenciaClinica>();
		usuario = new Usuario();
	}


	public Set<OcorrenciaClinica> getOcorrenciaClinica() {
		return ocorrenciaClinica;
	}

	public void setOcorrenciaClinica(Set<OcorrenciaClinica> ocorrenciaClinica) {
		this.ocorrenciaClinica = ocorrenciaClinica;
	}

	public Long getId() {
		return id;
	}

	public String getProfilaxia() {
		return profilaxia;
	}


	public void setProfilaxia(String profilaxia) {
		this.profilaxia = profilaxia;
	}


	public void setId(Long idDoenca) {
		this.id = idDoenca;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCausa() {
		return causa;
	}


	public void setCausa(String causa) {
		this.causa = causa;
	}


	public String getSintomas() {
		return sintomas;
	}


	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}


	public String getTratamento() {
		return tratamento;
	}


	public void setTratamento(String tratamento) {
		this.tratamento = tratamento;
	}


	public boolean isGeral() {
		return geral;
	}


	public void setGeral(boolean geral) {
		this.geral = geral;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		Doenca other = (Doenca) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
