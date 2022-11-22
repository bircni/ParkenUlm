package development.parkenulm;


public class ParkhausHTML {
    private final String name;
    public String HTML_String;

    public ParkhausHTML(String name, String HTML_String) {
        this.name = name;
        this.HTML_String = HTML_String;
    }

    /**
     * @return the name
     */
    public String getHaus() {
        return name;
    }

    /**
     * @return the HTML_String
     */
    public String getHTML_String() {
        return HTML_String;
    }
}