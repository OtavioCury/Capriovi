package br.ufpi.capriovi.managedBean.zootecnia.animal;

import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.facade.cadastros.AnimalFacade;
import br.ufpi.capriovi.facade.cadastros.RebanhoFacade;
import br.ufpi.capriovi.managedBean.BaseBeans;
import br.ufpi.capriovi.managedBean.admin.SystemSessionMB;

@Named(value = "animalMB")
@ViewScoped
public class AnimalMB extends BaseBeans{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6500704285882476184L;

	@Inject
	private AnimalFacade animalFacade;

	@Inject
	private SystemSessionMB systemSessionMB;

	@Inject
	private RebanhoFacade rebanhoFacade;

	private List<Animal> animais;	


	/**
	 * Retorna os animais de um rebanho
	 * @param rebanho
	 * @return
	 */
	public List<Animal> animaisRebanho(Rebanho rebanho){
		animais = new ArrayList<Animal>();
		animais = animalFacade.animaisRebanho(rebanho.getId());
		return animais;
	}

	/**
	 * Retorna os animais de uma fazenda
	 * @param 
	 * @return
	 */
	public List<Animal> animaisFazenda(){
		animais = new ArrayList<Animal>();
		if (systemSessionMB.getFazenda() != null) {
			animais = animalFacade.animaisPorFazenda(systemSessionMB.getFazenda().getId());
			for (Animal animal : animais) {
				animal.ajuste(180);
			}
		}		
		return animais;
	}

	/**
	 * Retorna os animais de uma lista de fazendas
	 * @param 
	 * @return
	 */
	public List<Animal> animaisFazendas(){
		animais = new ArrayList<Animal>();
		if (!systemSessionMB.getListFazendas().isEmpty()) {
			animais = animalFacade.animaisFazendas(systemSessionMB.getListFazendas());
		}		
		return animais;
	}


	/**
	 * Deleta animal da lista e do banco de dados
	 * @param animal
	 */
	@Transactional
	public void deletaAnimal(Animal animal){	

		List<Animal> filhos = new ArrayList<Animal>();

		if (animal.getSexo().getDescricao().equals("Macho")) {
			filhos = animalFacade.animaisFilhos(rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()), 
					animal.getNomeNumero(), true);
			for (Animal animalFilho : filhos) {
				animalFilho.setPai(null);
				animalFacade.atualizaAnimal(animalFilho);
			}
		}else{
			filhos = animalFacade.animaisFilhos(rebanhoFacade.listRebanhosByFazenda(systemSessionMB.getFazenda().getId()), 
					animal.getNomeNumero(), false);
			for (Animal animalFilho : filhos) {
				animalFilho.setMae(null);
				animalFacade.atualizaAnimal(animalFilho);
			}
		}


		this.animalFacade.deletarAnimal(animal.getId());
	}	

	public int getCountAnimais() {
		System.out.println(animalFacade.countTotal());
		return animalFacade.countTotal();
	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

}
