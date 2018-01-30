package strategicMap;

public class Team {
    public String name;
    public int id;
    public boolean playerTeam;
    
    /**
     * Cons
     * @param name
     * @param ID
     */
    public Team(String name, int ID, boolean playerTeam) {
        this.name = name;
        this.id = ID;
    }
    
    public String getName() {
        return name;
    }
    
    public int getID() {
        return id;
    }
    
    public boolean isPlayerTeam() {
        return playerTeam;
    }
}
