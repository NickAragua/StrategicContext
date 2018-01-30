package strategicMapUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import strategicMap.Encounter;
import strategicMap.Force;

public class EncounterWizardPanel extends JInternalFrame implements ActionListener {
    private final String ACTION_INTERCEPT = "Intercept";
    private final String ACTION_IGNORE = "Ignore";
    private final String ACTION_FIGHT = "Fight";
    private final String ACTION_EVADE = "Evade";
    private final String ACTION_SPECIAL = "Special";
    private final String ACTION_FORCE_SELECTION_DONE = "Next";
    
    Encounter encounter;
    Map<Force, JCheckBox> forceSelectionCheckBoxes;

    private BoardState boardState;
    
    public EncounterWizardPanel(BoardState boardState) {
        super("Detailed Info", true, true, false, false);
        this.setLayout(new GridBagLayout());
        this.boardState = boardState;
        forceSelectionCheckBoxes = new HashMap<>();
    }
    
    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
        GridBagConstraints gbc = new GridBagConstraints();
        displayEvadeElements(gbc);
    }
    
    private void displayCommonElements(GridBagConstraints gbc, boolean readonly) {
        gbc.gridy++;
        displayTeamSummaries(gbc);
        gbc.gridy++;
        displayForceSelection(gbc, readonly);
        /*JLabel instigatorSummary;
        JLabel playerLabel;
        JLabel computerLabel;
        JButton backButton;
        JButton nextButton;
        JButton confirmButton;*/
    }
    
    private void displayEvadeElements(GridBagConstraints gbc) {
        this.getContentPane().removeAll();
        gbc.gridy = 0;
        displayCommonElements(gbc, true);
        gbc.gridy++;
        
        if(encounter.playerIsAttacker()) {
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            
            JButton btnIntercept = new JButton("Intercept");
            btnIntercept.addActionListener(this);
            this.getContentPane().add(btnIntercept, gbc);
            
            gbc.gridx = 2;
            
            JButton btnIgnore = new JButton("Ignore");
            btnIgnore.addActionListener(this);
            this.getContentPane().add(btnIgnore, gbc);
        } else {
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.EAST;
            
            JButton btnFight = new JButton("Fight");
            btnFight.addActionListener(this);
            this.getContentPane().add(btnFight, gbc);
            
            gbc.gridx = 2;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            
            JButton btnEvade = new JButton("Evade");
            btnEvade.addActionListener(this);
            this.getContentPane().add(btnEvade, gbc);
            
            gbc.gridx = 3;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            
            JButton btnSpecial = new JButton("Special Scenario");
            btnSpecial.addActionListener(this);
            this.getContentPane().add(btnSpecial, gbc);
        }
        this.validate();
        this.repaint();
    }
    
    private void displayForceChoiceDialog(String message) {
        this.getContentPane().removeAll();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel messageLabel = new JLabel(message);
        add(messageLabel, gbc);
        
        gbc.gridy++;
        
        displayCommonElements(gbc, false);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        
        JButton btnCommit = new JButton(ACTION_FORCE_SELECTION_DONE);
        btnCommit.addActionListener(this);
        this.getContentPane().add(btnCommit, gbc);
        
        this.validate();
        this.repaint();
    }
    
    private void displayObjectiveSelectionDialog() {
        GridBagConstraints gbc = new GridBagConstraints();
        this.getContentPane().removeAll();
        
        gbc.gridy = 0;
        displayCommonElements(gbc, true);
        
        this.validate();
        this.repaint();
    }
    
    /** 
     * Displays the 'force selection' part of the interface
     * @param gbc GridBagConstraints object with gridy set to the row we want to start at
     * @param readonly Whether the checkboxes are readonly or editable
     */
    private void displayForceSelection(GridBagConstraints gbc, boolean readonly) {
        // player team, opfor
        JLabel playerLabel = new JLabel("Player Team Name Here");
        JLabel opforLabel = new JLabel("Opfor Team Name Here");
        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(playerLabel, gbc);
        gbc.gridx = 2;
        add(opforLabel, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        int gridy = gbc.gridy;
        for(int team : encounter.getTeams()) {
            gbc.gridy = gridy;
            for(Force force : encounter.getPrimaryForcesForTeam(team)) {
                gbc.gridwidth = 1;
                gbc.anchor = GridBagConstraints.EAST;
                add(new JLabel(""), gbc);
                
                gbc.gridx++;
                gbc.anchor = GridBagConstraints.WEST;
                add(new JLabel(force.getShortName()), gbc);
                
                gbc.gridy++;
                gbc.gridx--;
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
                    JCheckBox forceCheckBox;
                    if(forceSelectionCheckBoxes.containsKey(force)) {
                        forceCheckBox = forceSelectionCheckBoxes.get(force);
                    } else {
                        forceCheckBox = new JCheckBox();
                        forceSelectionCheckBoxes.put(force, forceCheckBox);
                    }
                    
                    forceCheckBox.setEnabled(!readonly);
                    
                    gbc.gridwidth = 1;
                    gbc.anchor = GridBagConstraints.EAST;
                    add(forceCheckBox, gbc);
                    
                    gbc.anchor = GridBagConstraints.WEST;
                    gbc.gridx++;
                    add(new JLabel(force.getShortName()), gbc);
                    
                    gbc.gridy++;
                    gbc.gridx--;
                }
                gbc.gridx += 2;
            }
        }
    }
    
    private void displayTeamSummaries(GridBagConstraints gbc) {
        String attackerText = "attacker";
        String defenderText = "defender";
        
        for(int team : encounter.getTeams()) {
            if(team == encounter.getAttackingTeamID()) {
                attackerText = encounter.getPrimaryTeamForcesSummary(team);
            } else {
                defenderText = encounter.getPrimaryTeamForcesSummary(team);
            }
        }
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        this.getContentPane().add(new JLabel(attackerText), gbc);
        
        gbc.gridx = 2;
        this.getContentPane().add(new JLabel(defenderText), gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
        case ACTION_IGNORE:
            this.setVisible(false);
            break;
        case ACTION_EVADE:
            // here, we invoke logic to check if the evasion was successful
            // if yes, then close the window and remove the encounter chain
            // if no, go to the force selection screen, informing the user of the evasion failure
            break;
        case ACTION_INTERCEPT:
            // here, we invoke logic to check if the interception was successful
            // if no, then close the window and remove the encounter chain after informing the user of the interception failure
            // if yes, go to the force selection screen, informing the user of the interception success 
            break;
        case ACTION_FIGHT:
            // go to the force selection screen
            displayForceChoiceDialog("Engage Intercepting Force");
            break;
        case ACTION_SPECIAL:
            // go to the force selection screen
            break;
        case ACTION_FORCE_SELECTION_DONE:
            displayObjectiveSelectionDialog();
            break;
            
        }
    }
    
    public Dimension getPreferredSize()
    {
        return super.getPreferredSize();
    }
}
