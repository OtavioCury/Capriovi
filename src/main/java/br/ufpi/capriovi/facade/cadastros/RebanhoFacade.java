package br.ufpi.capriovi.facade.cadastros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufpi.capriovi.dao.cadastros.RebanhoDAO;
import br.ufpi.capriovi.entidades.cadastros.Fazenda;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;


@Stateless
public class RebanhoFacade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7483216931077900404L;

	@Inject
	private RebanhoDAO rebanhoDAO;

	/**
	 * Retorna os rebanhos de uma lista de fazendas
	 * @param fazendas
	 * @return
	 */
	public List<Rebanho> rebanhosFazendas(List<Fazenda> fazendas){
		List<Rebanho> rebanhos = new ArrayList<Rebanho>();
		rebanhos = rebanhoDAO.rebanhosFazendas(idsFazendas(fazendas));
		return rebanhos;
	}

	/**
	 * Busca rebanho por nome
	 * @param nome
	 * @return
	 */
	public Rebanho buscaNome(String nome){
		return rebanhoDAO.buscarNome(nome);
	}

	/**
	 * Retorna os rebanhos de uma lista de ids de fazendas
	 * @param fazendas
	 * @return
	 */
	public List<Rebanho> rebanhosFazendasIds(List<Long> ids){
		List<Rebanho> rebanhos = new ArrayList<Rebanho>();
		rebanhos = rebanhoDAO.rebanhosFazendas(ids);
		return rebanhos;
	}

	public List<Long> idsFazendas(List<Fazenda> fazendas){
		List<Long> ids = new ArrayList<Long>();

		if (fazendas != null) {
			for (Fazenda fazenda : fazendas) {
				ids.add(fazenda.getId());
			}
		}

		return ids;
	}

	/**
	 * Retorna os rebanhos de uma fazenda
	 * @param fazendas
	 * @return
	 */
	public List<Rebanho> listRebanhosByFazenda(Long id){
		List<Rebanho> result = rebanhoDAO.rebanhoFazenda(id);
		return result;
	}

	/**
	 * 
	 * @param id
	 */
	public void deletarRebanho(Long id){
		rebanhoDAO.deletarRebanho(id);
	}
	/**
	 * 
	 * @param Rebanho
	 */
	public void atualizaRebanho(Rebanho Rebanho){
		rebanhoDAO.update(Rebanho);
	}
	/**
	 * 
	 * @param Rebanho
	 */
	public void adicionaRebanho(Rebanho Rebanho){
		rebanhoDAO.adicionaRebanho(Rebanho);
	}
}
