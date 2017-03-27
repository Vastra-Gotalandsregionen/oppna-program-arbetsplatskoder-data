package se.vgregion.obstetrix.lookup.domain.backingbean;

import com.liferay.faces.portal.context.LiferayFacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.obstetrix.lookup.domain.VgrObstetrixUserLookupLog;
import se.vgregion.obstetrix.lookup.service.Crud;
import se.vgregion.obstetrix.lookup.service.Jdbc;
import se.vgregion.portal.patient.event.PersonNummer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model/Controller of the application.
 * @author Claes Lundahl
 */
@Component(value = "app")
@Scope("view")
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired
    private Crud crud;

    private String number;

    private String numberWarning;

    private String userScreenName;
    private List<Result> results = new ArrayList<>();

    private String personalNumberColumn = "PERSONNR";

    /**
     * Executes search, logs the circumstances around the search and places the result so that it can be shown by the
     * view.
     * @param withPersonalNumber the number to search with.
     */
    public void search(String withPersonalNumber) {
        VgrObstetrixUserLookupLog log = new VgrObstetrixUserLookupLog();
        withPersonalNumber = withPersonalNumber.trim();
        log.setPersonalNumber(withPersonalNumber);
        log.setVgrId(getUserScreenName());
        crud.create(log);

        String tableName = "PAT";

        results.clear();
        for (Jdbc jdbc : Jdbc.getInstances()) {
            String sql = String.format("select count(*) from %s where %s = ?", tableName, personalNumberColumn);
            long count = jdbc.count(sql, withPersonalNumber.replace("-",""));
            Result result = new Result();
            result.setState(count == 0 ? "Nej" : "Ja");
            result.setDatabase(jdbc.getName());
            results.add(result);
        }

        PersonNummer personNummer = PersonNummer.personummer(withPersonalNumber);
        numberWarning = "";
        if (!personNummer.isCheckNumberValid()) {
            numberWarning += "Kontrollnummret är felaktigt. ";
        }
        if (!personNummer.isDayValid()) {
            numberWarning += "Dagen är felaktigt. ";
        }
        if (!personNummer.isMonthValid()) {
            numberWarning += "Månaden är felaktigt. ";
        }

    }

    private String getUserScreenName() {
        if (userScreenName == null) {
            userScreenName = LiferayFacesContext.getInstance().getUser().getScreenName();
            return userScreenName;
        } else {
            return userScreenName;
        }
    }

    /**
     * Executes search, logs the circumstances around the search and places the result so that it can be shown by the
     * view.
     */
    public void search() {
        search(getNumber());
    }

    /**
     * gets the number (personal id-number).
     * @return number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * setts the number (personal id-number).
     * @param number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * gets numberWarning.
     * @return numberWarning.
     */
    public String getNumberWarning() {
        return numberWarning;
    }

    /**
     * sets numberWarning.
     * @param numberWarning
     */
    public void setNumberWarning(String numberWarning) {
        this.numberWarning = numberWarning;
    }

    /**
     * Holds each result-part - each one describes the occurrence of the personal number in a db.
     */
    public static class Result implements Serializable {

        private String database;
        private String state;

        /**
         * gets database.
         * @return database.
         */
        public String getDatabase() {
            return database;
        }

        /**
         * sets database.
         * @param database new value.
         */
        public void setDatabase(String database) {
            this.database = database;
        }

        /**
         * gets state.
         * @return state
         */
        public String getState() {
            return state;
        }

        /**
         * sets state
         * @param state new value.
         */
        public void setState(String state) {
            this.state = state;
        }
    }

    /**
     * gets results.
     * @return result.
     */
    public List<Result> getResults() {
        return results;
    }

}
