package strategicMapUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import strategicMap.Encounter;
import strategicMap.Force;

public class EncounterWizardPanel extends JInternalFrame {
    Encounter encounter;
    Map<Force, JCheckBox> forceSelectionCheckboxes;
    //JRadioButton 
    
    public EncounterWizardPanel() {
        super("Detailed Info", true, true, false, false);
        this.setLayout(new GridBagLayout());
    }
    
    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
        GridBagConstraints gbc = new GridBagConstraints();
        displayCommonElements(gbc);
    }
    
    private void displayCommonElements(GridBagConstraints gbc) {
        gbc.gridy = 0;
        displayTeamSummaries(gbc);
        gbc.gridy++;
        displayForceSelection(gbc, false);
        /*JLabel instigatorSummary;
        JLabel playerLabel;
        JLabel computerLabel;
        JButton backButton;
        JButton nextButton;
        JButton confirmButton;*/
    }
    
    private void displayEvadeDialog() {
        
    }
    
    private void displayForceChoiceDialog() {
        
    }
    
    private void displayObjectiveSelectionDialog() {
        
    }
    
    private void displayForceSelection(GridBagConstraints gbc, boolean readonly) {
        // player team, opfor
        JLabel playerLabel = new JLabel("Player Team Name Here");
        JLabel opforLabel = new JLabel("Opfor Team Name Here");
        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        add(playerLabel, gbc);
        gbc.gridx = 2;
        add(opforLabel, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        int gridy = gbc.gridy;
        for(int team : encounter.getTeams()) {
            gbc.gridy = gridy;
            for(Force force : encounter.getPrimaryForcesForTeam(team)) {
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.EAST;
                add(new JLabel(force.getShortName()), gbc);
                gbc.gridy++;
            }
            gbc.gridx += 2;
        }
        
        if(encounter.hasPotentialReinforcements()) {
            JLabel reinforcementsLabel = new JLabel("Potential Reinforcements");
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(reinforcementsLabel, gbc);
            
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridy++;
            gbc.gridx = 0;
            gridy = gbc.gridy;
            for(int team : encounter.getTeams()) {
                gbc.gridy = gridy;
                for(Force force : encounter.getPotentialForcesForTeam(team)) {
                    gbc.gridwidth = 2;
                    gbc.anchor = GridBagConstraints.EAST;
                    add(new JLabel(force.getShortName()), gbc);
                    gbc.gridy++;
                }
                gbc.gridx += 2;
            }
        }
    }
    
    private void displayTeamSummaries(GridBagConstraints gbc) {
        String attackerText = "attacker";
        String defenderText = "defender";
        
        for(int team : encounter.getTeams()) {
            if(team == encounter.getAttackingTeam()) {
                attackerText = encounter.getPrimaryTeamForcesSummary(team);
            } else {
                defenderText = encounter.getPrimaryTeamForcesSummary(team);
            }
        }
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        this.add(new JLabel(attackerText), gbc);
        
        gbc.gridx = 2;
        this.add(new JLabel(defenderText), gbc);
    }
}
