package be.aga.dominionSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import be.aga.dominionSimulator.enums.DomBotType;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class DomDeck extends EnumMap< DomCardName, ArrayList<DomCard> > {
    protected static final Logger LOGGER = Logger.getLogger( DomDeck.class );
    static {
        LOGGER.setLevel( DomEngine.LEVEL );
        LOGGER.removeAllAppenders();
        if (DomEngine.addAppender)
            LOGGER.addAppender(new ConsoleAppender(new SimpleLayout()) );
    }

    private ArrayList< DomCard > drawDeck = new ArrayList< DomCard >();
    private final ArrayList< DomCard > discardPile = new ArrayList< DomCard >();
    private ArrayList< DomCard > islandMat = new ArrayList< DomCard >();

    private DomPlayer owner;
	private ArrayList<DomCard> putAsideCards=new ArrayList<DomCard>();
    public static final int DISCARD = 0;
    public static final int TOP_OF_DECK = 1;
    public static final int HAND = 2;

    public DomDeck (DomPlayer aDomPlayer) {
      super( DomCardName.class );
      owner = aDomPlayer; 
      islandMat = new ArrayList< DomCard >();
    }

    public void shuffle() {
      owner.setKnownTopCards(0);
	  if (owner.getTypes().contains(DomBotType.RiffleShuffle) )
		doRiffleShuffle();
  	  else  
  		if (owner.getTypes().contains(DomBotType.ShuffleOverhand))
	  	  doOverhandShuffle();
  	    else
          Collections.shuffle( drawDeck );
      if (DomEngine.haveToLog) DomEngine.addToLog( owner + " shuffles deck" );
	  if (count(DomCardName.Stash)>0)
        handleStash();
    }

	private void handleStash() {
	  ArrayList<DomCard> theStashes= new ArrayList<DomCard>();
	  for (DomCard card : drawDeck){
		if (card.getName()==DomCardName.Stash){
		  theStashes.add(card);
		}
	  }
	  for (DomCard card : theStashes){
		owner.putOnTopOfDeck(drawDeck.remove(drawDeck.indexOf(card)));
	  }
	}

    private void doOverhandShuffle() {
      for (int j=0;j<2+Math.random()*4;j++){
        ArrayList<DomCard> newDeck = new ArrayList<DomCard>();
    	while (!drawDeck.isEmpty()) {
		    int theNumber = (int) (drawDeck.size()*Math.random()/3.0);
		    for (int i=theNumber;i>=0;i--)
			  newDeck.add(0,drawDeck.remove(i));
    	}
    	drawDeck=newDeck;
     	LOGGER.debug("Iteratie " +j);
        LOGGER.debug("-------------");
    	for (DomCard card : drawDeck){
    		LOGGER.debug(card.getName());
    	}
      }
	}

    private void doRiffleShuffle() {
        for (int j=0;j<2+Math.random()*4;j++){
          ArrayList<DomCard> newDeck = new ArrayList<DomCard>();
          ArrayList<DomCard> leftDeck = new ArrayList<DomCard>();
          ArrayList<DomCard> rightDeck = new ArrayList<DomCard>();
		  int theSplit = (int) (drawDeck.size()/2.0 + drawDeck.size()*Math.random()/10.0);
		  for (int i=0;i<theSplit;i++){
			leftDeck.add(drawDeck.remove(0));
		  }
		  while (!drawDeck.isEmpty())
			rightDeck.add(drawDeck.remove(0));
		  
      	  while (!leftDeck.isEmpty() || !rightDeck.isEmpty()) {
            int i=0;
      		double theRiffledCards = Math.random()*4;
  		    for (i=0;i<theRiffledCards && leftDeck.size()>0;i++){
  		      newDeck.add(leftDeck.remove(leftDeck.size()-1));
  		    }
      		theRiffledCards = Math.random()*4;
  		    for (i=0;i<theRiffledCards && rightDeck.size()>0;i++){
  		      newDeck.add(rightDeck.remove(rightDeck.size()-1));
  		    }
      	  }
      	  drawDeck=newDeck;
         	LOGGER.debug("Iteratie " );
            LOGGER.debug("-------------");
        	for (DomCard card : drawDeck){
        		LOGGER.debug(card.getName());
        	}
        }
  	}

    public ArrayList< DomCard > getTopCards( int aI ) {
    	owner.setKnownTopCards(owner.getKnownTopCards()-aI);
        ArrayList< DomCard > theTopCards = new ArrayList< DomCard >();
        for (int i=0;i<aI ;i++) {
          if (drawDeck.isEmpty()) {
            if (discardPile.isEmpty())
              return theTopCards;
            drawDeck.addAll( discardPile);
            discardPile.clear();
            shuffle();
          }
          theTopCards.add( drawDeck.remove(0) );
        }
        return theTopCards;
    }

    public int count( DomCardName aCardName ) {
      ArrayList< DomCard > theList = get(aCardName); 
      return theList==null ? 0 : theList.size();
    }

    public int count( DomCardType aCardType ) {
        int theCount = 0;
        for (DomCardName theCardName : keySet()) {
          if (theCardName.hasCardType( aCardType )) {
            theCount+= get( theCardName ).size();
          }
        }
        return theCount;
    }

    public double getTotalTreasure() {
        double theTotalMoney =0;
        for (DomCardName theCardName : keySet()) {
           theTotalMoney+= theCardName.hasCardType( DomCardType.Treasure ) ? theCardName.getCoinValue() * get(theCardName).size() : 0;
        }
        return theTotalMoney;
    }

    public void addToDiscardPile( ArrayList< DomCard > aPile ) {
    	for (DomCard card : aPile)
    	  discard(card);
    }

    public void discard( DomCard aCard ) {
      discardPile.add( aCard );
      aCard.doWhenDiscarded();
    }

    public void gain( DomCard aCard , int aLocation) {
		if (checkPossessed(aCard) || !addPhysicalCard(aCard))
		  return;
		if (aLocation==HAND){
	      owner.getCardsInHand().add( aCard );
	      if (DomEngine.haveToLog) DomEngine.addToLog( owner + " gains a " + aCard + " in hand" );
		}
		if (aLocation==TOP_OF_DECK){
		  owner.putOnTopOfDeck(aCard);      	
		}
		if (aLocation==DISCARD){
	    	if (aCard.getName()==DomCardName.Nomad_Camp
	    	 || (!owner.getCardsFromPlay(DomCardName.Royal_Seal).isEmpty() && aCard.getDiscardPriority(1)>=16))
	    	  owner.putOnTopOfDeck(aCard);
	    	else
	          discardPile.add( aCard );
		}
		aCard.doWhenGained();
    }

    public boolean addPhysicalCard( DomCard aCard ) {
      //TODO Trader might conflict with Watchtower ????
      if (owner.countInDeck( DomCardName.Trader )>0 && owner.usesTrader(aCard))
        return false;
    	
      if (owner.countInDeck( DomCardName.Watchtower )>0 && owner.usesWatchtower(aCard))
        return false;
      
      if (!containsKey( aCard.getName() )) {
        put(aCard.getName(), new ArrayList< DomCard >());
      }
      get(aCard.getName()).add( aCard );
      aCard.setOwner(owner);
      owner.getCardsGainedLastTurn().add(aCard.getName());
      return true;
    }

    /**
     * @param aType
     * @return
     */
    public ArrayList< DomCard > revealUntilType( DomCardType aType ) {
        ArrayList< DomCard > theTopCards = new ArrayList< DomCard >();
        int theTotalDeckSize = getDeckAndDiscardSize();
        for (int i = 0; i< theTotalDeckSize;i++){
          DomCard theCard = getTopCard();
          theTopCards.add(theCard);
          if (DomEngine.haveToLog) DomEngine.addToLog( owner + " reveals " + theCard );
          if (theCard.hasCardType( aType ))
            break;
        }
        return theTopCards;
    }

    /**
     * 
     */
    public void discardTopCardFromDeck() {
      DomCard theCard = null;
      if (drawDeck.isEmpty()) {
        if (DomEngine.haveToLog) DomEngine.addToLog( owner + " has no cards in the deck to discard");
        return;
      }
      theCard = getTopCard();
      if (DomEngine.haveToLog) DomEngine.addToLog( owner + " discards " + theCard + " from the top of his deck");
      discard( theCard);
    }

    private boolean checkPossessed(DomCard aCard) {
        if (owner.possessor!=null) {
          if (DomEngine.haveToLog) 
            DomEngine.addToLog( owner + " is Possessed by " + owner.possessor + " who will gain " + aCard);
          owner.possessor.gain(aCard);
          return true;
        }
        return false;
	}

    public ArrayList< DomCard > revealTopCards( int aI ) {
        ArrayList< DomCard > theTopX = new ArrayList< DomCard >();
        int theTotalDeckSize = drawDeck.size()+discardPile.size();
        for (int i = 0; i<aI && i< theTotalDeckSize;i++){
          theTopX.add(getTopCard());
        }
        if (DomEngine.haveToLog) {
            if (theTopX.isEmpty()) {
              DomEngine.addToLog( owner + " reveals nothing" );
            } else {
              DomEngine.addToLog( owner + " reveals " + theTopX );
            }
        }
        return theTopX;
    }

    /**
     * @return
     */
    public DomCard getTopCard() {
      return getTopCards( 1 ).get( 0 );
    }

    /**
     * 
     */
    public int countVictoryPoints() {
      int count = 0; 
      for (DomCardName theCardName : keySet()) {
        if (theCardName.hasCardType( DomCardType.Victory) || theCardName.hasCardType( DomCardType.Curse )) {
          count += get( theCardName ).size()* theCardName.getVictoryValue(owner);    
        }
      }
      for (DomCard theCard : islandMat) {
        if (theCard.hasCardType( DomCardType.Victory) || theCard.hasCardType( DomCardType.Curse )) {
          count += theCard.getVictoryValue();    
        }
      }
      return count;
    }

    /**
     * @param aRemove
     */
    public void trash( DomCard aRemove ) {
      if (owner.possessor!=null) {
    	putCardAside(aRemove);
      } else {
	      try {
	        get(aRemove.getName()).remove( aRemove );
	        aRemove.owner=null;
	      } catch ( Exception e ) {
	        LOGGER.error("Problem when trashing " + aRemove);
	      }
      }
    }

    private void putCardAside(DomCard aRemove) {
      if (DomEngine.haveToLog) DomEngine.addToLog( owner + " puts aside "+aRemove );
	  putAsideCards.add(aRemove);
	}

	/**
     * 
     */
    public void showContents() {
        if (!DomEngine.haveToLog)
          return;
            
        StringBuilder theMessage=null;
        for (DomCardName theCardName : keySet()) {
          if (get( theCardName ).size() >0) {
            if (theMessage == null) {
              theMessage=new StringBuilder();
//            	.append( owner + " shows this deck (");
              theMessage.append("&nbsp;&nbsp;&nbsp;").append(countAllCards()).append(" cards : ["); 
            } else {
              theMessage.append( ", " );
            }
            theMessage.append( get( theCardName ).size() ).append( " " ).append( theCardName.toHTML() );
          }
        }
        theMessage.append( "]" );
        
        DomEngine.addToStartOfLog(theMessage.toString());
    }

    /**
     * @return
     */
    public int countAllCards() {
        int theCount=islandMat.size();
        for (DomCardName theName : keySet()){
            theCount+=get(theName).size();
        }
        return theCount;
    }

    /**
     * @return
     */
    public int getTotalMoney() {
        int theTotal = 0;
        for (DomCardName theCardName : keySet()) {
          theTotal += get(theCardName).size() * theCardName.getCoinValue();
        }
        return theTotal;
    }

    /**
     * @param aLastCard
     */
    public void putOnTopOfDeck( DomCard aCard ) {
    	owner.setKnownTopCards(owner.getKnownTopCards()+1);
       drawDeck.add(0, aCard );
    }

    /**
     * @param aCard
     */
    public void removePhysicalCard( DomCard aCard ) {
      get(aCard.getName()).remove( aCard );
    }

    /**
     * @return
     */
    public int getDeckAndDiscardSize() {
      return drawDeck.size()+discardPile.size();
    }

    /**
     * 
     */
    public void returnCardsFromIslandMat() {
      if (DomEngine.haveToLog) DomEngine.addToLog( owner + " returns all cards from the Island Mat: " + getIslandMat() );
      for (DomCard theCard : islandMat) {
        get(theCard.getName()).add( theCard );
        theCard.setOwner(owner);
        discardPile.add(theCard);
      }
      islandMat.clear();
    }

    /**
     * @param aCard
     */
    public void moveToIslandMat( DomCard aCard ) {
      removePhysicalCard( aCard );
      islandMat.add( aCard );
    }

    /**
     * @return
     */
    public ArrayList< DomCard > getIslandMat() {
      return islandMat;
    }

    /**
     * @return
     */
    public ArrayList< DomCard > collectAllCards() {
        ArrayList< DomCard > theCards = new ArrayList< DomCard >();
        for (DomCardName theCardName : keySet()) {
          theCards.addAll( get( theCardName ));    
        }
        return theCards;
    } 

	public void putDeckInDiscard() {
        if (DomEngine.haveToLog) DomEngine.addToLog( owner + " discards entire deck" );
    	discardPile.addAll(drawDeck);
		drawDeck.clear();
		owner.setKnownTopCards(0);
	}

	public int countDifferentCards() {
    	ArrayList<DomCardName> theSingleCards = new ArrayList<DomCardName>();
        for (DomCard theCard : islandMat) {
			if (!theSingleCards.contains(theCard.getName())){
        		theSingleCards.add(theCard.getName());
        	}
        }
		int theCount=theSingleCards.size();
		for (DomCardName theCardName : keySet()) {
	      theCount+=get(theCardName).isEmpty()? 0 : 1;
		}
		return theCount;
	}
	
	public DomCard getBottomCard(){
	   if (drawDeck.isEmpty())
		   return null;
	   return drawDeck.remove(drawDeck.size()-1);	
	}

	public void putCardOnBottomOfDeck(DomCard theBottomCard) {
		drawDeck.add(theBottomCard);
	}

	public ArrayList<DomCard> getPutAsideCards() {
		ArrayList<DomCard> thePutAsideCards = putAsideCards;
		putAsideCards=new ArrayList<DomCard>();
		return thePutAsideCards;
	}

	public ArrayList<DomCard> revealUntilCost(int aCost) {
        ArrayList< DomCard > theTopCards = new ArrayList< DomCard >();
        int theTotalDeckSize = getDeckAndDiscardSize();
        for (int i = 0; i< theTotalDeckSize;i++){
          DomCard theCard = getTopCard();
          theTopCards.add(theCard);
          if (DomEngine.haveToLog) DomEngine.addToLog( owner + " reveals " + theCard );
          if (theCard.getCoinCost(owner.getCurrentGame())>=aCost)
            break;
        }
        return theTopCards;
	}

	public DomCardName getMostLikelyCardOnTop() {
		if (getDeckAndDiscardSize()==0)
			return null;
		if (drawDeck.isEmpty()){
		  drawDeck.addAll(discardPile);
		  discardPile.clear();
		}
		if (owner.getKnownTopCards()>0)
			return drawDeck.get(0).getName();
		shuffle();
		DomCardName theMostLikelyCard=null;
		EnumMap<DomCardName, Integer> theCounts = new EnumMap<DomCardName, Integer>(DomCardName.class);
		for (DomCard card : drawDeck){
		  if (theCounts.get(card.getName())==null){
			  theCounts.put(card.getName(),1);
		  } else {
			  theCounts.put(card.getName(),theCounts.get(card.getName())+1);
		  }
		}
		for (DomCardName cardName : theCounts.keySet()){
			if (theMostLikelyCard==null
	           || theCounts.get(cardName)>theCounts.get(theMostLikelyCard))
			  theMostLikelyCard=cardName;
		}
		return theMostLikelyCard;
	}

	public ArrayList<DomCard> revealUntilVictoryOrCurse() {
        ArrayList< DomCard > theTopCards = new ArrayList< DomCard >();
        int theTotalDeckSize = getDeckAndDiscardSize();
        for (int i = 0; i< theTotalDeckSize;i++){
          DomCard theCard = getTopCard();
          theTopCards.add(theCard);
          if (DomEngine.haveToLog) DomEngine.addToLog( owner + " reveals " + theCard );
          if (theCard.hasCardType( DomCardType.Victory) || theCard.hasCardType(DomCardType.Curse))
            break;
        }
        return theTopCards;
	}

	public ArrayList<DomCard> revealUntilActionOrTreasure() {
        ArrayList< DomCard > theTopCards = new ArrayList< DomCard >();
        int theTotalDeckSize = getDeckAndDiscardSize();
        for (int i = 0; i< theTotalDeckSize;i++){
          DomCard theCard = getTopCard();
          theTopCards.add(theCard);
          if (DomEngine.haveToLog) DomEngine.addToLog( owner + " reveals " + theCard );
          if (theCard.hasCardType( DomCardType.Action) || theCard.hasCardType(DomCardType.Treasure))
            break;
        }
        return theTopCards;
	}

	public boolean drawPileHasCard(DomCardName aCardName) {
		for (DomCard card : drawDeck)
			if (card.getName()== aCardName)
				return true;
		return false;
	}

	public int countInDrawAndDiscard(DomCardName aCardName) {
		int theCount=0;
		for (DomCard card : drawDeck)
			theCount+=card.getName()== aCardName ? 1 : 0;
		for (DomCard card : discardPile)
			theCount+=card.getName()== aCardName ? 1 : 0;
		return theCount;
	}

	public void gain(DomCard aCard) {
		gain(aCard,DISCARD);
	}

	public void addPhysicalCardFromMasquerade(DomCard aCard) {
		//cards that were passed are not considered 'gained' so can not be watchtowered...
        if (!containsKey( aCard.getName() )) 
          put(aCard.getName(), new ArrayList< DomCard >());
        get(aCard.getName()).add( aCard );
        aCard.setOwner(owner);
	}

	public ArrayList<DomCard> removeCardsFromDiscard(DomCardName aCardName) {
		ArrayList<DomCard> theCards = new ArrayList<DomCard>();
		for (DomCard theCard : discardPile) {
			if (theCard.getName()==aCardName) 
				theCards.add(theCard);
		}
		for (DomCard theCard : theCards) {
		  discardPile.remove(theCard);
		}
		return theCards;
	}

	public double getDrawDeckSize() {
		return drawDeck.size();
	}

	public ArrayList<DomCard> getDiscardPile() {
		return discardPile;
	}
	@Override
	public String toString() {
		StringBuilder theString= new StringBuilder();
		for (DomCardName cardName : keySet()){
			theString.append(cardName + "[" +get(cardName).size()+"]");
		}
		return theString.toString();
	}

	public DomCard lookAtTopCard() {
		return drawDeck.get(0);
	}

	public boolean checkForcedStart() {
		if (owner.getforcedStart()==0)
		  return true;
		int theCopperCount = 0;
		for (int i=0;i<5;i++){
		  theCopperCount+=drawDeck.get(i).getName()==DomCardName.Copper ? 1 : 0;
		}
		if ((theCopperCount==2 ||theCopperCount==5)
		  && owner.getforcedStart()==52)
		  return true;
		if ((theCopperCount==3 ||theCopperCount==4)
	 	  && owner.getforcedStart()==43)
		  return true;
		return false;
	}
}