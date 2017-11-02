package br.ufpi.capriovi.dao.cadastros;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Fazenda;
import br.ufpi.capriovi.entidades.cadastros.Rebanho;


@Stateless
public class RebanhoDAO extends GenericDAO<Rebanho, Fazenda>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6894825551894786284L;

	public RebanhoDAO() {
		super(Rebanho.class);
	}

	/**
	 * Deleta o Rebanho do BD.
	 * @param id
	 */
	public void deletarRebanho(Long id){
		super.delete(id, Rebanho.class);
	}

	/**
	 * Salva o Rebanho no BD.
	 * @param Rebanho
	 */
	public void adicionaRebanho(Rebanho Rebanho){
		super.save(Rebanho);
	}

	/**
	 * Retorna uma lista de rebanhos de acordo com os ids
	 * @param ids
	 * @return
	 */
	public List<Rebanho> listaRebanhosIds(List<Long> ids){
		return findList(ids);
	}

	/**
	 * Busca rebanho por nome
	 * @param nome
	 * @return
	 */
	public Rebanho buscarNome(String nome){

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Rebanho> cq = cb.createQuery(Rebanho.class);
		Root<Rebanho> r = cq.from(Rebanho.class);

		cq.select(r);

		CriteriaQuery<Rebanho> select = cq.where(cb.equal(r.get("nome"), nome));

		TypedQuery<Rebanho> tq = getEm().createQuery(select);

		try {
			Rebanho rebanho = tq.getSingleResult();
			return rebanho;
		} catch (NoResultException nre) {
			return null;
		}
	}

	/**
	 * Lista os rebanhos de uma fazenda
	 * @param idFazenda
	 * @return
	 */
	public List<Rebanho> rebanhoFazenda(Long idFazenda) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Rebanho> cq = cb.createQuery(Rebanho.class);
		Root<Rebanho> root = cq.from(Rebanho.class);

		CriteriaQuery<Rebanho> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();
		predicados.add(cb.equal(root.get("fazenda"), idFazenda) );
		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Rebanho> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Lista os rebanhos de uma lista de fazendas
	 * @param idsFazenda
	 * @return
	 */
	public List<Rebanho> rebanhosFazendas(List<Long> idsFazenda) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Rebanho> cq = cb.createQuery(Rebanho.class);
		Root<Rebanho> root = cq.from(Rebanho.class);

		CriteriaQuery<Rebanho> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> p = new ArrayList<Predicate>();	

		p.add(cb.equal(root.get("fazenda"), idsFazenda.get(0)) );
		for (int i = 1, j = p.size()-1; i < idsFazenda.size(); i++, j++) {
			p.add(cb.or(p.get(j), cb.equal(root.get("fazenda"), idsFazenda.get(i)) ));
		}
		predicados.add(p.get(idsFazenda.size()-1) );

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Rebanho> tq = getEm().createQuery(query);		

		return tq.getResultList();
	}
}
