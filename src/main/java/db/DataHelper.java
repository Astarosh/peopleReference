package db;

import entity.Address;
import entity.HibernateUtil;
import entity.Human;
import entity.Streets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class DataHelper {

    private SessionFactory sessionFactory = null;
    private static DataHelper dataHelper;

    private DataHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static DataHelper getInstance() {
        if (dataHelper == null) {
            dataHelper = new DataHelper();
        }
        return dataHelper;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public String addHuman(Human human) {
        Serializable save = getSession().save(human);
        return save.toString();
    }

    public void addAddress(Address address) {
        getSession().save(address);
    }

    public List<Human> getHumansByFirstName(String str) {
        Criteria criteria = getSession().createCriteria(Human.class);
        criteria.add(Restrictions.ilike("firstname", str, MatchMode.EXACT));
        criteria.addOrder(Order.asc("firstname"));
        return criteria.list();
    }

    public List<Human> getHumansByLastName(String str) {
        Criteria criteria = getSession().createCriteria(Human.class);
        criteria.add(Restrictions.ilike("lastname", str, MatchMode.EXACT));
        criteria.addOrder(Order.asc("lastname"));
        return criteria.list();
    }

    public List<Human> getHumansByHouse(String str) {
        List<Address> addressList;
        addressList = getAddressByHouse(str);
        if (addressList != null && !addressList.isEmpty()) {
            Criteria criteria = getSession().createCriteria(Human.class);
            Disjunction objDisjunction = Restrictions.disjunction();
                for (Address address : addressList) {
                    System.out.println(address.getId());
                    objDisjunction.add(Restrictions.eq("address.id", address.getId()));
                }
                criteria.add(objDisjunction);
                return criteria.list();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Human> getHumansByStreet(String str) {
        List<Streets> streetsList;
        streetsList = getStreets(str);
        if (streetsList != null && !streetsList.isEmpty()) {
            List<Address> addressList;
            addressList = getAddressByStreetid(streetsList.get(0).getId());
            if (addressList != null && !addressList.isEmpty()) {
                Criteria criteria = getSession().createCriteria(Human.class);
                Disjunction objDisjunction = Restrictions.disjunction();
                for (Address address : addressList) {
                    System.out.println(address.getId());
                    objDisjunction.add(Restrictions.eq("address.id", address.getId()));
                }
                criteria.add(objDisjunction);
                return criteria.list();
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    public List<Human> getHumansByDate(Date date1, Date date2) {
        Criteria criteria = getSession().createCriteria(Human.class);
        criteria.add(Restrictions.between("birthdate", date1, date2));
        return criteria.list();
    }

    public List<Streets> getStreets(String str) {
//        System.out.println("searchbystreet");
//        Criteria criteria = getSession().createCriteria(Streets.class);
//        criteria.add(Restrictions.eq("streetname", str));
        List<Streets> result;
        org.hibernate.Query createQuery = getSession().createQuery("from Streets where streetname = :street_name");
        createQuery.setParameter("street_name", str);
        result = (List<Streets>) createQuery.list();
        return result;
    }

    public List<Address> getAddress(Address address) {
        List<Address> result;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Address ");
        queryBuilder.append("where (streetid = :street_id) ");
        queryBuilder.append("AND ");
        queryBuilder.append("(housekey = :house_key)");
        org.hibernate.Query query;
        query = getSession().createQuery(queryBuilder.toString());
        query.setParameter("street_id", address.getStreets().getId());
        query.setParameter("house_key", address.getHousekey());
        result = (List<Address>) query.list();
        return result;
    }

    public List<Address> getAddressByStreetid(long streetid) {
        List<Address> result;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Address ");
        queryBuilder.append("where streetid = :street_id");
        org.hibernate.Query query;
        query = getSession().createQuery(queryBuilder.toString());
        query.setParameter("street_id", streetid);
        result = (List<Address>) query.list();
        return result;
    }

    public List<Address> getAddressByHouse(String str) {
        List<Address> result;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Address ");
        queryBuilder.append("where housekey = :house_key");
        org.hibernate.Query query;
        query = getSession().createQuery(queryBuilder.toString());
        if (str != null && !"".equals(str) && str.matches(".*\\d+.*")) {
            query.setParameter("house_key", Long.parseLong(str));
            result = (List<Address>) query.list();
            return result;
        } else {
            return null;
        }
    }

    public int getCountHumanRows() {
        int count;
        Criteria criteriaCount = getSession().createCriteria(Human.class);
        criteriaCount.setProjection(Projections.rowCount());
        count = (int) (long) criteriaCount.uniqueResult();
        return count;
    }

    public int getCountAddressRows() {
        int count;
        Criteria criteriaCount = getSession().createCriteria(Address.class);
        criteriaCount.setProjection(Projections.rowCount());
        count = (int) (long) criteriaCount.uniqueResult();
        return count;
    }
}
