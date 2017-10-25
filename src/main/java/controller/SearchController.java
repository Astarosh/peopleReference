/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bean.Street;
import db.DataHelper;
import entity.Address;
import entity.Human;
import enums.SearchType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

/**
 *
 * @author Ast
 */
@ManagedBean(eager = true)
@javax.faces.bean.SessionScoped
public class SearchController implements Serializable {

    private String searchString;
    private SearchType searchType;
    private boolean isAddPeople;
    private Human human;
    private String sex;
    private Date date;
    private Date date2;
    private Street street;
    private Address address;
    private boolean isSearchSuccess;
    private List<Human> humanList;
    private int humanCount;
    private boolean isSearchByDate;
    private boolean isHumanDetailsShow;
    
    @ManagedProperty(value = "#{streetController}")
    private StreetController streetController;

    public StreetController getStreetController() {
        return streetController;
    }

    public void setStreetController(StreetController streetController) {
        this.streetController = streetController;
    }

    public SearchController() {
        searchString = "";
        searchType = SearchType.FIRSTNAME;
        sex = "m";
        LocalDateTime localDateTime = LocalDateTime.now();
        date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        date2 = date;
        human = new Human();
        street = new Street();
        address = new Address();
    }

    public void saveHuman() {
        human.setSex(sex.charAt(0));
        FacesMessage message;
        ResourceBundle bundle = ResourceBundle.getBundle("nls.properties", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext context = FacesContext.getCurrentInstance();
        if (human.getFirstname() == null || human.getLastname() == null || human.getPatronymic() == null || street.getStreetname() == null || address.getHousekey() == 0) {
            message = new FacesMessage(bundle.getString("add_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("addForm", message);
        } else {
            String save;
            Long streetID = streetController.getStreetId(street.getStreetname());
            if (streetID != -1) {
                address.setStreetid(streetID);
                List<Address> addAddress = DataHelper.getInstance().getAddress(address);
                if (addAddress.isEmpty()) {
                    int result = DataHelper.getInstance().getCountAddressRows();
                    address.setId(result);
                    DataHelper.getInstance().addAddress(address);
                    human.setAddress(address);
                } else {
                    human.setAddress(addAddress.get(0));
                }
                int result = DataHelper.getInstance().getCountHumanRows();
                human.setId(result);
                save = DataHelper.getInstance().addHuman(human);
            } else {
                save = null;
                message = new FacesMessage(bundle.getString("wrong_street"));
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage("addForm", message);
            }
            if (save == null) {
            } else {
                someChangesToValues(false, false, false, false);
            }
        }
    }

    public void addPeopleShow() {
        human = new Human();
        street = new Street();
        address = new Address();
        someChangesToValues(false, false, true, false);
    }

    public void addPeopleHide() {
        someChangesToValues(false, false, false, false);
    }

    public void humanDetailsShow() {
        someChangesToValues(false, false, false, true);
    }

    public void humanDetailsHide() {
        someChangesToValues(false, false, false, false);
    }

    public void someChangesToValues(Boolean isSearchByDate, Boolean isSearchSuccess, Boolean isAddPeople, Boolean isHumanDetailsShow) {
        this.isAddPeople = isAddPeople;
        this.isSearchSuccess = isSearchSuccess;
        this.isSearchByDate = isSearchByDate;
        this.isHumanDetailsShow = isHumanDetailsShow;
    }

    public void fillHumansBySearch() {
        switch (searchType) {
            case BIRTHDATE:
                someChangesToValues(true, true, false, false);
                humanList = DataHelper.getInstance().getHumansByDate(date, date2);
                break;
            case FIRSTNAME:
                someChangesToValues(false, true, false, false);
                humanList = DataHelper.getInstance().getHumansByFirstName(searchString);
                break;
            case LASTNAME:
                someChangesToValues(false, true, false, false);
                humanList = DataHelper.getInstance().getHumansByLastName(searchString);
                break;
            case HOUSE:
                someChangesToValues(false, true, false, false);
                humanList = DataHelper.getInstance().getHumansByHouse(searchString);
                break;
            case STREET:
                someChangesToValues(false, true, false, false);
                Long streetID = streetController.getStreetId(searchString);
                humanList = DataHelper.getInstance().getHumansByStreet(streetID);
                break;
            default:
                someChangesToValues(false, true, false, false);
                humanList = DataHelper.getInstance().getHumansByFirstName(searchString);
                break;
        }
        if (humanList != null) {
            humanCount = humanList.size();
        } else {
            humanCount = 0;
        }
    }

    public boolean isIsHumanDetailsShow() {
        return isHumanDetailsShow;
    }

    public void setIsHumanDetailsShow(boolean isHumanDetailsShow) {
        this.isHumanDetailsShow = isHumanDetailsShow;
    }

    public boolean isIsSearchByDate() {
        return isSearchByDate;
    }

    public void setIsSearchByDate(boolean isSearchByDate) {
        this.isSearchByDate = isSearchByDate;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public int getHumanCount() {
        return humanCount;
    }

    public void setHumanCount(int humanCount) {
        this.humanCount = humanCount;
    }

    public List<Human> getHumanList() {
        return humanList;
    }

    public void setHumanList(List<Human> humanList) {
        this.humanList = humanList;
    }

    public boolean isIsSearchSuccess() {
        return isSearchSuccess;
    }

    public void setIsSearchSuccess(boolean isSearchSuccess) {
        this.isSearchSuccess = isSearchSuccess;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    public boolean isIsAddPeople() {
        return isAddPeople;
    }

    public void setIsAddPeople(boolean isAddPeople) {
        this.isAddPeople = isAddPeople;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
