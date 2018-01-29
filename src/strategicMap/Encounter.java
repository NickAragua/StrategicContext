package strategicMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Encounter {
    // contains all forces that must participate in the battle, sorted by team
    Map<Integer, Set<Force>> primaryForces;
    // contains all secondary forces that could potentially participate in the battle, sorted by team
    Map<Integer, Set<Force>> potentialSecondaryForces;
    // contains all secondary forces that have been selected to participate in the battle, sorted by team
    Map<Integer, Set<Force>> selectedSecondaryForces;
    Map<Integer, Integer> selectedRetreatThresholds;
    boolean finalized;
    
    int attackingTeam;
    // TODO: special objectives
    
    /**
     * Initializes a new encounter at the given coordinates on the given board, with a given group of instigators
     * as the primary combatants.
     * @param coords
     * @param instigators
     */
    public Encounter(Set<Force> instigators, int attackingTeam) {
        primaryForces = new HashMap<>();
        potentialSecondaryForces = new HashMap<>();
        selectedSecondaryForces = new HashMap<>();
        selectedRetreatThresholds = new HashMap<>();
        this.attackingTeam = attackingTeam;
        
        for(Force force : instigators) {
            if(!primaryForces.containsKey(force.getTeam())) {
                primaryForces.put(force.getTeam(), new HashSet<>());
            }
            
            primaryForces.get(force.getTeam()).add(force);
        }
    }
    
    /**
     * Convenience function to add multiple secondary forces at once 
     * @param forces
     */
    public void addPotentialSecondaryForces(Set<Force> forces) {
        for(Force force : forces) {
            addPotentialSecondaryForce(force);
        }
    }
    
    /**
     * Adds a potential reinforcement group to the encounter
     * @param team
     * @param force
     */
    public void addPotentialSecondaryForce(Force force) {
        if(!potentialSecondaryForces.containsKey(force.getTeam())) {
            potentialSecondaryForces.put(force.getTeam(), new HashSet<>());
        }
        
        potentialSecondaryForces.get(force.getTeam()).add(force);
    }
    
    /**
     * Selects a potential secondary force as an active participant in the encounter
     * @param force
     */
    public void selectSecondaryForce(Force force) {
        if(!selectedSecondaryForces.containsKey(force.getTeam())) {
            selectedSecondaryForces.put(force.getTeam(), new HashSet<>());
        }
        
        selectedSecondaryForces.get(force.getTeam()).add(force);
    }
    
    /**
     * Sets the retreat threshold for a given team
     * @param team
     * @param threshold
     */
    public void setRetreatThreshold(int team, int threshold) {
        selectedRetreatThresholds.put(team, threshold);
    }
    
    /**
     * Gets the set of primary forces for the given team.
     */
    public Set<Force> getPrimaryForcesForTeam(int team) {
        return primaryForces.get(team);
    }
    
    /**
     * Gets the set of potential secondary forces for the given team.
     */
    public Set<Force> getPotentialForcesForTeam(int team) {
        return potentialSecondaryForces.get(team);
    }
    
    /**
     * Gets the set of selected secondary forces for the given team.
     */
    public Set<Force> getSecondaryForcesForTeam(int team) {
        return selectedSecondaryForces.get(team);
    }
    
    /**
     * Whether or not this encounter has been finalized. i.e. the player has committed to a course of action
     * @return
     */
    public boolean isFinalized() {
        return finalized;
    }
    
    /**
     * The player has committed to a course of action on this encounter.
     */
    public void finalize() {
        finalized = true;
    }
    
    public Set<Integer> getTeams() {
        return primaryForces.keySet();
    }
    
    public int getAttackingTeam() {
        return attackingTeam;
    }
    
    /**
     * Whether or not reinforcements are present in this encounter
     * @return True or false
     */
    public boolean hasPotentialReinforcements() {
        return potentialSecondaryForces.size() > 0;
    }
    
    /**
     * Get a summary string of all the primary forces for the given team.
     * @param team
     * @return
     */
    public String getPrimaryTeamForcesSummary(int team) {
        StringBuilder sb = new StringBuilder();
        
        for(Force force : primaryForces.get(team)) {
            sb.append(force.getShortName());
            sb.append(", ");
        }
        
        // if we've been adding commas, get rid of the last one
        if(primaryForces.get(team).size() > 1) {
            sb.deleteCharAt(sb.length() - 2);
        }
        
        return sb.toString();
    }
}