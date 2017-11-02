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
import br.ufpi.capriovi.entidades.cadastros.Doenca;

@Stateless
public class DoencaDAO extends GenericDAO<Doenca, Doenca>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4003467720185061538L;

	public DoencaDAO() {
		super(Doenca.class);
	}

	/**
	 * Deleta a doença do BD.
	 * @param id
	 */
	public void deletarDoenca(Long id){
		super.delete(id, Doenca.class);
	}

	/**
	 * Salva a doença no BD.
	 * @param doenca
	 */
	public void adicionaDoenca(Doenca doenca){
		super.save(doenca);
	}

	public Doenca findDoencaById(Long doencaId){
		TypedQuery<Doenca> tq = getEm().createQuery("SELECT d FROM Doenca d where d.id = " + doencaId, Doenca.class);

		return tq.getSingleResult();
	}

	/**
	 * Método que retorna as doenças cadastradas pelo usuário
	 * @param id
	 * @return
	 */
	public List<Doenca> doencasUsuario(Long id){
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Doenca> cq = cb.createQuery(Doenca.class);
		Root<Doenca> root = cq.from(Doenca.class);

		CriteriaQuery<Doenca> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		predicados.add(cb.or(cb.equal(root.get("usuario"), id), cb.equal(root.<String>get("geral"), 1))) ;

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Doenca> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

	/**
	 * Retorna doencas cadastradas pelo admin
	 * @return
	 */
	public List<Doenca> doencasAdmin(){
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Doenca> cq = cb.createQuery(Doenca.class);
		Root<Doenca> root = cq.from(Doenca.class);

		CriteriaQuery<Doenca> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		predicados.add(cb.equal(root.<String>get("geral"), 1));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Doenca> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}
}
