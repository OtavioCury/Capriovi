package br.ufpi.capriovi.managedBean.relatorios.producao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.model.chart.PieChartModel;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.producao.RelProducaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.tiposEnum.TipoPartoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoSexoEnum;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relNumeroDeCriasMB")
@ViewScoped
public class RelNumeroDeCriasMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6995374687857288299L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private RelProducaoFacade relProducaoFacade;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhosMarcados;

	private Date inicio;

	private Date fim;

	private List<Animal> animais;

	private List<String> sexo;
	private List<String> tipoParto;

	private int simples, duplo, triplo, quadruplo;

	private PieChartModel criasGrafico;

	@PostConstruct
	public void init() {
		sexo = TipoSexoEnum.getList();
		tipoParto = TipoPartoEnum.getList();
	}

	public String onFlowProcess(FlowEvent event) {
		if (this.validaRelatorio()) {
			this.result();
			return event.getNewStep();
		}
		return "escolheRebanho";
	}

	public boolean validaRelatorio() {
		try {
			CaprioviValidations.rebanhoSelecionadoVal(rebanhosMarcados);
			CaprioviValidations.dataInicioDataFimVal(inicio, fim);
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return false;
		}
		return true;
	}

	/**
	 * carrega os dados da tabela.
	 */
	public void result() {
		animais = new ArrayList<Animal>();
		animais = relProducaoFacade.relNumeroDeCrias(rebanhosMarcados, inicio, fim);
		//quantPartos();
	}

	public void quantPartos(){
		simples = 0;
		duplo = 0;
		triplo = 0;
		quadruplo = 0;
		for (Animal animal : animais) {
			if (animal.getParto() != null) {
				if (animal.getParto().getDescricao() == "Simples") {
					simples++;
				}else if (animal.getParto().getDescricao() == "Duplo") {
					duplo++;
				}else if (animal.getParto().getDescricao() == "Triplo") {
					triplo++;
				}else{
					quadruplo++;
				}
			}			
		}
	}

	public List<Rebanho> getRebanhos() {
		if (systemSessionMB.getFazenda() != null) {
			this.rebanhos = rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId());
		}
		return rebanhos;
	}

	public void setRebanhos(List<Rebanho> rebanhos) {
		this.rebanhos = rebanhos;
	}

	public List<Rebanho> getRebanhosMarcados() {
		return rebanhosMarcados;
	}

	public void setRebanhosMarcados(List<Rebanho> rebanhosMarcados) {
		this.rebanhosMarcados = rebanhosMarcados;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}	

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public List<String> getSexo() {
		return sexo;
	}

	public void setSexo(List<String> sexo) {
		this.sexo = sexo;
	}

	public List<String> getTipoParto() {
		return tipoParto;
	}

	public void setTipoParto(List<String> tipoParto) {
		this.tipoParto = tipoParto;
	}

	public PieChartModel getCriasGrafico() {
		return criasGrafico;
	}

	public void setCriasGrafico(PieChartModel criasGrafico) {
		this.criasGrafico = criasGrafico;
	}

	public int getSimples() {
		return simples;
	}

	public void setSimples(int simples) {
		this.simples = simples;
	}

	public int getDuplo() {
		return duplo;
	}

	public void setDuplo(int duplo) {
		this.duplo = duplo;
	}

	public int getTriplo() {
		return triplo;
	}

	public void setTriplo(int triplo) {
		this.triplo = triplo;
	}

	public int getQuadruplo() {
		return quadruplo;
	}

	public void setQuadruplo(int quadruplo) {
		this.quadruplo = quadruplo;
	}

}