package db;

import bean.Street;
import entity.Address;
import entity.HibernateUtil;
import entity.Human;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

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
                objDisjunction.add(Restrictions.eq("address.id", address.getId()));
            }
            criteria.add(objDisjunction);
            return criteria.list();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Human> getHumansByStreet(Long streetId) {
        List<Address> addressList;
        addressList = getAddressByStreetid(streetId);
        if (addressList != null && !addressList.isEmpty()) {
            Criteria criteria = getSession().createCriteria(Human.class);
            Disjunction objDisjunction = Restrictions.disjunction();
            for (Address address : addressList) {
                objDisjunction.add(Restrictions.eq("address.id", address.getId()));
            }
            criteria.add(objDisjunction);
            return criteria.list();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Human> getHumansByDate(Date date1, Date date2) {
        Criteria criteria = getSession().createCriteria(Human.class);
        criteria.add(Restrictions.between("birthdate", date1, date2));
        return criteria.list();
    }

    public List<Street> getStreets() {
        Street street;
        List<Street> streetList;
        streetList = new ArrayList<>();
        SQLQuery addScalar;
        addScalar = getSession().createSQLQuery("select id, streetname from streets").addScalar("id", LongType.INSTANCE).addScalar("streetname", StringType.INSTANCE);
        List<Object[]> rows = addScalar.list();
        for (Object[] row : rows) {
            street = new Street();
            street.setId(Long.parseLong(row[0].toString()));
            street.setStreetname(row[1].toString());
            streetList.add(street);
        }
        return streetList;
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
        query.setParameter("street_id", address.getStreetid());
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
