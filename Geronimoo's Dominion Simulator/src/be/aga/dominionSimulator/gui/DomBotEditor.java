package be.aga.dominionSimulator.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import be.aga.dominionSimulator.DomBuyRule;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.StartState;
import be.aga.dominionSimulator.enums.DomBotType;
import net.miginfocom.swing.MigLayout;

public class DomBotEditor extends EscapeDialog implements ActionListener {
  private static final int TEXT_FIELD_WIDTH = 50;

//made static for ease of use
  public static JLabel cardImageLabel;

  private final DomEngine myEngine;
  private final DomPlayer myChosenStrategy;
  private JPanel myBuyRulesPanel;
  private JPanel myImagePanel;
  private ArrayList<DomBuyRulePanel> myBuyRulePanels=new ArrayList<DomBuyRulePanel>();
  private JTextField myNameField;
  private JList myBotTypeList;
  private JTextArea myDescriptionField;
  private JTextField myAuthorField;
  private JCheckBox myShuffleDeckBox;
  private JTextField myStartingHandField;
  private JTextField myBoardField;
  private JTextField myStartingDrawDeckField;
  private JTextField myBaneField;
  private JTextField myStartingDiscardPile;

    public DomBotEditor ( final DomEngine anEngine , final DomPlayer aStrategy ) {
        myEngine=anEngine;
        myEngine.getGui().getGlassPane().setVisible(true);
        myChosenStrategy = aStrategy;
        buildGui();
        setTitle("Strategy Editor - " + myChosenStrategy);
        pack();
        setVisible( true );
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            dispose();
          }
        });
    }

    protected void savePlayer() {
    	DomPlayer theNewPlayer = new DomPlayer(myNameField.getText());
    	theNewPlayer.setAuthor(myAuthorField.getText());
    	theNewPlayer.setDescription(myDescriptionField.getText().replaceAll("\\r\\n|\\r|\\n", "XXXX"));
    	StartState theStartState = getStartState();
    	if (theStartState==null) {
            JOptionPane.showMessageDialog(this, "An error was found in the Start State", "Error", JOptionPane.ERROR_MESSAGE);
            return;
    	}
    	if (theStartState.myOriginalDiscard.isEmpty()
    	&& theStartState.myOriginalDrawDeck.isEmpty()
    	&& theStartState.myOriginalHand.isEmpty()) {
    	  theNewPlayer.setStartState(null);
    	} else {
    	  theNewPlayer.setStartState(theStartState);
    	}
    	if (!theNewPlayer.addBoard(myBoardField.getText(), myBaneField.getText())){
            JOptionPane.showMessageDialog(this, "An error was found in the Board", "Error", JOptionPane.ERROR_MESSAGE);
            return;
    	}
    	for (DomBuyRulePanel theRulePanel : myBuyRulePanels) {
    	  theNewPlayer.addBuyRule(theRulePanel.getBuyRule(theNewPlayer));
    	}
    	theNewPlayer.setTypes(getMyTypes());
    	theNewPlayer.addType(DomBotType.UserCreated);
    	theNewPlayer.addType(DomBotType.Bot);
    	if (!theNewPlayer.hasType(DomBotType.ThreePlayer) && ! theNewPlayer.hasType(DomBotType.FourPlayer))
    		theNewPlayer.addType(DomBotType.TwoPlayer);
    	if (!theNewPlayer.hasType(DomBotType.Colony))
    		theNewPlayer.addType(DomBotType.Province);
    	myEngine.addUserBot(theNewPlayer);
        dispose();
    }

	private StartState getStartState() {
		StartState theStartState = new StartState();
		if (!theStartState.addDiscard(myStartingDiscardPile.getText())
		 || !theStartState.addDrawDeck(myStartingDrawDeckField.getText(), myShuffleDeckBox.isSelected()?"true":"false")
		 || !theStartState.addHand(myStartingHandField.getText()))
			return null;
		return theStartState;
	}

	private void buildGui()	{
        MigLayout layout = new MigLayout(
                "",
                "[][][][]push[][]",
                "[][grow]");
        getContentPane().setLayout(layout);

        //name field
        getContentPane().add(new JLabel("Name"));
        myNameField = new JTextField(myChosenStrategy.toString(),20);
        getContentPane().add(myNameField);

        //author field
        getContentPane().add(new JLabel("written by"));
        myAuthorField = new JTextField(myChosenStrategy.getAuthor(),13);
        getContentPane().add(myAuthorField);

        //Save and Cancel buttons
        JButton theSaveButton = new JButton("Save");
        theSaveButton.setMnemonic('S');
        theSaveButton.addActionListener(this);
        theSaveButton.setActionCommand("Save");
        getContentPane().add(theSaveButton);

        JButton theCancelButton = new JButton("Cancel");
        theCancelButton.setMnemonic('C');
        theCancelButton.addActionListener(this);
        theCancelButton.setActionCommand("Cancel");
        getContentPane().add(theCancelButton, "wrap");

        //tabbed panes
        getContentPane().add(getTabbedPane(), "span, grow");

//      myImagePanel = getImagePanel();
//      theCons.gridx++;
//      theCons.gridy=0;
//      theCons.gridheight=2;
//      getContentPane().add( myImagePanel,theCons );
	}

	private void fillBuyRulesPanel() {
	  myBuyRulesPanel.removeAll();
      final GridBagConstraints theCons = DomGui.getGridBagConstraints( 2 );
      JLabel theLabel = new JLabel("Important!! These will be evaluated from top to bottom for each buy!!");
      theLabel.setForeground(Color.red);
      myBuyRulesPanel.add(theLabel, theCons);
      JButton theBTN = new JButton("Toggle View of Buy Conditions");
      theBTN.setMnemonic('T');
      theBTN.addActionListener(this);
      theBTN.setActionCommand("toggle conditions");
      theCons.gridy++;
      theCons.fill=GridBagConstraints.NONE;
      theCons.anchor=GridBagConstraints.CENTER;
      myBuyRulesPanel.add(theBTN, theCons);
      theCons.anchor=GridBagConstraints.NORTHWEST;
      theCons.fill=GridBagConstraints.BOTH;

      int i=0;
      theBTN = new JButton("Add a Buy Rule");
      theBTN.setMnemonic('A');
      theBTN.addActionListener(this);
      theBTN.setActionCommand("add rule " + i);
      theCons.gridy++;
      theCons.fill=GridBagConstraints.NONE;
      myBuyRulesPanel.add(theBTN, theCons);
      theCons.fill=GridBagConstraints.BOTH;
      
      for (DomBuyRulePanel thePanel : myBuyRulePanels){
    	i++;
    	theCons.gridy++;
	    myBuyRulesPanel.add(thePanel, theCons);
      }
      
      theBTN = new JButton("Add a Buy Rule");
      theBTN.setMnemonic('B');
      theBTN.addActionListener(this);
      theBTN.setActionCommand("add rule "+i);
      theCons.gridy++;
      theCons.fill=GridBagConstraints.NONE;
      myBuyRulesPanel.add(theBTN, theCons);
      theCons.fill=GridBagConstraints.BOTH;

      theCons.gridy++;
      DomGui.addHeavyLabel(myBuyRulesPanel, theCons);
      
      validate();
	}

	/**
     * @return
     */
    private JPanel getImagePanel() {
        final JPanel thePanel = new JPanel();
        thePanel.setLayout( new GridBagLayout() );
        thePanel.setBorder( new TitledBorder( "" ));
        final GridBagConstraints theCons = DomGui.getGridBagConstraints( 2 );
    	if (cardImageLabel == null) {
      	  cardImageLabel= new JLabel(new ImageIcon(getClass().getResource("images/Godctoon.gif")));
      	}
        thePanel.add(cardImageLabel,theCons);
        return thePanel;
    }

    private JScrollPane getBuyRulesPanel() {
  	  myBuyRulesPanel = new JPanel();
      myBuyRulesPanel.setLayout( new GridBagLayout() );
  	  myBuyRulesPanel.setBorder(new TitledBorder("Buy Rules"));
      JScrollPane scr = new JScrollPane(myBuyRulesPanel);
      scr.getVerticalScrollBar().setUnitIncrement(24);

      for (final DomBuyRule theRule : myChosenStrategy.getPrizeBuyRules()){
        DomBuyRulePanel theRulePanel = theRule.getGuiPanel(this);
        myBuyRulePanels.add(theRulePanel);
      }
      for (final DomBuyRule theRule : myChosenStrategy.getBuyRules()){
        DomBuyRulePanel theRulePanel = theRule.getGuiPanel(this);
        myBuyRulePanels.add(theRulePanel);
      }
      fillBuyRulesPanel();

      return scr;
    }

	private JTabbedPane getTabbedPane() {
		JTabbedPane theTabbedPane = new JTabbedPane();
		theTabbedPane.add("Description", getDescriptionField());
        theTabbedPane.add("Types", getTypeList());
		theTabbedPane.add("Start State", getStartStatePanel());
		theTabbedPane.add("Board", getBoardPanel());
        theTabbedPane.add("Buy Rules", getBuyRulesPanel());
		return theTabbedPane;
	}

    private JPanel flowLeft(JComponent c) {
        final JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(c);
        return p;
    }

    private JPanel getStartStatePanel() {
        final JPanel thePanel = new JPanel();
        thePanel.setLayout( new GridBagLayout() );
//        thePanel.setBorder( new TitledBorder( "" ));
        final GridBagConstraints theCons = DomGui.getGridBagConstraints( 2 );
        //starting hand
        thePanel.add(new JLabel("Starting Hand"),theCons);
        myStartingHandField = new JTextField(TEXT_FIELD_WIDTH);
        theCons.gridx++;
        thePanel.add(myStartingHandField,theCons);
        //starting discard
        theCons.gridx=0;
        theCons.gridy++;
        thePanel.add(new JLabel("Starting discard pile"),theCons);
        myStartingDiscardPile = new JTextField(TEXT_FIELD_WIDTH);
        theCons.gridx++;
        thePanel.add(myStartingDiscardPile,theCons);
        //starting draw deck
        theCons.gridx=0;
        theCons.gridy++;
        thePanel.add(new JLabel("Starting draw deck"),theCons);
        myStartingDrawDeckField = new JTextField(TEXT_FIELD_WIDTH);
        theCons.gridx++;
        thePanel.add(myStartingDrawDeckField,theCons);
        myShuffleDeckBox = new JCheckBox("Shuffle draw deck", null, true);
//        theCons.gridx++;
        theCons.gridx=0;
        theCons.gridy++;
        thePanel.add(myShuffleDeckBox,theCons);
        //info button
        JButton theInfoBTN = new JButton("Info");
        theInfoBTN.setMnemonic('I');
        theInfoBTN.addActionListener(this);
        theInfoBTN.setActionCommand("StartStateInfo");
        theCons.gridx++;
        theCons.fill=GridBagConstraints.NONE;
        theCons.anchor=GridBagConstraints.EAST;
        thePanel.add(theInfoBTN,theCons);

        StartState theStartState = myChosenStrategy.getStartState();
        if (theStartState!=null) {
        	myStartingDiscardPile.setText(theStartState.myOriginalDiscard);
        	myStartingDrawDeckField.setText(theStartState.myOriginalDrawDeck);
        	myStartingHandField.setText(theStartState.myOriginalHand);
        	myShuffleDeckBox.setSelected(theStartState.isShuffleDrawDeck());
        }

        return flowLeft(thePanel);
	}

	private JPanel getBoardPanel() {
        final JPanel thePanel = new JPanel();
        thePanel.setLayout( new GridBagLayout() );
//        thePanel.setBorder( new TitledBorder( "" ));
        final GridBagConstraints theCons = DomGui.getGridBagConstraints( 2 );
        //starting hand
        thePanel.add(new JLabel("Board"),theCons);
        myBoardField = new JTextField(myChosenStrategy.getBoard(), TEXT_FIELD_WIDTH);
        theCons.gridx++;
        thePanel.add(myBoardField,theCons);
        //starting discard
        theCons.gridx=0;
        theCons.gridy++;
        theCons.fill=GridBagConstraints.NONE;
        thePanel.add(new JLabel("Bane Card"),theCons);
        myBaneField = new JTextField(myChosenStrategy.getBaneCard(), 20);
        theCons.gridx++;
        thePanel.add(myBaneField,theCons);
        //info button
        JButton theInfoBTN = new JButton("Info");
        theInfoBTN.setMnemonic('I');
        theInfoBTN.addActionListener(this);
        theInfoBTN.setActionCommand("BoardInfo");
        theCons.gridy++;
        theCons.fill=GridBagConstraints.NONE;
        theCons.anchor=GridBagConstraints.EAST;
        thePanel.add(theInfoBTN,theCons);

        return flowLeft(thePanel);
	}

    private HashSet<DomBotType> getMyTypes() {
        HashSet<DomBotType> myTypes=new HashSet<DomBotType>();
        for (Object botType : myBotTypeList.getSelectedValues()){
            myTypes.add((DomBotType) botType);
        }
        return myTypes;
    }

    private JList getTypeList() {
    	myBotTypeList = new JList(DomBotType.values());
    	ArrayList<DomBotType> thePossibleTypes = new ArrayList<DomBotType>(); 
    	for (DomBotType botType : DomBotType.values()){
    		thePossibleTypes.add(botType);
    	}
    	myBotTypeList.setBorder(new TitledBorder("Choose type(s) of this strategy"));
    	int[] theSelectedIndices = new int[20];
    	int i=0;
    	for (DomBotType type : myChosenStrategy.getTypes()){
    		theSelectedIndices[i++] = thePossibleTypes.indexOf(type); 
    	}
    	myBotTypeList.setSelectedIndices(theSelectedIndices);
    	myBotTypeList.setBorder(new TitledBorder("Ctrl-button for multiple types"));

    	return myBotTypeList;
	}

	private JScrollPane getDescriptionField() {
		myDescriptionField = new JTextArea(myChosenStrategy.getDescription().replaceAll("XXXX", System.getProperty( "line.separator" )) ,5,30);
		JScrollPane thePane = new JScrollPane(myDescriptionField);
		return thePane;
	}

    @Override
	public void actionPerformed(ActionEvent aE) {
      if (aE.getActionCommand().equals( "StartStateInfo" )) {
    	  String theText= "<html><u>This tab allows defining a forced starting state (instead of starting with 7 Coppers and 3 Estates)</u>"
        		+"<br>Use the correct spelling for cards and capitals where needed (and no ending 's' for multiples)!"
        		+"<br>This is correct: <i>2 Mountebank, 2 Copper, Village</i>" 
        		+"<br><br>The draw deck can either be shuffled or put into a fixed order." 
        		+"<br><br>If starting hand is empty, 5 cards will be drawn." 
        		+"</html>";
          JOptionPane.showMessageDialog(this, theText, "Info", JOptionPane.INFORMATION_MESSAGE);
      }
    	
      if (aE.getActionCommand().equals( "BoardInfo" )) {
    	  String theText= "<html><u>This tab allows defining a board</u>"
        		+"<br><br>Use the correct spelling for cards and capitals where needed!"
        		+"<br>This is correct: <i>Chapel, Throne Room, Pawn, Courtyard, Vault, Caravan, Cellar, Moat, Witch and Worker's Village</i>" 
        		+"<br><br>The Bane card for Young Witch, if present, must be defined separately" +
        				"</html>";
          JOptionPane.showMessageDialog(this, theText, "Info", JOptionPane.INFORMATION_MESSAGE);
      }
    	
      if (aE.getActionCommand().equals( "Move Up" )) {
    	 JButton theButton = (JButton) aE.getSource();
    	 DomBuyRulePanel thePanel = (DomBuyRulePanel) theButton.getClientProperty("my rule box");
		 int theIndex = myBuyRulePanels.indexOf(thePanel);
		 if (theIndex>0) {
			 myBuyRulePanels.remove(theIndex);
			 myBuyRulePanels.add(theIndex - 1, thePanel);
			 fillBuyRulesPanel();
			 return;
		 }
      }
      if (aE.getActionCommand().equals( "Move Down" )) {
     	 final JButton theButton = (JButton) aE.getSource();
     	 DomBuyRulePanel thePanel = (DomBuyRulePanel) theButton.getClientProperty("my rule box");
 		 int theIndex = myBuyRulePanels.indexOf(thePanel);
		 if (theIndex<myBuyRulePanels.size()-1) {
			 myBuyRulePanels.remove(theIndex);
			 myBuyRulePanels.add(theIndex+1, thePanel);
			 fillBuyRulesPanel();
			 return;
		 }
      }
      if (aE.getActionCommand().equals("Delete")) {
      	 JButton theButton = (JButton) aE.getSource();
      	 DomBuyRulePanel thePanel = (DomBuyRulePanel) theButton.getClientProperty("my rule box");
    	 myBuyRulePanels.remove(thePanel);
	     fillBuyRulesPanel();
		 return;
      }
      if (aE.getActionCommand().startsWith("add rule")) {
    	 DomBuyRule theDummyBuyRule = new DomBuyRule("Copper", null, null);
    	 int theIndex = new Integer(aE.getActionCommand().substring(9));
    	 myBuyRulePanels.add(theIndex, theDummyBuyRule.getGuiPanel(this));
 	     fillBuyRulesPanel();
 		 return;
      }
      if (aE.getActionCommand().equals("Duplicate")) {
      	 final JButton theButton = (JButton) aE.getSource();
      	 DomBuyRulePanel thePanel = (DomBuyRulePanel) theButton.getClientProperty("my rule box");
  		 int theIndex = myBuyRulePanels.indexOf(thePanel);
    	 myBuyRulePanels.add(theIndex, thePanel.getBuyRule(myChosenStrategy).getGuiPanel(this));
 	     fillBuyRulesPanel();
      }
      if (aE.getActionCommand().startsWith("toggle")) {
    	 for (DomBuyRulePanel thePanel : myBuyRulePanels) {
    	  thePanel.toggleBuyConditions();
    	 }
       }
      if (aE.getActionCommand().equals("Save" )) {
        savePlayer();
      }
      if (aE.getActionCommand().equals("Cancel" )) {
        dispose();
      }
	}
    @Override
	public void dispose() {
      myEngine.getGui().getGlassPane().setVisible(false);
      super.dispose();
	}
}