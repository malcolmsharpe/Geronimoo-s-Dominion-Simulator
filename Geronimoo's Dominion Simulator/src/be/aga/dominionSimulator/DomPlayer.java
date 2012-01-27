package be.aga.dominionSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import be.aga.dominionSimulator.cards.FarmlandCard;
import be.aga.dominionSimulator.cards.HagglerCard;
import be.aga.dominionSimulator.cards.HerbalistCard;
import be.aga.dominionSimulator.cards.Noble_BrigandCard;
import be.aga.dominionSimulator.cards.SchemeCard;
import be.aga.dominionSimulator.cards.Secret_ChamberCard;
import be.aga.dominionSimulator.cards.TraderCard;
import be.aga.dominionSimulator.cards.WatchtowerCard;
import be.aga.dominionSimulator.enums.DomBotComparator;
import be.aga.dominionSimulator.enums.DomBotFunction;
import be.aga.dominionSimulator.enums.DomBotOperator;
import be.aga.dominionSimulator.enums.DomBotType;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;
import be.aga.dominionSimulator.enums.DomPhase;
import be.aga.dominionSimulator.enums.DomPlayStrategy;

public class DomPlayer implements Comparable< DomPlayer >{
   protected static final Logger LOGGER = Logger.getLogger( DomPlayer.class );
   static {
       LOGGER.setLevel( DomEngine.LEVEL );
       LOGGER.removeAllAppenders();
       if (DomEngine.addAppender)
           LOGGER.addAppender(new ConsoleAppender(new SimpleLayout()) );
   }

   private ArrayList<DomBuyRule> buyRules=new ArrayList<DomBuyRule>();
   private ArrayList<DomBuyRule> prizeBuyRules=new ArrayList<DomBuyRule>();
   private EnumMap< DomCardName, DomPlayStrategy >  playStrategies = new EnumMap< DomCardName, DomPlayStrategy >(DomCardName.class);

   private DomDeck deck = new DomDeck(this);
   private ArrayList< DomCard > cardsInPlay = new ArrayList< DomCard >();
   private ArrayList< DomCard > cardsInHand = new ArrayList< DomCard >();
   private ArrayList< DomCard > nativeVillageMat = new ArrayList< DomCard >();
   private ArrayList<DomCard> horseTradersPile=new ArrayList<DomCard>();
   
   protected String name;
   public int actionsLeft;
   protected int buysLeft;
   public int availableCoins = 0;
   public int availablePotions = 0;
   protected DomGame game;
   private int wins=0;
   private int ties=0;
   private ArrayList< Integer > moneyCurve = new ArrayList< Integer >();
   private ArrayList< Integer > VPcurve= new ArrayList< Integer >();
   private int turns=0;
   private int victoryTokens=0;
   private int pointsBeforeBuys =0;
   private int sumTurns=0;
   protected int pirateShipLevel = 0;
   public long actionTime=0;
   public long countVPTime=0;
   public long buyTime=0;
   private int hoardCount=0;
   private int talismanCount=0;
   private int forcedStart=0;
   private int coppersmithCount=0;
   private ArrayList< DomCard > boughtCards=new ArrayList< DomCard >();
   private int actionsplayed;
   private ArrayList<DomCardName> forbiddenCardsToBuy=new ArrayList<DomCardName>();
   private boolean extraOutpostTurn;
   private ArrayList<DomPlayer> possessionTurns=new ArrayList<DomPlayer>();
   DomPlayer possessor;
   private ArrayList<DomCardName> cardsGainedLastTurn=new ArrayList<DomCardName>();
   private int sameCardCount=0;
   private DomCardName previousPlayedCardName=null;
   public boolean pprUsed=false;
   private HashSet<DomBotType> types=new HashSet<DomBotType>();
   private String description="No description available";
   private String author="Anonymous";
   private DomPhase currentPhase=null;
   private ArrayList<DomCard> cardsToStayInPlay=new ArrayList<DomCard>();
   private int knownTopCards=0;
   private StartState myStartState=null;
   private ArrayList<DomCardName> mySuggestedBoardCards= new ArrayList<DomCardName>();
   private DomCardName myBaneCard;

public DomPlayer ( String aString ) {
  name = aString;
  types.add(DomBotType.Bot);
}

public DomPlayer(String aName, String anAuthor, String aDescription) {
	this(aName);
	if (anAuthor!=null)
		author=anAuthor;
	if (aDescription!=null)
		description=aDescription;
}

public void makeBuyDecision() {
    for (DomBuyRule theBuyRule : getBuyRules()) {
		if (theBuyRule.getCardToBuy().hasCardType(DomCardType.Prize))
	      continue;
	    if ( getTotalAvailableCurrency().compareTo( theBuyRule.getCardToBuy().getCost(getCurrentGame()) )<0) 
	      continue;
	    if (checkBuyConditions(theBuyRule) && tryToBuy(theBuyRule.getCardToBuy()))
	      return;
    }
    if (DomEngine.haveToLog) DomEngine.addToLog( name + " buys NOTHING!" );
    //a bit dirty setting buysLeft to 0 to make him stop trying to buy stuff and say 'buys nothing'
    //TODO maybe clean this up
    buysLeft=0;
}

private boolean checkBuyConditions(DomBuyRule theBuyRule) {
  for (DomBuyCondition theCondition : theBuyRule.getBuyConditions()) {
    if (!theCondition.isTrue(possessor!=null? possessor : this)) 
      return false;
  }
  return true;
}

public int getTotalMoneyExcludingNativeVillage() {
  return deck.getTotalMoney()-getMoneyFromVillageMat();
}

/**
 * @return
 */
private int getMoneyFromVillageMat() {
    int theTotal = 0;
    for (DomCard theCard : nativeVillageMat) {
      theTotal += theCard.getPotentialCoinValue();
    }
    return theTotal;
}

/**
 * @param aI
 * @return
 */
public int getMoneyInHand( ) {
    int theTotal = 0;
    for (int i=0;i<cardsInHand.size();i++) {
      theTotal += cardsInHand.get(i).getPotentialCoinValue();
    }
    return theTotal;
}

    /**
     * 
     */
    public void shuffleDeck() {
      deck.shuffle();
    }

    /**
     * @param aI
     */
    public void drawCards( int aI ) {
      ArrayList< DomCard > theDrawnCards =deck.getTopCards(aI); 
      cardsInHand.addAll( theDrawnCards );
      if (DomEngine.haveToLog) DomEngine.addToLog(this + " draws " + theDrawnCards.size() + " cards");
      showHand();
    }

    /**
     * 
     */
    public void showHand() {
        if (DomEngine.haveToLog) DomEngine.addToLog( name + "'s cards in Hand: " + cardsInHand );
    }

    /**
     * @return
     */
    public int countInDeck( DomCardName aCardName ) {
      return deck.count(aCardName);
    }

    /**
     * @return
     */
    public int count( DomCardType aCardType) {
      return deck.count( aCardType );
    }

    /**
     * @return
     */
    public ArrayList< DomCard > getCardsFromHand(DomCardName theCardName) {
        ArrayList< DomCard > theCards = new ArrayList< DomCard >();
        for (DomCard theCard : cardsInHand) {
            if (theCard.getName().equals(theCardName))
                theCards.add( theCard );
        }
        return theCards;
    }

    /**
     * @return
     */
    public ArrayList< DomCard > getCardsFromPlay(DomCardName theCardName) {
        ArrayList< DomCard > theCards = new ArrayList< DomCard >();
        for (DomCard theCard : cardsInPlay) {
            if (theCard.getName().equals(theCardName))
                theCards.add( theCard );
        }
        return theCards;
    }

    public void takeTurn() {
        doActionPhase();
        doBuyPhase();
        doCleanUpPhase();
    }

	private void doCleanUpPhase() {
		setPhase(DomPhase.CleanUp);
		cardsToStayInPlay.clear();
        handleHerbalists();
        handleSchemes();
        discardAll();
   	    discard(deck.getPutAsideCards());
        drawHandForNextTurn();
        resetVariables();
        setPhase(null);
	}

	private void showBeginningOfTurnLog() {
	  DomEngine.addToLog(possessor!=null?"</i><i>" : "</i>");
      DomEngine.addToLog("<FONT style=\"COLOR: blue\"> *** " + this + "'s turn " 
    		  + getTurns() 
    		  +(possessor!=null?" (Possessed by "+possessor+")" : "")
    		  +(extraOutpostTurn?" (from Outpost)":"") + " ***"
              +"</FONT>");
      showHand();
	}

    private void resetVariables() {
        actionsLeft = 1;
        buysLeft = 1;
        availableCoins = 0;
        availablePotions = 0;
        getCurrentGame().setPrincessCount(0);
        getCurrentGame().setBridgeCount(0);
        getCurrentGame().setQuarryCount(0);
        hoardCount=0;
        coppersmithCount=0;
        talismanCount=0;
        actionsplayed=0;
        pointsBeforeBuys = countVictoryPoints();
	}

	private void resolveHorseTraders() {
    	if (horseTradersPile.isEmpty())
    		return;
        if (DomEngine.haveToLog) 
          DomEngine.addToLog( this + " adds " + horseTradersPile.size() + " " + DomCardName.Horse_Traders.toHTML() + " to his hand");
		cardsInHand.addAll(horseTradersPile);
    	drawCards(horseTradersPile.size());
		horseTradersPile.clear();
	}

	private void drawHandForNextTurn() {
        for (DomCard theCard : cardsInPlay) {
           if (theCard.getName()==DomCardName.Outpost) {
        	 drawCards(3);
        	 return;
           }
        }
        drawCards( 5 );
	}

	/**
     * 
     */
    private void resolveDurationEffects() {
//        LOGGER.info(durationPile);
       for (DomCard aCard:cardsInPlay) {
         if (DomEngine.haveToLog) DomEngine.addToLog( this + " resolves duration effect from "+ aCard );
         aCard.resolveDuration();
         aCard.setDiscardAtCleanup(true);
       }
    }

    public void discardHand() {
      if (DomEngine.haveToLog) DomEngine.addToLog( this + " discards all cards in hand");
      deck.addToDiscardPile( cardsInHand );
      cardsInHand.clear();
    }

    protected void discardAll() {
        deck.addToDiscardPile( cardsInHand );
        cardsInHand.clear();
        for (DomCard theCard : cardsInPlay) {
          theCard.handleCleanUpPhase();
        }
        cardsInPlay.clear();
        cardsInPlay.addAll(cardsToStayInPlay);
    }

	private void handleSchemes() {
		//TODO maybe rethink this 
        Collections.sort(cardsInPlay, DomCard.SORT_FOR_DISCARDING);
        for (DomCard theCard : cardsInPlay) {
          if (theCard.getName()==DomCardName.Scheme) {
            ((SchemeCard)theCard).maybeAddTagFor(cardsInPlay);
          }
        }
	}

	private void handleHerbalists() {
		//kind of looks dirty to handle Herbalist but 
        //concurrent array modifications are annoying
        Collections.sort(cardsInPlay, DomCard.SORT_FOR_DISCARDING);
        for (DomCard theCard : cardsInPlay) {
          if (theCard.getName()==DomCardName.Herbalist) {
            ((HerbalistCard)theCard).maybeAddTagFor(cardsInPlay);
          }
        }
	}

    private void initializeTurn() {
      if (possessor==null && !extraOutpostTurn){
        turns++;
        sumTurns++;
      }
      if (DomEngine.haveToLog) showBeginningOfTurnLog();
      actionsLeft = 1;
      buysLeft = 1;
      availableCoins = 0;
      availablePotions = 0;
      hoardCount=0;
      getCurrentGame().setPrincessCount(0);
      getCurrentGame().setBridgeCount(0);
      getCurrentGame().setQuarryCount(0);
      coppersmithCount=0;
      talismanCount=0;
      actionsplayed=0;
      forbiddenCardsToBuy.clear();
      cardsGainedLastTurn.clear();
      boughtCards.clear();
      sameCardCount=0;
      previousPlayedCardName=null;
    }

    private void doBuyPhase() {
      long theTime=System.currentTimeMillis();
      setPhase(DomPhase.Buy);
      playTreasures();
      updateMoneyCurve();
      while (buysLeft>0) {
         makeBuyDecision();
         buysLeft--;
      }
      updateVPCurve(false);
      buyTime+=System.currentTimeMillis()-theTime;
    }

    private void showBuyStatus() {
    	StringBuilder theMessage = new StringBuilder();
        theMessage.append(this + " has $" + availableCoins);
        if (availablePotions>0) {
            theMessage.append(" and ");
            for (int i=0;i<availablePotions;i++)
                theMessage.append("P");
        }
        theMessage.append(" to spend and "+ buysLeft + " buy" + (buysLeft>1 ? "s" : ""));
        DomEngine.addToLog(theMessage.toString());
	}

	/**
	 * @param isCumulative 
     * 
     */
    private void updateVPCurve(boolean isCumulative) {
        if (VPcurve.size()<getTurns()) {
          //initialize curves
          if (isCumulative)
            VPcurve.add(countVictoryPoints());
          else
            VPcurve.add(countVictoryPoints()-pointsBeforeBuys);
        } else {
          if (isCumulative)
        	  VPcurve.set(getTurns()-1,VPcurve.get(getTurns()-1) + countVictoryPoints());
          else
            VPcurve.set(getTurns()-1,VPcurve.get(getTurns()-1) + countVictoryPoints()-pointsBeforeBuys);
        }
    }

    /**
     * 
     */
    private void updateMoneyCurve() {
        if (moneyCurve.size()<getTurns()) {
          //initialize curves
          moneyCurve.add(availableCoins);
        } else {
          moneyCurve.set(getTurns()-1,moneyCurve.get(getTurns()-1) + availableCoins);
        }
    }

    /**
     * @return
     */
    protected double getTotalTreasure() {
      return deck.getTotalTreasure();
    }

    /**
     * @return
     */
    public int countAllCards() {
      return deck.countAllCards();
    }
    
    /**
     * @param aCardName 
     * @return 
     */
    protected boolean tryToBuy( DomCardName aCardName ) {
        if (game.countInSupply( aCardName )==0) {
//          if (DomEngine.haveToLog) DomEngine.addToLog( aCardName + " is no more available to buy");
          return false;
        }
        if (suicideIfBuys(aCardName)){
          if (DomEngine.haveToLog) DomEngine.addToLog( 
        		  "<FONT style=\"BACKGROUND-COLOR: red\">SUICIDE!</FONT> Can not buy " + aCardName.toHTML());
          return false;
        }
        if (forbiddenCardsToBuy.contains(aCardName))
        	return false;

        if (aCardName==DomCardName.Grand_Market && !getCardsFromPlay(DomCardName.Copper).isEmpty())
        	return false;
        
        buy(game.takeFromSupply( aCardName ));
        return true;
    }
    
    /**
     * @param theCard
     */
    public void handleSpecialBuyEffects( DomCard theCard ) {
    	for (DomCard theHaggler : getCardsFromPlay(DomCardName.Haggler)){
    		((HagglerCard)theHaggler).haggleFor(theCard);
    	}
    	int theGoonsCount =getCardsFromPlay(DomCardName.Goons).size();
        if (theGoonsCount>0) {
          addVP(theGoonsCount);
        }
        for (int i=0;i<talismanCount;i++) {
          if (new DomCost(4, 0).compareTo(theCard.getCost(getCurrentGame()))>=0 
          && !theCard.hasCardType( DomCardType.Victory )) {
            DomCard theDouble = getCurrentGame().takeFromSupply( theCard.getName());
            if (theDouble!=null) {
              gain(theDouble);
            }
          }
        }
        for (int i=0;i<hoardCount;i++) {
          if (theCard.hasCardType( DomCardType.Victory )) {
            DomCard theGold = getCurrentGame().takeFromSupply( DomCardName.Gold);
            if (theGold!=null) {
              gain(theGold);
            }
          }
        }
        for (int i=0;i<game.getEmbargoTokensOn(theCard.name);i++) {
          DomCard theCurse= getCurrentGame().takeFromSupply( DomCardName.Curse);
          if (theCurse!=null) {
            gain(theCurse);
          }
        }
        if (theCard.getName()==DomCardName.Mint) {
        	for (int i=0;i<getCardsInPlay().size();){
        		DomCard theCardToTrash = getCardsInPlay().get(i);
        		if (theCardToTrash.hasCardType(DomCardType.Treasure)) {
        			trash(removeCardFromPlay(theCardToTrash));
        		} else {
        			i++;
        		}
        	}
        }
        if (theCard.getName()==DomCardName.Farmland){
          ((FarmlandCard)theCard).remodelSomething();	
        }
        if (theCard.getName()==DomCardName.Noble_Brigand){
          ((Noble_BrigandCard)theCard).attack(this);	
        }
    }

    public void buy( DomCard aCard ) {
        if (DomEngine.haveToLog) DomEngine.addToLog( this + " buys a " + aCard  );
        deck.gain( aCard );
        boughtCards.add(aCard);
        availableCoins-=aCard.getCoinCost(getCurrentGame());
        availablePotions-=aCard.getPotionCost();
        handleSpecialBuyEffects( aCard );
    }

    public void playTreasures() {
        DomCard theCardToPlay;
        do {
            theCardToPlay = null;
            for (DomCard theCard : cardsInHand) {
                if (theCard.hasCardType(DomCardType.Treasure)) {
                     if (theCardToPlay == null || theCard.getPlayPriority()<theCardToPlay.getPlayPriority()) {
                    	if (theCard.wantsToBePlayed())
                          theCardToPlay = theCard;   
                     }
                }
            }
            if (theCardToPlay!=null) {
              play(removeCardFromHand( theCardToPlay ));
            }
        } while (theCardToPlay!=null);

        if (DomEngine.haveToLog) {
          if (previousPlayedCardName!=null) {
            DomEngine.addToLog( name + " plays " + (sameCardCount+1)+" "+ previousPlayedCardName.toHTML()
            		           + (sameCardCount>0 ? "s" : ""));
            previousPlayedCardName=null;
            sameCardCount=0;
          }
          showBuyStatus();
        }
    }

    /**
     * @param theCardToPlay
     * @return
     */
    public DomCard removeCardFromHand( DomCard theCardToPlay ) {
      return cardsInHand.remove(cardsInHand.indexOf( theCardToPlay ));
    }

    protected void doActionPhase() {
      setPhase(DomPhase.Action);
	  initializeTurn();
	  resolveHorseTraders();
	  resolveDurationEffects();
      long theTime=System.currentTimeMillis();
      DomCard theCardToPlay = null;
      do {
        theCardToPlay = getNextActionToPlay();
        if (theCardToPlay!=null) {
          actionsLeft--;
          play(removeCardFromHand( theCardToPlay ));
        }
      } while (actionsLeft>0 && theCardToPlay!=null);
      actionTime+=System.currentTimeMillis()-theTime;
    }

    private void setPhase(DomPhase aPhase) {
       currentPhase = aPhase;	
	}

    public DomCard getNextActionToPlay( ) {
    	ArrayList<DomCard> theActionsToConsider = getCardsFromHand(DomCardType.Action);
    	if (theActionsToConsider.isEmpty())
    		return null;
    	Collections.sort(theActionsToConsider,DomCard.SORT_FOR_PLAYING);
    	for (DomCard card : theActionsToConsider){
    		if (card.wantsToBePlayed())
    			return card;
    	}
        return null;
    }

    /**
     * @param aCard
     */
    public void play( DomCard aCard ) {
      if (aCard.hasCardType( DomCardType.Action))
        increaseActionsPlayed();
      cardsInPlay.add(aCard);
      if (DomEngine.haveToLog){ 
        playAndLog(aCard);
      } else {
        playThis(aCard);
      }
    }

	private void playAndLog(DomCard aCard) {
		if (previousPlayedCardName!=aCard.getName()){
            if (previousPlayedCardName!=null){
              DomEngine.addToLog( name + " plays " + (sameCardCount+1)+" "+ previousPlayedCardName.toHTML() 
                                  + (sameCardCount>0 ? "s" : ""));
            }
            if (!aCard.hasCardType( DomCardType.Kingdom ) && !aCard.hasCardType(DomCardType.Prize)){
                previousPlayedCardName=aCard.getName();
            } else {
            	previousPlayedCardName=null;
            }
            sameCardCount=0;
        } else {
        	sameCardCount++;
        }
        if (aCard.hasCardType( DomCardType.Kingdom ) || aCard.hasCardType(DomCardType.Prize)){
          DomEngine.addToLog( name + " plays "+ aCard);
        }
        DomEngine.logIndentation++;
        playThis(aCard);
        DomEngine.logIndentation--;
	}

    public void playThis(DomCard aCard) {
		if (aCard.hasCardType(DomCardType.Duration))
		  aCard.setDiscardAtCleanup(false);
		aCard.play();
	}

	public void discard( ArrayList< DomCard > aCards ) {
      for (DomCard theCard : aCards)
        discard( theCard );
    }

    /**
     * @return
     */
    public int countTreasureFromNativeVillageMat() {
        int theTotal = 0;
        for (DomCard theCard : nativeVillageMat) {
            if (theCard.hasCardType( DomCardType.Treasure )) {
                theTotal += theCard.getPotentialCoinValue();
            }
        }
        return theTotal;
    }


    public DomPlayer getPossessor() {
		return possessor;
	}

	/**
     * @param aTreasure
     * @return
     */
    public ArrayList< DomCard > revealUntilType( DomCardType aType ) {
      return deck.revealUntilType( aType );
    }
    
    /**
     * @param aTreasure
     * @return
     */
    public ArrayList< DomCard > revealUntilCost(int aCost) {
      return deck.revealUntilCost(aCost );
    }

    /**
     * @param aDomGame
     */
    public void setCurrentGame( DomGame aDomGame ) {
      game = aDomGame;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return name;
    }

    /**
     * @param aDomGame 
     * 
     */
    public void initializeForGame(DomGame aDomGame) {
        pprUsed=false;
        setCurrentGame( aDomGame );
        turns=0;
        pirateShipLevel=0;
        victoryTokens=0;
        pointsBeforeBuys = 3;
        cardsInPlay = new ArrayList< DomCard >();
        cardsInHand = new ArrayList< DomCard >();
        horseTradersPile.clear();
        nativeVillageMat = new ArrayList< DomCard >();
        deck = new DomDeck(this);
        possessor=null;
        extraOutpostTurn=false;
        possessionTurns.clear();
        setKnownTopCards(0);
        if (myStartState!=null) {
        	putPlayerInStartState();
        	if (myStartState.getHand().isEmpty())
        		drawCards(5);
        } else{
           dealStartCards();
           doTrueRandomShuffle();
           while (!checkForcedStart())
             doTrueRandomShuffle();
           drawCards( 5  );
        }
        addSuggestedBoardCards();
    }

    private void addSuggestedBoardCards() {
		for (DomCardName cardName : mySuggestedBoardCards){
			if (getCurrentGame().countInSupply(cardName)==0)
				getCurrentGame().getBoard().addCardPile(cardName);
		}
		if (myBaneCard!=null){
		  if (getCurrentGame().countInSupply(myBaneCard)==0)
			getCurrentGame().getBoard().addCardPile(myBaneCard);
		  getCurrentGame().getBoard().markAsBane(myBaneCard);
		}
	}

	private void putPlayerInStartState() {
		for (DomCardName cardName : myStartState.getDrawDeck()) {
			if (getCurrentGame().countInSupply(cardName)==0)
				getCurrentGame().getBoard().addCardPile(cardName);
			gainOnTopOfDeck(getCurrentGame().takeFromSupply(cardName));
		}
		for (DomCardName cardName : myStartState.getDiscard()) {
			if (getCurrentGame().countInSupply(cardName)==0)
				getCurrentGame().getBoard().addCardPile(cardName);
			gain(getCurrentGame().takeFromSupply(cardName));
		}
		if (myStartState.isShuffleDrawDeck())
			shuffleDeck();
		for (DomCardName cardName : myStartState.getHand()) {
			if (getCurrentGame().countInSupply(cardName)==0)
				getCurrentGame().getBoard().addCardPile(cardName);
			DomCard theCard = getCurrentGame().takeFromSupply(cardName);
			cardsInHand.add(theCard);
			getDeck().addPhysicalCard(theCard);
		}
	}

	/**
     * 
     */
    public void addWin() {
      wins++;
    }

    /**
     * @return
     */
    public int getWins() {
      return wins;
    }

    /**
     * @return
     */
    public int countVictoryPoints() {
        long theTime=System.currentTimeMillis();
        int theTotalVP =victoryTokens + deck.countVictoryPoints();
        countVPTime+=System.currentTimeMillis()-theTime;
      return theTotalVP; 
    }

    /**
     * @param aI 
     * @return
     */
    public Integer getMoneyCurve(int aI) {
        if (aI<moneyCurve.size())
          return moneyCurve.get(aI);
        else 
          return moneyCurve.get(moneyCurve.size()-1);
   }

    /**
     * @return
     */
    public ArrayList< Integer > getMoneyCurve() {
        return moneyCurve;
    }

    /**
     * @param aI
     */
    public void setTurns( int aI ) {
       turns = aI;
    }

    /**
     * @return
     */
    public int getTurns() {
      return turns;
    }

    /**
     * 
     */
    public void addTie() {
      ties++;
    }

    /**
     * @return
     */
    public int getTies() {
      return ties;
    }

    /**
     * @param discardsLeft 
     * @param discardToTopOfDeck 
     */
    public void doForcedDiscard(int discardsLeft, boolean discardToTopOfDeck) {
    	if (!discardToTopOfDeck){
    		//Tunnel needs absolute priority to get discarded
    		while (discardsLeft>0 && !getCardsFromHand(DomCardName.Tunnel).isEmpty()){
    			discardFromHand(getCardsFromHand(DomCardName.Tunnel).get(0));
    			discardsLeft--;
    		}
    	}
        //first discard excess terminals
        ArrayList< DomCard > theTerminalsInHand = getCardsFromHand( DomCardType.Terminal );
        Collections.sort(theTerminalsInHand, DomCard.SORT_FOR_DISCARD_FROM_HAND);
        int i=theTerminalsInHand.size()-actionsLeft;
        while (i>0 && discardsLeft>0) {
          discard(theTerminalsInHand.remove(0), discardToTopOfDeck);
          discardsLeft--;
          i--;
        }
        Collections.sort(getCardsInHand(), DomCard.SORT_FOR_DISCARD_FROM_HAND);
        if (discardsLeft<1)
        	return;
        checkForPossibleTrashingBeforeDiscarding(discardsLeft);
        //then discard the rest
        while (!cardsInHand.isEmpty() && discardsLeft>0 ) {
            discard(cardsInHand.get(0), discardToTopOfDeck);
		    discardsLeft--;
        }
    }

    private void discard(DomCard aCard, boolean discardToTopOfDeck) {
        if (discardToTopOfDeck)
          discardFromHandToTopOfDeck(aCard);
         else
          discardFromHand(aCard);
	}

	private void checkForPossibleTrashingBeforeDiscarding(int discardsLeft) {
    	if (cardsInHand.size()<=discardsLeft+1 
    	 || getCardsFromHand(DomCardType.Trasher).isEmpty())
    	  return;

    	int theNumberOfCardsNeededForTrashing = 2;

		ArrayList<DomCard> theCardsNeededForTrashing = new ArrayList<DomCard>();
		for (DomCard card : cardsInHand) {
		  if (card.getTrashPriority()<16)
		     theCardsNeededForTrashing.add(card);
		}
		
		if (theCardsNeededForTrashing.isEmpty())
		  return;
		
//    	if (!getCardsFromHand(DomCardName.Bishop).isEmpty()
//    	 || !getCardsFromHand(DomCardName.Ambassador).isEmpty()	
//    	 || !getCardsFromHand(DomCardName.Develop).isEmpty()	
//    	 || !getCardsFromHand(DomCardName.Expand).isEmpty()	
//    	 || !getCardsFromHand(DomCardName.Masquerade).isEmpty()	
//    	 || !getCardsFromHand(DomCardName.Remodel).isEmpty()	
//    	 || !getCardsFromHand(DomCardName.Salvager).isEmpty()	
//    	 || !getCardsFromHand(DomCardName.Trader).isEmpty()	
//    	 || !getCardsFromHand(DomCardName.Trade_Route).isEmpty()	
//    	 || !getCardsFromHand(DomCardName.Transmute).isEmpty()){
//    	}

    	if (!getCardsFromHand(DomCardName.Steward).isEmpty()
    	 || !getCardsFromHand(DomCardName.Trading_Post).isEmpty()	
    	 || !getCardsFromHand(DomCardName.Remake).isEmpty()	){
    		if (theCardsNeededForTrashing.size()<2)
    			return;
    		theNumberOfCardsNeededForTrashing = 3;
    	}
    	if (!getCardsFromHand(DomCardName.Chapel).isEmpty()
    	 || !getCardsFromHand(DomCardName.Forge).isEmpty()){
    		if (theCardsNeededForTrashing.size()>4)
    		  theNumberOfCardsNeededForTrashing=5;
    		else
    		  theNumberOfCardsNeededForTrashing=theCardsNeededForTrashing.size()+1;
    		if (theNumberOfCardsNeededForTrashing>cardsInHand.size()-discardsLeft)
    			theNumberOfCardsNeededForTrashing = cardsInHand.size()-discardsLeft;
    	}
    	
    	if (cardsInHand.size()-discardsLeft<theNumberOfCardsNeededForTrashing)
    		return;

    	if (theNumberOfCardsNeededForTrashing<=1)
      	  return;

    	DomCost theTotalMoney = getTotalAvailableCurrency();
    	for (int i=discardsLeft;i<cardsInHand.size();i++){
    	  theTotalMoney.add(cardsInHand.get(i).getPotentialCurrencyValue());
    	}
    	if (isGoingToBuyTopCardInBuyRules(theTotalMoney))
    	  return;
    	
		Collections.sort(theCardsNeededForTrashing,DomCard.SORT_FOR_TRASHING);
    	//now we move the cards to trash and trasher to the back of the hand
    	for (int j=0;j<theNumberOfCardsNeededForTrashing-1;j++){
    	  cardsInHand.add(removeCardFromHand(theCardsNeededForTrashing.get(j)));
    	}
    	cardsInHand.add(removeCardFromHand(getCardsFromHand(DomCardType.Trasher).get(0)));
	}

	public boolean isGoingToBuyTopCardInBuyRules(DomCost theTotalMoney) {
		if (getBuyRules().isEmpty())
          //something is wrong, so we don't bother
    	  return false;
    	DomCardName theTopCardInBuyRules = getBuyRules().get(0).getCardToBuy();
    	if (wantsToGainOrKeep(theTopCardInBuyRules) && getDesiredCard(theTotalMoney, false)==theTopCardInBuyRules)
    	  //we don't want to mess with a hand if we're going to buy the top card this turn (although we could)
    	  return true;
    	return false;
	}

	public ArrayList< DomCard > getCardsFromHand(DomCardType aCardType) {
        ArrayList< DomCard > theCards = new ArrayList< DomCard >();
        for (DomCard theCard : cardsInHand) {
            if (theCard.hasCardType(aCardType)) {
              theCards.add( theCard );
            }
        }
        return theCards;
    }

    public ArrayList< DomCard > getCardsFromPlay(DomCardType aCardType) {
        ArrayList< DomCard > theCards = new ArrayList< DomCard >();
        for (DomCard theCard : cardsInPlay) {
            if (theCard.hasCardType(aCardType)) {
              theCards.add( theCard );
            }
        }
        return theCards;
    }

    /**
     * @param aCardToDiscard
     */
    public void discardFromHand( DomCard aCardToDiscard ) {
      if (DomEngine.haveToLog) DomEngine.addToLog( this + " discards a " + aCardToDiscard);
      deck.discard( removeCardFromHand( aCardToDiscard ));
    }

    public void discardFromHandToTopOfDeck( DomCard aCardToDiscard ) {
      putOnTopOfDeck( removeCardFromHand( aCardToDiscard ));
    }
    
    /**
     * @param aCurse
     * @return
     */
    public boolean discardFromHand( DomCardName aCardName ) {
        for (DomCard theCard : cardsInHand) {
            if (theCard.getName().equals( aCardName )) {
                discardFromHand(theCard);
                return true;
            }
        }
        return false;
    }

    /**
     * @param aCardName 
     * @return 
     */
    public void gain( DomCardName aCardName ) {
      gain(getCurrentGame().takeFromSupply( aCardName ));
    }


    /**
     */
    public void gain( DomCard aCard ) {
      if (aCard!=null) {
        if (DomEngine.haveToLog) DomEngine.addToLog( this + " gains a " + aCard  );
          deck.gain( aCard );
      }
    }

    /**
     * @param aPlatinum
     */
    public void gainInHand( DomCardName aCardName ) {
       if (getCurrentGame().countInSupply(aCardName)>0)
         gainInHand(game.takeFromSupply( aCardName ));
    }
    
    public void gainInHand( DomCard aCard) {
      deck.gain(aCard, DomDeck.HAND);
    }
    
    /**
     * @param aRemove
     */
    public void trash( DomCard aRemove ) {
      if (aRemove!=null) {
    	if (aRemove.owner==null || possessor==null) {
          if (DomEngine.haveToLog) DomEngine.addToLog( this + " trashes a " + aRemove);
          game.addToTrash(aRemove);
    	}
        if (aRemove.owner!=null) {
          deck.trash(aRemove);
        }
      }
    }

    /**
     * 
     */
    public void discardTopCardFromDeck() {
      deck.discardTopCardFromDeck();
    }

    /**
     * @param aCard
     */
    public void gainOnTopOfDeck( DomCard aCard ) {
      deck.gain(aCard, DomDeck.TOP_OF_DECK);
    }

    /**
     * @return
     */
    public int getHandSize() {
      return cardsInHand.size();
    }

    /**
     * @param aI
     * @return
     */
    public double getVPCurve( int aI ) {
        if (aI<VPcurve.size())
          return VPcurve.get(aI);
        else 
          return VPcurve.get(VPcurve.size()-1);
    }

    /**
     * @param aCardName 
     * @return
     */
    public boolean suicideIfBuys(DomCardName aCardName) {
    	if (possessor!=null){
    	  return possessor.suicideIfBuys(aCardName);
    	}

    	if (getTypes().contains(DomBotType.AppliesPPR)
    	&& (aCardName==DomCardName.Province || aCardName==DomCardName.Colony) 
        && game.countInSupply( aCardName )==2  
        && checkPenultimateProvinceRule(aCardName))
          return true;
        
        if (game.countInSupply( aCardName )!=1)
          return false;
        
        if (game.countEmptyPiles() < 2 ) {
            if (aCardName!=DomCardName.Province && aCardName!=DomCardName.Colony) { 
              return false;
            }
        }
        for (DomPlayer thePlayer : getOpponents()) {
            if (countVictoryPoints()<thePlayer.countVictoryPoints()-aCardName.getVictoryValue(this)){
              return true;
            }
            if (countVictoryPoints()==thePlayer.countVictoryPoints()-aCardName.getVictoryValue(this)){
              if (getTurns()>thePlayer.getTurns()){
                 return true;
              }
            }
        }
        return false;
    }

    /**
     * @param aCardName 
     * @return
     */
    private boolean checkPenultimateProvinceRule(DomCardName aCardName) {
    	for (DomPlayer thePlayer : getOpponents()) {
    		for (DomBuyRule rule : thePlayer.getBuyRules()){
    			DomCardName theCard=rule.getCardToBuy();
    			if (theCard==aCardName)
    				continue;
    			if (getCurrentGame().countInSupply(theCard)>0
    			  && countVictoryPoints()<thePlayer.countVictoryPoints()
    			  && countVictoryPoints()+theCard.getVictoryValue(this)>thePlayer.countVictoryPoints()) {
    	            if (DomEngine.haveToLog) DomEngine.addToLog( "Penultimate Province Rule!!!" );
    				return true;
    			}
    		}
          }
        return false;
    }

    public double getSumTurns() {
        return sumTurns;
    }

    public void addVP( int aI ) {
        victoryTokens+=aI;
        if (DomEngine.haveToLog) DomEngine.addToLog( this + " gains +" +  aI +"&#x25BC;");
    }

    public void handleGameEnd() {
       if (!deck.getIslandMat().isEmpty()) 
         deck.returnCardsFromIslandMat();
    }

    public boolean checkDefense() {
      while (!getCardsFromHand(DomCardName.Horse_Traders).isEmpty()) {
        if (DomEngine.haveToLog) DomEngine.addToLog( this + " sets a " + DomCardName.Horse_Traders.toHTML() + " aside" );
       	horseTradersPile.add(removeCardFromHand(getCardsFromHand(DomCardName.Horse_Traders).get(0)));
      }
      if (!getCardsFromHand(DomCardName.Secret_Chamber).isEmpty()){
      	((Secret_ChamberCard) getCardsFromHand(DomCardName.Secret_Chamber).get(0)).react();
      }
      for (DomCard theCard: cardsInPlay) {
        if (theCard.getName()==DomCardName.Lighthouse ){
          if (DomEngine.haveToLog) DomEngine.addToLog( theCard + " prevents the attack!" );
          return true;
        }
      }
      if (!getCardsFromHand(DomCardName.Moat).isEmpty()) {
          if (DomEngine.haveToLog) DomEngine.addToLog( this + " reveals a " + DomCardName.Moat.toHTML() + " from hand and prevents the attack");
          return true;
      }
      return false;
    }

    /**
     * @param aI
     * @return
     */
    public ArrayList< DomCard > revealTopCards( int aI ) {
      return deck.revealTopCards( aI );
    }

    public void showDeck() {
      deck.showContents();
    }

    public void discard( DomCard aCard ) {
      if (DomEngine.haveToLog) DomEngine.addToLog( this + " discards " + aCard );
      deck.discard( aCard );
    }

    public DomGame getCurrentGame() {
      return game;
    }

    public ArrayList< DomCard > getCardsInHand() {
      return cardsInHand;
    }

    public ArrayList< DomCard > getCardsInPlay() {
        return cardsInPlay;
    }

    public void removePhysicalCard( DomCard aCard ) {
      deck.removePhysicalCard(aCard);
      aCard.owner=null;
    }

    public void returnToSupply( DomCard aCard) {
      if (DomEngine.haveToLog) DomEngine.addToLog( this + " returns " + aCard + " to the supply" );
      getCurrentGame().returnToSupply( aCard);
    }

    public  ArrayList< DomPlayer > getOpponents() {
        ArrayList< DomPlayer > theLaterOpponents = new ArrayList< DomPlayer >();
        ArrayList< DomPlayer > theFirstOpponents = null;
        for (DomPlayer thePlayer : getCurrentGame().getPlayers()) {
          if (thePlayer==this) { 
            theFirstOpponents = new ArrayList< DomPlayer >();
          } else {
              if (theFirstOpponents!=null) {
                theFirstOpponents.add( thePlayer );
              } else {
                theLaterOpponents.add( thePlayer );        
              }
          }
        }
        if (theFirstOpponents==null){
          theFirstOpponents = new ArrayList< DomPlayer >();
        }
        theFirstOpponents.addAll( theLaterOpponents );
        return theFirstOpponents;
    }


	public boolean usesTrader(DomCard aCard) {
      ArrayList<DomCard> theTradersInHand = getCardsFromHand( DomCardName.Trader );
      if (theTradersInHand.size()>0) {
         if (((TraderCard) theTradersInHand.get( 0 )).wantsToReact(aCard)) {
           return ((TraderCard) theTradersInHand.get( 0 )).react(aCard);
         }
      }
      return false;
	}
	
    public boolean usesWatchtower( DomCard aCard ) {
      ArrayList<DomCard> theWatchTowersInHand = getCardsFromHand( DomCardName.Watchtower );
      if (theWatchTowersInHand.size()>0) {
         if (((WatchtowerCard) theWatchTowersInHand.get( 0 )).wantsToReact(aCard)) {
           return ((WatchtowerCard) theWatchTowersInHand.get( 0 )).react(aCard);
         }
      }
      return false;
    }

    public void addAvailableBuys( int aI ) {
      if (DomEngine.haveToLog) DomEngine.addToLog( this + " gets +" +aI + " buys");
      buysLeft+=aI;
    }

    public void addAvailableCoins( int aI ) {
      if (DomEngine.haveToLog && aI>0) DomEngine.addToLog( this + " gets +$" +aI );
      availableCoins+=aI;
    }

    public void addActions( int aI ) {
      if (DomEngine.haveToLog ) DomEngine.addToLog( this + " gets +" + aI + " actions");
      actionsLeft+=aI;
    }

    public ArrayList< DomCard > getNativeVillageMat() {
      return nativeVillageMat;
    }

    public int getTotalMoneyInDeck() {
        return deck.getTotalMoney();
    }

    public int getDeckSize() {
      return deck.getDeckAndDiscardSize();
    }

    public boolean isBigTurnReady() {
      return false;
    }

    public int getPirateShipLevel() {
        return pirateShipLevel;
    }

    /**
     * @param aPirateShipLevel The pirateShipLevel to set.
     */
    public void setPirateShipLevel( int aPirateShipLevel ) {
        pirateShipLevel = aPirateShipLevel;
    }

    /**
     * @param aCardToPass
     * @param theMasqueradePlayer 
     */
    public void passCardToTheLeftForMasquerade( DomCard aCardToPass, DomPlayer theMasqueradePlayer ) {
        ArrayList< DomPlayer > theOpponents = getOpponents();
        DomPlayer theOpponentToTheLeft = theOpponents.get(0);
        if (theOpponentToTheLeft!=theMasqueradePlayer) { 
          theOpponentToTheLeft.passCardToTheLeftForMasquerade(theOpponentToTheLeft.chooseCardToPass(), theMasqueradePlayer);
        }
        if (aCardToPass!=null) {
          if (DomEngine.haveToLog) DomEngine.addToLog( this + " passes a " +aCardToPass + " to " + theOpponentToTheLeft );
          removePhysicalCard( aCardToPass );
          theOpponentToTheLeft.receiveCard(aCardToPass);
        }
    }

    private void receiveCard( DomCard aCardToPass ) {
      deck.addPhysicalCardFromMasquerade(aCardToPass);
      cardsInHand.add( aCardToPass);
    }

    /**
     * @return
     */
    public DomCard chooseCardToPass() {
    	if (getCardsInHand().isEmpty())
    		return null;
        Collections.sort(getCardsInHand(),DomCard.SORT_FOR_TRASHING);
        return removeCardFromHand( getCardsInHand().get(0));
    }

    public int getActionsLeft() {
      return actionsLeft;
    }

    /**
     * @return
     */
    public int getTotalAvailableCoins() {
      return getMoneyInHand()+availableCoins;
    }

    /**
     * @param aRemoveCardFromHand
     */
    public void moveToIslandMat( DomCard aCard ) {
      if (DomEngine.haveToLog) DomEngine.addToLog( this + " adds " + aCard + " to the Island Mat" );
      deck.moveToIslandMat( aCard );
    }

    /**
     * @param aIslandCard
     * @return
     */
    public DomCard removeCardFromPlay( DomCard aCard ) {
      return cardsInPlay.remove(cardsInPlay.indexOf( aCard ));
   }

    /**
     * @param aDomCard
     */
    public void putOnTopOfDeck( DomCard aDomCard ) {
      deck.putOnTopOfDeck(aDomCard);
      if (DomEngine.haveToLog) DomEngine.addToLog( this + " puts "  + aDomCard + " on top of the deck");
   }

	public ArrayList<DomCard> collectAllCards() {
		return deck.collectAllCards();
	}

    /**
     * 
     */
    public void increaseHoardCount() {
      hoardCount++;
    }

    /**
     * 
     */
    public void increaseCoppersmithCount() {
      coppersmithCount++;
      if (DomEngine.haveToLog) DomEngine.addToLog( this + "'s Coppers are now worth $" + (coppersmithCount+1) );
   }

    /**
     * @return
     */
    public int getCoppersmithsPlayed() {
      return coppersmithCount;
    }

    /**
     * @return
     */
    public double getAvailableCoins() {
      return availableCoins;
    }

    /**
     * @return
     */
    public int getforcedStart() {
      return forcedStart;
    }

    public DomDeck getDeck() {
      return deck;
    }

    /**
     * @return Returns the boughtCards.
     */
    public ArrayList< DomCard > getBoughtCards() {
      return boughtCards;
    }
 
    public void addBuyRule(DomBuyRule buyRule) {
      if (buyRule.getCardToBuy().hasCardType(DomCardType.Prize)){
    	prizeBuyRules.add(buyRule);
      } else {
        buyRules.add(buyRule);
      }
    }
      
    public DomPlayer getCopy(String aName) {
      DomPlayer theCopy = new DomPlayer(aName);
      theCopy.buyRules = (ArrayList< DomBuyRule >) buyRules.clone();
      theCopy.prizeBuyRules= (ArrayList< DomBuyRule >) prizeBuyRules.clone();
      theCopy.playStrategies = playStrategies.clone();
      for (DomBotType botType : getTypes()){
    	theCopy.addType(botType);
      }
      theCopy.setStartState(myStartState);
      theCopy.setSuggestedBoard(mySuggestedBoardCards);
      theCopy.setBane(myBaneCard);
      return theCopy;
    }

	private void setBane(DomCardName aBaneCard) {
		myBaneCard=aBaneCard;
	}

	private void setSuggestedBoard(ArrayList<DomCardName> aSuggestedBoardCards) {
		mySuggestedBoardCards=aSuggestedBoardCards;
	}

	public DomPlayer getColonyCopy(String string) {
	   DomPlayer theCopy = getCopy(string);
	   theCopy.removeType(DomBotType.Province);
	   theCopy.buyRules.add(0,new DomBuyRule("Colony", null, null));
	   int theLastGreenIndex =1;
	   for (int i=0;i<theCopy.buyRules.size();i++){
		   DomBuyRule rule = theCopy.buyRules.get(i);
		   if (rule.getCardToBuy().hasCardType(DomCardType.Victory)){
			 theLastGreenIndex = i;
		   }
		   if (rule.getCardToBuy()==DomCardName.Province) {
			   theCopy.buyRules.remove(i);
			   rule=new DomBuyRule("Province", null, null);
			   rule.addCondition(
					   new DomBuyCondition(DomBotFunction.countCardsInSupply
					                      ,DomCardName.Colony
					                      ,DomCardType.Action
					                      ,"0"
					                      ,DomBotComparator.smallerOrEqualThan
					                      ,DomBotFunction.constant
					                      ,DomCardName.Copper
					                      ,DomCardType.Action
					                      ,"6"
					                      ,DomBotOperator.plus
					                      ,"0"));
			   theCopy.buyRules.add(i,rule);
		   }
		   if (rule.getCardToBuy()==DomCardName.Duchy) {
			   theCopy.buyRules.remove(i);
			   rule=new DomBuyRule("Duchy", null, null);
			   rule.addCondition(
					   new DomBuyCondition(DomBotFunction.countCardsInSupply
					                      ,DomCardName.Colony
					                      ,DomCardType.Action
					                      ,"0"
					                      ,DomBotComparator.smallerOrEqualThan
					                      ,DomBotFunction.constant
					                      ,DomCardName.Copper
					                      ,DomCardType.Action
					                      ,"5"
					                      ,DomBotOperator.plus
					                      ,"0"));
			   theCopy.buyRules.add(i,rule);
		   }
		   if (rule.getCardToBuy()==DomCardName.Estate) {
			   theCopy.buyRules.remove(i);
			   rule=new DomBuyRule("Estate", null, null);
			   rule.addCondition(
					   new DomBuyCondition(DomBotFunction.countCardsInSupply
					                      ,DomCardName.Colony
					                      ,DomCardType.Action
					                      ,"0"
					                      ,DomBotComparator.smallerOrEqualThan
					                      ,DomBotFunction.constant
					                      ,DomCardName.Copper
					                      ,DomCardType.Action
					                      ,"2"
					                      ,DomBotOperator.plus
					                      ,"0"));
			   theCopy.buyRules.add(i,rule);
		   }
	   }
	   theCopy.buyRules.add(theLastGreenIndex+1, new DomBuyRule("Platinum", null, null));
       theCopy.addType(DomBotType.Colony);

	   return theCopy;
	}

    private void removeType(DomBotType aType) {
		types.remove(aType);
	}

	public  ArrayList< DomCardName > getCardsNeededInSupply() {
        ArrayList< DomCardName > theCards = new ArrayList< DomCardName >();
        for (DomBuyRule theRule : getBuyRules()) {
          if (theRule.getCardToBuy().hasCardType( DomCardType.Kingdom ) 
           || theRule.getCardToBuy()==DomCardName.Potion
           || theRule.getCardToBuy()==DomCardName.Colony) {
            theCards.add(theRule.getCardToBuy());
          }
          for (DomBuyCondition buyCondition : theRule.getBuyConditions()){
        	  if (buyCondition.getLeftCardName()!=null)
        		  theCards.add(buyCondition.getLeftCardName());
        	  if (buyCondition.getRightCardName()!=null)
        		  theCards.add(buyCondition.getRightCardName());
          }
        }
        return theCards;
      }

      /**
       * @param aResolveAttrib
       * @param aResolveAttrib2
       */
      public void addPlayStrategy( String aCard, String aStrategy) {
        if (aStrategy!=null) {
          playStrategies.put( DomCardName.valueOf( aCard ), DomPlayStrategy.valueOf( aStrategy ) );
        }
      }
      
      /* (non-Javadoc)
       * @see DominionSimulator.DomPlayer#getStrategyFor(DominionSimulator.DomCard)
       */
      public DomPlayStrategy getPlayStrategyFor( DomCard aCard ) {
          DomPlayStrategy theStrategy = playStrategies.get( aCard.name);
          return theStrategy==null ? DomPlayStrategy.standard : theStrategy;
      }

      /**
       * @return 
       */
      public  ArrayList< DomBuyRule > getBuyRules() {
    	  if (possessor!=null)
    		return possessor.getBuyRules();
          return buyRules;
      }

      /**
       * @param aI
       */
      public void forceStart( int aI ) {
         forcedStart = aI;
      }
      
      public DomCardName getDesiredCard(DomCost anAvailableCurrency, boolean costExact) {
    	  return getDesiredCard(null, anAvailableCurrency, costExact, false, null);
      }

      public DomCardName getDesiredCard(DomCardType aDesiredType
    		  						, DomCost anAvailableCurrency
    		  						, boolean costExact
    		  						, boolean noConstraints
    		  						, DomCardType aForbiddenType) {
          for (DomBuyRule theRule : getBuyRules()) {
              if (aDesiredType!=null && !theRule.getCardToBuy().hasCardType(aDesiredType))
              	continue;
              if (aForbiddenType!=null && theRule.getCardToBuy().hasCardType(aForbiddenType))
                 continue;
              boolean wantsToGain=true;
              for (DomBuyCondition theCondition : theRule.getBuyConditions()) {
//            	  if (noConstraints && theCondition.getLeftFunction()==DomBotFunction.isActionPhase){
//            		  //TODO probably remove this...
//            		  //be very careful: this fix was added so Estates can be gained with Remodel, but their trash priority stays low
//            		  //so eg Coppers aren't remodeled into Estates before Estates into Caravans
//            		  wantsToGain=false;
//            		  break;
//            	  }
                  if (!theCondition.isTrue(possessor!=null? possessor : this)) {
                      wantsToGain=false;
                      break;
                  }
              }
              if (wantsToGain) {
                if ((!costExact && anAvailableCurrency.compareTo( theRule.getCardToBuy().getCost( getCurrentGame() ))>=0)
                 ||(costExact && anAvailableCurrency.compareTo( theRule.getCardToBuy().getCost( getCurrentGame() ))==0)) {
                   if (noConstraints || 
                     (!suicideIfBuys(theRule.getCardToBuy())
                     && getCurrentGame().countInSupply( theRule.getCardToBuy() )>0))
                        return theRule.getCardToBuy();
                }
              }
          }
          return null;
      }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( DomPlayer aO ) {
      return toString().compareTo( aO.toString() );
    }

    /**
     * @param aCard
     */
    public void addBuyRuleFor( DomCardName aCard ) {
        for (DomBuyRule theRule : getBuyRules()){
          if (theRule.getCardToBuy().hasCardType(DomCardType.Victory))
        	  continue;
          if (aCard.getCost().getCoins() >= theRule.getCardToBuy().getCost().getCoins()
        	  || theRule.getCardToBuy()==DomCardName.Silver) {
        	insertBuyRule(aCard, theRule);
			return;
          }
        }
    }

	private void insertBuyRule(DomCardName aCard, DomBuyRule theRule) {
		String theBane = null;
		if (aCard==DomCardName.Young_Witch)
			 theBane=((DomCardName) DomCardName.getPossibleBaneCards()[(int)Math.random()*DomCardName.getPossibleBaneCards().length]).name();
		 DomBuyRule theNewRule = new DomBuyRule( aCard.name(), DomPlayStrategy.standard.name(),theBane);
		 DomBuyCondition theCondition = new DomBuyCondition();
		 theCondition.addLeftHand( "countCardsInDeck", aCard.name() );
		 theCondition.addComparator( "smallerThan" );
		 theCondition.addRightHand( "constant", "1" );
		 theNewRule.addCondition( theCondition );
		 getBuyRules().add(getBuyRules().indexOf( theRule ), theNewRule );
		 if (aCard.getPotionCost()>0){
		     theNewRule = new DomBuyRule( DomCardName.Potion.name(), DomPlayStrategy.standard.name(),null);
		     theCondition = new DomBuyCondition();
		     theCondition.addLeftHand( "countCardsInDeck", DomCardName.Potion.name() );
		     theCondition.addComparator( "smallerThan" );
		     theCondition.addRightHand( "constant", "1" );
		     theNewRule.addCondition( theCondition );
		     getBuyRules().add(getBuyRules().indexOf( theRule ), theNewRule );
		 }
		 return;
	}

    /**
     * @param aPotionValue
     */
    public void addAvailablePotion( int aPotionValue ) {
      if (DomEngine.haveToLog && aPotionValue>0) DomEngine.addToLog( this + " gets +" + aPotionValue + "P");
      availablePotions+=aPotionValue;
    }

    /**
     * @param aCardsInHand 
     * @return 
     * 
     */
    public DomCost getTotalPotentialCurrency() {
        int theTotalCoins = availableCoins;
        int theTotalPotions = availablePotions;
        for (int i=0;i<cardsInHand.size();i++) {
          DomCard theCardInHand = cardsInHand.get(i);
          theTotalCoins += theCardInHand.getPotentialCoinValue();
          theTotalPotions += theCardInHand.getPotionValue();
        }
        return new DomCost( theTotalCoins, theTotalPotions );
    }

    /**
     * @return 
     * 
     */
    public DomCost getPotentialCurrencyFromNativeVillageMat() {
        int theTotalCoins=0;
        int theTotalPotions=0;
        for (DomCard theCard : nativeVillageMat) {
          theTotalCoins += theCard.getPotentialCoinValue();
          theTotalPotions += theCard.getPotionValue();
        }
        return new DomCost( theTotalCoins, theTotalPotions );
    }


    /**
     * @param aCardToTrash
     * @return
     */
    public boolean removingReducesBuyingPower( DomCard aCardToTrash ) {
        DomCost theValue = aCardToTrash.getPotentialCurrencyValue();
        if (theValue.compareTo( DomCost.ZERO )>0) {
          DomCost theTotalCurrency = getTotalPotentialCurrency();
          int theIndex = cardsInHand.indexOf( aCardToTrash);
          cardsInHand.remove( aCardToTrash );
          DomCost theReducedCurrency = getTotalPotentialCurrency();
          cardsInHand.add(theIndex, aCardToTrash );
          if (getDesiredCard( theTotalCurrency, false ) != getDesiredCard( theReducedCurrency, false ))
            return true;
        }
        return false;
    }
    /**
     * @param aCardToTrash
     * @return
     */
    public boolean addingThisIncreasesBuyingPower( DomCost aCost ) {
        if (aCost.compareTo( DomCost.ZERO )>0) {
          DomCost theTotalCurrency = getTotalPotentialCurrency();
          DomCost theAddedCurrency = theTotalCurrency.add(aCost);
          if (getDesiredCard( theTotalCurrency, false ) != getDesiredCard( theAddedCurrency, false ))
            return true;
        }
        return false;
    }

    /**
     * 
     */
    public void increasePirateShipLevel() {
      pirateShipLevel++;
      if (DomEngine.haveToLog) DomEngine.addToLog( this + "'s Pirate Ships now worth $" + pirateShipLevel);
    }

	public void putInHand(DomCard aCard) {
        getCardsInHand().add( aCard );
        if (DomEngine.haveToLog ) DomEngine.addToLog( this + " adds the " + aCard + " to his hand");
	}

	public void putDeckInDiscard() {
      deck.putDeckInDiscard();
	}

	public boolean isDeckEmpty() {
		return deck.getDeckAndDiscardSize()==0;
	}

	public boolean isUserCreated() {
		return types.contains(DomBotType.UserCreated);
	}

	public String getXML() {
        StringBuilder theXML = new StringBuilder();
        String newline = System.getProperty( "line.separator" );
        theXML.append("<player name=\"").append(name).append("\"").append(newline);
        theXML.append(" author=\"").append(author).append("\"").append(newline);
        theXML.append(" description=\"").append(description.replace(newline,"XXXX")).append("\"");
        theXML.append(">").append(newline);
        for (DomBotType botType : getTypes()){
          theXML.append(" <type name=\"").append(botType.name()).append("\"/>").append(newline);
        }
        if (myStartState!=null)
          theXML.append(myStartState.getXML());
        if (!mySuggestedBoardCards.isEmpty()) {
            theXML.append("  <board contents=\"").append(mySuggestedBoardCards.toString().replaceAll("\\[|\\]","")).append("\"");
            theXML.append(" bane=\"").append(myBaneCard).append("\"");            
            theXML.append("/>").append(newline);
        }
        for (DomBuyRule theRule : getPrizeBuyRules()){
          theXML.append(theRule.getXML());
        }
        for (DomBuyRule theRule : getBuyRules()){
          theXML.append(theRule.getXML());
        }
        theXML.append("</player>").append(newline);
		return theXML.toString();
	}

	public double countMaxOpponentsVictoryPoints() {
		int theMaxVP = 0;
		for (DomPlayer player : getOpponents()) {
			int theVP = player.countVictoryPoints();
			theMaxVP = theVP>theMaxVP? theVP : theMaxVP;
		}
		return theMaxVP;	
	}

	public DomCost getTotalAvailableCurrency() {
		return new DomCost( availableCoins, availablePotions );
	}

	public void addToBoughtCards(DomCard theCard) {
		boughtCards.add(theCard);
	}

	public void increaseActionsPlayed() {
		actionsplayed++;
	}

	public int getActionsPlayed() {
		return actionsplayed;
	}

	public void addForbiddenCardToBuy(DomCardName aCard) {
		forbiddenCardsToBuy.add(aCard);
	}

	public  ArrayList<DomCardName> getForbiddenCardsToBuy() {
		return forbiddenCardsToBuy;
	}

	public ArrayList<DomCard> removeCardsFromDiscard(DomCardName aCardName) {
		return deck.removeCardsFromDiscard(aCardName);
	}

	public int countDifferentCardsInDeck() {
		return deck.countDifferentCards();
	}

	public int getVictoryTokens() {
		return victoryTokens;
	}

	public DomCard getBottomCardFromDeck() {
		return deck.getBottomCard();
	}

	public void putOnBottomOfDeck(DomCard theBottomCard) {
		deck.putCardOnBottomOfDeck(theBottomCard);
	    if (DomEngine.haveToLog) DomEngine.addToLog(this + " puts "+theBottomCard+" on the bottom");
	}

	public void addPossessionTurn(DomPlayer owner) {
	  possessionTurns.add(owner);
	}

	public ArrayList<DomCardName> getCardsGainedLastTurn() {
		return cardsGainedLastTurn;
	}

	public void increaseTalismanCount() {
		talismanCount++;
	}

	public void setComputerGenerated() {
      types.add(DomBotType.Generated);
	}

	public boolean isComputerGenerated() {
	  return types.contains(DomBotType.Generated);
	}

	public ArrayList<DomCard> revealUntilVictoryOrCurse() {
		return deck.revealUntilVictoryOrCurse();
	}

	public ArrayList<DomCard> revealUntilActionOrTreasure() {
		return deck.revealUntilActionOrTreasure();
	}

	public ArrayList<DomBuyRule> getPrizeBuyRules() {
		return prizeBuyRules;
	}

	public ArrayList<DomPlayer> getPossessionTurns() {
  	  return possessionTurns;
	}

	public void setPossessor(DomPlayer aPossessor) {
      possessor=aPossessor;
	}

	public boolean hasExtraOutpostTurn() {
      return extraOutpostTurn;
	}

	public void addPhysicalCard(DomCard aCard) {
		deck.get(aCard.getName()).add( aCard );
	}

	public boolean wants(DomCardName aCardName) {
		//first check if pile not empty and not suicide
		if (getCurrentGame().countInSupply(aCardName)==0
		|| suicideIfBuys(aCardName))
			return false;
        //then check if buy rules indicate player wants the card
		for (DomBuyRule buyRule : getBuyRules()){
		  if (buyRule.getCardToBuy()==aCardName) {
			  if (buyRule.wantsToBuyOrGainNow(this))
				  return true;
		  }
		}
		return false;
	}

	public void doTrueRandomShuffle() {
	  deck.shuffle();
	}

	public int getBuysLeft() {
	  return buysLeft;
	}

	public int getProbableActionsLeft() {
	  int probableActionsLeft = getActionsLeft();
	  for (DomCard theCard : getCardsInHand()){
		  if (theCard.hasCardType(DomCardType.Terminal))
			  probableActionsLeft--;
		  if (theCard.hasCardType(DomCardType.Village))
			  probableActionsLeft++;
	  }
	  return probableActionsLeft;
	}

	public HashSet<DomBotType> getTypes() {
		return types;
	}

	public boolean hasType(Object object) {
		if (types.contains(object))
			return true;
		return false;
	}
	
	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return author;
	}

	public void addType(DomBotType aType) {
		types.add(aType);
	}

	public void setAuthor(String text) {
		author = text;
	}

	public void setDescription(String text) {
		description = text;
	}

	public void setTypes(HashSet<DomBotType> myTypes) {
		types=myTypes;
	}

	public boolean isInBuyPhase() {
		return getCurrentGame().isBuyPhase();
	}

	public boolean wantsToGainOrKeep(DomCardName aCardName) {
        //check if buy rules indicate player wants the card
		for (DomBuyRule buyRule : getBuyRules()){
		  if (buyRule.getCardToBuy()==aCardName) {
			  if (buyRule.wantsToBuyOrGainNow(this))
				  return true;
		  }
		}
		return false;
	}

	public boolean stillInEarlyGame() {
	    for (DomCardName cardName : DomCardName.values()){
	      if (cardName==DomCardName.Estate)      
	    	  continue;
	      if (cardName.hasCardType(DomCardType.Victory) && cardName.getDiscardPriority(1)<10)
	        if (countInDeck(cardName)>0)
	          return false;
	    }
		return true;
	  }

	public DomPhase getPhase() {
		return currentPhase;
	}

	public void addToCardsToStayInPlay(DomCard domCard) {
	  cardsToStayInPlay.add(domCard);		
	}

	public void setExtraOutpostTurn(boolean b) {
		extraOutpostTurn=b;
	}

	public  int getKnownTopCards() {
		return knownTopCards;
	}

	public void setKnownTopCards(int size) {
	   if (size<0)
		   size=0;
       knownTopCards=size;		
	}

	public boolean checkForcedStart() {
		return getDeck().checkForcedStart();
	}

	public void setStartState(StartState tmp) {
		myStartState=tmp;
	}

	public StartState getStartState() {
		return myStartState;
	}

	void dealStartCards() {
	   for (int c=0;c<7;c++) {
		 gainOnTopOfDeck(getCurrentGame().takeFromSupply(DomCardName.Copper));
	   }
	   for (int c=0;c<3;c++) {
		 gainOnTopOfDeck(getCurrentGame().takeFromSupply(DomCardName.Estate));
	   }
	}

	public boolean addBoard(String aBoardCards, String aBane) {
		if (!StartState.dissectAndAdd(aBoardCards, mySuggestedBoardCards))
			return false;
		try {
			myBaneCard = DomCardName.valueOf(aBane);
		} catch (Exception e) {
			if (!aBane.trim().isEmpty()) {
			  return false;
			}
			myBaneCard=null;
		} 
		return true;
	}

	public String getBoard() {
		return mySuggestedBoardCards.toString().replaceAll("\\[|\\]","" );
	}

	public String getBaneCard() {
		if (myBaneCard==null)
			return "";
		return myBaneCard.toString();
	}
}