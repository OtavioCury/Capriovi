package br.ufpi.capriovi.managedBean.controleAnimal.manejoReprodutivo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.facade.controleAnimal.ManejoReprodutivoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@Named(value = "manejoReprodutivoMB")
@ViewScoped
public class ManejoReprodutivoMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5311751289468579949L;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	@Inject
	private ManejoReprodutivoFacade manejoReprodutivoFacade;

	private List<ManejoReprodutivo> manejos;


	/**
	 * carrega os dados da tabela.
	 */
	@PostConstruct
	public void init() {		
		manejos = new ArrayList<ManejoReprodutivo>();
		if (systemSessionMB.getFazenda() != null) {
			this.manejos = manejoReprodutivoFacade.listarPorRebanho(
					rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()));
		}		
	}

	/**
	 * retorna todos os manejos reprodutivos de todas as fazendas do usuário
	 * @return
	 */
	public List<ManejoReprodutivo> manejosFazendas(){
		manejos = new ArrayList<ManejoReprodutivo>();	
		if (!systemSessionMB.getListFazendas().isEmpty()) {
			this.manejos = manejoReprodutivoFacade.listarPorRebanho(rebanhoFacade.rebanhosFazendas(systemSessionMB.getListFazendas()));
		}		
		return manejos;
	}

	/**
	 * Deleta menejo da lista e do banco de dados
	 * @param manejo
	 */
	public void deletaManejo(ManejoReprodutivo manejo){
		manejos.remove(manejo);
		manejoReprodutivoFacade.deletarManejoReprodutivo(manejo.getId());
	}

	public List<ManejoReprodutivo> getManejos() {
		return manejos;
	}


	public void setManejos(List<ManejoReprodutivo> manejos) {
		this.manejos = manejos;
	}


	public ManejoReprodutivoFacade getManejoReprodutivoFacade() {
		return manejoReprodutivoFacade;
	}


	public void setManejoReprodutivoFacade(ManejoReprodutivoFacade manejoReprodutivoFacade) {
		this.manejoReprodutivoFacade = manejoReprodutivoFacade;
	}

}
