package br.ufpi.capriovi.managedBean;



import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import br.ufpi.capriovi.entidades.cadastros.Usuario;
import br.ufpi.capriovi.facade.cadastros.UsuarioFacade;

public abstract class BaseBeans implements Serializable {

	private static final long serialVersionUID = -5377726703339445533L;
	
	@Inject
	private UsuarioFacade usuarioFacade; 
	
	private Usuario usuario;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public void getUserByContext(){
		FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext external = context.getExternalContext();
        String username = external.getRemoteUser();
        this.setUsuario(usuarioFacade.buscarId(username));  
	}
	
	public Usuario getUsuario() {
		if (usuario == null) {
			getUserByContext();	
		}
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
