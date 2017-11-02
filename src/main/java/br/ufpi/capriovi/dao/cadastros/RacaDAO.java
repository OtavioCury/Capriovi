package br.ufpi.capriovi.dao.cadastros;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Raca;

@Stateless
public class RacaDAO extends GenericDAO<Raca, Raca>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4794598416210344315L;

	public RacaDAO(){
		super(Raca.class);
	}

	/**
	 * Deleta uma raça do BD.
	 * @param id
	 */
	public void deletarRaca(Long id){
		super.delete(id, Raca.class);
	}

	/**
	 * Salva uma raça no BD.
	 * @param raca
	 */
	public void adicionaRaca(Raca raca){
		super.save(raca);
	}

	/**
	 * Método que retorna as raças cadastradas pelo usuário
	 * @param id
	 * @return
	 */
	public List<Raca> racasUsuario(Long id){
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Raca> cq = cb.createQuery(Raca.class);
		Root<Raca> root = cq.from(Raca.class);

		CriteriaQuery<Raca> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		predicados.add(cb.or(cb.equal(root.get("usuario"), id), cb.equal(root.<String>get("geral"), 1))) ;

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Raca> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}
	
	/**
	 * Retorna raças cadastradas pelo admin
	 * @return
	 */
	public List<Raca> racasAdmin(){
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Raca> cq = cb.createQuery(Raca.class);
		Root<Raca> root = cq.from(Raca.class);

		CriteriaQuery<Raca> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		predicados.add(cb.equal(root.<String>get("geral"), 1));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Raca> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}
}
