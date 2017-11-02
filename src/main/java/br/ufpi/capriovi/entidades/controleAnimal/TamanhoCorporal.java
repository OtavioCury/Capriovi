package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufpi.capriovi.entidades.cadastros.Animal;

/**
 * Entity implementation class for Entity: TamanhoCorporal
 *
 */
@Entity

public class TamanhoCorporal implements Serializable {


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

	private Double alturaCernelha;

	private Double alturaGarupa;

	private Double comprimentoCorporal;

	private Double alturaPerna;

	private Double perimetroCanela;

	private Double comprimentoPerna;

	private Double perimetroPerna;

	private Double circunferenciaToracica;

	private Double comprimentoOrelha;

	private Double comprimentoCabeca;

	private Double longitudeRosto;

	private Double larguraIleos;

	private Double larguraIsquios;

	private Double comprimentoGarupa;

	private Double comprimentoCauda;

	private Double perimetroCauda;	

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

	public Double getAlturaCernelha() {
		return alturaCernelha;
	}

	public void setAlturaCernelha(Double alturaCernelha) {
		this.alturaCernelha = alturaCernelha;
	}

	public Double getAlturaGarupa() {
		return alturaGarupa;
	}

	public void setAlturaGarupa(Double alturaGarupa) {
		this.alturaGarupa = alturaGarupa;
	}

	public Double getComprimentoCorporal() {
		return comprimentoCorporal;
	}

	public void setComprimentoCorporal(Double comprimentoCorporal) {
		this.comprimentoCorporal = comprimentoCorporal;
	}

	public Double getAlturaPerna() {
		return alturaPerna;
	}

	public void setAlturaPerna(Double alturaPerna) {
		this.alturaPerna = alturaPerna;
	}

	public Double getPerimetroCanela() {
		return perimetroCanela;
	}

	public void setPerimetroCanela(Double perimetroCanela) {
		this.perimetroCanela = perimetroCanela;
	}

	public Double getComprimentoPerna() {
		return comprimentoPerna;
	}

	public void setComprimentoPerna(Double comprimentoPerna) {
		this.comprimentoPerna = comprimentoPerna;
	}

	public Double getPerimetroPerna() {
		return perimetroPerna;
	}

	public void setPerimetroPerna(Double perimetroPerna) {
		this.perimetroPerna = perimetroPerna;
	}

	public Double getCircunferenciaToracica() {
		return circunferenciaToracica;
	}

	public void setCircunferenciaToracica(Double circunferenciaToracica) {
		this.circunferenciaToracica = circunferenciaToracica;
	}

	public Double getComprimentoOrelha() {
		return comprimentoOrelha;
	}

	public void setComprimentoOrelha(Double comprimentoOrelha) {
		this.comprimentoOrelha = comprimentoOrelha;
	}

	public Double getComprimentoCabeca() {
		return comprimentoCabeca;
	}

	public void setComprimentoCabeca(Double comprimentoCabeca) {
		this.comprimentoCabeca = comprimentoCabeca;
	}

	public Double getLongitudeRosto() {
		return longitudeRosto;
	}

	public void setLongitudeRosto(Double longitudeRosto) {
		this.longitudeRosto = longitudeRosto;
	}

	public Double getLarguraIleos() {
		return larguraIleos;
	}

	public void setLarguraIleos(Double larguraIleos) {
		this.larguraIleos = larguraIleos;
	}

	public Double getLarguraIsquios() {
		return larguraIsquios;
	}

	public void setLarguraIsquios(Double larguraIsquios) {
		this.larguraIsquios = larguraIsquios;
	}

	public Double getComprimentoGarupa() {
		return comprimentoGarupa;
	}

	public void setComprimentoGarupa(Double comprimentoGarupa) {
		this.comprimentoGarupa = comprimentoGarupa;
	}

	public Double getComprimentoCauda() {
		return comprimentoCauda;
	}

	public void setComprimentoCauda(Double comprimentoCauda) {
		this.comprimentoCauda = comprimentoCauda;
	}

	public Double getPerimetroCauda() {
		return perimetroCauda;
	}

	public void setPerimetroCauda(Double perimetroCauda) {
		this.perimetroCauda = perimetroCauda;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		TamanhoCorporal other = (TamanhoCorporal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
