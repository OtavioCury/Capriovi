package br.ufpi.capriovi.managedBean.controleAnimal.movimentacaoAnimal;

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
import br.ufpi.capriovi.entidades.controleAnimal.MovimentacaoAnimal;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.MovimentacaoAnimalFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.managedBean.zootecnia.rebanho.RebanhoMB;
import br.ufpi.capriovi.suporte.tiposEnum.TipoMotivoSaidaEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoStatusEnum;

@SessionScoped
@Named(value = "movimentacaoAnimalAddEditMB")
public class MovimentacaoAnimalAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1922366885065847902L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	MovimentacaoAnimalFacade movimentacaoAnimalFacade;

	@Inject
	AnimalFacade animalFacade;

	@Inject
	RebanhoMB rebanhoMB;

	@Inject 
	RebanhoFacade rebanhoFacade;

	private MovimentacaoAnimal movimentacaoAnimal;

	private String title;

	private List<Animal> animais;

	private List<String> nomes;

	private String motivoNome;

	private List<String> allAnimalNames;


	public MovimentacaoAnimalAddEdit() {
		super();
		this.movimentacaoAnimal = new MovimentacaoAnimal();
	}

	public MovimentacaoAnimal getMovimentacaoAnimal() {
		return movimentacaoAnimal;
	}

	public void setMovimentacaoAnimal(MovimentacaoAnimal MovimentacaoAnimal) {
		this.movimentacaoAnimal = MovimentacaoAnimal;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado ao adicionar movimentação animal
	 */
	public void add() {
		this.title = "Adicionar Movimentação Animal";
		this.movimentacaoAnimal = new MovimentacaoAnimal();	

		inicializa();

		this.motivoNome = new String();		
	}

	/**
	 * Método chamado ao atualizar movimentação animal
	 */
	public void update(MovimentacaoAnimal u) {
		this.title = "Atualizar Movimentação Animal";
		this.movimentacaoAnimal = u;

		inicializa();

		this.motivoNome = this.movimentacaoAnimal.getMotivoSaida().getDescricao();

	}

	/**
	 * Função que inicializa alguns dos atributos do bean
	 */
	public void inicializa(){
		this.nomes = new ArrayList<String>();
		this.animais = animalFacade.animaisPorFazenda(systemSessionMB.getFazenda().getId());
		allAnimalNames = new ArrayList<String>();
		this.allAnimalNames = nomeAnimais();
	}

	public void delete(Long id) {
		this.movimentacaoAnimalFacade.deletarMovimentacaoAnimal(id);
	}

	public void cancel() {
		this.movimentacaoAnimal = new MovimentacaoAnimal();
	}

	/**
	 * Método chamado ao salvar movimentação animal
	 */
	public void save() {
		if (this.movimentacaoAnimal != null) {
			if (this.movimentacaoAnimal.getId() == null) {
				// Add
				setAtributos();

				this.movimentacaoAnimalFacade.adicionaMovimentacaoAnimal(movimentacaoAnimal);

				animalFacade.atualizaAnimal(movimentacaoAnimal.getAnimal());

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Movimentação Animal adicionada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// Update
				setAtributos();

				this.movimentacaoAnimalFacade.atualizaMovimentacaoAnimal(movimentacaoAnimal);

				animalFacade.atualizaAnimal(movimentacaoAnimal.getAnimal());

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Movimentação Animal atualizada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}
	}

	/**
	 * Método chamado no autocomplete no cadastro de movimentação animal
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
	 * Retorna o nome dos animais de uma lista
	 * @return
	 */
	public List<String> nomeAnimais(){
		for (Animal a: this.animais) {
			this.nomes.add(a.getNomeNumero());
		}
		return this.nomes;
	}

	public Animal returnAnimal(String nome){
		for (Animal a : this.animais) {
			if(a.getNomeNumero().equals(nome)){
				return a;
			}
		}
		return null;
	}


	public void setAtributos(){
		this.movimentacaoAnimal.setAnimal(returnAnimal(this.movimentacaoAnimal.getAnimal().getNomeNumero()));
		this.movimentacaoAnimal.setRebanho(this.movimentacaoAnimal.getAnimal().getRebanho());

		if (this.movimentacaoAnimal.getAnimal() == null) {
			this.movimentacaoAnimal.setAnimal(null);
			this.movimentacaoAnimal.setRebanho(null);
		}

		if(this.motivoNome.equals("Venda")){
			this.movimentacaoAnimal.setMotivoSaida(TipoMotivoSaidaEnum.getEnumByCodigo(1));
		}else if (this.motivoNome.equals("Morte")) {
			this.movimentacaoAnimal.setMotivoSaida(TipoMotivoSaidaEnum.getEnumByCodigo(2));
			this.movimentacaoAnimal.getAnimal().setStatus(TipoStatusEnum.STATUS_INATIVO);
		}else if (this.motivoNome.equals("Roubo")) {
			this.movimentacaoAnimal.setMotivoSaida(TipoMotivoSaidaEnum.getEnumByCodigo(3));
		}else if (this.motivoNome.equals("Alimentação")) {
			this.movimentacaoAnimal.setMotivoSaida(TipoMotivoSaidaEnum.getEnumByCodigo(4));
		}else if (this.motivoNome.equals("Emprestimo")) {
			this.movimentacaoAnimal.setMotivoSaida(TipoMotivoSaidaEnum.getEnumByCodigo(5));
		}else{
			this.movimentacaoAnimal.setMotivoSaida(TipoMotivoSaidaEnum.getEnumByCodigo(6));
		}
	}

	public void atualizaMovimentacaoAnimal(MovimentacaoAnimal movimentacaoAnimal) {
		this.movimentacaoAnimalFacade.atualizaMovimentacaoAnimal(movimentacaoAnimal);
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

	public Date getTodayDate() {
		Date todayDate = new Date();
		return todayDate;
	}

	public String getMotivoNome() {
		return motivoNome;
	}

	public void setMotivoNome(String motivoNome) {
		this.motivoNome = motivoNome;
	}

	public List<String> getAllAnimalNames() {
		return allAnimalNames;
	}

	public void setAllAnimalNames(List<String> allAnimalNames) {
		this.allAnimalNames = allAnimalNames;
	}

	public void onChangeAnimal(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Animal selecionado: ", movimentacaoAnimal.getAnimal().getNomeNumero()));
	}

}
