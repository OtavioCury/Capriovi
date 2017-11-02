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
import br.ufpi.capriovi.entidades.controleAnimal.Vacinacao;

@Stateless
public class VacinacaoDAO extends GenericDAO<Vacinacao, Vacinacao>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VacinacaoDAO() {
		super(Vacinacao.class);
	}

	/**
	 * Deleta o vacinação.
	 * @param id
	 */
	public void deletarVacinacao(Long id){
		super.delete(id, Vacinacao.class);
	}

	/**
	 * Salva o vacinação.
	 * @param Vacinacao
	 */
	public void adicionaVacinacao(Vacinacao vacinacao){
		super.save(vacinacao);
	}

	/**
	 * Método que retorna as vacinações cadastradas por um usuário
	 * @param id
	 * @return
	 */
	public List<Vacinacao> vacinacaoUsuario(Long id){
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Vacinacao> cq = cb.createQuery(Vacinacao.class);
		Root<Vacinacao> root = cq.from(Vacinacao.class);

		CriteriaQuery<Vacinacao> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		predicados.add(cb.equal(root.get("usuario"), id));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Vacinacao> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}


}
