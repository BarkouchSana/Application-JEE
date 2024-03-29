package control;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.util.Locale;

@ManagedBean
@SessionScoped
public class LocalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String language = "en"; 

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void changeLanguage() {
    	FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(language));
    }

    public Locale getLocale() {
        return new Locale(language);
    }
}

