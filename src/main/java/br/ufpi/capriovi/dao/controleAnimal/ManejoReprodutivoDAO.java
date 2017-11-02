package br.ufpi.capriovi.dao.controleAnimal;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;

@Stateless
public class ManejoReprodutivoDAO extends GenericDAO<ManejoReprodutivo, Doenca>{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5835280679311080874L;

	public ManejoReprodutivoDAO() {
		super(ManejoReprodutivo.class);
	}

	/**
	 * Deleta o manejo reprodutivodo.
	 * @param id
	 */
	public void deletarManejoReprodutivo(Long id){
		super.delete(id, ManejoReprodutivo.class);
	}

	/**
	 * Salva um manejo reprodutivo.
	 * @param ManejoReprodutivo
	 */
	public void adicionaManejoReprodutivo(ManejoReprodutivo manejoReprodutivo){
		super.save(manejoReprodutivo);
	}
	
	/**
	 * Lista os manejos reprodutivos de uma lista de rebanhos
	 * @param idsReb
	 * @return
	 */
	public List<ManejoReprodutivo> manejosRebs(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<ManejoReprodutivo> cq = cb.createQuery(ManejoReprodutivo.class);
		Root<ManejoReprodutivo> root = cq.from(ManejoReprodutivo.class);

		CriteriaQuery<ManejoReprodutivo> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> p = new ArrayList<Predicate>();	
		p.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = p.size()-1; i < idsReb.size(); i++, j++) {
			p.add(cb.or(p.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(p.get(idsReb.size()-1) );

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<ManejoReprodutivo> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

}
