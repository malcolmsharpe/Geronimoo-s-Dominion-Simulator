package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class Horn_of_PlentyCard extends DomCard {
    public Horn_of_PlentyCard () {
      super( DomCardName.Horn_of_Plenty);
    }

    public void play() {
  	  ArrayList<DomCardName> theSingleCards = new ArrayList<DomCardName>();
      for (DomCard card : owner.getCardsInPlay()) {
		if (!theSingleCards.contains(card.getName())){
    	  theSingleCards.add(card.getName());
    	}
      }
      if (DomEngine.haveToLog) 
      	DomEngine.addToLog( owner + " has " + theSingleCards.size() + " different cards in play");
      DomCardName theCardToGain = owner.getDesiredCard(new DomCost(theSingleCards.size(), 0), false);
      if (theCardToGain==null) {
    	//possibly null if played by Venture
        theCardToGain=owner.getCurrentGame().getBestCardInSupplyFor(owner, null, new DomCost(theSingleCards.size(), 0));
      }
      if (theCardToGain==null) 
        return;
      owner.gain(theCardToGain);
      if (theCardToGain.hasCardType(DomCardType.Victory))
        owner.trash(owner.removeCardFromPlay(this));
    }
    
    @Override
    public boolean wantsToBePlayed() {
    	  ArrayList<DomCardName> theSingleCards = new ArrayList<DomCardName>();
  		  theSingleCards.add(getName());
          for (DomCard card : owner.getCardsInPlay()) {
    		if (!theSingleCards.contains(card.getName())){
        	  theSingleCards.add(card.getName());
        	}
          }
          DomCardName theCardToGain = owner.getDesiredCard(new DomCost(theSingleCards.size(), 0), false);
          if (theCardToGain==null) {
              return false;
          }
          return true;
    }
}