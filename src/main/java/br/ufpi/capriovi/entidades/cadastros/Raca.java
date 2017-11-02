package br.ufpi.capriovi.entidades.cadastros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

import br.ufpi.capriovi.suporte.tiposEnum.TipoCriacaoEnum;

/**
 * Entity implementation class for Entity: Raca
 *
 */
@Entity

public class Raca implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 

	private boolean geral; //se a raça foi cadastrada por um usuário ou ela tem que aparecer para todos
	
	@JsonIgnore	
	@OneToMany(mappedBy = "raca", cascade = CascadeType.REMOVE)
	private List<Animal> animal;
		
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Lob
	@Column(length=512)
	private String origem;

	@Lob
	@Column(length=512)
	private String aspectoGeral;

	@Lob
	private byte[] foto;

	private String nome;

	@Column(name="tipo_criacao")
	private TipoCriacaoEnum criacao;

	public Raca() {
		animal = new ArrayList<Animal>();
		usuario = new Usuario();
	}

	public TipoCriacaoEnum getCriacao() {
		return criacao;
	}

	public void setCriacao(TipoCriacaoEnum criacao) {
		this.criacao = criacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
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

	public List<Animal> getAnimal() {
		return animal;
	}

	public void setAnimal(List<Animal> animal) {
		this.animal = animal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long idRaca) {
		this.id = idRaca;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getAspectoGeral() {
		return aspectoGeral;
	}

	public void setAspectoGeral(String aspectoGeral) {
		this.aspectoGeral = aspectoGeral;
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
		Raca other = (Raca) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



}
