package br.ufpi.capriovi.managedBean.controleAnimal.vacinacao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.ufpi.capriovi.entidades.cadastros.Medicamento;
import br.ufpi.capriovi.entidades.controleAnimal.Vacinacao;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.MedicamentoFacade;
import br.ufpi.capriovi.facade.controleAnimal.VacinacaoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;
/**
 * Comunicação com a view que adiciona e/ou atualiza o usuário.
 * @author thasciano
 *
 */
@SessionScoped
@Named(value = "vacinacaoAddEditMB")
public class VacinacaoAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7727389282513315120L;

	@Inject
	VacinacaoFacade vacinacaoFacade;

	@Inject
	RebanhoMB rebanhoMB;

	@Inject
	AnimalFacade animalFacade;

	@Inject
	MedicamentoFacade medicamentoFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	private Vacinacao vacinacao;

	private String title;


	private List<Medicamento> medicamentos;

	private List<String> nomesMedicamento;

	private List<String> allMedicamentoNames;	


	public Vacinacao getVacinacao() {
		return vacinacao;
	}

	public void setVacinacao(Vacinacao vacinacao) {
		this.vacinacao = vacinacao;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado para adicionar uma vacinação
	 */
	public void add() {
		this.title = "Adicionar Vacinação";
		this.vacinacao = new Vacinacao();		

		inicializa();

	}

	/**
	 * Método chamado para atualizar uma vacinação
	 * @param u
	 */
	public void update(Vacinacao u) {
		this.title = "Atualizar Vacinação";
		this.vacinacao = u;		

		inicializa();
	}

	/**
	 * Função que inicializa alguns atributos do bean
	 */
	public void inicializa(){		
		this.nomesMedicamento = new ArrayList<String>();
		this.medicamentos = medicamentoFacade.medicamentosUsuario(getUsuario());
		allMedicamentoNames = new ArrayList<String>();
		this.allMedicamentoNames = nomeMedicamentos();
	}

	public void delete(Long id) {
		this.vacinacaoFacade.deletarVacinacao(id);
	}

	public void cancel() {
		this.vacinacao = new Vacinacao();
	}

	/**
	 * Método chamado para salvar uma vacinação
	 */
	public String save() {		
		if (this.vacinacao.getId() == null) {
			// Add
			setAtributos();
			if (!vacinacao.getAnimais().isEmpty()) {
				this.vacinacaoFacade.adicionaVacinacao(vacinacao);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vacinação adicionada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				return "list.xhtml?faces-redirect=true";
			}else{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Você deve selecionar ao menos um animal!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return "addEdit";
			}				
		} else {
			// Update
			setAtributos();
			if (!vacinacao.getAnimais().isEmpty()) {
				this.vacinacaoFacade.atualizaVacinacao(vacinacao);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vacinação atualizada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				return "list.xhtml?faces-redirect=true";
			}else{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Você deve selecionar ao menos um animal!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return "addEdit";
			}
		}

	}

	/**
	 * Método chamado no autocomplete 'Principio Ativo'
	 * @param query
	 * @return
	 */
	public List<String> completeMedicamento(String query) {

		List<String> filteredNames = new ArrayList<String>();

		for (int i = 0; i < allMedicamentoNames.size(); i++) {
			String nome = allMedicamentoNames.get(i);
			if(nome.startsWith(query)) {
				filteredNames.add(nome);
			}
		}

		return filteredNames;
	}

	/**
	 * Retorna uma lista com os nomes de principios ativos
	 * @return
	 */
	public List<String> nomeMedicamentos(){
		for (Medicamento a: this.medicamentos) {
			this.nomesMedicamento.add(a.getNome());
		}
		return this.nomesMedicamento;
	}

	/**
	 * Retorna um objeto de acordo com o nome
	 * @param nome
	 * @return
	 */
	public Medicamento returnMedicamento(String nome){
		for (Medicamento a : this.medicamentos) {
			if(a.getNome().equals(nome)){
				return a;
			}
		}
		return null;
	}

	/**
	 * Método que seta o animal e o rebanho de um principio ativo
	 */
	public void setAtributos(){
		if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")) {
			vacinacao.setUsuario(getUsuario());
		} else {
			vacinacao.setUsuario(systemSessionMB.getFazenda().getPecuarista());
		}

		vacinacao.setMedicamento((returnMedicamento(vacinacao.getMedicamento().getNome())));
	}

	/**
	 * Retorna a data atual 
	 * @return
	 */
	public Date getTodayDate() {
		Date todayDate = new Date();
		return todayDate;
	}

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

	public List<String> getNomesMedicamento() {
		return nomesMedicamento;
	}

	public void setNomesMedicamento(List<String> nomesMedicamento) {
		this.nomesMedicamento = nomesMedicamento;
	}

	public List<String> getAllMedicamentoNames() {
		return allMedicamentoNames;
	}

	public void setAllMedicamentoNames(List<String> allMedicamentoNames) {
		this.allMedicamentoNames = allMedicamentoNames;
	}

	/**
	 * Método que cria uma menssagem na tela quando um medicamento é selecionado
	 * @param event
	 */
	public void onChangeMedicamento(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Medicamento selecionado: ", vacinacao.getMedicamento().getNome()));
	}

}
