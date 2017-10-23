/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validators;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Ast
 */
@FacesValidator("validators.number_validator")
public class Number_validator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        FacesMessage message;
        ResourceBundle bundle = ResourceBundle.getBundle("nls.properties", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        try {
            char c;
            String str = null;
            if (value == null) {
                throw new IllegalArgumentException(bundle.getString("enter_concern"));
            } else if(!(value instanceof Number)){
                //throw new IllegalArgumentException(bundle.getString("concern_mustnot_contains_strings"));
            }
            str = value.toString().trim();
            for(int i = 0; i< str.length(); i++){
                c = str.charAt(i);
                if(!Character.isDigit(c)){
                    throw new IllegalArgumentException(bundle.getString("concern_mustnot_contains_strings"));
                }
            }
            if (str.length() == 0) {
                throw new IllegalArgumentException(bundle.getString("enter_concern"));
            }
        } catch (IllegalArgumentException ex) {
            message = new FacesMessage(ex.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
