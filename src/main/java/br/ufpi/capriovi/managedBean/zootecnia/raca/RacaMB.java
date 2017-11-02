package br.ufpi.capriovi.managedBean.zootecnia.raca;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.cadastros.Raca;
import br.ufpi.capriovi.facade.cadastros.RacaFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@Named(value = "racaMB")
@ViewScoped
public class RacaMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4401387836628006657L;

	@Inject
	private RacaFacade racaFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	private List<Raca> racasUsuario;

	private List<Raca> racasCaprinos;

	private List<Raca> racasOvinos;
	
	/**
	 * Carrega as raças na tabela
	 */
	@PostConstruct
	public void init(){
		racasUsuario = new ArrayList<Raca>();
		if (getUsuario().getPermissao().contains("ROLE_PECUARISTA")) {
			racasUsuario = racaFacade.racasUsuario(getUsuario());
		}else if (getUsuario().getPermissao().contains("ROLE_FUNCIONARIO")){
			racasUsuario = racaFacade.racasUsuario(systemSessionMB.getFazenda().getPecuarista());
		}else{
			racasUsuario = racaFacade.racasAdmin();
		}
		caprinos(racasUsuario);
		ovinos(racasUsuario);
	}

	/**
	 * Preenche a lita racasCaprinos com as racas vindo do BD
	 * @param racas
	 */
	public void caprinos(List<Raca> racas){
		racasCaprinos = new ArrayList<Raca>();
		if (!racas.isEmpty()) {
			for (Raca raca : racas) {
				if (raca.getCriacao().getDescricao().equals("Caprinos")) {
					racasCaprinos.add(raca);
				}
			}
		}
	}

	/**
	 * Preenche a lita racasOvinos com as racas vindo do BD
	 * @param racas
	 */
	public void ovinos(List<Raca> racas){
		racasOvinos = new ArrayList<Raca>();
		if (!racas.isEmpty()) {
			for (Raca raca : racas) {
				if (raca.getCriacao().getDescricao().equals("Ovinos")) {
					racasOvinos.add(raca);
				}
			}
		}
	}


	public List<Raca> getRacasUsuario() {
		return racasUsuario;
	}

	public void setRacasUsuario(List<Raca> racasUsuario) {
		this.racasUsuario = racasUsuario;
	}

	public List<Raca> getRacasCaprinos() {
		return racasCaprinos;
	}

	public void setRacasCaprinos(List<Raca> racasCaprinos) {
		this.racasCaprinos = racasCaprinos;
	}

	public List<Raca> getRacasOvinos() {
		return racasOvinos;
	}

	public void setRacasOvinos(List<Raca> racasOvinos) {
		this.racasOvinos = racasOvinos;
	}

	/**
	 * Deleta uma raça de caprino da lista e do BD
	 * @param raca
	 */
	public void deletaRacaCaprino(Raca raca){
		racasCaprinos.remove(raca);		
		racaFacade.deletarRaca(raca.getId());
	}


	/**
	 * Deleta uma raça de ovino da lista e do BD
	 * @param raca
	 */
	public void deletaRacaOvino(Raca raca){
		racasOvinos.remove(raca);		
		racaFacade.deletarRaca(raca.getId());
	}

}

