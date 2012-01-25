package be.aga.dominionSimulator.cards;

import java.util.ArrayList;
import java.util.Collections;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.enums.DomCardName;

public class StewardCard extends DomCard {
    public StewardCard () {
      super( DomCardName.Steward);
    }

    public void play() {
//    	if (owner.getActionsLeft()>0 && owner.getDeckSize()>0) {
//    	  owner.drawCards( 2 );
//    	  return;
//    	}
    	//trash if there are 2 cards you want to trash
        if (!playForTrash())
         //get the money if it ensures a better buy
         if (!playForMoney())
           //draw 2 cards
           if (!playForCards())
        	 //if your deck is empty get $2 anyway (this might have positive effects with +buys
        	 //which are not accounted for (yet)
        	 owner.addAvailableCoins(2);
    }

    private boolean playForCards() {
      if (owner.getDeckSize()==0){
    	  return false;
      }
      owner.drawCards( 2 );
      return true;
    }

    private boolean playForMoney() {
      if (owner.addingThisIncreasesBuyingPower(new DomCost(2, 0))) {
    	owner.addAvailableCoins(2);
        return true;
      }
      return false;
    }

    private boolean playForTrash() {
        ArrayList<DomCard> cardsInHand = owner.getCardsInHand();
        if (cardsInHand.isEmpty())
        	return false;
        if (cardsInHand.size()==1){
        	if (cardsInHand.get(0).getTrashPriority()<16){
              owner.trash(owner.removeCardFromHand( cardsInHand.get(0)));
              return true;
        	} else {
              return false;
        	}
        }
        Collections.sort(cardsInHand,SORT_FOR_TRASHING);
        if (cardsInHand.get(0).getTrashPriority()<20 && cardsInHand.get(1).getTrashPriority()<20) {
        	owner.trash(owner.removeCardFromHand( cardsInHand.get(0)));
        	owner.trash(owner.removeCardFromHand( cardsInHand.get(0)));
        	return true;
        }
        return false;
    }
}