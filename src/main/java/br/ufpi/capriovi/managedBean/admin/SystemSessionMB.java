package br.ufpi.capriovi.managedBean.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Fazenda;
import br.ufpi.capriovi.entidades.cadastros.Funcionario;
import br.ufpi.capriovi.entidades.cadastros.Usuario;
import br.ufpi.capriovi.facade.cadastros.FazendaFacade;
import br.ufpi.capriovi.facade.cadastros.UsuarioFacade;

@Named(value = "systemSessionMB")
@SessionScoped
public class SystemSessionMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3509836991358791993L;

	@Inject
	private UsuarioFacade usuarioFacade;

	@Inject
	private FazendaFacade fazendaFacade; 

	private Usuario usuario;

	private Fazenda fazenda;

	private List<Fazenda> listFazendas;

	@PostConstruct
	public void loadFazendas() {		
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String username = external.getRemoteUser();
		listFazendas = new ArrayList<Fazenda>();
		this.setUsuario(usuarioFacade.buscarId(username));
		if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")) {
			this.listFazendas = fazendaFacade.listAllId(usuario.getId());
			this.fazenda = new Fazenda();
			if(!this.listFazendas.isEmpty()){
				this.fazenda = this.listFazendas.get(0);
			}
		}else if(getUsuario().getPermissao().contains("ROLE_FUNCIONARIO")){
			Funcionario f;
			f = (Funcionario) getUsuario();
			fazenda = new Fazenda();
			fazenda = fazendaFacade.find(f.getFazenda().getId());
			listFazendas.add(fazenda);
		}

	}
	
	/**
	 * Carrega as fazendas do usuário
	 */
	public void updateFazendas() {		
		this.listFazendas = fazendaFacade.listAllId(usuario.getId());
		for (Fazenda fazenda : listFazendas) {
			if (fazenda.getId() == this.fazenda.getId()) {
				this.fazenda = fazenda;
			}
		}
	}

	public void deletaFazenda(Fazenda fazenda){
		fazendaFacade.deletarFazenda(fazenda.getId());
		listFazendas.remove(fazenda);		
		if (!listFazendas.isEmpty()) {
			this.fazenda = listFazendas.get(0);
		}else{
			Fazenda aux = new Fazenda();
			this.fazenda = aux;
		}
	}

	public Fazenda getFazenda() {
		return fazenda;
	}

	public void setFazenda(Fazenda fazenda) {
		this.fazenda = fazenda;
	}

	public List<Fazenda> getListFazendas() {
		return listFazendas;
	}

	public void setListFazendas(List<Fazenda> listFazendas) {
		this.listFazendas = listFazendas;
	}

	public void getUserByContext(){
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String username = external.getRemoteUser();
		this.setUsuario(usuarioFacade.buscarId(username));  
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
