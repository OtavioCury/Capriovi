package br.ufpi.capriovi.managedBean.zootecnia.raca;

import java.io.ByteArrayInputStream;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.ufpi.capriovi.entidades.cadastros.Raca;
import br.ufpi.capriovi.facade.cadastros.RacaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;
import br.ufpi.capriovi.suporte.tiposEnum.TipoCriacaoEnum;

@SessionScoped
@Named(value = "racaAddEditMB")
public class RacaAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4890666628757883187L;

	@Inject
	RacaFacade racaFacade;

	@Inject
	SystemSessionMB systemSessionMB;

	private Raca raca;

	private String title;

	private UploadedFile file;

	private StreamedContent imagem;

	private int tipo;	

	public Raca getRaca() {
		return raca;
	}

	public void setRaca(Raca raca) {
		this.raca = raca;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Método chamado para adicionar uma raça, 1 para Caprinos e 2 para ovinos
	 * @param tipo
	 */
	public void add(int tipo) { 
		this.tipo = tipo;
		this.title = "Adicionar Raça";
		this.raca = new Raca();

	}

	/**
	 * Método chamado para atualizar uma raça
	 * @param u
	 */
	public void update(Raca u) {
		this.title = "Atualizar Raça";
		this.raca = u;

	}

	public void delete(Long id) {
		this.racaFacade.deletarRaca(id);
	}

	public void cancel() {
		this.raca = new Raca();
	}

	/**
	 * Método chamado para salvar uma raça
	 */
	public void save() {
		if (this.raca != null) {
			if (this.raca.getId() == null) {
				// Add
				if (tipo == 1) {
					raca.setCriacao(TipoCriacaoEnum.getEnumByCodigo(1));
				}else{
					raca.setCriacao(TipoCriacaoEnum.getEnumByCodigo(2));
				}
				if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")  ||  
						getUsuario().getPermissao().contains("ROLE_ADMIN")	) {
					raca.setUsuario(getUsuario());
				}else{
					raca.setUsuario(systemSessionMB.getFazenda().getPecuarista());
				}	

				if(getUsuario().getPermissao().contains("ROLE_ADMIN")){
					raca.setGeral(true);
				}
				setAtributos();
				this.racaFacade.adicionaRaca(raca);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Raça adicionada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			} else {
				// Update
				setAtributos();
				this.racaFacade.atualizaRaca(raca);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Raça atualizada com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			}
		}
	}

	public void setAtributos(){
		if (file != null) {	
			raca.setFoto(file.getContents());
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public StreamedContent getImagem() {
		if (raca.getFoto() != null) {
			imagem = new DefaultStreamedContent(new ByteArrayInputStream(raca.getFoto()));
		}		
		return imagem;
	}

	public void setImagem(StreamedContent imagem) {
		this.imagem = imagem;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
}
