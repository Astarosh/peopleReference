/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bean.Street;
import db.DataHelper;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Ast
 */
@ApplicationScoped
@Named
public class StreetController {

    private final List<Street> STREETLIST;

    public StreetController() {
        STREETLIST = DataHelper.getInstance().getStreets();
    }

    public Long getStreetId(String streetName) {
        Long streetId = Long.valueOf(-1);
        for (Street streets : STREETLIST) {
            if (streets.getStreetname().equals(streetName)) {
                streetId = streets.getId();
            }
        }
        return streetId;
    }

    public String getStreetName(Long id) {
        String streetName = "";
        for (Street streets : STREETLIST) {
            if (streets.getId() == id) {
                streetName = streets.getStreetname();
            }
        }
        return streetName;
    }

    public List<Street> getSTREETLIST() {
        return STREETLIST;
    }

}
