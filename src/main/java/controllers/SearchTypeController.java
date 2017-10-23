package controllers;

import enums.SearchType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@RequestScoped
@Named
public class SearchTypeController implements Serializable {

    private final Map<String, SearchType> searchList = new HashMap<>(); // хранит все виды поисков (по автору, по названию)

    public SearchTypeController() {

        ResourceBundle bundle = ResourceBundle.getBundle("nls.properties", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchList.clear();
        searchList.put(bundle.getString("first_name"), SearchType.FIRSTNAME);
        searchList.put(bundle.getString("last_name"), SearchType.LASTNAME);
        searchList.put(bundle.getString("street"), SearchType.STREET);
        searchList.put(bundle.getString("house"), SearchType.HOUSE);
        searchList.put(bundle.getString("birthdate"), SearchType.BIRTHDATE);
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }
}
