package br.ufpi.capriovi.managedBean.zootecnia.fazenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.ufpi.capriovi.entidades.cadastros.Fazenda;
import br.ufpi.capriovi.entidades.cadastros.Pecuarista;
import br.ufpi.capriovi.facade.cadastros.FazendaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@SessionScoped
@Named(value = "fazendaAddEditMB")
public class FazendaAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5365049053211011021L;

	@Inject
	private FazendaFacade fazendaFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	private Fazenda fazenda;

	private Pecuarista pecuarista;

	private String title;

	private List<String> estados;	

	@PostConstruct
	public void init() {
		this.fazenda = new Fazenda();
		estados();
	}

	public Fazenda getFazenda() {
		return fazenda;
	}

	public void setFazenda(Fazenda fazenda) {
		this.fazenda = fazenda;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado para adicionar uma fazenda
	 */
	public void add() {
		this.title = "Adicionar Fazenda";
		this.fazenda = new Fazenda();
		estados();

	}

	/**
	 * Método chamado para atualizar uma fazenda
	 * @param f
	 */
	public void update(Fazenda f) {
		this.title = "Atualizar Fazenda";
		this.fazenda = f;
		estados();

	}

	/**
	 * Deleta uma fazenda do BD
	 * @param id
	 */
	@Transactional
	public void delete(Long id) {
		this.fazendaFacade.deletarFazenda(id);		
	}

	/**
	 * Chamado para voltar para a página home.xhtml 
	 * @return
	 */
	public void cancel() {
		this.fazenda = new Fazenda();		
	}

	/**
	 * Salva uma fazenda no banco de dados e atualiza a lista de fazenda em sessão
	 * @return
	 */
	@Transactional
	public void save() {
		if (this.fazenda != null) {
			if (this.fazenda.getId() == null) {
				// Add
				pecuarista = (Pecuarista) systemSessionMB.getUsuario();
				this.fazenda.setPecuarista(pecuarista);
				this.fazenda.setRegistro(new Date());
				this.fazendaFacade.adicionaFazenda(fazenda);						
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fazenda adicionada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// Update								
				this.fazendaFacade.atualizaFazenda(fazenda);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fazenda atualizada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}
		this.systemSessionMB.updateFazendas();		
	}

	/**
	 * Preencha a lista de estados
	 */
	public void estados(){
		this.estados = new ArrayList<String>();
		
		this.estados.add("Acre");
		this.estados.add("Alagoas");
		this.estados.add("Amapá");
		this.estados.add("Amazonas");
		this.estados.add("Bahia");
		this.estados.add("Ceará");
		this.estados.add("Distrito Federal");
		this.estados.add("Espírito Santo");
		this.estados.add("Goiás");
		this.estados.add("Maranhão");
		this.estados.add("Mato Grosso");
		this.estados.add("Mato Grosso do Sul");
		this.estados.add("Minas Gerais");
		this.estados.add("Pará");
		this.estados.add("Paraíba");
		this.estados.add("Paraná");
		this.estados.add("Pernambuco");
		this.estados.add("Piauí");
		this.estados.add("Rio de Janeiro");
		this.estados.add("Rio Grande do Norte");
		this.estados.add("Rio Grande do Sul");
		this.estados.add("Rondônia");
		this.estados.add("Roraima");
		this.estados.add("Santa Catarina");
		this.estados.add("São Paulo");
		this.estados.add("Sergipe");
		this.estados.add("Tocantins");		
	}

	public List<String> getEstados() {
		return estados;
	}

	public void setEstados(List<String> estados) {
		this.estados = estados;
	}

}
