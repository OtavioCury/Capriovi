package br.ufpi.capriovi.managedBean.controleAnimal.ocorrenciaClinica;

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
import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.DoencaFacade;
import br.ufpi.capriovi.facade.controleAnimal.OcorrenciaClinicaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;

@SessionScoped
@Named(value = "ocorrenciaClinicaAddEditMB")
public class OcorrenciaClinicaAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1922366885065847902L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	RebanhoMB rebanhoMB;

	@Inject
	OcorrenciaClinicaFacade ocorrenciaClinicaFacade;

	@Inject
	DoencaFacade doencaFacade;

	@Inject
	AnimalFacade animalFacade;

	private OcorrenciaClinica ocorrenciaClinica;

	private String title;

	private List<Animal> animais;

	private List<String> nomes;

	private List<String> allAnimalNames;

	private List<Doenca> doenca;

	private List<String> nomesDoenca;

	private List<String> allDoencaNames;


	public OcorrenciaClinicaAddEdit() {
		super();
		this.ocorrenciaClinica = new OcorrenciaClinica();
	}

	public OcorrenciaClinica getOcorrenciaClinica() {
		return ocorrenciaClinica;
	}

	public void setOcorrenciaClinica(OcorrenciaClinica ocorrenciaClinica) {
		this.ocorrenciaClinica = ocorrenciaClinica;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado para adicionar uma ocorrencia clinica
	 */
	public void add() {
		this.title = "Adicionar Ocorrencia Clinica";
		this.ocorrenciaClinica = new OcorrenciaClinica();	

		inicializa();
	}

	/**
	 * Método chamado para atualizar uma ocorrencia clinica
	 */
	public void update(OcorrenciaClinica u) {
		this.title = "Atualizar Ocorrencia Clinica";
		this.ocorrenciaClinica = u;

		inicializa();
	}
	
	/**
	 * Função que inicializa o bean
	 */
	public void inicializa(){
		this.nomes = new ArrayList<String>();
		this.animais = animalFacade.animaisPorFazenda(systemSessionMB.getFazenda().getId());
		
		allAnimalNames = new ArrayList<String>();
		this.allAnimalNames = nomeAnimais();

		this.nomesDoenca = new ArrayList<String>();
		this.doenca = doencaFacade.doencasUsuario(getUsuario());

		this.allDoencaNames = nomeDoencas();

	}

	public void delete(Long id) {
		this.ocorrenciaClinicaFacade.deletarOcorrenciaClinica(id);
	}

	public void cancel() {
		this.ocorrenciaClinica = new OcorrenciaClinica();
	}

	/**
	 * Método chamado para salvar uma ocorrencia clinica
	 */
	public void save() {
		if (this.ocorrenciaClinica != null) {
			if (this.ocorrenciaClinica.getId() == null) {
				// Add

				animal();

				this.ocorrenciaClinicaFacade.adicionaOcorrenciaClinica(ocorrenciaClinica);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocorrência adicionada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// Update
				animal();

				this.ocorrenciaClinicaFacade.atualizaOcorrenciaClinica(ocorrenciaClinica);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ocorrência atualizada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}
	}

	/**
	 * Método autocomplete chamado no cadastro de uma ocorrencia clinica 
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
	 * Método autocomplete chamado no cadastro de uma ocorrencia clinica
	 * @param query
	 * @return
	 */
	public List<String> completeDoenca(String query) {

		List<String> filteredNames = new ArrayList<String>();

		for (int i = 0; i < allDoencaNames.size(); i++) {
			String nome = allDoencaNames.get(i);
			if(nome.startsWith(query)) {
				filteredNames.add(nome);
			}
		}

		return filteredNames;
	}

	/**
	 * Salva animal, rebanho e doença de uma ocorrência clinica
	 */
	public void animal(){
		this.ocorrenciaClinica.setAnimal(returnAnimal(this.ocorrenciaClinica.getAnimal().getNomeNumero()));
		this.ocorrenciaClinica.setRebanho(this.ocorrenciaClinica.getAnimal().getRebanho());
		this.ocorrenciaClinica.setDoenca(returnDoenca(this.ocorrenciaClinica.getDoenca().getNome()));

		if (this.ocorrenciaClinica.getAnimal() == null) {
			this.ocorrenciaClinica.setAnimal(null);
			this.ocorrenciaClinica.setRebanho(null);
		}

		if (this.ocorrenciaClinica.getDoenca() == null) {
			this.ocorrenciaClinica.setDoenca(null);
		}
	}

	/**
	 * Retorna a data atual
	 * @return
	 */
	public Date getTodayDate() {
		Date todayDate = new Date();
		return todayDate;
	}

	/**
	 * Retorna uma lista de nome de animais
	 * @return
	 */
	public List<String> nomeAnimais(){
		for (Animal a: this.animais) {
			this.nomes.add(a.getNomeNumero());
		}
		return this.nomes;
	}

	/**
	 * Retorna um objeto animal de acordo com um nome
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
	 * Retorna uma lista com o nome das doenças
	 * @return
	 */
	public List<String> nomeDoencas(){
		for (Doenca a: this.doenca) {
			this.nomesDoenca.add(a.getNome());
		}
		return this.nomesDoenca;
	}

	/**
	 * Retorna um objeto doença de acordo com um nome
	 * @param nome
	 * @return
	 */
	public Doenca returnDoenca(String nome){
		for (Doenca a : this.doenca) {
			if(a.getNome().equals(nome)){
				return a;
			}
		}
		return null;
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

	public List<String> getAllAnimalNames() {
		return allAnimalNames;
	}

	public void setAllAnimalNames(List<String> allAnimalNames) {
		this.allAnimalNames = allAnimalNames;
	}

	public List<Doenca> getDoenca() {
		return doenca;
	}

	public void setDoenca(List<Doenca> doenca) {
		this.doenca = doenca;
	}

	public List<String> getNomesDoenca() {
		return nomesDoenca;
	}

	public void setNomesDoenca(List<String> nomesDoenca) {
		this.nomesDoenca = nomesDoenca;
	}

	public List<String> getAllDoencaNames() {
		return allDoencaNames;
	}

	public void setAllDoencaNames(List<String> allDoencaNames) {
		this.allDoencaNames = allDoencaNames;
	}

	public void onChangeAnimal(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Animal selecionado: ", ocorrenciaClinica.getAnimal().getNomeNumero()));
	}

	public void onChangeDoenca(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Doença selecionada: ", ocorrenciaClinica.getDoenca().getNome()));
	}
}
