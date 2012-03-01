package edu.sinclair.ssp.dao;

import java.util.UUID;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.sinclair.ssp.model.Person;

@Repository
public class PersonDao {

	//private static final Logger logger = LoggerFactory.getLogger(PersonDao.class);

	@Autowired
	private SessionFactory sessionFactory;

	public Person get(UUID id) {
		return (Person) this.sessionFactory.getCurrentSession().get(Person.class, id); 
	}

}
