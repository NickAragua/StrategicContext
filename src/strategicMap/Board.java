package strategicMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {
    // hexes
    private Hex[][] hexes;
    // forces, sorted by team and coordinates
    private Map<Coords, Map<Integer, Set<Force>>> teamForces;
    private Map<Integer, Team> teams;
    private Team playerTeam;
    
    // encounters, sorted by coordinates
    private Map<Coords, Encounter> encounters;
    int width;
    int height;
    
    public Board(int width, int height, int playerTeamID) {
        hexes = new Hex[width][height];
        this.width = width;
        this.height = height;
        teamForces = new HashMap<>();
        teams = new HashMap<>();
        encounters = new HashMap<>();
        
        // anything below is code to place things on the board for testing
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                hexes[x][y] = new Hex();
                hexes[x][y].red = 255 - (x * 10);
                hexes[x][y].green = 255 - (y * 10);
            }
        }
        
        playerTeam = new Team("Player", playerTeamID, true);
        Team opforTeam = new Team("Opfor", 2, false);
        
        for(int x = 0; x < 5; x++) {
            Force force = new Force("Force " + x + " Team 1", playerTeam);
            Coords coords = new Coords(x, 5);
            addForce(force, coords);
        }
        
        for(int x = 0; x < 5; x++) {
            Force force = new Force("Force " + x + " Team 2", opforTeam);
            Coords coords = new Coords(x, 6);
            addForce(force, coords);
        }
        
        Set<Force> testInstigators = new HashSet<>();
        testInstigators.addAll(getForcesAt(new Coords(4, 5)));
        testInstigators.addAll(getForcesAt(new Coords(4, 6)));
        Encounter testEnc = new Encounter(testInstigators, this, opforTeam.getID());
        
        Set<Force> testReinforcementsTeamOne = new HashSet<>();
        testReinforcementsTeamOne.addAll(getForcesAt(new Coords(3, 5)));
        //testReinforcementsTeamOne.addAll(getForcesAt(new Coords(2, 5)));
        
        Set<Force> testReinforcementsTeamTwo = new HashSet<>();
        testReinforcementsTeamOne.addAll(getForcesAt(new Coords(3, 6)));
        testReinforcementsTeamOne.addAll(getForcesAt(new Coords(2, 6)));
        
        testEnc.addPotentialSecondaryForces(testReinforcementsTeamOne);
        testEnc.addPotentialSecondaryForces(testReinforcementsTeamTwo);
        
        encounters.put(new Coords(4, 5), testEnc);
    }
    
    public Hex getHex(int x, int y) {
        return hexes[x][y];
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void addTeam(Team team) {
        teams.put(team.getID(), team);
    }
    
    public void addForce(Force force, Coords coords) {
        Team team = force.getTeam();
        
        if(!teams.containsKey(team.getID())) {
            teams.put(team.getID(), team);
        }
        
        if(!teamForces.containsKey(coords)) {
            teamForces.put(coords, new HashMap<>());
        }
        
        if(!teamForces.get(coords).containsKey(team.getID())) {
            teamForces.get(coords).put(team.getID(), new HashSet<>());
        }
        
        teamForces.get(coords).get(team.getID()).add(force);
    }
    
    /**
     * Returns a count of all forces for the given team at the given coordinates.
     * @param coords Coordinates
     * @param team Team
     * @return The force count.
     */
    public int getForceCount(Coords coords, int team) {
        return getForcesAt(coords, team).size();
    }
    
    /**
     * Returns a set of all forces for the given team at the given coordinates.
     * @param coords Coordinates
     * @param team Team
     * @return Force set. Empty list if no forces found.
     */
    public Set<Force> getForcesAt(Coords coords, int team) {
        if(teamForces.containsKey(coords) && teamForces.get(coords).containsKey(team)) {
            return teamForces.get(coords).get(team);
        }
        
        return new HashSet<Force>();
    }
    
    /**
     * Returns a set of all forces for at the given coordinates, regardless of team
     * @param coords Coordinates
     * @param team Team
     * @return Force set. Empty list if no forces found.
     */
    public Set<Force> getForcesAt(Coords coords) {
        HashSet<Force> retval = new HashSet<>();
        
        if(teamForces.containsKey(coords)) {
            for(int teamID : teams.keySet()) {
                Set<Force> forces = teamForces.get(coords).get(teamID);
                
                if(forces != null) {
                    retval.addAll(forces);
                }
            }
        }
        
        return retval;
    }
    
    /**
     * Returns the encounter at the given coordinates
     * @param coords
     * @return
     */
    public Encounter getEncounterAt(Coords coords) {
        return encounters.get(coords);
    }
    
    public void removeEncounter(Encounter enc) {
        encounters.remove(enc);
    }
    
    public Team getPlayerTeam() {
        return playerTeam;
    }
    
    public Team getTeam(int teamID) {
        return teams.get(teamID);
    }
}
