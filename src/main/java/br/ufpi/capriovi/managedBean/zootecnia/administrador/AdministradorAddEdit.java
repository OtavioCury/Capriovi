package br.ufpi.capriovi.managedBean.zootecnia.administrador;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Administrador;
import br.ufpi.capriovi.facade.cadastros.AdministradorFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.suporte.MyEncoder;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;


@SessionScoped
@Named(value = "administradorAddEditMB")
public class AdministradorAddEdit extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -824222059673366166L;

	@Inject
	AdministradorFacade administradorFacade;

	private Administrador administrador;

	private String title;

	private List<String> estados;	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void add() {
		this.title = "Adicionar Administrador";		
		this.administrador = new Administrador();

		estados();

	}

	public void update(Administrador a) {
		this.title = "Atualizar Administrador";
		this.administrador = a;		

		estados();
	}

	public void delete(Long id) {
		this.administradorFacade.deletarAdministrador(id);
	}

	public void cancel() {
		this.administrador = new Administrador();
	}

	public String save() {		

		// Add

		String senha;
		try {
			senha = MyEncoder.encriptar(administrador.getPassword());
			administrador.setPassword(senha);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}							
		return testUsuario();
	}

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



	public String testUsuario(){
		try {
			this.administradorFacade.addAdministrador(administrador);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Cadastro realizado com sucesso!"));
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			return "login.xhtml?faces-redirect=true";
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));

			return "cadastroAdmin";
		}
	}

	public List<String> getEstados() {
		return estados;
	}

	public void setEstados(List<String> estados) {
		this.estados = estados;
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}
}
