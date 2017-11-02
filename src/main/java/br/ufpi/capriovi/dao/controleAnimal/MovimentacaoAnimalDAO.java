package br.ufpi.capriovi.dao.controleAnimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Animal;
import br.ufpi.capriovi.entidades.controleAnimal.MovimentacaoAnimal;

@Stateless
public class MovimentacaoAnimalDAO extends GenericDAO<MovimentacaoAnimal, Animal>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6370855804652326782L;

	public MovimentacaoAnimalDAO() {
		super(MovimentacaoAnimal.class);
	}

	/**
	 * Deleta o movimentação animal.
	 * @param id
	 */
	public void deletarMovimentacaoAnimal(Long id){
		super.delete(id, MovimentacaoAnimal.class);
	}

	/**
	 * Salva o movimentacao animal.
	 * @param MovimentacaoAnimal
	 */
	public void adicionaMovimentacaoAnimal(MovimentacaoAnimal movimentacaoAnimal){
		super.save(movimentacaoAnimal);
	}

	/**
	 * Lista as movimentação animais de ums lista de rebanhos
	 * @param idsReb
	 * @return
	 */
	public List<MovimentacaoAnimal> movimentosRebs(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<MovimentacaoAnimal> cq = cb.createQuery(MovimentacaoAnimal.class);
		Root<MovimentacaoAnimal> root = cq.from(MovimentacaoAnimal.class);

		CriteriaQuery<MovimentacaoAnimal> query = cq.select(root);

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

		TypedQuery<MovimentacaoAnimal> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

	/**
	 * Lista as movimentação animais de ums lista de rebanhos, com filtros de data e motivo
	 * @param idsReb
	 * @return
	 */
	public List<MovimentacaoAnimal> relMovimentacao(List<Long> idsReb, Integer idTipoMov, 
			Date dataInicio, Date dataFim) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<MovimentacaoAnimal> cq = cb.createQuery(MovimentacaoAnimal.class);
		Root<MovimentacaoAnimal> root = cq.from(MovimentacaoAnimal.class);

		CriteriaQuery<MovimentacaoAnimal> query = cq.select(root);

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

		if(idTipoMov > 0){
			predicados.add(cb.equal(root.<String>get("motivoSaida"), idTipoMov));
		}

		//restrição de data
		if (dataInicio != null && dataFim != null) {
			predicados.add(cb.between(root.<Date>get("data"), dataInicio, dataFim));
		}else if (dataInicio != null && dataFim == null){
			predicados.add(cb.greaterThanOrEqualTo(root.<Date>get("data"), dataInicio));
		}else if (dataInicio == null && dataFim != null){
			predicados.add(cb.lessThanOrEqualTo(root.<Date>get("data"), dataFim));
		}

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<MovimentacaoAnimal> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

}
