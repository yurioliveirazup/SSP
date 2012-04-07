package edu.sinclair.ssp.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import edu.sinclair.ssp.model.ObjectStatus;
import edu.sinclair.ssp.model.Person;

/**
 * CRUD methods for the Person model.
 * <p>
 * Default sort order is <code>lastName</code> then <code>firstName</code>.
 */
@Repository
public class PersonDao extends AbstractAuditableCrudDao<Person> implements
		AuditableCrudDao<Person> {

	/**
	 * Constructor
	 */
	public PersonDao() {
		super(Person.class);
	}

	/**
	 * Return all entities in the database, filtered only by the specified
	 * parameters. Sorted by <code>lastName</code> then <code>firstName</code>.
	 * 
	 * @param status
	 *            Object status filter
	 * @return All entities in the database, filtered only by the specified
	 *         parameters.
	 */
	@Override
	public List<Person> getAll(ObjectStatus status) {
		return getAll(status, null, null, null, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Person> getAll(ObjectStatus status, Integer firstResult,
			Integer maxResults, String sort, String sortDirection) {

		Criteria criteria = createCriteria();

		if (firstResult != null && firstResult.intValue() >= 0) {
			criteria.setFirstResult(firstResult);
		}

		if (maxResults != null && maxResults.intValue() > 0) {
			criteria.setMaxResults(maxResults);
		}

		if (StringUtils.isEmpty(sort)) {
			criteria.addOrder(Order.asc("lastName")).addOrder(
					Order.asc("firstName"));
		} else {
			criteria = addOrderToCriteria(criteria, sort, sortDirection);
		}

		if (status != ObjectStatus.ALL) {
			criteria.add(Restrictions.eq("objectStatus", status));
		}

		return criteria.list();
	}

	public Person fromUsername(String username) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(
				Person.class);
		query.add(Restrictions.eq("username", username));
		return (Person) query.uniqueResult();
	}

	public Person fromUserId(String userId) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(
				Person.class);
		query.add(Restrictions.eq("userId", userId));
		return (Person) query.uniqueResult();
	}
}
