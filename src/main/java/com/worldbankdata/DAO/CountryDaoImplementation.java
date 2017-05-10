package com.worldbankdata.DAO;

import com.worldbankdata.model.Country;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CountryDaoImplementation implements CountryDAO {
    //private SessionFactory sessionFactory;
    // Hold a reusable reference to a SessionFactory (since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build() ;
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }
    public CountryDaoImplementation(){

    }
    @SuppressWarnings("deprecation")
    public List<Country> fetchAllCountries(){
        // Open a session
        Session session = sessionFactory.openSession();

        //Initialize CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        //Create CriteriaQuery
        CriteriaQuery<Country> criteria = builder.createQuery(Country.class);

        //Specify criteria
        criteria.from(Country.class);

        //Execute query
        List<Country> countries = session.createQuery(criteria).getResultList();

        // Close the session
        session.close();

        return countries;
    }
    public void save(Country country){

        //Open a session
        Session session = sessionFactory.openSession();
        //Begin a transaction
        session.beginTransaction();
        //Use the session to save the contact
        String id = (String) session.save(country);
        //Commit the transaction
        session.getTransaction().commit();
        // Close the session
        session.close();
    }
    private List<Country> nonNullCountries(){
        return fetchAllCountries().stream().filter( country -> country.getInternetUsers() != null && country.getAdultLiteracyRate() != null).collect(Collectors.toList());
    }


    public Country findCountryByCode(String code){
        //Open a session
        Session session = sessionFactory.openSession();
        //Retrieve the persistent object (or null if not found)
        Country country = session.get(Country.class, code);
        // Close the session
        session.close();
        //Return the object
        return country;
    }
    public void delete(Country country){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        //Retrieve the persistent object (or null if not found)
        session.delete(country);
        session.getTransaction().commit();
        session.close();
    }
    public void update(Country country){
        //Open a session
        Session session = sessionFactory.openSession();
        //Begin a transaction
        session.beginTransaction();
        //Use the session to update the contact
        session.update(country);
        //Commit the transaction
        session.getTransaction().commit();
        //Close the session
        session.close();
    }
    public Country getMaxInternetUsers(){
        return nonNullCountries().stream()
                .max(Comparator.comparingDouble(Country::getInternetUsers))
                .get();
    }

    public Country getMinInternetUsers() {
        return nonNullCountries().stream()
                .min(Comparator.comparingDouble(Country::getInternetUsers))
                .get();
    }
    public Country getMaxAdultLiteracy() {
        return nonNullCountries().stream()
                .max(Comparator.comparingDouble(Country::getAdultLiteracyRate))
                .get();
    }
    public Country getMinAdultLiteracy(){
        return nonNullCountries().stream()
                .min(Comparator.comparingDouble(Country::getAdultLiteracyRate))
                .get();
    }

    public Double correlationCoefficient(){
        double[] adultLiteracyRates = new double[nonNullCountries().size()];
        double[] internetUsers = new double[nonNullCountries().size()];
        int i = 0;
        for(Country country : nonNullCountries()){

            if(country.getAdultLiteracyRate() != null && country.getInternetUsers() != null){
                adultLiteracyRates[i] = country.getAdultLiteracyRate();
                internetUsers[i] = country.getInternetUsers();
                i++;
            }
        }
        PearsonsCorrelation correlationCoefficient = new PearsonsCorrelation();
        return correlationCoefficient.correlation(adultLiteracyRates, internetUsers);
    }


}
