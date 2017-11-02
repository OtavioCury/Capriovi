package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.suporte.tiposEnum.TipoParicao;
import br.ufpi.capriovi.suporte.tiposEnum.TipoPartoEnum;

/**
 * Entity implementation class for Entity: ManejoReprodutivo
 *
 */
@Entity

public class ManejoReprodutivo implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_rebanho")	
	private Rebanho rebanho;
	
	@ManyToOne(cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_mae")
	private Animal matriz;

	@Temporal(value=TemporalType.DATE)
	private Date dataDaCobertura;

	private BigDecimal pesoMatriz;

	@ManyToOne(cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_pai")	
	private Animal reprodutor;

	@Temporal(value=TemporalType.DATE)	
	private Date dataParto;
	@Column(name = "tipo_paricao")
	private TipoParicao paricao;
	@Column(name = "tipo_parto")
	private TipoPartoEnum parto;
	private Integer criasMacho;
	private Integer criasFemea;
	private int gestacao;

	@Temporal(value=TemporalType.DATE)	
	private Date desmame;
	private int amamentacao;

	@Temporal(value=TemporalType.DATE)
	private Date dataCioPosParto;

	private Double scoreCorporalmatriz;
	private String observacao;

	private Double pesoTotal;

	private Double perimetroEscrotalReprodutor;

	@Transient
	private Double media; //media perímetro escrotal (relatório)

	@Transient
	private Double soma; //quantidade de mensurações (relatório)	

	@Transient
	private Date previsaoParto; //data da previsão de parto (relatório)

	public ManejoReprodutivo() {
		this.matriz = new Animal();
		this.reprodutor = new Animal();
		this.rebanho = new Rebanho();
		this.criasFemea = new Integer(0);
		this.criasMacho = new Integer(0);

	}

	public Long getId() {
		return id;
	}

	public void setId(Long idManejoReprodutivo) {
		this.id = idManejoReprodutivo;
	}

	public Rebanho getRebanho() {
		return rebanho;
	}

	public void setRebanho(Rebanho rebanho) {
		this.rebanho = rebanho;
	}

	public Animal getMatriz() {
		return matriz;
	}

	public void setMatriz(Animal animal) {
		this.matriz = animal;
	}

	public Date getDataDaCobertura() {
		return dataDaCobertura;
	}

	public void setDataDaCobertura(Date dataDaCobertura) {
		this.dataDaCobertura = dataDaCobertura;
	}

	public BigDecimal getPesoMatriz() {
		return pesoMatriz;
	}

	public void setPesoMatriz(BigDecimal pesoMatriz) {
		this.pesoMatriz = pesoMatriz;
	}

	public Animal getReprodutor() {
		return reprodutor;
	}

	public void setReprodutor(Animal reprodutor) {
		this.reprodutor = reprodutor;
	}

	public Date getDataParto() {
		return dataParto;
	}

	public void setDataParto(Date dataParto) {
		this.dataParto = dataParto;
	}

	public Date getDataCioPosParto() {
		return dataCioPosParto;
	}

	public Integer getCriasMacho() {
		return criasMacho;
	}

	public void setCriasMacho(Integer criasMacho) {
		this.criasMacho = criasMacho;
	}

	public Integer getCriasFemea() {
		return criasFemea;
	}

	public void setCriasFemea(Integer criasFemea) {
		this.criasFemea = criasFemea;
	}

	public void setDataCioPosParto(Date dataCioPosParto) {
		this.dataCioPosParto = dataCioPosParto;
	}

	public int getGestacao() {
		return gestacao;
	}

	public void setGestacao(int gestacao) {
		this.gestacao = gestacao;
	}

	public Date getDesmame() {
		return desmame;
	}

	public void setDesmame(Date desmame) {
		this.desmame = desmame;
	}

	public int getAmamentacao() {
		return amamentacao;
	}

	public void setAmamentacao(int amamentacao) {
		this.amamentacao = amamentacao;
	}

	public Double getScoreCorporalmatriz() {
		return scoreCorporalmatriz;
	}

	public void setScoreCorporalmatriz(Double scoreCorporalmatriz) {
		this.scoreCorporalmatriz = scoreCorporalmatriz;
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

	public TipoParicao getParicao() {
		return paricao;
	}

	public void setParicao(TipoParicao paricao) {
		this.paricao = paricao;
	}

	public TipoPartoEnum getParto() {
		return parto;
	}

	public void setParto(TipoPartoEnum parto) {
		this.parto = parto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManejoReprodutivo other = (ManejoReprodutivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Double getPesoTotal() {
		return pesoTotal;
	}

	public void setPesoTotal(Double pesoTotal) {
		this.pesoTotal = pesoTotal;
	}

	public void atualizaMacho(){
		this.criasMacho++;
	}

	public void atualizaFemea() {
		this.criasFemea++;
	}

	public Double getPerimetroEscrotalReprodutor() {
		return perimetroEscrotalReprodutor;
	}

	public void setPerimetroEscrotalReprodutor(Double perimetroEscrotalReprodutor) {
		this.perimetroEscrotalReprodutor = perimetroEscrotalReprodutor;
	}

	public Double getMedia() {
		return media;
	}

	public void setMedia(Double media) {
		this.media = media;
	}

	public Double getSoma() {
		return soma;
	}

	public Date getPrevisaoParto() {
		return previsaoParto;
	}

	public void setPrevisaoParto(Date previsaoParto) {
		this.previsaoParto = previsaoParto;
	}

	public void setSoma(Double soma) {
		this.soma = soma;
	}

}
