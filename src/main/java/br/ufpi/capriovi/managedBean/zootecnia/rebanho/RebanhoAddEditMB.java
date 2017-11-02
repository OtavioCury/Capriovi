package br.ufpi.capriovi.managedBean.zootecnia.rebanho;

import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.primefaces.event.SelectEvent;

import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.tiposEnum.TipoCriacaoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoFinalidadeRebanhoEnum;
import br.ufpi.capriovi.suporte.tiposEnum.TipoManejoEnum;

@Named(value = "rebanhoAddEditMB")
@SessionScoped
public class RebanhoAddEditMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3891166147899985039L;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	private Rebanho rebanho;

	private String title;

	private String criacaoNome;

	private String manejoNome;

	private String finalidadeNome;

	private int indexRebanho;	

	public Rebanho getRebanho() {
		return rebanho;
	}

	public void setRebanho(Rebanho rebanho) {
		this.rebanho = rebanho;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado ao adicionar um rebanho
	 * @param fazenda
	 */
	public void add() {
		this.title = "Adicionar Rebanho";
		this.rebanho = new Rebanho();
		this.finalidadeNome = new String();
		this.criacaoNome = new String();
		this.manejoNome = new String();

	}

	/**
	 * Método chamado ao atualizar um rebanho
	 * @param r
	 */
	public void update(Rebanho r) {
		this.title = "Atualizar Rebanho";
		this.rebanho = r;		
		this.finalidadeNome = r.getFinalidade().getDescricao();
		this.criacaoNome = r.getCriacao().getDescricao();
		this.manejoNome = r.obterManejo().getDescricao();

	}

	@Transactional
	public String delete(Long id) {	
		this.rebanhoFacade.deletarRebanho(id);
		return "/pages/entidades/rebanho/list.xhtml?faces-redirect=true";
	}

	public String cancel() {
		this.rebanho = new Rebanho();
		return "/pages/entidades/rebanho/list.xhtml?faces-redirect=true";
	}

	/**
	 * Método chamado ao salvar um rebanho
	 */
	@Transactional
	public void save() {
		if (this.rebanho != null) {
			if (this.rebanho.getId() == null) {
				if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")) {
					this.rebanho.setFazenda(systemSessionMB.getFazenda());
				}else if(getUsuario().getPermissao().contains("ROLE_FUNCIONARIO")){
					this.rebanho.setFazenda(systemSessionMB.getFazenda());
				}				
				setEnuns();
				this.rebanho.setRegistro(new Date());
				this.rebanhoFacade.adicionaRebanho(rebanho);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rebanho adicionado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				this.rebanho.setFazenda(systemSessionMB.getFazenda());
				setEnuns();
				this.rebanhoFacade.atualizaRebanho(rebanho);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rebanho atualizado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}		
	}

	/**
	 * Método chamado para salvar os enuns do rebanho
	 */
	public void setEnuns(){

		if(this.criacaoNome.equals("Caprinos")){
			this.rebanho.setCriacao(TipoCriacaoEnum.getEnumByCodigo(1));
		}else if (this.criacaoNome.equals("Ovinos")) {
			this.rebanho.setCriacao(TipoCriacaoEnum.getEnumByCodigo(2));
		}else if (this.criacaoNome.equals("Ambos")) {
			this.rebanho.setCriacao(TipoCriacaoEnum.getEnumByCodigo(3));
		}

		if(this.manejoNome.equals("Intensivo")){
			this.rebanho.setManejo(TipoManejoEnum.getEnumByCodigo(1));
		}else if (this.manejoNome.equals("Extensivo")) {
			this.rebanho.setManejo(TipoManejoEnum.getEnumByCodigo(2));
		}else if (this.manejoNome.equals("Semi-Intensivo")) {
			this.rebanho.setManejo(TipoManejoEnum.getEnumByCodigo(3));
		}

		if(this.finalidadeNome.equals("Carne")){
			this.rebanho.setFinalidade(TipoFinalidadeRebanhoEnum.getEnumByCodigo(1));
		}else if (this.finalidadeNome.equals("Leite")) {
			this.rebanho.setFinalidade(TipoFinalidadeRebanhoEnum.getEnumByCodigo(2));
		}else if (this.finalidadeNome.equals("Misto")) {
			this.rebanho.setFinalidade(TipoFinalidadeRebanhoEnum.getEnumByCodigo(3));
		}

	}

	public String getCriacaoNome() {
		return criacaoNome;
	}

	public void setCriacaoNome(String criacaoNome) {
		this.criacaoNome = criacaoNome;
	}

	public String getManejoNome() {
		return manejoNome;
	}

	public void setManejoNome(String manejoNome) {
		this.manejoNome = manejoNome;
	}

	public String getFinalidadeNome() {
		return finalidadeNome;
	}

	public void setFinalidadeNome(String finalidadeNome) {
		this.finalidadeNome = finalidadeNome;
	}

	public void onChangeFazenda(SelectEvent event){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Fazenda selecionada: ", rebanho.getFazenda().getNome()));
	}

	public int getIndexRebanho() {
		return indexRebanho;
	}

	public void setIndexRebanho(int indexRebanho) {
		this.indexRebanho = indexRebanho;
	}
}
