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
import br.ufpi.capriovi.entidades.cadastros.Medicamento;
import br.ufpi.capriovi.entidades.controleAnimal.Vacinacao;

@Stateless
public class MedicamentoDAO extends GenericDAO<Medicamento, Vacinacao>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7046380014248777022L;

	public MedicamentoDAO() {
		super(Medicamento.class);
	}

	/**
	 * Deleta um medicamento do BD.
	 * @param id
	 */
	public void deletarMedicamento(Long id){
		super.delete(id, Medicamento.class);
	}

	/**
	 * Salva um medicamento no BD.
	 * @param medicamento
	 */
	public void adicionaMedicamento(Medicamento medicamento){
		super.save(medicamento);
	}

	/**
	 * Método que retorna os medicamentos cadastrados por um usuário
	 * @param id
	 * @return
	 */
	public List<Medicamento> medicamentosUsuario(Long id){
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Medicamento> cq = cb.createQuery(Medicamento.class);
		Root<Medicamento> root = cq.from(Medicamento.class);

		CriteriaQuery<Medicamento> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		predicados.add(cb.equal(root.get("usuario"), id));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Medicamento> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}
}
