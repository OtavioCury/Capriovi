package br.ufpi.capriovi.entidades.controleAnimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import br.ufpi.capriovi.entidades.cadastros.Animal;

/**
 * Entity implementation class for Entity: ControleLeiteiro
 *
 */
@Entity

public class ControleLeiteiro implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idControleLeiteiro;
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_animal")
	private Animal animal;
	@Temporal(value=TemporalType.DATE)
	private Date dataParto;
	@Temporal(value=TemporalType.DATE)
	private Date dataSecagem;
	private int quantControles;
	private BigDecimal producaoTotal;
	//private TipoStatusControle status;
	@OneToMany(mappedBy = "controleLeiteiro", cascade = CascadeType.REMOVE)
	private Set<ControleLeiteiroData> listControle;

	public ControleLeiteiro() {
		listControle = new HashSet<ControleLeiteiroData>();
	}

	public Long getIdControleLeiteiro() {
		return idControleLeiteiro;
	}

	public void setIdControleLeiteiro(Long idControleLeiteiro) {
		this.idControleLeiteiro = idControleLeiteiro;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Date getDataParto() {
		return dataParto;
	}

	public void setDataParto(Date dataParto) {
		this.dataParto = dataParto;
	}

	public Date getDataSecagem() {
		return dataSecagem;
	}

	public void setDataSecagem(Date dataSecagem) {
		this.dataSecagem = dataSecagem;
	}

	public int getQuantControles() {
		return quantControles;
	}

	public void setQuantControles(int quantControles) {
		this.quantControles = quantControles;
	}

	public BigDecimal getProducaoTotal() {
		return producaoTotal;
	}

	public void setProducaoTotal(BigDecimal producaoTotal) {
		this.producaoTotal = producaoTotal;
	}

	//	public TipoStatusControle getStatus() {
	//		return status;
	//	}
	//
	//	public void setStatus(TipoStatusControle status) {
	//		this.status = status;
	//	}
	//
	//	public Integer getTipoStatus() {
	//		if (status != null)
	//			return status.getCodigo();
	//		return 1;
	//	}
	//
	//	public void setTipoStatus(Integer codigo) {
	//		this.status = TipoStatusControle.getEnumByCodigo(codigo);
	//	}

	public Set<ControleLeiteiroData> getListControle() {
		return listControle;
	}

	public void setListControle(Set<ControleLeiteiroData> listControle) {
		this.listControle = listControle;
	}

	public void inserirControle(){

	}

	public void finalizarControle(Date dataSecagem) {
		if (dataSecagem.before(this.dataParto))
			throw new RuntimeException(
					"A Data da Secagem deve ser maior que as Datas de Pesagem desse controle.");

		if (this.listControle.size() == 0)
			throw new RuntimeException(
					"Não foi inserido nenhum controle para este animal.");

		for (ControleLeiteiroData controle : getListControle()) {
			if (dataSecagem.before(controle.getDataPesagem()))
				throw new RuntimeException(
						"A Data da Secagem deve ser maior que todas as Datas de Pesagem.");
		}

		this.dataSecagem = dataSecagem;
		//this.status = TipoStatusControle.STATUS_INATIVO;
	}



}
