package development.parkenulm;

import java.util.ArrayList;

public class ParkhausDB {
    private static final String[] ParkhausTB = {
            "ze11",
            "ze6",
            "ze2",
            "ze3",
            "ze7",
            "ze4",
            "ze1",
            "ze5",
            "ze9",
            "ze10"
    };
    ;

    private static final ArrayList<ParkhausHTML> HTML_Strings = new ArrayList<>();

    public static ArrayList<ParkhausHTML> getHTML_Strings() {
        HTML_Strings.add(new ParkhausHTML("Am Bahnhof", "https://www.parken-in-ulm.de/parkhaeuser/bahnhof.php"));
        HTML_Strings.add(new ParkhausHTML("Am Rathaus", "https://www.parken-in-ulm.de/parkhaeuser/rathaus.php"));
        HTML_Strings.add(new ParkhausHTML("Deutschhaus", "https://www.parken-in-ulm.de/parkhaeuser/deutschhaus.php"));
        HTML_Strings.add(new ParkhausHTML("Fischerviertel", "https://www.parken-in-ulm.de/parkhaeuser/fischerviertel.php"));
        HTML_Strings.add(new ParkhausHTML("Salzstadel", "https://www.parken-in-ulm.de/parkhaeuser/salzstadel.php"));
        HTML_Strings.add(new ParkhausHTML("Frauenstrasse", "https://www.parken-in-ulm.de/parkhaeuser/frauenstrasse.php"));
        HTML_Strings.add(new ParkhausHTML("Congress Nord", "https://www.parken-in-ulm.de/parkhaeuser/congress_nord.php"));
        HTML_Strings.add(new ParkhausHTML("Congress Süd", "https://www.parken-in-ulm.de/parkhaeuser/congress_sued.php"));
        HTML_Strings.add(new ParkhausHTML("Kornhaus", "https://www.parken-in-ulm.de/parkhaeuser/kornhaus.php"));
        HTML_Strings.add(new ParkhausHTML("Theater", "https://www.parken-in-ulm.de/parkhaeuser/theater.php"));
        return HTML_Strings;
    }

    private static final ArrayList<Parkhaus> ParkhausList = new ArrayList<>();

    /**
     * @return the ParkhausTB
     */
    public static String[] getParkhausTB() {
        return ParkhausTB;
    }

    /**
     * @return the ParkhausDB
     */
    public static ArrayList<Parkhaus> getParkhausDB() {
        ParkhausList.add(new Parkhaus("Am Bahnhof", "?", "?"));
        ParkhausList.add(new Parkhaus("Am Rathaus", "?", "?"));
        ParkhausList.add(new Parkhaus("Deutschhaus", "?", "?"));
        ParkhausList.add(new Parkhaus("Fischerviertel", "?", "?"));
        ParkhausList.add(new Parkhaus("Salzstadel", "?", "?"));
        ParkhausList.add(new Parkhaus("Frauenstraße", "?", "?"));
        ParkhausList.add(new Parkhaus("Basteicenter", "?", "?"));
        ParkhausList.add(new Parkhaus("Maritim Hotel", "?", "?"));
        ParkhausList.add(new Parkhaus("Kornhaus", "?", "?"));
        ParkhausList.add(new Parkhaus("Theater", "?", "?"));
        return ParkhausList;
    }

}