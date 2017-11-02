package br.ufpi.capriovi.managedBean.zootecnia.administrador;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Administrador;
import br.ufpi.capriovi.facade.cadastros.AdministradorFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;

@Named(value = "administradorMB")
@ViewScoped
public class AdministradorMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1297281938078610741L;


	@Inject
	private AdministradorFacade administradorFacade;

	private String userAdmin;

	private String senhaAdmin;

	private boolean bloqueado;

	private List<Administrador> administradores;

	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {	
		bloqueado = true;
		administradores = new ArrayList<Administrador>();
		administradores = administradorFacade.listAll();
	}

	public void testaAdmin(){
		if (userAdmin.equals("projetoadmin123") && senhaAdmin.equals("projetoadmin123")) {
			bloqueado = false;		
		}
	}

	public List<Administrador> getAdministradores() {
		return administradores;
	}

	public void setAdministradores(List<Administrador> administradores) {
		this.administradores = administradores;
	}

	public String getUserAdmin() {
		return userAdmin;
	}

	public void setUserAdmin(String userAdmin) {
		this.userAdmin = userAdmin;
	}

	public String getSenhaAdmin() {
		return senhaAdmin;
	}

	public void setSenhaAdmin(String senhaAdmin) {
		this.senhaAdmin = senhaAdmin;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}


}
