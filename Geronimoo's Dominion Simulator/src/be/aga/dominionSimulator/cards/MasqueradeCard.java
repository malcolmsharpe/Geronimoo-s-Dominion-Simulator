package be.aga.dominionSimulator.cards;

import java.util.Collections;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.enums.DomCardName;

public class MasqueradeCard extends DomCard {
    public MasqueradeCard () {
      super( DomCardName.Masquerade);
    }

    public void play() {
      owner.drawCards(2);
      if (!owner.getOpponents().isEmpty()) {
        owner.passCardToTheLeftForMasquerade(owner.chooseCardToPass(),owner);
      }
      Collections.sort(owner.getCardsInHand(),SORT_FOR_TRASHING);
      if (owner.getCardsInHand().isEmpty())
    	  return;
      DomCard theCardToTrash=owner.getCardsInHand().get( 0 );
      if (theCardToTrash.getTrashPriority() < 16 && !owner.removingReducesBuyingPower( theCardToTrash )) {
        owner.trash(owner.removeCardFromHand( theCardToTrash));
      } else {
        if (DomEngine.haveToLog ) DomEngine.addToLog( owner + " trashes nothing");
      }
    }
}