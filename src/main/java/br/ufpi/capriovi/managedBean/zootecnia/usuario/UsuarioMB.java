package br.ufpi.capriovi.managedBean.zootecnia.usuario;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Usuario;
import br.ufpi.capriovi.facade.cadastros.UsuarioFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
/**
 * Comunicação direta com a view.
 * @author thasciano
 *
 */
@Named(value = "usuarioMB")
@SessionScoped
public class UsuarioMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9094051009222835749L;

	@Inject
	private UsuarioFacade usuarioFacade;

	private List<Usuario> usuarios;


	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {		
		usuarios = usuarioFacade.todosUsuarios();
		if (usuarios == null) {
			usuarios = new ArrayList<Usuario>();
		}
	}


	public List<Usuario> getUsuarios() {
		return usuarios;
	}


	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}


	public Usuario buscaUsuario(){
		Usuario usuario;
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext external = context.getExternalContext();
		String username = external.getRemoteUser();
		usuario = usuarioFacade.buscarId(username);
		return usuario;
	}

}
