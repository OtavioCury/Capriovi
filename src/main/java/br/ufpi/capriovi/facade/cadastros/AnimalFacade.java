package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.dao.cadastros.AnimalDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.cadastros.Fazenda;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;



@Stateless
public class AnimalFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1119934125575493450L;

	@Inject
	private AnimalDAO animalDAO;

	@Inject
	private RebanhoFacade rebanhoFacade;

	/**
	 * 
	 * @return A quantidade de Animais cadastrados.
	 */
	public int countTotal() {
		return GenericDAO.getCountTable();
	}	

	/**
	 * Busca um animal por nomeNumero
	 * @param nomeNumero
	 * @return
	 */
	public Animal animalNome(String nomeNumero){
		return animalDAO.buscarNomeNumero(nomeNumero);
	}

	/**
	 * Retorna todos os animais de um rebanho
	 * @param id
	 * @return
	 */
	public List<Animal> animaisRebanho(Long id) {
		List<Animal> result = animalDAO.listAll("rebanho", id);
		return result;
	}

	/**
	 * Lista todos os animais de um usuário por sexo
	 * @param rebanhos
	 * @param sexo
	 * @return
	 */
	public List<Animal> listAllSexo(List<Rebanho> rebanhos, int sexo) {
		List<Animal> result = new ArrayList<Animal>();

		if (!rebanhos.isEmpty()) {
			result = animalDAO.animaisSexo(getIdsRebanho(rebanhos), sexo);
		}

		return result;
	}

	/**
	 * Lista todos os animais de um usuário
	 * @param rebanhos
	 * @return
	 */
	public List<Animal> animaisRebanhos(List<Rebanho> rebanhos) {
		List<Animal> result = new ArrayList<Animal>();

		if (!rebanhos.isEmpty()) {
			result = animalDAO.animaisVivosRebanhos(getIdsRebanho(rebanhos));
		}

		return result;
	}

	/**
	 * Retorna os ids de uma lista de rebanhos
	 * @param rebanhos
	 * @return
	 */
	public List<Long> getIdsRebanho(List<Rebanho> rebanhos){
		List<Long> ids = new ArrayList<Long>();
		for (Rebanho rebanho : rebanhos) {
			ids.add(rebanho.getId());
		}
		return ids;
	}

	/**
	 * Retorna os animais de uma fazenda
	 * @param idFazenda
	 * @return
	 */
	public List<Animal> animaisPorFazenda(Long idFazenda){
		return animaisRebanhos(rebanhoFacade.listRebanhosByFazenda(idFazenda));
	}

	/**
	 * Retorna os animais de uma lista de fazendas
	 * @param idFazenda
	 * @return
	 */
	public List<Animal> animaisFazendas(List<Fazenda> fazendas){
		return animaisRebanhos(rebanhoFacade.rebanhosFazendas(fazendas));
	}

	/**
	 * Retorna os animais de uma lista de ids de fazendas
	 * @param idFazenda
	 * @return
	 */
	public List<Animal> animaisFazendasIds(List<Long> ids){
		return animaisRebanhos(rebanhoFacade.rebanhosFazendasIds(ids));
	}

	/**
	 * 
	 * @param id
	 */
	public void deletarAnimal(Long id){				
		animalDAO.deletarAnimal(id);
	}

	/**
	 * Atualiza um animal
	 * @param Animal
	 */
	public void atualizaAnimal(Animal animal){		
		animalDAO.update(animal);	
	}

	/**
	 * Adiciona um animal
	 * @param Animal
	 * 
	 */
	public void adicionaAnimal(Animal animal){
		animalDAO.adicionaAnimal(animal);
	}

	/**
	 * Testa se já existe um animal cadastrado em uma fazenda com o mesmo Nome/Número
	 * @param nome
	 * @param rebanhos
	 * 
	 */
	public boolean nomePresente(Animal animal, List<Rebanho> rebanhos, boolean adicionar){
		if (adicionar == true) {
			if (animalDAO.animalNome(getIdsRebanho(rebanhos), animal.getNomeNumero()) != null) {
				return true;
			}else{
				return false;
			}
		}else{
			if (animalDAO.animalNome(getIdsRebanho(rebanhos), animal.getNomeNumero()) != null &&
					(animalDAO.animalNome(getIdsRebanho(rebanhos), animal.getNomeNumero()).getId() != animal.getId())) {
				return true;
			}else{
				return false;
			}
		}		
	}

	/**
	 * Retorna os filhos de um animal
	 * @param rebanhos
	 * @param id
	 * @return
	 */
	public List<Animal> animaisFilhos(List<Rebanho> rebanhos, String nome, boolean macho){
		return animalDAO.animalFilhos(getIdsRebanho(rebanhos), nome, macho);
	}
}
