package controller;

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

    private final Map<String, SearchType> SEARCHLIST = new HashMap<>(); // хранит все виды поисков (по автору, по названию)

    public SearchTypeController() {

        ResourceBundle bundle = ResourceBundle.getBundle("nls.properties", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        SEARCHLIST.clear();
        SEARCHLIST.put(bundle.getString("first_name"), SearchType.FIRSTNAME);
        SEARCHLIST.put(bundle.getString("last_name"), SearchType.LASTNAME);
        SEARCHLIST.put(bundle.getString("street"), SearchType.STREET);
        SEARCHLIST.put(bundle.getString("house"), SearchType.HOUSE);
        SEARCHLIST.put(bundle.getString("birthdate"), SearchType.BIRTHDATE);
    }

    public Map<String, SearchType> getSEARCHLIST() {
        return SEARCHLIST;
    }
}
