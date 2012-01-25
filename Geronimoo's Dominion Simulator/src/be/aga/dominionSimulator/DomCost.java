package be.aga.dominionSimulator;

public class DomCost implements Comparable< DomCost >{

    public static final DomCost ZERO = new DomCost( 0, 0 );
    int coins = 0;
    int potions = 0;

    /**
     * @return Returns the coins.
     */
    public int getCoins() {
        return coins;
    }

    /**
     * @param aCoins The coins to set.
     */
    public void setCoins( int aCoins ) {
        coins = aCoins;
    }

    /**
     * @return Returns the potions.
     */
    public int getPotions() {
        return potions;
    }

    /**
     * @param aPotions The potions to set.
     */
    public void setPotions( int aPotions ) {
        potions = aPotions;
    }


    /**
     * @param aCoinCost
     * @param aPotionCost
     */
    public DomCost ( int aCoinCost , int aPotionCost ) {
        coins = aCoinCost;
        potions = aPotionCost;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( DomCost aO ) {
    	//CAREFUL when using this!!
    	//the cost of King's Court for example will be smaller then the cost of Transmute
    	//and the cost of Transmute will also be smaller than the cost of King's Court! 
        if (this.coins<aO.getCoins() || this.potions<aO.getPotions())
           return -1;
        
        if (this.coins==aO.getCoins() && this.potions==aO.getPotions()) 
            return 0;

        return 1;
    }

    /**
     * @param aCost
     * @return
     */
    public DomCost add( DomCost aCostToAdd ) {
      DomCost theNewCost = new DomCost(  coins + aCostToAdd.coins, potions + aCostToAdd.potions );
//      if (theNewCost.compareTo(ZERO)<0)
//    	  theNewCost=ZERO;
      return theNewCost; 
    }

	public DomCost subtract(DomCost aCostToAdd) {
	      DomCost theNewCost = new DomCost(  coins - aCostToAdd.coins, potions - aCostToAdd.potions );
//	      if (theNewCost.compareTo(ZERO)<0)
//	    	  theNewCost=ZERO;
	      return theNewCost; 
	} 
}