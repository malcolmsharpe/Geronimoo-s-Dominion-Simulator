package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.enums.DomCardName;

public class EstateCard extends DomCard {
    public EstateCard () {
      super( DomCardName.Estate);
    }
    
    @Override
    public int getTrashPriority() {
      if (owner!=null) {
    	if (owner.wantsToGainOrKeep(DomCardName.Estate)) {
//        	if (!owner.isInBuyPhase()) {
//        		//fix for Remodel
//        		owner.getCurrentGame().setBuyphase(true);
//        		if (!owner.wantsToGainOrKeep(DomCardName.Estate)){
//            	  owner.getCurrentGame().setBuyphase(false);
//        		  return 19;
//        		}
//        	}
        	return 35;
    	}
      }
      return super.getTrashPriority();
    }
    
    @Override
    public int getDiscardPriority(int aActionsLeft) {
    	if (aActionsLeft>0 && owner.getCardsInHand().contains(this) 
    	&& !owner.getCardsFromHand(DomCardName.Baron).isEmpty() && owner.getCardsFromHand(DomCardName.Estate).size()==1)
    		return 29;
    	return super.getDiscardPriority(aActionsLeft);
    }
}