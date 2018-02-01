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
import javax.swing.JSlider;

import strategicMap.Encounter;
import strategicMap.Force;

public class EncounterWizardPanel extends JInternalFrame implements ActionListener {
    private final String ACTION_INTERCEPT = "Intercept";
    private final String ACTION_IGNORE = "Ignore";
    private final String ACTION_FIGHT = "Fight";
    private final String ACTION_EVADE = "Evade";
    private final String ACTION_SPECIAL = "Special";
    private final String ACTION_FORCE_SELECTION_DONE = "Next";
    private final String ACTION_COMMIT = "Commit";
    
    Encounter encounter;
    Map<Force, JCheckBox> forceSelectionCheckBoxes;
    JSlider retreatThresholdSlider;

    private BoardState boardState;
    
    public EncounterWizardPanel(BoardState boardState) {
        super("Detailed Info", true, true, false, false);
        this.setLayout(new GridBagLayout());
        this.boardState = boardState;
        forceSelectionCheckBoxes = new HashMap<>();
        
        retreatThresholdSlider = new JSlider();
        retreatThresholdSlider.setMajorTickSpacing(5);
        retreatThresholdSlider.setSnapToTicks(true);
        retreatThresholdSlider.setPaintTicks(true);
        retreatThresholdSlider.setPaintLabels(true);
    }
    
    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
        GridBagConstraints gbc = new GridBagConstraints();
        displayEvadeElements(gbc);
    }
    
    /**
     * Displays the elements that are common to all stages of the wizard
     * @param gbc
     * @param readonly Whether or not the force selection is read only.
     */
    private void displayCommonElements(GridBagConstraints gbc, boolean readonly) {
        gbc.gridy++;
        displayTeamSummaries(gbc);
        gbc.gridy++;
        displayForceSelection(gbc, readonly);
    }
    
    /**
     * Displays elements that are relevant to the user's selection on whether to engage or 
     * avoid the encounter.
     * @param gbc
     */
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
    
    /**
     * Displays elements that are relevant to the user's selection of reinforcements for the fight.
     * @param message A message informing the user whether their interception/evasion succeeded.
     */
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
    
    /**
     * Displays the wizard in a state where the player can choose objectives.
     */
    private void displayObjectiveSelectionDialog() {
        GridBagConstraints gbc = new GridBagConstraints();
        this.getContentPane().removeAll();
        
        gbc.gridy = 0;
        JLabel lblObjectiveSelection = new JLabel("Objective selection:");
        getContentPane().add(lblObjectiveSelection, gbc);
        
        displayCommonElements(gbc, true);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblRetreatThreshold = new JLabel("Select Retreat Threshold:");
        getContentPane().add(lblRetreatThreshold, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        
        retreatThresholdSlider.setMinimum(35);
        retreatThresholdSlider.setMaximum(55);

        this.getContentPane().add(retreatThresholdSlider, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnCommit = new JButton(ACTION_COMMIT);
        btnCommit.addActionListener(this);
        this.getContentPane().add(btnCommit, gbc);
        
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
    
    /**
     * Displays a line with the names of the participating teams.
     * @param gbc
     */
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
        case ACTION_COMMIT:
            commitEncounter();
            this.setVisible(false);
            break;
        }
    }
    
    /**
     * Called in the event that the player, through some route, winds up having to fight.
     */
    private void commitEncounter() {
        for(Force force : forceSelectionCheckBoxes.keySet()) {
            JCheckBox forceBox = forceSelectionCheckBoxes.get(force);
            
            if(forceBox.isSelected()) {
                encounter.selectSecondaryForce(force);
            }
        }
        
        encounter.setRetreatThreshold(boardState.getPlayerTeam().getID(), retreatThresholdSlider.getValue());
        
        encounter.finalize();
    }
    
    public Dimension getPreferredSize()
    {
        return super.getPreferredSize();
    }
}
