package br.ufpi.capriovi.managedBean.relatorios.melhoramentoGen;

import java.util.ArrayList;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.MelhorGeneticoFacade;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.InputNsgaII;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.relatorios.ResumoNsgaII;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "nsgaIIMB")
@ViewScoped
public class NsgaIIMB extends BaseBeans {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8618590124358432124L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private MelhorGeneticoFacade melhorGeneticoFacade;

	private InputNsgaII inputNsgaII;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhoDT;

	private ResumoNsgaII resumoNsgaII;
	
	private DualListModel<Animal> machos;
	private DualListModel<Animal> femeas;
	private Map<String,Integer> pesos = new HashMap<String, Integer>();

	public String onFlowProcess(FlowEvent event) {
//		if (this.validaRelatorio()) {
//			this.result();
//			return event.getNewStep();
//		}
//		return "escolheRebanho";
		
		if (event.getNewStep().equals("relatorio")) {
			if(validaRelatorioAnimal()){
				if(this.execute()){
					return event.getNewStep();
				} else {
					return "Casal";
				}
			}
		} else {
			if (this.validaRelatorio()) {
				if (this.validaRelatorio()) {
					if(this.executeSelecao()){
						return event.getNewStep();
					}
				}
			} else {
				return "selection";
			}
		}
		return "selection";
	}

	
	public boolean executeSelecao() {
		try {
			HashMap<String, ArrayList<Animal>> MachosEFemeas = melhorGeneticoFacade.selecaoIndividualMassal(inputNsgaII, rebanhoDT);
			this.machos = new DualListModel<Animal>(MachosEFemeas.get("machos"), new ArrayList<Animal>());
			this.femeas = new DualListModel<Animal>(MachosEFemeas.get("femeas"), new ArrayList<Animal>());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage()
							, ""));
			return false;
		}
		return true;
	}
	
	public boolean execute() {
		try {
			this.resumoNsgaII = melhorGeneticoFacade.NSGAII(machos.getTarget(), femeas.getTarget(), inputNsgaII);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage()
							, ""));
			return false;
		}
		return true;
	}
	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {
		this.inputNsgaII = new InputNsgaII();
		pesos = new HashMap<String, Integer>();
		pesos.put("060 dias", 60);
		pesos.put("120 dias",120);
		pesos.put("180 dias",180);
	}
	public boolean validaRelatorio() {
		try {
			CaprioviValidations.rebanhoSelecionadoVal(rebanhoDT);
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return false;
		}
		return true;
	}
	public boolean validaRelatorioAnimal() {
		try {
			CaprioviValidations.animalNaoSelecionado(machos.getTarget(), femeas.getTarget());
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage()
							, ""));
			return false;
		}
		return true;
	}
	public void result() {
		//this.resumoNsgaII = melhorGeneticoFacade.NSGAII(inputNsgaII, rebanhoDT);
	}
	public void onTransfer(TransferEvent event) {
		StringBuilder builder = new StringBuilder();
		for(Object item : event.getItems()) {
			builder.append(((Animal) item).getNomeNumero()).append("<br />");
		}

		FacesMessage msg = new FacesMessage();
		msg.setSeverity(FacesMessage.SEVERITY_INFO);
		msg.setSummary("Items Transferred");
		msg.setDetail(builder.toString());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	} 
	public void onSelect(SelectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
	}

	public void onUnselect(UnselectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
	}

	public void onReorder() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
	} 


	public InputNsgaII getInputNsgaII() {
		return inputNsgaII;
	}

	public void setInputNsgaII(InputNsgaII inputNsgaII) {
		this.inputNsgaII = inputNsgaII;
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

	public ResumoNsgaII getResumoNsgaII() {
		return resumoNsgaII;
	}

	public void setResumoNsgaII(ResumoNsgaII resumoNsgaII) {
		this.resumoNsgaII = resumoNsgaII;
	}

	public List<Rebanho> getRebanhoDT() {
		return rebanhoDT;
	}

	public void setRebanhoDT(List<Rebanho> rebanhoDT) {
		this.rebanhoDT = rebanhoDT;
	}

	public DualListModel<Animal> getMachos() {
		return machos;
	}

	public void setMachos(DualListModel<Animal> machos) {
		this.machos = machos;
	}

	public DualListModel<Animal> getFemeas() {
		return femeas;
	}

	public void setFemeas(DualListModel<Animal> femeas) {
		this.femeas = femeas;
	}

	public Map<String, Integer> getPesos() {
		return pesos;
	}

	public void setPesos(Map<String, Integer> pesos) {
		this.pesos = pesos;
	}
	
}
