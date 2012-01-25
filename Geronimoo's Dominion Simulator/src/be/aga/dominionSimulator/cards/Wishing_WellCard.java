package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.enums.DomCardName;

public class Wishing_WellCard extends DomCard {
    public Wishing_WellCard () {
      super( DomCardName.Wishing_Well);
    }

    public void play() {
      owner.addActions(1);
      owner.drawCards(1);
      if (owner.getDeckSize()==0)
    	return;
      DomCardName theChoice=owner.getDeck().getMostLikelyCardOnTop();
      if (DomEngine.haveToLog) DomEngine.addToLog(owner + " names " + theChoice.toHTML());
      DomCard theRevealedCard = owner.revealTopCards(1).get(0);
	  if (theRevealedCard.getName()==theChoice){
        owner.putInHand(theRevealedCard);
	  }else{
        owner.putOnTopOfDeck(theRevealedCard);
      }
    }
}