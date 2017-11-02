package br.ufpi.capriovi.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortMeta;

public class GenericDAO<T, S> implements Serializable {

	private static final long serialVersionUID = 1L;

	private static int countTable;

	@PersistenceContext(unitName = "CaprioviCRUDPrimefaces")
	private EntityManager em;

	private Class<T> entityClass;

	public GenericDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	public EntityManager getEm() {
		return em;
	}

	public T update(T entity) {
		return em.merge(entity);
	}

	public T find(long entityID) {
		return em.find(entityClass, entityID);
	}
	
	public List<T> findList(List<Long> ids){
		List<T> list = new ArrayList<T>();
		for (Long id : ids) {
			list.add(find(id));
		}
		return list;
	}

	protected void delete(Object id, Class<T> classe) {
		T entityToBeRemoved = em.find(classe, id);
		em.remove(entityToBeRemoved);
	}

	public void save(T entity) {
		em.persist(entity);
	}    

	public List<T> findAll() {
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		@SuppressWarnings("unused")
		Root<T> rootU = cq.from(entityClass);
		TypedQuery<T> query = getEm().createQuery(cq);

		return query.getResultList();
	}    	

	/**
	 * Metodo para carregar o dados de forma lazy.
	 * 
	 * @param first
	 * @param pageSize
	 * @param multiSortMeta
	 * @param filters
	 * @return
	 */
	public List<T> listLazyMultSort(int first, int pageSize,
			List<SortMeta> multiSortMeta, Map<String, Object> filters) {

		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		Root<T> r = cq.from(entityClass);
		CriteriaQuery<T> query = cq.select(r);

		/* filtros para pesquisa para cada coluna*/
		List<Predicate> predicados = new ArrayList<Predicate>();

		if (filters != null) {
			for (String key : filters.keySet()){
				predicados.add(cb.like(r.<String>get(""+key), (filters.get(key) + "%")));
			}	
		}
		query.where(predicados.toArray(new Predicate[]{}));

		/*Order By*/
		if (multiSortMeta != null) {
			for (SortMeta sortMeta : multiSortMeta) {
				if(sortMeta.getSortOrder().ordinal()==0){
					query.orderBy(cb.asc(r.get(""+sortMeta.getSortField())));
				}else if(sortMeta.getSortOrder().ordinal()==1){
					query.orderBy(cb.desc(r.get(""+sortMeta.getSortField())));

				}
			}
		}

		TypedQuery<T> tq = getEm().createQuery(query);
		setCountTable(getEm().createQuery(query).getResultList().size());
		tq.setFirstResult(first);
		tq.setMaxResults(pageSize);

		return tq.getResultList();
	}	

	public List<T> listAll(String nomeBD, Long id) {	
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);

		Root<T> r = cq.from(entityClass);
		Join<T, S> idS = r.join(nomeBD);

		CriteriaQuery<T> select = cq.select(r).where(cb.equal(idS.get("id"), id));

		TypedQuery<T> tq = getEm().createQuery(select);

		return tq.getResultList();
	}	

	public static int getCountTable() {
		return countTable;
	}

	public static void setCountTable(int countTable) {
		GenericDAO.countTable = countTable;
	}		

}
