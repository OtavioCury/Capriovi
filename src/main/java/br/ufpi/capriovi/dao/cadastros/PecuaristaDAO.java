package br.ufpi.capriovi.dao.cadastros;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.ufpi.capriovi.dao.GenericDAO;
import br.ufpi.capriovi.entidades.cadastros.Pecuarista;



@Stateless
public class PecuaristaDAO extends GenericDAO<Pecuarista, Pecuarista>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4645271611110209747L;

	public PecuaristaDAO() {
		super(Pecuarista.class);
	}

	/**
	 * Busca um pecuarista por username
	 * @param username
	 * @return
	 */
	public Pecuarista buscarId(String username){

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Pecuarista> cq = cb.createQuery(Pecuarista.class);

		Root<Pecuarista> r = cq.from(Pecuarista.class);

		cq.select(r);


		CriteriaQuery<Pecuarista> select = cq.where(cb.equal(r.get("username"), username));

		TypedQuery<Pecuarista> tq = getEm().createQuery(select);
		Pecuarista pecuarista = tq.getSingleResult();

		return pecuarista;
	}

	/**
	 * Deleta o pecuarista do BD.
	 * @param id
	 */
	public void deletarPecuarista(Long id){
		super.delete(id, Pecuarista.class);
	}

	/**
	 * Salva o pecuarista no BD.
	 * @param Pecuarista
	 */
	public void adicionaPecuarista(Pecuarista Pecuarista){
		super.save(Pecuarista);
	}

}
