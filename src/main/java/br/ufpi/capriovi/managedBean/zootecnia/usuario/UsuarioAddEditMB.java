package br.ufpi.capriovi.managedBean.zootecnia.usuario;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Usuario;
import br.ufpi.capriovi.facade.cadastros.UsuarioFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.suporte.Email;
import br.ufpi.capriovi.suporte.MyEncoder;
import br.ufpi.capriovi.suporte.exceptions.MensagensExceptions;
/**
 * Comunicação com a view que adiciona e/ou atualiza o usuário.
 * @author thasciano
 *
 */
@SessionScoped
@Named(value = "usuarioAddEditMB")
public class UsuarioAddEditMB extends BaseBeans{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	UsuarioFacade usuarioFacade;

	@Inject
	UsuarioMB usuarioMB;

	private Usuario usuario;

	private String title;

	private List<String> estados;

	public UsuarioAddEditMB() {
		super();
		this.usuario = new Usuario();
		this.estados = new ArrayList<String>();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void add() {
		this.title = "Adicionar Usuário";
		this.usuario = new Usuario();
	}

	public void update(Usuario u) {
		this.title = "Atualizar Usuário";
		this.usuario = u;
	}

	public void delete(Long id) {
		this.usuarioFacade.deletarUsuario(id);
	}

	public void cancel() {
		this.usuario = new Usuario();
	}

	public void procuraUsuario(){
		this.usuario = usuarioMB.buscaUsuario();
		this.estados = new ArrayList<String>();
		estados();
	}

	public String enviarEmail() throws RuntimeException, NoSuchAlgorithmException{
		try {
			this.usuarioFacade.recuperaSenha(this.usuario.getEmail(), usuario.getCpf());

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Um email foi enviado com um link para a alteração de senha!"));
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			return "login.xhtml?faces-redirect=true";
		} catch (MensagensExceptions e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));

			return "recuperarSenha.xhtml?faces-redirect=true";
		}
	}

	public void save() {
		if (this.usuario != null) {
			if (this.usuario.getId() == null) {
				// Add
				this.usuarioFacade.adicionaUsuario(usuario);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário adicionado com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
			} else {
				// Update				
				this.usuarioFacade.atualizaUsuario(usuario);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informações atualizadas com sucesso!",  null);
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
	}

	public void atualizaUsuario(Usuario usuario) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário atualizado com sucesso!",  null);
		FacesContext.getCurrentInstance().addMessage(null, message);
		this.usuarioFacade.atualizaUsuario(usuario);
	}

	public void atualizaSenha(){		
		try {
			usuario.setPassword(MyEncoder.encriptar(usuario.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.usuarioFacade.atualizaUsuario(usuario);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sua senha foi alterada com sucesso!",  null);
		FacesContext.getCurrentInstance().addMessage(null, message);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true); 
	}

	/**
	 * Remove um usuario do sistema
	 */
	public void removeUsuario(){
		usuarioFacade.deletarUsuario(usuario.getId());
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Conta removida com sucesso!",  null);
		FacesContext.getCurrentInstance().addMessage(null, message);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	}

	/**
	 * Método que habilita usuário após confirmação por email
	 * @param token
	 */
	public void testaToken(String token){		
		usuario = usuarioFacade.usuarioToken(token);
		usuario.setStatusUser(true);
		if (usuario.getStatusAdmin() == true) {
			usuario.setStatusGeral(true);
		}
		usuarioFacade.atualizaUsuario(usuario);

	}

	/**
	 * Método da confirmação por email de recuperação de senha
	 * @param token
	 */
	public void testaTokenSenha(String token){		
		usuario = usuarioFacade.usuarioTokenSenha(token);
	}

	/**
	 * Método usado pelos administradores para habilitar o acesso de algum usuário
	 * @param usuario
	 */
	public void habilitaUsuario(Usuario usu){
		usu.setStatusAdmin(true);
		if (usu.getStatusUser() == true) {
			usu.setStatusGeral(true);
			usuarioFacade.atualizaUsuario(usu);
			Email.acesso(usu, true);
		}else{
			usuarioFacade.atualizaUsuario(usu);
			Email.acesso(usu, false);
		}	
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário habilitado com sucesso!",  null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<String> getEstados() {
		return estados;
	}

	public void setEstados(List<String> estados) {
		this.estados = estados;
	}

	public void estados(){
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

}
