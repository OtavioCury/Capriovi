package br.ufpi.capriovi.managedBean.relatorios.genetico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.genetico.RelGeneticoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "relIntervalosGeracoesMB")
@ViewScoped
public class RelIntervalosGeracoesMB extends BaseBeans {

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

	private List<Animal> animais;

	private List<Animal> machos;

	private List<Animal> femeas;	

	private double mediaMacho, mediaFemea, mediaRebanho;


	CaprioviValidations aux = new CaprioviValidations();

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
	 * carrega os dados das tabelas.
	 */
	public void result() {
		this.animais = relGeneticoFacade.relIntervaloGeracoes(rebanhosMarcados);
		machos = new ArrayList<Animal>();
		femeas = new ArrayList<Animal>();
		if (animais.size() > 0) {
			sexo();
			medias();
		}				
	}

	/**
	 * Calcula a média dos intervalos de gerações para cada rebanho
	 */
	private void medias() {
		// TODO Auto-generated method stub

		if (!animais.isEmpty()) {

			double soma = 0;

			if (!machos.isEmpty()) {
				for (Animal animal : machos) {
					soma  = soma + animal.getIntervaloGeracao();			
				}

				mediaMacho = soma/machos.size();
			}

			if (!femeas.isEmpty()) {
				soma = 0;

				for (Animal animal : femeas) {
					soma  = soma + animal.getIntervaloGeracao();			
				}

				mediaFemea = soma/femeas.size();
			}			

			soma = 0;

			for (Animal animal : animais) {
				soma  = soma + animal.getIntervaloGeracao();
			}

			mediaRebanho = soma/animais.size();

		}
	}

	/**
	 * Coloca os animais em diferentes arrays de acordo com o sexo
	 */
	private void sexo() {
		// TODO Auto-generated method stub		
		for (Animal animal : animais) {
			if (animal.getSexo().getDescricao().equals("Fêmea")) {
				femeas.add(animal);
			}else{
				machos.add(animal);
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

	public Rebanho getRebanhosMarcados() {
		return rebanhosMarcados;
	}

	public void setRebanhosMarcados(Rebanho rebanhosMarcados) {
		this.rebanhosMarcados = rebanhosMarcados;
	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public List<Animal> getMachos() {
		return machos;
	}

	public void setMachos(List<Animal> machos) {
		this.machos = machos;
	}

	public List<Animal> getFemeas() {
		return femeas;
	}

	public void setFemeas(List<Animal> femeas) {
		this.femeas = femeas;
	}	

	public double getMediaMacho() {
		return mediaMacho;
	}
	public void setMediaMacho(double mediaMacho) {
		this.mediaMacho = mediaMacho;
	}

	public double getMediaFemea() {
		return mediaFemea;
	}

	public void setMediaFemea(double mediaFemea) {
		this.mediaFemea = mediaFemea;
	}

	public double getMediaRebanho() {
		return mediaRebanho;
	}

	public void setMediaRebanho(double mediaRebanho) {
		this.mediaRebanho = mediaRebanho;
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Rebanho selecionado", ((Rebanho) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}	 

}
