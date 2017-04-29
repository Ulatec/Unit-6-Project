package com.worldbankdata;

import com.worldbankdata.model.Country;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by mark on 4/25/2017.
 */
public class SessionManager {
    private SessionFactory sessionFactory;
    // Hold a reusable reference to a SessionFactory (since we need only one)


    private static SessionFactory buildSessionFactory(){
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build() ;
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    public SessionManager(){
        sessionFactory = buildSessionFactory();
    }
    public List<Country> fetchAllCountries(){
        //Open a session
        Session session = sessionFactory.openSession();
        // Create criteria
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Country> criteria = builder.createQuery(Country.class);

        criteria.from(Country.class);
//        Criteria criteria = session.createCriteria(Country.class);
        // Get a list of Countries objects according to the Criteria object
        List<Country> countries = session.createQuery(criteria).getResultList();
        //Close a session
        session.close();
        countries.stream().forEach(System.out::println);
        System.out.println(countries.size());
        return countries;
    }
    public String test(){
        return "hello";
    }
}
