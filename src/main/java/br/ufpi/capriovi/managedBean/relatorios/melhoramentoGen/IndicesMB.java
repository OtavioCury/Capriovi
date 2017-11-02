package br.ufpi.capriovi.managedBean.relatorios.melhoramentoGen;

import java.io.IOException;
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

import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.MelhorGeneticoFacade;
import br.ufpi.capriovi.facade.relatorios.melhoramentoGen.util.InputNsgaII;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
import br.ufpi.capriovi.suporte.utils.ExporterFacade;
import br.ufpi.capriovi.suporte.validators.CaprioviValidations;

@Named(value = "indicesMB")
@ViewScoped
public class IndicesMB extends BaseBeans{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8618590124358432124L;

	@Inject
	private RebanhoMB rebanhoMB;

	@Inject
	private MelhorGeneticoFacade melhorGeneticoFacade;

	private InputNsgaII inputNsgaII;

	private List<Rebanho> rebanhos;

	private List<Rebanho> rebanhoDT;

	private ArrayList<Animal> resumoIndices;

	private DualListModel<Animal> machos;
	private DualListModel<Animal> femeas;
	private Map<String,Integer> pesos = new HashMap<String, Integer>();

	@Inject
	private ExporterFacade ex;

	public void exportExcel() {

		try {
			ex.ler();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Animal macho;

	public void gerarRelatorio() {
		if(this.execute()){
			this.ativarDialog();
		}
	}

	public boolean validaRelatorio() {
		try {
			CaprioviValidations.rebanhoSelecionadoVal(rebanhoDT);
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage()
							, ""));
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

	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {
		this.inputNsgaII = new InputNsgaII();
		//cities
		pesos = new HashMap<String, Integer>();
		pesos.put("120 dias",120);
		// Aqui só vai precisar do peso aos 120 dias.
		//pesos.put("060 dias", 60);
		//pesos.put("180 dias",180);

	}

	public boolean executeSelecao() {
		try {
			HashMap<String, ArrayList<Animal>> MachosEFemeas = melhorGeneticoFacade.selecaoIndices(inputNsgaII, rebanhoDT);
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
			resumoIndices = melhorGeneticoFacade.algGenIndices(machos.getTarget(), femeas.getTarget());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage()
							, ""));
			return false;
		}
		return true;
	}

	public void ativarDialog() {
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("resizable", true);
		options.put("draggable", true);
		options.put("modal", true);
		options.put("width", "1360");
		options.put("height", "500");
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("showEffect", "fade");
		RequestContext.getCurrentInstance().openDialog("relIndicesDialog", options, null);
	}
	
	public String onFlowProcess(FlowEvent event) {
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
	
	/*
	public String onFlowProcess(FlowEvent event) {
		if(this.validaRelatorio()){
			if(this.executeSelecao()){
				return event.getNewStep();
			}
		}
		return "selection";

	}
*/
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
		rebanhos = rebanhoMB.getRebanhosUsuario();
		return rebanhos;
	}

	public void setRebanhos(List<Rebanho> rebanhos) {
		this.rebanhos = rebanhos;
	}

	public List<Rebanho> getRebanhoDT() {
		return rebanhoDT;
	}

	public void setRebanhoDT(List<Rebanho> rebanhoDT) {
		this.rebanhoDT = rebanhoDT;
	}

	public Animal getMacho() {
		return macho;
	}

	public void setMacho(Animal macho) {
		this.macho = macho;
	}

	public List<Animal> getResumoIndices() {
		return resumoIndices;
	}

	public void setResumoIndices(ArrayList<Animal> resumoIndices) {
		this.resumoIndices = resumoIndices;
	}

	public Map<String, Integer> getPesos() {
		return pesos;
	}

	public void setPesos(Map<String, Integer> pesos) {
		this.pesos = pesos;
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


}
