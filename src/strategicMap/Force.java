package strategicMap;

public class Force {    
    private String shortName;
    private Team team;
    
    public Force(String shortName, Team team) {
        this.shortName = shortName;
        this.team = team;
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
        return String.format("%s:<br/>%s", shortName, "Vindicator VND-1R<br/>Trebuchet TBT-5T<br/>Phoenix Hawk PXH-1D<br/>Firestarter FS9-K");
    }
    
    public int getTeamID() {
        return team.getID();
    }
    
    public Team getTeam() {
        return team;
    }
    
    public int hashCode() {
        return shortName.hashCode();
    }
}
