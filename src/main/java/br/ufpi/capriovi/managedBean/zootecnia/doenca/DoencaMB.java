package br.ufpi.capriovi.managedBean.zootecnia.doenca;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.facade.cadastros.DoencaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@Named(value = "doencaMB")
@ViewScoped
public class DoencaMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -891068157939819889L;

	@Inject
	private DoencaFacade doencaFacade;

	private List<Doenca> doencasUsuario;

	@Inject
	private SystemSessionMB systemSessionMB;

	/**
	 * carrega as doenças na tabela.
	 */
	@PostConstruct
	public void init() {
		doencasUsuario = new ArrayList<Doenca>();
		if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")) {
			doencasUsuario = doencaFacade.doencasUsuario(getUsuario());
		}else if (getUsuario().getPermissao().contains("ROLE_FUNCIONARIO")){
			doencasUsuario = doencaFacade.doencasUsuario(systemSessionMB.getFazenda().getPecuarista());
		}else{
			doencasUsuario = doencaFacade.doencasAdmin();
		}		
	}

	public List<Doenca> getDoencasUsuario() {
		return doencasUsuario;
	}

	public void setDoencasUsuario(List<Doenca> doencasUsuario) {
		this.doencasUsuario = doencasUsuario;
	}

	/**
	 * Deleta uma doença da tabela
	 * @param doenca
	 */
	public void deletaDoenca(Doenca doenca){		
		doencasUsuario.remove(doenca);
		doencaFacade.deletarDoenca(doenca.getId());
	}


}
