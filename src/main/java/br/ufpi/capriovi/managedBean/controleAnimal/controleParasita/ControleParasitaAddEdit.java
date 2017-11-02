package br.ufpi.capriovi.managedBean.controleAnimal.controleParasita;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Medicamento;
import br.ufpi.capriovi.entidades.controleAnimal.ControleParasita;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.MedicamentoFacade;
import br.ufpi.capriovi.facade.controleAnimal.ControleParasitaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;
/**
 * Comunicação com a view que adiciona e/ou atualiza o usuário.
 * @author thasciano
 *
 */
@SessionScoped
@Named(value = "controleParasitaAddEditMB")
public class ControleParasitaAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6490237467048223802L;

	@Inject
	private SystemSessionMB systemSessionMB;	

	@Inject
	ControleParasitaFacade controleParazitaFacade;

	@Inject
	RebanhoMB rebanhoMB;

	@Inject
	AnimalFacade animalFacade;

	@Inject
	MedicamentoFacade medicamentoFacade;

	private ControleParasita controleParasita;

	private Medicamento Medicamento;

	private String title;

	private List<Animal> animais;

	private List<String> nomes;

	private List<Medicamento> medicamentos;

	private List<String> nomesMedicamentos;	

	private List<String> allAnimalNames;

	private List<String> allMedicamentosNames;


	public ControleParasitaAddEdit() {
		super();
		this.controleParasita = new ControleParasita();
	}

	public ControleParasita getControleParasita() {
		return controleParasita;
	}

	public void setControleParasita(ControleParasita controleParasita) {
		this.controleParasita = controleParasita;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método que adiciona um controle parasita 
	 */
	public void add() {
		this.title = "Adicionar Controle Parasita";
		this.controleParasita = new ControleParasita();

		inicializa();	
	}

	/**
	 * Método que atualiza um controle parasita
	 * @param u
	 */
	public void update(ControleParasita u) {
		this.title = "Atualizar Controle Parasita";
		this.controleParasita = u;

		inicializa();		

	}

	public void delete(Long id) {
		this.controleParazitaFacade.deletarControleParazita(id);
	}

	public void cancel() {
		this.controleParasita = new ControleParasita();
	}

	/**
	 * Método chamado para salvar um controle parasita
	 */
	public void save() {
		if (this.controleParasita != null) {
			if (this.controleParasita.getId() == null) {
				// Add
				setAtributos();

				this.controleParazitaFacade.adicionaControleParazita(controleParasita);

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Controle Parasitário adicionado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// Update
				setAtributos();

				this.controleParazitaFacade.atualizaControleParazita(controleParasita);

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Controle Parasitário atualizado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}
	}

	/**
	 * Inicializa as listas e atributos do MB 
	 */
	public void inicializa(){
		this.nomes = new ArrayList<String>();
		this.animais = animalFacade.animaisPorFazenda(systemSessionMB.getFazenda().getId());

		this.nomesMedicamentos = new ArrayList<String>();
		this.medicamentos = medicamentoFacade.medicamentosUsuario(getUsuario());

		allAnimalNames = new ArrayList<String>();
		this.allAnimalNames = nomeAnimais();
		allMedicamentosNames = new ArrayList<String>();
		this.allMedicamentosNames = nomeMedicamentos();
	}

	/**
	 * Método chamado no autocomplete no input animal
	 * @param query
	 * @return
	 */
	public List<String> completeAnimal(String query) {

		List<String> filteredNames = new ArrayList<String>();

		for (int i = 0; i < allAnimalNames.size(); i++) {
			String nome = allAnimalNames.get(i);
			if(nome.startsWith(query)) {
				filteredNames.add(nome);
			}
		}

		return filteredNames;
	}

	/**
	 * Popula a lista com o nome dos animais
	 * @return
	 */
	public List<String> nomeAnimais(){
		for (Animal a: this.animais) {
			this.nomes.add(a.getNomeNumero());
		}
		return this.nomes;
	}

	/**
	 * Retorna o objeto Animal de acordo com o nome
	 * @param nome
	 * @return
	 */
	public Animal returnAnimal(String nome){
		for (Animal a : this.animais) {
			if(a.getNomeNumero().equals(nome)){
				return a;
			}
		}
		return null;
	}

	/**
	 * Método chamado no autocomplete de Medicamento ativo
	 * @param query
	 * @return
	 */
	public List<String> completeMedicamento(String query) {

		List<String> filteredNames = new ArrayList<String>();

		for (int i = 0; i < allMedicamentosNames.size(); i++) {
			String nome = allMedicamentosNames.get(i);
			if(nome.startsWith(query)) {
				filteredNames.add(nome);
			}
		}

		return filteredNames;
	}

	/**
	 * Popula a lista com os nomes dos medicamentos ativos
	 * @return
	 */
	public List<String> nomeMedicamentos(){
		for (Medicamento p: this.medicamentos) {
			this.nomesMedicamentos.add(p.getNome());
		}
		return this.nomesMedicamentos;
	}

	/**
	 * Retorna o objeto medicamento de acordo com o nome
	 * @param nome
	 * @return
	 */
	public Medicamento returnMedicamento(String nome){
		for (Medicamento p: this.medicamentos) {
			if(p.getNome().equals(nome)){
				return p;
			}
		}
		return null;
	}

	/**
	 * Método chamado na hora de salvar para setar os atributos de um Medicamento ativo
	 */
	public void setAtributos(){
		this.controleParasita.setAnimal(returnAnimal(this.controleParasita.getAnimal().getNomeNumero()));
		this.controleParasita.setRebanho(this.controleParasita.getAnimal().getRebanho());

		if (controleParasita.getAnimal() == null) {
			this.controleParasita.setAnimal(null);
			this.controleParasita.setRebanho(null);
		}

		this.controleParasita.setMedicamento(returnMedicamento(this.controleParasita.getMedicamento().getNome()));

		if (controleParasita.getMedicamento() == null) {
			this.controleParasita.setMedicamento(null);
		}		

	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public List<String> getNomes() {
		return nomes;
	}

	public void setNomes(List<String> nomes) {
		this.nomes = nomes;
	}

	public Medicamento getMedicamento() {
		return Medicamento;
	}

	public void setMedicamento(Medicamento Medicamento) {
		this.Medicamento = Medicamento;
	}

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

	public List<String> getNomesMedicamentos() {
		return nomesMedicamentos;
	}

	public void setNomesMedicamentos(List<String> nomesmedicamentos) {
		this.nomesMedicamentos = nomesmedicamentos;
	}

	public Date getTodayDate() {
		Date todayDate = new Date();
		return todayDate;
	}	

	public List<String> getAllAnimalNames() {
		return allAnimalNames;
	}

	public void setAllAnimalNames(List<String> allAnimalNames) {
		this.allAnimalNames = allAnimalNames;
	}

	public List<String> getAllMedicamentosNames() {
		return allMedicamentosNames;
	}

	public void setAllmedicamentosNames(List<String> allmedicamentosNames) {
		this.allMedicamentosNames = allmedicamentosNames;
	}

	public void onChangeAnimal(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Animal selecionado: ", controleParasita.getAnimal().getNomeNumero()));
	}

	public void onChangeMedicamento(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Princípio Ativo selecionada: ", controleParasita.getMedicamento().getNome()));
	}

}
