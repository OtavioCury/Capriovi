package br.ufpi.capriovi.managedBean.relatorios.genetico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import br.ufpi.capriovi.suporte.relatorios.ResumoGanhoGeneticoEsp;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.genetico.RelGeneticoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relGanhoGeneticoEsperadoMB")
@ViewScoped
public class RelGanhoGeneticoEsperadoMB extends BaseBeans {
	/**
	 * 
	 */
	private static final long serialVersionUID = -229716363721751207L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private RelGeneticoFacade relGeneticoFacade;

	private List<Rebanho> rebanhos;

	private Rebanho rebanhosMarcados;

	private ResumoGanhoGeneticoEsp resumoGanhoGeneticoEsp;

	private Map<String, Integer> pesos = new HashMap<String, Integer>();
	private Map<String, Double> propMachos = new HashMap<String, Double>();
	private Map<String, Double> propFemeas = new HashMap<String, Double>();
//	private Map<String, Double> herdCaract = new HashMap<String, Double>();
	private Map<String, Double> intensidade = new HashMap<String, Double>();

	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {
		resumoGanhoGeneticoEsp = new ResumoGanhoGeneticoEsp();
		pesos = new HashMap<String, Integer>();
		pesos.put("060 dias", 060);
		pesos.put("120 dias", 120);
		pesos.put("180 dias", 180);

		propMachos = new HashMap<String, Double>();
		propMachos.put("5%", 0.05);
		propMachos.put("10%", 0.1);
		propMachos.put("15%", 0.15);

		propFemeas = new HashMap<String, Double>();
		propFemeas.put("30%", 0.3);
		propFemeas.put("40%", 0.4);
		propFemeas.put("50%", 0.5);
		propFemeas.put("60%", 0.6);
		propFemeas.put("70%", 0.7);
		propFemeas.put("80%", 0.8);

		intensidade = new HashMap<String, Double>();
		intensidade.put("5%", 2.06);
		intensidade.put("10%", 1.76);
		intensidade.put("15%", 1.56);
		intensidade.put("30%", 1.16);
		intensidade.put("40%", 0.97);
		intensidade.put("50%", 0.8);
		intensidade.put("60%", 0.64);
		intensidade.put("70%", 0.5);
		intensidade.put("80%", 0.35);
		
//		herdCaract = new HashMap<String, Double>();
//		herdCaract.put("Peso", 0.3);

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
			CaprioviValidations.rebanhoSelecionadoSingleVal(rebanhosMarcados);
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
		this.resumoGanhoGeneticoEsp.setIntensidadeMacho(this.intensidade.get( ( (int)(resumoGanhoGeneticoEsp.getPropMachos()*100)) +"%" ));
		this.resumoGanhoGeneticoEsp.setIntensidadeFemea(this.intensidade.get( ( (int)(resumoGanhoGeneticoEsp.getPropFemeas()*100)) +"%" ));
		this.resumoGanhoGeneticoEsp.setRebanho(rebanhosMarcados.getNome());
		this.resumoGanhoGeneticoEsp = relGeneticoFacade.relGanhoGeneticoEsperado(rebanhosMarcados.getId(), 
				resumoGanhoGeneticoEsp);

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

	public Rebanho getRebanhosMarcados() {
		return rebanhosMarcados;
	}

	public void setRebanhosMarcados(Rebanho rebanhosMarcados) {
		this.rebanhosMarcados = rebanhosMarcados;
	}

	public ResumoGanhoGeneticoEsp getResumoGanhoGeneticoEsp() {
		return resumoGanhoGeneticoEsp;
	}

	public void setResumoGanhoGeneticoEsp(ResumoGanhoGeneticoEsp resumoGanhoGeneticoEsp) {
		this.resumoGanhoGeneticoEsp = resumoGanhoGeneticoEsp;
	}

	public Map<String, Integer> getPesos() {
		return pesos;
	}

	public void setPesos(Map<String, Integer> pesos) {
		this.pesos = pesos;
	}

	public Map<String, Double> getPropMachos() {
		return propMachos;
	}

	public void setPropMachos(Map<String, Double> propMachos) {
		this.propMachos = propMachos;
	}

	public Map<String, Double> getPropFemeas() {
		return propFemeas;
	}

	public void setPropFemeas(Map<String, Double> propFemeas) {
		this.propFemeas = propFemeas;
	}

//	public Map<String, Double> getHerdCaract() {
//		return herdCaract;
//	}
//
//	public void setHerdCaract(Map<String, Double> herdCaract) {
//		this.herdCaract = herdCaract;
//	}

	public Map<String, Double> getIntensidade() {
		return intensidade;
	}

	public void setIntensidade(Map<String, Double> intensidade) {
		this.intensidade = intensidade;
	}

}
