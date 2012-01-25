package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.enums.DomCardName;

public class PeddlerCard extends DomCard {
    public PeddlerCard () {
      super( DomCardName.Peddler);
    }

    public void play() {
      owner.addActions(1);
      owner.addAvailableCoins(1);
      owner.drawCards(1);
    }
    
    @Override
    public int getPlayPriority() {
    	//TODO it's one of the best combos with Salvager, but can't handle multiple buys (yet) 
//    	if (!owner.getCardsFromHand(DomCardName.Salvager).isEmpty()
//    	&& owner.getCardsFromHand(DomCardName.Peddler).size()==1
//    	&& !owner.stillInEarlyGame()) {
//    		//little fix for Salvager so we have a shot at $8 in the end game
//    		return owner.getCardsFromHand(DomCardName.Salvager).get(0).getPlayPriority()+1;
//    	}
    	return super.getPlayPriority();
    }

}