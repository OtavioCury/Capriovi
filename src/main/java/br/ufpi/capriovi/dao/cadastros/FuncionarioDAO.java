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
import br.ufpi.capriovi.entidades.cadastros.Funcionario;


@Stateless
public class FuncionarioDAO extends GenericDAO<Funcionario, Funcionario>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8698694660849812765L;

	public FuncionarioDAO() {
		super(Funcionario.class);
	}

	/**
	 * Deleta um funcionario do BD.
	 * @param id
	 */
	public void deletarFuncionario(Long id){
		super.delete(id, Funcionario.class);
	}

	/**
	 * Deleta um funcionário do BD
	 * @param funcionario
	 */
	public void adicionaFuncionario(Funcionario funcionario){
		super.save(funcionario);
	}
	
	/**
	 * Lista os funcionários de uma lista de Fazendas
	 * @param idsFazendas
	 * @return
	 */
	public List<Funcionario> listFuncionariosFazenda(List<Long> idsFazendas) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Funcionario> cq = cb.createQuery(Funcionario.class);
		Root<Funcionario> root = cq.from(Funcionario.class);

		CriteriaQuery<Funcionario> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();

		//clausula OR
		List<Predicate> p = new ArrayList<Predicate>();	
		p.add(cb.equal(root.get("fazenda"), idsFazendas.get(0)) );
		for (int i = 1, j = p.size()-1; i < idsFazendas.size(); i++, j++) {
			p.add(cb.or(p.get(j), cb.equal(root.get("fazenda"), idsFazendas.get(i)) ));
		}
		predicados.add(p.get(idsFazendas.size()-1) );

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Funcionario> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

}
