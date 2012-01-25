package be.aga.dominionSimulator;

import java.util.ArrayList;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;
import be.aga.dominionSimulator.enums.DomPhase;

public class DomGame {
    private static final Logger LOGGER = Logger.getLogger( DomGame.class );
    static {
        LOGGER.setLevel( DomEngine.LEVEL );
        LOGGER.removeAllAppenders();
        if (DomEngine.addAppender)
            LOGGER.addAppender(new ConsoleAppender(new SimpleLayout()) );
    }

  ArrayList< DomPlayer > players = new ArrayList< DomPlayer >();
  DomBoard board;
  public long checkGameFinishTime=0;
  public long playerTurnTime=0;
  private int bridgeCount=0;
  private int princessCount=0;
  private int quarryCount=0;
  private DomPlayer activePlayer;
  public boolean emptyPilesEnding=false;


/**
   * @param aPlayers
 * @param aGameType 
 * @param aforce43Start 
   */
  public DomGame ( DomBoard aBoard, ArrayList< DomPlayer > aPlayers, ArrayList< DomCardName > aCardNames) {
     players.addAll( aPlayers );
     if (aBoard==null){
       board = new DomBoard(DomCardName.class, players);
       board.initialize();
     }else{
       board=aBoard;
     }
     initializePlayers();
  }

/**
 * @return 
 * 
 */
private void initializePlayers() {
    for (DomPlayer thePlayer : players) 
      thePlayer.initializeForGame(this);
    //adding the starting estates adds tokens to the trade route mat which we don't want
    getBoard().clearTradeRouteMat();
}

/**
 * @return
 */
public ArrayList< DomPlayer > getPlayers() {
  return players;
}

/**
 * @param aCopper
 * @param aI
 * @return
 */
public DomCard takeFromSupply( DomCardName aCardName ) {
  return board.take(aCardName);
}

/**
 * @return 
 * 
 */
public void run() {
    long theTime = System.currentTimeMillis();
    int turn = 0;
    do {
        turn++;
        DomEngine.logPlayerIndentation = 0;
        for (int i=0;i<players.size()&&!isGameFinished();i++) {
          activePlayer = players.get(i);
          theTime = System.currentTimeMillis();
          //first take all possessed turns
      	  while (!activePlayer.getPossessionTurns().isEmpty() && !isGameFinished()) {
     	    activePlayer.setPossessor(activePlayer.getPossessionTurns().remove(0));
            activePlayer.takeTurn();
      	  }
      	  activePlayer.setPossessor(null);
      	  //take normal turn
      	  if (!isGameFinished()) {
            activePlayer.takeTurn();
      	  }
      	  //take Outpost turn
          if (activePlayer.hasExtraOutpostTurn() && !isGameFinished()){
        	activePlayer.takeTurn();
          }
          playerTurnTime += System.currentTimeMillis()-theTime;
          DomEngine.logPlayerIndentation++;
        }
    } while (!isGameFinished() );
    DomEngine.logPlayerIndentation = 0;
}

public void determineWinners() {
    int theMaxPoints = 0;
    int theMinTurns = 10000;
    int winners = 0;
     
    for (DomPlayer thePlayer : players) {
      thePlayer.handleGameEnd();
    }
    for (DomPlayer thePlayer : players) {
      if (DomEngine.haveToLog) DomEngine.addToStartOfLog( "");
      thePlayer.showDeck();
      if (DomEngine.haveToLog) 
    	  DomEngine.addToStartOfLog( "<B>"+thePlayer + "</B> has " +  thePlayer.countVictoryPoints() + " points "
    			  +(thePlayer.getVictoryTokens()>0 ? (" ("+thePlayer.getVictoryTokens()+"&#x25BC;) ") : "")
                  +"and took " + thePlayer.getTurns() + " turns");
      theMaxPoints = thePlayer.countVictoryPoints()>theMaxPoints ? thePlayer.countVictoryPoints() : theMaxPoints;
    }
    for (DomPlayer thePlayer : players) {
      if (thePlayer.countVictoryPoints()>=theMaxPoints) { 
        theMinTurns = thePlayer.getTurns()<theMinTurns ? thePlayer.getTurns() : theMinTurns;
      }
    }
    for (DomPlayer thePlayer : players) {
      if (thePlayer.countVictoryPoints()>=theMaxPoints && thePlayer.getTurns()<=theMinTurns) {
        winners++;
      }
    }
    if (DomEngine.haveToLog) DomEngine.addToStartOfLog( "");
    for (DomPlayer thePlayer : players) {
      if (thePlayer.countVictoryPoints()>=theMaxPoints && thePlayer.getTurns()<=theMinTurns) {
        if (winners > 1) {
          if (DomEngine.haveToLog) DomEngine.addToStartOfLog( thePlayer + " ties for the win !!" );
//          if (players.get(0).pprUsed)
          thePlayer.addTie();
        } else {
          if (DomEngine.haveToLog) DomEngine.addToStartOfLog( thePlayer + " wins this game!!");
//          if (players.get(0).pprUsed)
          thePlayer.addWin();
        }
      } 
    }
}

/**
 * @return
 */
boolean isGameFinished() {
    long theTime = System.currentTimeMillis();
    if (players.get( 0 ).getTurns()>60){
      LOGGER.debug( "Too many turns!!! Game ended!" );
      checkGameFinishTime+=System.currentTimeMillis()-theTime;
      return true;
    }
    
    boolean isGameFinished;
	if (players.size()==1) {
    	isGameFinished = board.count( DomCardName.Province)<=4 
            || (board.get(DomCardName.Colony)!=null && board.count( DomCardName.Colony)<=4 );
        checkGameFinishTime+=System.currentTimeMillis()-theTime;
    } else {
    	isGameFinished = board.count( DomCardName.Province)==0 
          || (board.get(DomCardName.Colony)!=null && board.count( DomCardName.Colony)==0 );
        checkGameFinishTime+=System.currentTimeMillis()-theTime;
    }

    if (isGameFinished) {
      return true;
    }

    if (board.countEmptyPiles() >= 3) {
      if (DomEngine.haveToLog) {
        DomEngine.addToLog("");
        DomEngine.addToLog("Three piles depleted!");
      }
      checkGameFinishTime+=System.currentTimeMillis()-theTime;
      emptyPilesEnding=true;
      return true;
    }

    return false;
}

/**
 * @param aProvince
 * @return
 */
public int countInSupply( DomCardName aCardName ) {
  return board.count( aCardName );
}

/**
 * @param aRemove
 */
public void addToTrash( DomCard aRemove ) {
  board.addToTrash(aRemove);
}

/**
 * @param aCard
 */
public void returnToSupply( DomCard aCard ) {
   board.add( aCard);
}

/**
 * @return
 */
public int countEmptyPiles() {
  return board.countEmptyPiles();
}

/**
 * @return
 */
public ArrayList<String> getEmptyPiles() {
    return board.getEmptyPiles();
}

/**
 * @return
 */
public ArrayList< DomCard > getTrashedCards() {
    return board.getTrashedCards();
}

public DomBoard getBoard() {
  return board;
}

/**
 * @param aCardToTrash
 * @return
 */
public DomCard removeFromTrash( DomCard aCard) {
  return board.removeFromTrash(aCard);
}

public ArrayList<DomCard> revealFromBlackMarketDeck() {
	return board.revealFromBlackMarketDeck();
}

public void returnToBlackMarketDeck(DomCard theCard) {
	board.returnToBlackMarketDeck(theCard);
}

public int getEmbargoTokensOn(DomCardName aCard) {
	return board.getEmbargoTokensOn(aCard);
}

public void putEmbargoTokenOn(DomCardName aCard) {
  board.putEmbargoTokenOn(aCard);
}

public DomCardName getBestCardInSupplyFor(DomPlayer aPlayer, DomCardType aType, DomCost domCost, boolean anExactCost) {
	return board.getBestCardInSupplyFor(aPlayer, aType, domCost, anExactCost, null);
}

public DomCardName getBestCardInSupplyFor(DomPlayer aPlayer, DomCardType aType, DomCost domCost) {
	return board.getBestCardInSupplyFor(aPlayer, aType, domCost, false, null);
}

public DomCardName getCardForSwindler(DomPlayer aPlayer, DomCost domCost) {
	return board.getCardForSwindler( aPlayer, domCost);
}

public double countCardsInSmallestPile() {
	return board.countCardsInSmallestPile();
}

public boolean isBuyPhase() {
  return activePlayer.getPhase()==DomPhase.Buy;
}

public int getBridgesPlayed() {
	return bridgeCount;
}

public void setBridgeCount(int bridgeCount) {
	this.bridgeCount = bridgeCount;
}

public int getPrincessesInPlay() {
	return princessCount;
}

public void setPrincessCount(int princessCount) {
	this.princessCount = princessCount;
}

public int getQuarriesPlayed() {
	return quarryCount;
}

public void setQuarryCount(int quarryCount) {
	this.quarryCount = quarryCount;
}

public void increaseBridgeCount() {
  bridgeCount++;
}

public void setPrincessPlayed() {
  princessCount=1;
}
public void increaseQuarryCount() {
  quarryCount++;		
}

  public int countActionsInPlay() {
    int theCount = 0;
	for (DomCard theCard : activePlayer.getCardsInPlay()) {
	  theCount+=theCard.hasCardType( DomCardType.Action ) ? 1 : 0;
	}
    return theCount;
  }

public int getGainsNeededToEndGame() {
	return getBoard().getGainsNeededToEndGame();
}

public DomCardName getBestCardInSupplyNotOfType(DomPlayer aPlayer,
		DomCardType aType, DomCost domCost) {
	return board.getBestCardInSupplyFor(aPlayer, null, domCost, false, aType);
}

public int getHighwaysInPlay() {
	return activePlayer.getCardsFromPlay(DomCardName.Highway).size();
}

public DomPlayer getActivePlayer() {
	return activePlayer;
}
}