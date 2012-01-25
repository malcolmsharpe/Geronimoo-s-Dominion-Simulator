package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.enums.DomCardName;

public class PawnCard extends DomCard {
    public PawnCard () {
      super( DomCardName.Pawn);
    }

    public void play() {
      int theChoicesCount=0;
      boolean coinsChosen=false;
      boolean actionChosen=false;
      
      if (owner.getNextActionToPlay()!=null && owner.getActionsLeft()==0) {
    	  owner.addActions(1);
    	  theChoicesCount++;
    	  actionChosen=true;
      }
      if (owner.addingThisIncreasesBuyingPower( new DomCost( 1,0 ))) {
    	  owner.addAvailableCoins(1);
    	  theChoicesCount++;
    	  coinsChosen=true;
      }
      if (owner.getDeckSize()>0 && theChoicesCount<2){
    	  owner.drawCards(1);
    	  theChoicesCount++;
      }
      if (theChoicesCount<2) {
    	  owner.addAvailableBuys(1);
    	  theChoicesCount++;
      }
      if (theChoicesCount<2 && !coinsChosen){
    	  owner.addAvailableCoins(1);
          theChoicesCount++;
      }
      if (theChoicesCount<2 && !actionChosen){
    	  owner.addActions(1);
          theChoicesCount++;
      }

    }
}