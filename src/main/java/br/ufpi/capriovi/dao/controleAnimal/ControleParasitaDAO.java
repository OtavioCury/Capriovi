package br.ufpi.capriovi.dao.controleAnimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.controleAnimal.ControleParasita;

@Stateless
public class ControleParasitaDAO extends GenericDAO<ControleParasita, ControleParasita>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ControleParasitaDAO() {
		super(ControleParasita.class);
	}

	/**
	 * Deleta o controle parasitário.
	 * @param id
	 */
	public void deletarControleParazita(Long id){
		super.delete(id, ControleParasita.class);
	}

	/**
	 * Salva o controle parasitário.
	 * @param ControleParasita
	 */
	public void adicionaControleParazita(ControleParasita controleParazita){
		super.save(controleParazita);
	}
	
	/**
	 * Lista os controles parasitas de uma lista de rebanhos
	 * @param idsReb
	 * @return
	 */
	public List<ControleParasita> controlesRebanho(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<ControleParasita> cq = cb.createQuery(ControleParasita.class);
		Root<ControleParasita> root = cq.from(ControleParasita.class);

		CriteriaQuery<ControleParasita> query = cq.select(root);

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

		TypedQuery<ControleParasita> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}
	
	/**
	 * Relatório Controle Parasitario.
	 * 
	 * @param idsReb
	 * @return
	 */
	public List<ControleParasita> relControleParasitario(List<Long> idsReb, Date dataInicio, Date dataFim) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<ControleParasita> cq = cb.createQuery(ControleParasita.class);
		Root<ControleParasita> root = cq.from(ControleParasita.class);

		//Join
		Join<Animal, ControleParasita> joinAniParasitario = root.join("animal", JoinType.INNER);

		CriteriaQuery<ControleParasita> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> p = new ArrayList<Predicate>();	
		p.add(cb.equal(joinAniParasitario.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = p.size()-1; i < idsReb.size(); i++, j++) {
			p.add(cb.or(p.get(j), cb.equal(joinAniParasitario.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(p.get(idsReb.size()-1) );

		//Join
		//Animal x ocorrencia Clinica
		predicados.add( cb.equal(joinAniParasitario, root.get("animal")) );
		
		//restrição de data
		if (dataInicio != null && dataFim != null) {
			predicados.add(cb.between(root.<Date>get("dataVernifugacao"), dataInicio, dataFim));
		}else if (dataInicio != null && dataFim == null){
			predicados.add(cb.greaterThanOrEqualTo(root.<Date>get("dataVernifugacao"), dataInicio));
		}else if (dataInicio == null && dataFim != null){
			predicados.add(cb.lessThanOrEqualTo(root.<Date>get("dataVernifugacao"), dataFim));
		}
		
		//animal tem que estar vivo.
		predicados.add(cb.equal(joinAniParasitario.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{})).distinct(true);

		TypedQuery<ControleParasita> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

}
