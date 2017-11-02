package br.ufpi.capriovi.entidades.relatorio;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.suporte.tiposEnum.TipoStatusVermifugaEnum;

@Entity
@Table(name = "relatorioVermifugacao")
public class RelatorioVermifugação implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	

	private Double notaVermifugacao;

	@Temporal(value = TemporalType.DATE)
	private Date data;

	@Column(name = "tipo_statusVermifuga")
	private TipoStatusVermifugaEnum statusVermifuga;

	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_animal")
	private Animal animal;

	public RelatorioVermifugação() {
		super();
	}
	public RelatorioVermifugação(Double notaVermifugacao, Date data, int codigo) {		
		this.notaVermifugacao = notaVermifugacao;
		this.data = data;
		setStatusVermifuga(TipoStatusVermifugaEnum.getEnumByCodigo(codigo));
	}
	public Double getNotaVermifugacao() {
		return notaVermifugacao;
	}
	public void setNotaVermifugacao(Double notaVermifugacao) {
		this.notaVermifugacao = notaVermifugacao;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
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
	public TipoStatusVermifugaEnum getStatusVermifuga() {
		return statusVermifuga;
	}
	public void setStatusVermifuga(TipoStatusVermifugaEnum statusVermifuga) {
		this.statusVermifuga = statusVermifuga;
	}
}
