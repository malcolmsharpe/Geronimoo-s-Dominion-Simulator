package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class Mining_VillageCard extends DomCard {
    public Mining_VillageCard () {
      super( DomCardName.Mining_Village);
    }

    public void play() {
      owner.addActions( 2 );
      owner.drawCards( 1 );
      posibblyTrashThis();
    }

    private final void posibblyTrashThis() {
      //quick fix for Tactician
      if (!owner.getCardsFromHand(DomCardName.Tactician).isEmpty())
    	  return;
      if (owner.addingThisIncreasesBuyingPower( new DomCost( 2,0 ))) {
    	DomPlayer theOwner = owner;
        owner.trash(owner.removeCardFromPlay( this ));
        //owner has now become null... so we use theOwner
        theOwner.addAvailableCoins( 2 );
      }
    }
}