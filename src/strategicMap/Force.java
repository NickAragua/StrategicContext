package strategicMap;

public class Force {
    private String shortName;
    
    public Force(String shortName) {
        this.shortName = shortName;
    }
    
    public Force() {
    }
    
    /**
     * Gets a text description of the force, suitable for display in dropdowns, checkbox lists and other "one-liner" environments
     * @return
     */
    public String getShortName() {
        return shortName;
    }
    
    /**
     * Gets a status summary string representing the force's state, suitable for display in the info pane.
     * @return
     */
    public String getFullSummary() {
        return "Long description\n" + shortName;
    }
    
    public int hashCode() {
        return shortName.hashCode();
    }
}
