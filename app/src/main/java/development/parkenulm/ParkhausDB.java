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
        ParkhausList.add(new Parkhaus("Am Bahnhof", "540", "540", null));
        ParkhausList.add(new Parkhaus("Am Rathaus", "558", "558", null));
        ParkhausList.add(new Parkhaus("Deutschhaus", "594", "594", null));
        ParkhausList.add(new Parkhaus("Fischerviertel", "395", "395", null));
        ParkhausList.add(new Parkhaus("Salzstadel", "530", "530", null));
        ParkhausList.add(new Parkhaus("Frauenstraße", "720", "720", null));
        ParkhausList.add(new Parkhaus("Basteicenter", "400", "400", null));
        ParkhausList.add(new Parkhaus("Maritim Hotel", "235", "235", null));
        ParkhausList.add(new Parkhaus("Kornhaus", "135", "135", null));
        ParkhausList.add(new Parkhaus("Theater", "80", "80", null));
        return ParkhausList;
    }

}