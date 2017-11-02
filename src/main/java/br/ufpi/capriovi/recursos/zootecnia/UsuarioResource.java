package br.ufpi.capriovi.recursos.zootecnia;

import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.ufpi.capriovi.dao.cadastros.UsuarioDAO;
import br.ufpi.capriovi.entidades.cadastros.Usuario;

@Path("usuario")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

	@Inject
	private UsuarioDAO usuarioDAO;

	/**
	 * Método reposnsável pelo login do usuário
	 * @param login
	 * @param senha
	 * @return usuario
	 */
	@GET
	@Path("login/{login}/{senha}")
	public Usuario login(@javax.ws.rs.PathParam(value = "login") String login, @javax.ws.rs.PathParam(value = "senha") String senha){		
		Usuario usuario = null;		
		try {
			usuario = usuarioDAO.testaLogin(login, senha);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return usuario;
	}			

}
