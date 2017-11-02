package br.ufpi.capriovi.dao.cadastros;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Fazenda;
import br.ufpi.capriovi.entidades.cadastros.Pecuarista;



@Stateless
public class FazendaDAO extends GenericDAO<Fazenda, Pecuarista> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -47438445229033598L;

	public FazendaDAO() {
		super(Fazenda.class);
	}

	/**
	 * Deleta a fazenda do BD.
	 * @param id
	 */
	public void deletarFazenda(Long id){
		super.delete(id, Fazenda.class);
	}

	/**
	 * Salva a fazenda no BD.
	 * @param Fazenda
	 */
	public void adicionaFazenda(Fazenda fazenda){
		super.save(fazenda);
	}
	
	/**
	 * Acha uma fazenda de acordo com o id
	 * @param id
	 * @return
	 */
	public Fazenda findFazenda(Long id){
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Fazenda> cq = cb.createQuery(Fazenda.class);

		Root<Fazenda> r = cq.from(Fazenda.class);

		cq.select(r);

		CriteriaQuery<Fazenda> select = cq.where(cb.equal(r.get("id"), id));

		TypedQuery<Fazenda> tq = getEm().createQuery(select);
		Fazenda fazenda = tq.getSingleResult();

		return fazenda;
	}
}
