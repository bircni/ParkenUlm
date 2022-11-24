package development.parkenulm;

/**
 * This class represents a Parkhaus.
 */
public class Parkhaus {
    private final String name;
    public final String freiePlaetze;
    private final String platzInsg;

    private final String openTimes;

    /**
     * Constructor for the Parkhaus class.
     *
     * @param name         The name of the Parkhaus.
     * @param freiePlaetze The exchange rate for one euro.
     * @param platzInsg    The platzInsg of the country.
     */
    public Parkhaus(String name, String platzInsg, String freiePlaetze, String openTimes) {
        this.name = name;
        this.freiePlaetze = freiePlaetze;
        this.platzInsg = platzInsg;
        this.openTimes = openTimes;
    }

    /**
     * @return the name
     */
    public String getHaus() {
        return name;
    }

    /**
     * @return the platzInsg
     */
    public String getPlatz() {
        return platzInsg;
    }

    /**
     * @return the freiePlaetze
     */
    public String getFrei() {
        return freiePlaetze;
    }

    /**
     * @return the openTimes
     */
    public String getOpenTimes() {
        return openTimes;
    }
}