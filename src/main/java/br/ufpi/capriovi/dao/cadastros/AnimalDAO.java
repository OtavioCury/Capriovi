package br.ufpi.capriovi.dao.cadastros;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
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
import br.ufpi.capriovi.entidades.cadastros.Rebanho;
import br.ufpi.capriovi.entidades.controleAnimal.Carcaca;
import br.ufpi.capriovi.entidades.controleAnimal.DesenvolvimentoPonderal;
import br.ufpi.capriovi.entidades.controleAnimal.ManejoReprodutivo;
import br.ufpi.capriovi.entidades.controleAnimal.OcorrenciaClinica;
import br.ufpi.capriovi.entidades.relatorio.RelatorioVermifugação;


@Stateless
public class AnimalDAO extends GenericDAO<Animal, Rebanho>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3321666013070680209L;

	public AnimalDAO() {
		super(Animal.class);
	}

	/**
	 * Deleta um animal.
	 * @param id
	 */
	public void deletarAnimal(Long id){
		super.delete(id, Animal.class);
	}

	/**
	 * Salva animal.
	 * @param animal
	 */
	public void adicionaAnimal(Animal animal){
		super.save(animal);
	}

	/**
	 * Busca animal por nomeNumero
	 * @param nomeNumero
	 * @return
	 */
	public Animal buscarNomeNumero(String nomeNumero){

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> r = cq.from(Animal.class);

		cq.select(r);

		CriteriaQuery<Animal> select = cq.where(cb.equal(r.get("nomeNumero"), nomeNumero));

		TypedQuery<Animal> tq = getEm().createQuery(select);

		try {
			Animal animal = tq.getSingleResult();
			return animal;
		} catch (NoResultException nre) {
			return null;
		}
	}

	/**
	 * Lista todos os animais vivos de uma lista de rebanhos.
	 * 
	 * @param idsReb
	 * @return
	 */
	public List<Animal> animaisVivosRebanhos(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

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

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);
		return tq.getResultList();
	}

	/**
	 * Lista todos os animais de um rebanhos.
	 * 
	 * @param idReb
	 * @return
	 */
	public List<Animal> animaisRebanho(Long idReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();		

		//Rebanho
		predicados.add(cb.equal(root.get("rebanho"), idReb));

		//animal tem que estar vivo.
		//predicados.add(cb.equal(root.<String>get("status"), 1));
		query.orderBy(cb.asc(root.get("nascimento")));
		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Lista todos os animais vivos de um rebanhos.
	 * 
	 * @param idReb
	 * @return
	 */
	public List<Animal> animaisVivosRebanho(Long idReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();		

		//Rebanho
		predicados.add(cb.equal(root.get("rebanho"), idReb));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}


	/**
	 * Lista todos os relatorios vermifugação de um animal.
	 * 
	 * @param idReb
	 * @return
	 */
	public List<RelatorioVermifugação> relatorioVermifugaAnimal(Long idAnimal) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<RelatorioVermifugação> cq = cb.createQuery(RelatorioVermifugação.class);
		Root<RelatorioVermifugação> root = cq.from(RelatorioVermifugação.class);

		CriteriaQuery<RelatorioVermifugação> query = cq.select(root);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();		

		//Rebanho
		predicados.add(cb.equal(root.get("animal"), idAnimal));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<RelatorioVermifugação> tq = getEm().createQuery(query);

		return tq.getResultList();
	}	

	/**
	 * Lista animais de uma lista de rebanhos por sexo
	 * @param idsReb
	 * @param sexo
	 * @return
	 */
	public List<Animal> animaisSexo(List<Long> idsReb,int sexo) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

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

		predicados.add(cb.equal(root.<String>get("sexo"), sexo));

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

	/**
	 * Relatório Animais por entrada.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param nomeBD
	 * @param idsReb
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public List<Animal> animaisPorEntrada(List<Long> idsReb, Date dataInicio, Date dataFim) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> p = new ArrayList<Predicate>();	
		p.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = p.size()-1; i < idsReb.size(); i++, j++) {
			p.add(cb.or(p.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(p.get(idsReb.size()-1) );

		//restrição de data
		if (dataInicio != null && dataFim != null) {
			predicados.add(cb.between(root.<Date>get("dataEntrada"), dataInicio, dataFim));
		}else if (dataInicio != null && dataFim == null){
			predicados.add(cb.greaterThanOrEqualTo(root.<Date>get("dataEntrada"), dataInicio));
		}else if (dataInicio == null && dataFim != null){
			predicados.add(cb.lessThanOrEqualTo(root.<Date>get("dataEntrada"), dataFim));
		}

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{}));		

		TypedQuery<Animal> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());

		return tq.getResultList();
	}

	/**
	 * Relatório numero de crias.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param nomeBD
	 * @param idsReb
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public List<Animal> relNumeroDeCrias(List<Long> idsReb, Date dataInicio, Date dataFim) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

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

		//restrição de data
		if (dataInicio != null && dataFim != null) {
			predicados.add(cb.between(root.<Date>get("nascimento"), dataInicio, dataFim));
		}else if (dataInicio != null && dataFim == null){
			predicados.add(cb.greaterThanOrEqualTo(root.<Date>get("nascimento"), dataInicio));
		}else if (dataInicio == null && dataFim != null){
			predicados.add(cb.lessThanOrEqualTo(root.<Date>get("nascimento"), dataFim));
		}

		predicados.add(cb.isNotNull(root.get("parto")));
		//data de entrada não pode ser nula
		predicados.add(cb.isNotNull(root.get("nascimento")));
		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);		
		return tq.getResultList();
	}

	/**
	 * Reletório Número de Partos.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param idsReb
	 * @param idTipoMov
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public List<Animal> relNumeroDePartos(List<Long> idsReb,
			Date dataInicio, Date dataFim) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		//Join		
		Join<Animal, ManejoReprodutivo> joinAniManRep = root.join("manejoReprodutivoMatriz", JoinType.INNER);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> pred1 = new ArrayList<Predicate>();	
		pred1.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = pred1.size()-1; i < idsReb.size(); i++, j++) {
			pred1.add(cb.or(pred1.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(pred1.get(idsReb.size()-1) );

		//restrição de data
		if (dataInicio != null && dataFim != null) {
			predicados.add(cb.between(joinAniManRep.<Date>get("dataParto"), dataInicio, dataFim));
		}else if (dataInicio != null && dataFim == null){
			predicados.add(cb.greaterThanOrEqualTo(joinAniManRep.<Date>get("dataParto"), dataInicio));
		}else if (dataInicio == null && dataFim != null){
			predicados.add(cb.lessThanOrEqualTo(joinAniManRep.<Date>get("dataParto"), dataFim));
		}

		//femeas
		predicados.add(cb.equal(root.<String>get("sexo"), 2));		

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Relatório Conformidade Precossidade e musculatura - CPM.
	 * 
	 * @param idsReb
	 * @return
	 */
	public List<Animal> relCpm(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		//Join
		Join<Animal, DesenvolvimentoPonderal> joinAniDesPond = root.join("desenvolvimentoPonderal", JoinType.INNER);

		CriteriaQuery<Animal> query = cq.select(root);

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

		//Join
		//Animal x Movimentação Animal
		predicados.add( cb.equal(root, joinAniDesPond.get("animal")) );

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		//não pode ser nulo
		predicados.add(cb.notEqual(joinAniDesPond.<String>get("cpm"), 0));

		query.where(predicados.toArray(new Predicate[]{})).distinct(true);

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Relatório Área Olho Lombo.
	 * 
	 * @param idsReb
	 * @return
	 */
	public List<Animal> relCarcaca(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		//Join
		Join<Animal, Carcaca> joinAniCarcaca = root.join("carcaca", JoinType.INNER);

		CriteriaQuery<Animal> query = cq.select(root);

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

		//Join
		//Animal x Movimentação Animal
		predicados.add( cb.equal(root, joinAniCarcaca.get("animal")) );

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{})).distinct(true);

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Relatório Acasalamento NSGAII.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param nomeBD
	 * @param idsReb
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public List<Animal> relNsgaII(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		//Join
		Join<Animal, DesenvolvimentoPonderal> joinAniDesPond = root.join("desenvolvimentoPonderal", JoinType.INNER);

		CriteriaQuery<Animal> query = cq.select(root);

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

		query.orderBy(cb.asc(joinAniDesPond.get("data")));//testar

		//Join
		//Animal x Movimentação Animal
		predicados.add( cb.equal(root, joinAniDesPond.get("animal")) );


		query.where(predicados.toArray(new Predicate[]{})).distinct(true);

		query.orderBy(cb.asc(root.get("nascimento")));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Relatório Conformidade Precossidade e musculatura - CPM.
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

	/**
	 * Relatório femeas em idade reprodutiva.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param nomeBD
	 * @param idsReb
	 * @param idTipoMov
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public List<Animal> relFemeasIdadeReprodutiva(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> pred1 = new ArrayList<Predicate>();	
		pred1.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = pred1.size()-1; i < idsReb.size(); i++, j++) {
			pred1.add(cb.or(pred1.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(pred1.get(idsReb.size()-1) );

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		predicados.add(cb.equal(root.<String>get("sexo"), 2));

		query.orderBy(cb.asc(root.get("nascimento")));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Relatório média cio pós parto.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param nomeBD
	 * @param idsReb
	 * @param idTipoMov
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public List<Animal> relMediaCioPosParto(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		//Join		
		@SuppressWarnings("unused")
		Join<Animal, ManejoReprodutivo> joinAniManRep = root.join("manejoReprodutivoMatriz", JoinType.INNER);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> pred1 = new ArrayList<Predicate>();	
		pred1.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = pred1.size()-1; i < idsReb.size(); i++, j++) {
			pred1.add(cb.or(pred1.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(pred1.get(idsReb.size()-1) );

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		predicados.add(cb.equal(root.<String>get("sexo"), 2));

		query.orderBy(cb.asc(root.get("nascimento")));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Retorna os animais de vários rebanhos para o relatório de intervalo de gerações 
	 * @param idsReb
	 * @return
	 */
	public List<Animal> relIntervaloGeracoes(List<Long> idsReb) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> pred1 = new ArrayList<Predicate>();	
		pred1.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = pred1.size()-1; i < idsReb.size(); i++, j++) {
			pred1.add(cb.or(pred1.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(pred1.get(idsReb.size()-1) );

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.orderBy(cb.asc(root.get("nascimento")));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}

	/**
	 * Retorna os animais de um rebanhos para o relatório de intervalo de gerações 
	 * @param id
	 * @return
	 */
	public List<Animal> relIntervaloGeracoesReb(Long id) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();		

		predicados.add(cb.equal(root.get("rebanho"), id));		
		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.orderBy(cb.asc(root.get("nascimento")));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}



	public List<Animal> relIntervaloGeracoes(Long id) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> pred1 = new ArrayList<Predicate>();	

		pred1.add(cb.equal(root.get("rebanho"), id));		
		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.orderBy(cb.asc(root.get("nascimento")));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}


	/**
	 * Relatório cobertura do reprodutor.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @param idsReb
	 * @param idTipoMov
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public List<Animal> relCoberturaReprodutor(List<Long> idsReb,
			Date dataInicio, Date dataFim) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		//Join		
		Join<Animal, ManejoReprodutivo> joinAniManRep = root.join("manejoReprodutivo", JoinType.INNER);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> pred1 = new ArrayList<Predicate>();	
		pred1.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = pred1.size()-1; i < idsReb.size(); i++, j++) {
			pred1.add(cb.or(pred1.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(pred1.get(idsReb.size()-1) );

		//restrição de data
		if (dataInicio != null && dataFim != null) {
			predicados.add(cb.between(joinAniManRep.<Date>get("dataDaCobertura"), dataInicio, dataFim));
		}else if (dataInicio != null && dataFim == null){
			predicados.add(cb.greaterThanOrEqualTo(joinAniManRep.<Date>get("dataDaCobertura"), dataInicio));
		}else if (dataInicio == null && dataFim != null){
			predicados.add(cb.lessThanOrEqualTo(joinAniManRep.<Date>get("dataDaCobertura"), dataFim));
		}

		//machos
		predicados.add(cb.equal(root.<String>get("sexo"), 1));

		//animal tem que estar vivo.
		predicados.add(cb.equal(root.<String>get("status"), 1));

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}


	/**
	 * Retorna os filhos de um animal
	 * @param idsReb
	 * @param id
	 * @return
	 */
	public List<Animal> animalFilhos(List<Long> idsReb, String nome, boolean macho) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);	

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> pred1 = new ArrayList<Predicate>();	
		pred1.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = pred1.size()-1; i < idsReb.size(); i++, j++) {
			pred1.add(cb.or(pred1.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(pred1.get(idsReb.size()-1) );

		//machos
		if (macho == true) {
			predicados.add(cb.equal(root.<String>get("nomePai"), nome));
		}else{
			predicados.add(cb.equal(root.<String>get("nomeMae"), nome));
		}				

		query.where(predicados.toArray(new Predicate[]{}));

		TypedQuery<Animal> tq = getEm().createQuery(query);

		return tq.getResultList();
	}


	/**
	 * Busca um animal por nome em uma lista de rebanhos
	 * @param idsReb
	 * @param nomeNumero
	 * @return
	 */
	public Animal animalNome(List<Long> idsReb, String nomeNumero) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Animal> cq = cb.createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);

		CriteriaQuery<Animal> query = cq.select(root);

		List<Predicate> predicados = new ArrayList<Predicate>();

		//Rebanhos
		//clausula OR
		List<Predicate> p = new ArrayList<Predicate>();	
		p.add(cb.equal(root.get("rebanho"), idsReb.get(0)) );
		for (int i = 1, j = p.size()-1; i < idsReb.size(); i++, j++) {
			p.add(cb.or(p.get(j), cb.equal(root.get("rebanho"), idsReb.get(i)) ));
		}
		predicados.add(p.get(idsReb.size()-1) );

		predicados.add(cb.equal(root.<String>get("nomeNumero"), nomeNumero));

		query.where(predicados.toArray(new Predicate[]{}));		

		TypedQuery<Animal> tq = getEm().createQuery(query);

		try {
			Animal animal = tq.getSingleResult();
			return animal;
		} catch (NoResultException nre) {
			return null;
		}		
	}
}