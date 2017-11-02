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
import br.ufpi.capriovi.entidades.cadastros.Doenca;
import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;

@Stateless
public class OcorrenciaClinicaDAO extends GenericDAO<OcorrenciaClinica, Animal>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6370855804652326782L;

	public OcorrenciaClinicaDAO() {
		super(OcorrenciaClinica.class);
	}

	/**
	 * Deleta o ocorrência clinicado.
	 * @param id
	 */
	public void deletarOcorrenciaClinica(Long id){
		super.delete(id, OcorrenciaClinica.class);
	}

	/**
	 * Salva o ocorrência clínica.
	 * @param OcorrenciaClinicas
	 */
	public void adicionaOcorrenciaClinica(OcorrenciaClinica ocorrenciaClinica){
		super.save(ocorrenciaClinica);
	}

	/**
	 * Lista ocorrências clinicas de uma lista de rebanhos
	 * @param idsReb
	 * @return
	 */
	public List<OcorrenciaClinica> ocorrenciasRebanhos(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<OcorrenciaClinica> cq = cb.createQuery(OcorrenciaClinica.class);
		Root<OcorrenciaClinica> root = cq.from(OcorrenciaClinica.class);

		CriteriaQuery<OcorrenciaClinica> query = cq.select(root);

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

		TypedQuery<OcorrenciaClinica> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

	/**
	 * Relatório Ocorrencias Clinicas.
	 * 
	 * @param idsReb
	 * @return
	 */
	public List<OcorrenciaClinica> relOcorrenciaClinica(List<Long> idsReb, Date dataInicio, Date dataFim, Doenca doenca) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<OcorrenciaClinica> cq = cb.createQuery(OcorrenciaClinica.class);
		Root<OcorrenciaClinica> root = cq.from(OcorrenciaClinica.class);

		//Join
		Join<Animal, OcorrenciaClinica> joinAniOcoCli = root.join("animal", JoinType.INNER);

		CriteriaQuery<OcorrenciaClinica> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> p = new ArrayList<Predicate>();	
		p.add(cb.equal(joinAniOcoCli.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = p.size()-1; i < idsReb.size(); i++, j++) {
			p.add(cb.or(p.get(j), cb.equal(joinAniOcoCli.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(p.get(idsReb.size()-1) );

		//Join
		//Animal x ocorrencia Clinica
		predicados.add( cb.equal(joinAniOcoCli, root.get("animal")) );

		//restrição de data
		if (dataInicio != null && dataFim != null) {
			predicados.add(cb.between(root.<Date>get("data"), dataInicio, dataFim));
		}else if (dataInicio != null && dataFim == null){
			predicados.add(cb.greaterThanOrEqualTo(root.<Date>get("data"), dataInicio));
		}else if (dataInicio == null && dataFim != null){
			predicados.add(cb.lessThanOrEqualTo(root.<Date>get("data"), dataFim));
		}

		if(doenca != null){
			predicados.add(cb.equal(root.<String>get("doenca"), doenca.getId()));
		}

		//animal tem que estar vivo.
		predicados.add(cb.equal(joinAniOcoCli.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{})).distinct(true);

		TypedQuery<OcorrenciaClinica> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

}
