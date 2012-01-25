package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class MultiplicationCard extends DomCard {

	private final ArrayList<DomCard> myDurationCards = new ArrayList<DomCard>();

	public MultiplicationCard(DomCardName aCardName) {
	  super(aCardName);
	}
	
	public void play(){
	  cleanDurationsFromPreviousGames();
      DomCard theCardToMultiply = getCardToMultiply();
      if (theCardToMultiply == null)
    	return;
      //little fix for Tactician
      if (theCardToMultiply.hasCardType(DomCardType.Duration) && theCardToMultiply.getName()!=DomCardName.Tactician){
        myDurationCards.add(theCardToMultiply);
        setDiscardAtCleanup(false);
      }
      owner.removeCardFromHand(theCardToMultiply);
      owner.getCardsInPlay().add(theCardToMultiply);
      play(theCardToMultiply, 1);
      //little fix for Tactician
      if (theCardToMultiply.getName()!=DomCardName.Tactician){
	      play(theCardToMultiply, 2);
	      if (getName()==DomCardName.King$s_Court) {
	        play(theCardToMultiply, 3);
	      }
      }
   }

	private void cleanDurationsFromPreviousGames() {
	  if (myDurationCards.isEmpty()) 
		return;
	  //this is needed to check for lingering Duration cards linked to this card from a previous game
	  for (DomCard card : myDurationCards){
		  if (!owner.getCardsInPlay().contains(card)){
			  myDurationCards.clear();
			  return;
		  }
	  }
	}

	private void play(DomCard theCardToMultiply, int i ) {
		String aLogAppend = " with the " + this;
		if (i==2) 
		  aLogAppend = " again";
		if (i==3) 
	      aLogAppend = " a third time";
		owner.increaseActionsPlayed();
		if (DomEngine.haveToLog ) {
		  DomEngine.addToLog( owner + " plays " + theCardToMultiply + aLogAppend);
		  DomEngine.logIndentation++;
		}
		if (i>1){
  		  playSecondTime(theCardToMultiply);
		}else {
		  theCardToMultiply.play();
		}
		if (DomEngine.haveToLog ) {
  		  DomEngine.logIndentation--;
		}
	}
	    
    private void playSecondTime(DomCard theCardToMultiply) {
        switch (theCardToMultiply.getName()) {
        case Feast:
          //fix for Feast which gets trashed and causes null pointer when played a second time
          DomCardName theDesiredCard = owner.getDesiredCard(new DomCost( 5, 0), false);
		  if (theDesiredCard==null) {
		    if (DomEngine.haveToLog) DomEngine.addToLog("but gains nothing");
		  } else {
			owner.gain(theDesiredCard);
		  }
		  break;
        case Mining_Village:
          //fix for Mining Village which gets trashed and causes null pointer when played a second time
          owner.addActions( 2 );
          owner.drawCards( 1 );
          if (owner.getCardsInPlay().contains(theCardToMultiply)) {
            if (owner.addingThisIncreasesBuyingPower( new DomCost( 2,0 ))) {
              owner.trash(owner.removeCardFromPlay( theCardToMultiply ));
              owner.addAvailableCoins( 2 );
            }
          }
          break;
        case Embargo:
        	theCardToMultiply.owner=owner;
        	owner.playThis(theCardToMultiply);
        	break;
        default:
        	owner.playThis(theCardToMultiply);
        }
	}

	public void resolveDuration() {
      for (DomCard card : myDurationCards) {
    	if (DomEngine.haveToLog) DomEngine.addToLog( owner + " played " +card + " with "+ this);
	    card.resolveDuration();
	    if (getName()==DomCardName.King$s_Court){
	      if (DomEngine.haveToLog) DomEngine.addToLog( owner + " played " +card + " with "+ this);
		  card.resolveDuration();
	    }
      }
      myDurationCards.clear();
    }
    
    public DomCard getCardToMultiply( ) {
        DomCard theCardToPlay = null;
        for (int i = 0;i<owner.getCardsInHand().size();i++) {
          DomCard theCard = owner.getCardsInHand().get( i );
          if (theCard.getName()==DomCardName.Throne_Room || theCard.getName()==DomCardName.King$s_Court){
        	return theCard;
          }
          if (theCard.hasCardType(DomCardType.Action) && theCard.wantsToBePlayed()){
        	if (theCard.hasCardType(DomCardType.Terminal)  
                && (owner.getActionsLeft()>0 || owner.getCardsFromHand(DomCardType.Terminal).size()==owner.getCardsFromHand(DomCardType.Action).size()) 
        	    && (theCardToPlay == null ||theCard.getDiscardPriority(1)> theCardToPlay.getDiscardPriority(1))) {
              theCardToPlay = theCard;
        	} 
    		if (!theCard.hasCardType(DomCardType.Terminal) 
      	     && (theCardToPlay == null ||theCard.getDiscardPriority(1)> theCardToPlay.getDiscardPriority(1))){
               theCardToPlay = theCard;
       		}
          }
        }
        //TODO mandatory playing of action cards with Throne Room
        return theCardToPlay;
    }

    @Override
    public int getPlayPriority() {
    	int theActionCount=0;
    	for (DomCard theCard : owner.getCardsInHand()) {
    		if (theCard==this)
    			continue;
    		if (theCard.getName()==DomCardName.King$s_Court
    		|| theCard.getName()==DomCardName.Throne_Room)
    			return 0;
            if (theCard.hasCardType(DomCardType.Action))
            	theActionCount++;
    	}
    	if (theActionCount==1)
    		return 0;
    	return super.getPlayPriority();
    }
    
    @Override
    public boolean wantsToBePlayed() {
    	int theActionCount=0;
    	for (DomCard theCard : owner.getCardsInHand()) {
    		if (theCard==this 
    		 || theCard.getName()==DomCardName.King$s_Court
    		 || theCard.getName()==DomCardName.Throne_Room)
    			continue;
            if (theCard.hasCardType(DomCardType.Action)) 
            		//&& theCard.wantsToBePlayed())
            	theActionCount++;
    	}
    	return theActionCount>0;
    }
}