package be.aga.dominionSimulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;

import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;
import be.aga.dominionSimulator.enums.DomSet;

public class DomBoard extends EnumMap< DomCardName, ArrayList<DomCard> > {

    private ArrayList< DomPlayer > players;
    
    private ArrayList< DomCard > trashPile = new ArrayList< DomCard >();
	private ArrayList<DomCard> blackMarketDeck=new ArrayList<DomCard>();
	private ArrayList<DomCard> prizePile=new ArrayList<DomCard>();
    private EnumMap< DomCardName, Integer > embargoTokens = new EnumMap<DomCardName, Integer>(DomCardName.class);
    private HashSet<DomCardName> tradeRouteMat = new HashSet<DomCardName>();
	private int gainsNeededToEndGame;

    public DomBoard ( Class< DomCardName > aKeyType, ArrayList< DomPlayer > aPlayers ) {
      super( aKeyType );
      players = aPlayers;
    }

    public void initialize() {
      gainsNeededToEndGame=0;
      addCustomKingdoms();
      addTreasures();
      addVictoriesAndCurses();
      createPrizePile();
      if (get(DomCardName.Black_Market)!=null) 
    	createBlackMarketDeck();
      markBaneCards();
    }
    
    private void createPrizePile() {
		for (DomCardName cardName : DomCardName.values()){
			if (cardName.hasCardType(DomCardType.Prize)){
				prizePile.add(cardName.createNewCardInstance());
			}
		}
	}

	private void markBaneCards() {
		for (DomPlayer domPlayer : players){
		  for (DomBuyRule buyRule : domPlayer.getBuyRules())
			if (buyRule.getBane()!=null && get(buyRule.getBane())!=null){
  			  for (DomCard card : get(buyRule.getBane())){
				card.setAsBane();
			  }
			}
		}
	}

	public void markAsBane(DomCardName aBane) {
  	  for (DomCard card : get(aBane)){
	    card.setAsBane();
	  }
	}

	private void createBlackMarketDeck() {
    	blackMarketDeck = new ArrayList<DomCard>();
    	for (DomCardName theCardName : DomCardName.values()) {
    	  if (get(theCardName)==null) {
		    if ( theCardName!=DomCardName.Treasure_Map
		    && theCardName.hasCardType(DomCardType.Kingdom)
		    && (theCardName.getPotionCost()==0 || get(DomCardName.Potion)!=null)){
    		  DomCard theCard = theCardName.createNewCardInstance();
    		  theCard.setFromBlackMarket(true);
    		  blackMarketDeck.add(theCard);
   		    }
    	  }
    	}
    	Collections.shuffle(blackMarketDeck);
	}

    private void addCustomKingdoms() {
        for (DomPlayer thePlayer : players) {
          for (DomCardName theCard : thePlayer.getCardsNeededInSupply()){
            addCardPile( theCard );
          }
        }
    }

    public void addCardPile( DomCardName aCardName ) {
        if (get(aCardName)!=null)
            return;
        put( aCardName, new ArrayList< DomCard >() );
        int theNumber = 10;
        if (aCardName.hasCardType(DomCardType.Victory)) {
          theNumber = players.size()<3 ? 8 : 12;
        }
        switch (aCardName) {
		case Estate:
			theNumber+=players.size()*3;			
			break;
		case Curse:
			theNumber=players.size()==1 ? 10 : (players.size()-1)*10;			
			break;
		case Copper:
			theNumber=60;			
			break;
		case Silver:
			theNumber=40;			
			break;
		case Gold:
			theNumber=30;			
			break;
		case Platinum:
			theNumber=12;			
			break;
		default:
			break;
		}
        for (int j =0;j<theNumber;j++) {
          get(aCardName).add( aCardName.createNewCardInstance());
        }
    }

    private void addVictoriesAndCurses() {
      addCardPile(DomCardName.Estate);
      addCardPile(DomCardName.Duchy);
      addCardPile(DomCardName.Province);
      addCardPile(DomCardName.Curse);
    }

    /**
     * 
     */
    private void addTreasures() {
    	addCardPile(DomCardName.Copper);
    	addCardPile(DomCardName.Silver);
    	addCardPile(DomCardName.Gold);
        if (get(DomCardName.Colony)!=null) {
          addCardPile(DomCardName.Platinum);
        }
    }
    
    public DomCard take( DomCardName aCardName ) {
    	if (aCardName.hasCardType(DomCardType.Prize)){
    		for (DomCard thePrize : prizePile){
    			if (thePrize.getName()==aCardName)
    				return prizePile.remove(prizePile.indexOf(thePrize));
    		}
    	}
        ArrayList< DomCard > theList = get(aCardName);
        if (theList==null)
        	return null;
    	if (aCardName.hasCardType(DomCardType.Victory)){
    	  tradeRouteMat.add(aCardName);
    	}
    	//reset this variable (to speed up the getgainsneeded-method)
    	gainsNeededToEndGame=0;
		return theList.isEmpty() ? null : theList.remove( 0 );
    }

    public int countEmptyPiles() {
        int theEmptyPiles=0;
        for (DomCardName theCardName : keySet()) {
            theEmptyPiles+=get(theCardName).size()==0 ? 1 : 0 ;
        }
        return theEmptyPiles;
    }

    public int count( DomCardName aCardName ) {
        ArrayList< DomCard > theList = get(aCardName); 
        return theList==null ? 0 : theList.size();
    }

    public void add( DomCard aCard ) {
       if (aCard.isFromBlackMarket()) {
    	 blackMarketDeck.add(aCard);
       } else {
    	 if (aCard.hasCardType(DomCardType.Prize)){
    		 prizePile.add(aCard);
    	 } else {
		     if (get(aCard.getName())==null) {
		       put(aCard.getName(), new ArrayList< DomCard >());
		     }
		     get(aCard.getName()).add( aCard );
    	 }
       }
       if (aCard.owner!=null) {
         aCard.owner.removePhysicalCard(aCard);
       }
    }

    public void addToTrash( DomCard aRemove ) {
      trashPile.add( aRemove );
    }

    public ArrayList< String > getEmptyPiles() {
        ArrayList< String > theList = new ArrayList< String >();
        for (DomCardName theCardName : keySet()) {
          if (get(theCardName).size()== 0){
              theList.add( theCardName.toHTML() );
          }
        }
        return theList;
    }

    public ArrayList< DomCard > getTrashedCards() {
      return trashPile;
    }

	public void reset() {
		gainsNeededToEndGame=0;
		for (DomCard theCard : trashPile) {
	       add(theCard);
		}
		trashPile.clear();
		for (DomPlayer thePlayer : players) {
	       ArrayList<DomCard> theCards = thePlayer.collectAllCards();
	       for (DomCard theCard : theCards) {
	         add(theCard);
		   }
		}
		Collections.shuffle(blackMarketDeck);
		embargoTokens=new EnumMap<DomCardName, Integer>(DomCardName.class);
		clearTradeRouteMat();
	}

	public void clearTradeRouteMat() {
		tradeRouteMat.clear();
	}

    public DomCard removeFromTrash( DomCard aCard ) {
      return trashPile.remove( trashPile.indexOf( aCard ) );
    }

	public ArrayList<DomCard> revealFromBlackMarketDeck() {
		ArrayList<DomCard> theCards = new ArrayList<DomCard>();
		for (int i=0;i<3 && !blackMarketDeck.isEmpty(); i++) {
	      theCards.add(blackMarketDeck.remove(0));
		}
		return theCards;
	}

	public void returnToBlackMarketDeck(DomCard theCard) {
		blackMarketDeck.add(blackMarketDeck.size()-1, theCard);
	}

	public int getEmbargoTokensOn(DomCardName aCard) {
		if (embargoTokens.get(aCard)==null)
		  return 0;
		return embargoTokens.get(aCard);
	}

	public void putEmbargoTokenOn(DomCardName aCard) {
		Integer theTokens = embargoTokens.get(aCard);
		if (theTokens==null) {
		  embargoTokens.put(aCard, 1);
		} else {
		  embargoTokens.put(aCard, theTokens+1);
		}
	}

	public DomCardName getRandomCardWithEmbargoToken() {
		if (embargoTokens.keySet().isEmpty())
			return DomCardName.Curse;
		ArrayList<DomCardName> theCards = new ArrayList<DomCardName>(Arrays.asList(embargoTokens.keySet().toArray(new DomCardName[0])));
		Collections.shuffle(theCards);
		return theCards.get(0);
	}

	public String getEmbargoInfo() {
		if (embargoTokens.keySet().isEmpty())
			return null;
		String theInfo = "";
		for (DomCardName theCard:embargoTokens.keySet()) {
		  theInfo+=theCard.toHTML()+" ("+embargoTokens.get(theCard).toString()+"), ";
		}
		return theInfo;
	}

	public DomCardName getBestCardInSupplyFor(DomPlayer aPlayer, DomCardType aType, DomCost domCost, boolean anExactCost, DomCardType aForbiddenType) {
		DomCardName theCardToGet = null;
		for (DomCardName theCardName : keySet()){
			if (get(theCardName)!=null 
		    && !get(theCardName).isEmpty() 
		    && (aType==null || theCardName.hasCardType(aType))
		    && (aForbiddenType==null || !theCardName.hasCardType(aForbiddenType))
		    && ((!anExactCost && domCost.compareTo(theCardName.getCost(aPlayer.getCurrentGame()))>=0)
		    	|| (anExactCost && domCost.compareTo(theCardName.getCost(aPlayer.getCurrentGame()))==0))
		    && ( theCardToGet==null ||
		      theCardName.getTrashPriority(aPlayer)>theCardToGet.getTrashPriority(aPlayer))) {
				theCardToGet=theCardName;
			}
		}
		return theCardToGet;
	}
	
	public DomCardName getCardForSwindler(DomPlayer aPlayer, DomCost domCost) {
		DomCardName theCardToGet = null;
		for (DomCardName theCardName : keySet()){
			if (get(theCardName)!=null && !get(theCardName).isEmpty() 
		    && theCardName.getCost(aPlayer.getCurrentGame()).compareTo(domCost)==0
		    && (theCardToGet==null ||
		    	theCardName.getTrashPriority(aPlayer)<theCardToGet.getTrashPriority(aPlayer))) {
			  theCardToGet=theCardName;
			}
		}
		return theCardToGet;
	}

	public int countTradeRouteTokens() {
		return tradeRouteMat.size();
	}

	public boolean isPrizeAvailable(DomCardName cardToBuy) {
		if (cardToBuy==DomCardName.Duchy && count(DomCardName.Duchy)>0)
			return true;
		for (DomCard card : prizePile){
			if (card.getName()==cardToBuy)
				return true;
		}
		return false;
	}

	public double countCardsInSmallestPile() {
		int theSmallest=10;
		for (DomCardName cardName : keySet()){
			if (get(cardName).size()<theSmallest && get(cardName).size()>0)
				theSmallest=get(cardName).size();
		}
		return theSmallest;
	}

	public int getGainsNeededToEndGame() {
		if (gainsNeededToEndGame!=0)
			return gainsNeededToEndGame;
		ArrayList<Integer> theCounts = new ArrayList<Integer>();
		for (DomCardName cardName : keySet()){
			theCounts.add(get(cardName).size());
		}
		Collections.sort(theCounts);
		int theCountGainsToEndGame = theCounts.get(0) + theCounts.get(1) + theCounts.get(2);
		if (get(DomCardName.Colony)!=null && count(DomCardName.Colony)<theCountGainsToEndGame)
			theCountGainsToEndGame=count(DomCardName.Colony);
		if (count(DomCardName.Province)<theCountGainsToEndGame)
			theCountGainsToEndGame=count(DomCardName.Province);
		gainsNeededToEndGame=theCountGainsToEndGame;
		return gainsNeededToEndGame;
	}

	public static ArrayList<DomCardName> getRandomBoard() {
		ArrayList<DomCardName> theCardsToChooseFrom = new ArrayList<DomCardName>();
		for (DomSet set : DomSet.values()){
			if (set!=DomSet.Common && !isExcluded(set)){
				for (DomCardName cardName : set.getCards()){
					if (!isExcluded(cardName)){
						theCardsToChooseFrom.add(cardName);
					}
				}
			}
		}
		Collections.shuffle(theCardsToChooseFrom);
		ArrayList<DomCardName> theChosenCards = new ArrayList<DomCardName>();
		for (int i=0;i<11;i++){
			theChosenCards.add(theCardsToChooseFrom.get(i));
		}
		return theChosenCards;
	}

	private static boolean isExcluded(DomCardName cardName) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean isExcluded(DomSet set) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String toString() {
		StringBuilder theString= new StringBuilder();
		for (DomCardName cardName : keySet()){
			theString.append(cardName + "[" +get(cardName).size()+"]");
		}
		return theString.toString();
	}
}