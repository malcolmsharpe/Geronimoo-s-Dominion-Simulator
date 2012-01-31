package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class TorturerCard extends DomCard {
    public TorturerCard () {
      super( DomCardName.Torturer);
    }

    public void play() {
      owner.drawCards(3);
      for (DomPlayer thePlayer : owner.getOpponents()) {
          if (thePlayer.checkDefense()) 
         	continue;
	      boolean trashCardPresent=false;
	      if (owner.getCurrentGame().countInSupply(DomCardName.Curse )==0){
	         if (DomEngine.haveToLog) 
	           DomEngine.addToLog( thePlayer + " chooses to gain a Curse because the Curse pile is empty");
	         continue;
	      }
	      ArrayList<DomCard> cardsInHand = thePlayer.getCardsInHand();
	      for (DomCard theCard : cardsInHand){
	    	if (theCard.hasCardType(DomCardType.Trasher)){
	    	   trashCardPresent = true;
	    	   break;
	    	}
	      }
	      if (trashCardPresent)
	    	thePlayer.gainInHand(DomCardName.Curse);
	      else  
	        thePlayer.doForcedDiscard(2, false);
      }
    }
}