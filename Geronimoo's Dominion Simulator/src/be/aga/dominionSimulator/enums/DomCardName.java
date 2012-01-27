package be.aga.dominionSimulator.enums;


import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import be.aga.dominionSimulator.DomBuyRule;
import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomGame;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.cards.*;
import be.aga.dominionSimulator.gui.EscapeDialog;

public enum DomCardName  {
    //common cards
    Copper (0, 0, 1, 0, 20, 15, new DomCardType[]{DomCardType.Treasure}),
    Silver (3, 0, 2, 0, 25, 20, new DomCardType[]{DomCardType.Treasure}),
    Gold (6, 0, 3, 0, 30, 31, new DomCardType[]{DomCardType.Treasure}),
    Platinum (9, 0, 5, 0, 35, 50, new DomCardType[]{DomCardType.Treasure}),
    Potion (4, 0, 0, 0, 40, 22, new DomCardType[]{DomCardType.Treasure}),
    Estate (2, 0, 0, 1, 100, 9, new DomCardType[]{DomCardType.Victory}),
    Duchy (5, 0, 0, 3, 100, 8, new DomCardType[]{DomCardType.Victory}),
    Province (8, 0, 0, 6, 100, 7, new DomCardType[]{DomCardType.Victory}),
    Colony (11, 0, 0, 10, 100, 6, new DomCardType[]{DomCardType.Victory}),
    Curse (0, 0, 0, -1, 100, 10, new DomCardType[]{DomCardType.Curse}),
   
    //kingdom cards
    Adventurer (6, 0, 0, 0, 22, 40, new DomCardType[]{DomCardType.Action , DomCardType.Kingdom, DomCardType.Terminal}),
    Alchemist (3, 1, 0, 0, 9, 40 , new DomCardType[]{DomCardType.Action , DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Card_Advantage}),
    Ambassador (3, 0, 0, 0, 35, 18, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher}),
    Apothecary (2, 1, 0, 0, 19, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Card_Advantage}),
    Apprentice (5, 0, 0, 0, 20, 19, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Trasher, DomCardType.Cycler, DomCardType.Card_Advantage, DomCardType.TrashForBenefit}),
    Bag_of_Gold (0, 0, 0, 0, 7, 25, new DomCardType[]{DomCardType.Action, DomCardType.Prize}),
    Bank (7, 0, 0, 0, 1000, 30, new DomCardType[]{DomCardType.Treasure, DomCardType.Kingdom}),
    Baron (4, 0, 0, 0, 22, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Bazaar (5, 0, 1, 0, 5, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Village}),
    Bishop (4, 0, 0, 0, 22, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.TrashForBenefit}),
    Black_Market (3, 0, 2, 0, 32, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Border_Village (6, 0, 0, 0, 5, 19, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Village}),
    Bridge (4, 0, 2, 0, 25, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Bureaucrat (4, 0, 0, 0, 29, 20, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}), 
    Cache (5, 0, 3, 0, 100, 31, new DomCardType[]{DomCardType.Treasure, DomCardType.Kingdom}),
    Caravan (4, 0, 0, 0, 8, 27, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Duration, DomCardType.Cycler, DomCardType.Card_Advantage}),
    Cartographer (5, 0, 0, 0, 19, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Cellar (2, 0, 0, 0, 21, 17, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}), 
    Chancellor (3, 0, 2, 0, 30, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Chapel (2, 0, 0, 0, 37, 18, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher}),
    City (5, 0, 0, 0, 5, 30, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Village}),
    Conspirator (4, 0, 2, 0, 35, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Contraband (5, 0, 3, 0, 5, 27, new DomCardType[]{DomCardType.Treasure, DomCardType.Kingdom}),
    Coppersmith (4, 0, 0, 0, 26, 21, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Council_Room (5, 0, 0, 0, 25, 24, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}), 
    Counting_House (5, 0, 0, 0, 25, 24, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Courtyard (2, 0, 0, 0, 24, 24, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}), 
    Crossroads (2, 0, 0, 0, 1, 17, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Village, DomCardType.Card_Advantage}),
    Cutpurse (4, 0, 2, 0, 32, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Attack, DomCardType.Terminal}),
    Develop (3, 0, 0, 0, 33, 16, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.TrashForBenefit}), 
    Diadem (0, 0, 2, 0, 50, 30, new DomCardType[]{DomCardType.Prize, DomCardType.Treasure}),
    Duchess (2, 0, 2, 0, 35, 19, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Duke (5, 0, 0, 3, 100, 8, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom}),
    Embargo (2, 0, 2, 0, 25, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Embassy (5, 0, 0, 0, 25, 24, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}), 
    Envoy (4, 0, 0, 0, 24, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}),
    Expand (7, 0, 0, 0, 30, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.TrashForBenefit}), 
    Explorer (5, 0, 0, 0, 37, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Fairgrounds (6, 0, 0, 0, 100, 7, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom}),
    Familiar (3, 1, 0, 0, 15, 40, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Cycler}),
    Farming_Village (4, 0, 0, 0, 1, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Village, DomCardType.Cycler}),
    Farmland (6, 0, 0, 2, 100, 9, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom}),
    Feast (4, 0, 0, 0, 28, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Festival (5, 0, 2, 0, 1, 26, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Village}),
    Fishing_Village (3, 0, 1, 0, 1, 21, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Duration, DomCardType.Village}),
    Followers (0, 0, 0, 0, 18, 31, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Prize, DomCardType.Terminal, DomCardType.Card_Advantage}),
    Fool$s_Gold(2, 0, 2, 0, 85, 27, new DomCardType[]{DomCardType.Reaction, DomCardType.Kingdom, DomCardType.Treasure}),
    Forge (7, 0, 0, 0, 30, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.TrashForBenefit}), 
    Fortune_Teller (3, 0, 2, 0, 33, 22, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Gardens (4, 0, 0, 0, 100, 9, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom}),
    Ghost_Ship (5, 0, 0, 0, 21, 26, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}),
    Golem (4, 1, 0, 0, 18, 40, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Village, DomCardType.Card_Advantage}),
    Goons (6, 0, 2, 0, 20, 27, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Governor (5, 0, 0, 0,11, 40, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Card_Advantage, DomCardType.TrashForBenefit}), 
    Grand_Market (6, 0, 2, 0, 12, 39, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}), 
    Great_Hall (3, 0, 0, 1, 5, 16, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Victory, DomCardType.Cycler}),
    Haggler (5, 0, 2, 0, 24, 27, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Hamlet (2, 0, 0, 0, 5, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Village}),
    Harem (6, 0, 2, 2, 55, 25, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom, DomCardType.Treasure}),
    Harvest (5, 0, 3, 0, 19, 28, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Haven (2, 0, 0, 0, 15, 19, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Duration, DomCardType.Cycler}),
    Herbalist (2, 0, 1, 0, 40, 21, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Highway (5, 0, 1, 0, 7, 28, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Hoard (6, 0, 2, 0, 60, 32, new DomCardType[]{DomCardType.Kingdom, DomCardType.Treasure}),
    Horn_of_Plenty (5, 0, 0, 0, 1001, 26, new DomCardType[]{DomCardType.Kingdom, DomCardType.Treasure}),
    Horse_Traders (4, 0, 1, 0, 29, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Reaction}), 
    Hunting_Party (5, 0, 0, 0, 7, 28, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Card_Advantage}), 
    Ill_Gotten_Gains (5, 0, 2, 0, 95, 15, new DomCardType[]{DomCardType.Treasure, DomCardType.Kingdom}),
    Inn (5, 0, 0, 0, 7, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Village}),
    Ironworks (4, 0, 0, 0, 25, 24, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom}),
    Island (4, 0, 0, 2, 37, 22, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom, DomCardType.Action, DomCardType.Terminal}),
    Jack_of_all_Trades (4, 0, 0, 0, 20, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Jester (5, 0, 2, 0, 19, 26, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    King$s_Court (7, 0, 0, 0, 5, 35, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Multiplier}),
    Laboratory (5, 0, 0, 0, 8, 40, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Card_Advantage}), 
    Library (5, 0, 0, 0, 20, 30, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}), 
    Lighthouse (2, 0, 1, 0, 9, 21, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Duration}),
    Loan (3, 0, 1, 0, 10, 17, new DomCardType[]{DomCardType.Treasure, DomCardType.Kingdom}),
    Lookout (3, 0, 0, 0, 3, 16, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom}),
    Mandarin (5, 0, 3, 0, 26, 24, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Margrave (5, 0, 0, 0, 24, 26, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage, DomCardType.Attack}), 
    Market (5, 0, 1, 0, 13, 30, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}), 
    Masquerade (3, 0, 0, 0, 27, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage, DomCardType.Trasher}), 
    Menagerie (3, 0, 0, 0, 1, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Cycler}),
    Merchant_Ship (5, 0, 2, 0, 19, 28, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Duration}), 
    Militia (4, 0, 2, 0, 30, 25, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}), 
    Mine (5, 0, 0, 0, 37, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Minion (5, 0, 2, 0, 16, 40, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom}),
    Mining_Village (4, 0, 2, 0, 16, 18, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Village, DomCardType.Cycler}),
    Mint (5, 0, 0, 0, 40, 19, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Moat (2, 0, 0, 0, 40, 23, new DomCardType[]{DomCardType.Action, DomCardType.Reaction, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}), 
    Moneylender (4, 0, 1, 0, 23, 21, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Monument (4, 0, 2, 0, 22, 26, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Mountebank (5, 0, 2, 0, 18, 27, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Native_Village (2, 0, 0, 0, 2, 17, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Village}),
    Navigator (4, 0, 2, 0, 29, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Noble_Brigand (4, 0, 1, 0, 23, 21, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Nomad_Camp (4, 0, 2, 0, 29, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Nobles (6, 0, 0, 2, 12, 32, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Victory, DomCardType.Village,DomCardType.Card_Advantage}),
    Oasis (3, 0, 1, 0, 17, 21, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Oracle (3, 0, 0, 0, 20, 22, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Card_Advantage, DomCardType.Terminal}),
    Outpost (5, 0, 0, 0, 20, 28, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Duration, DomCardType.Terminal}),
    Pawn (2, 0, 1, 0, 11, 17, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Pearl_Diver (2, 0, 0, 0, 4, 16, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Peddler (8, 0, 1, 0, 10, 30, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Philosopher$s_Stone(3, 1, 0, 0, 8,30, new DomCardType[]{DomCardType.Kingdom, DomCardType.Treasure}),
    Pirate_Ship (4, 0, 0, 0, 20, 20, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Possession (6, 1, 0, 0, 22, 45, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Princess (0, 0, 2, 0, 23, 31, new DomCardType[]{DomCardType.Action, DomCardType.Prize, DomCardType.Terminal}),
    Quarry (4, 0, 3, 0, 65, 29, new DomCardType[]{DomCardType.Kingdom, DomCardType.Treasure}),
    Rabble (5, 0, 0, 0, 19, 29, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}),
    Remake (4, 0, 0, 0, 35, 16, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.TrashForBenefit}), 
    Remodel (4, 0, 0, 0, 24, 18, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.TrashForBenefit}), 
    Royal_Seal (5, 0, 2, 0, 70, 25, new DomCardType[]{DomCardType.Kingdom, DomCardType.Treasure}),
    Saboteur (5, 0, 0, 0, 20, 23, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Salvager (4, 0, 0, 0, 25, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.TrashForBenefit}),
    Scheme (3, 0, 0, 0, 10, 16, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Scout (4, 0, 0, 0, 2, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Scrying_Pool (2, 1, 0, 0, 7, 35 , new DomCardType[]{DomCardType.Action , DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Card_Advantage}),
    Sea_Hag (4, 0, 0, 0, 19, 27, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Secret_Chamber (2, 0, 0, 0, 40, 16, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Reaction}), 
    Shanty_Town (3, 0, 0, 0, 6, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Village, DomCardType.Card_Advantage}),
    Silk_Road (4, 0, 0, 0, 100, 9, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom}),
    Smithy (4, 0, 0, 0, 25, 24, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}),
    Smugglers (3, 0, 0, 0, 30, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Spice_Merchant (4, 0, 0, 0, 9, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}), 
    Spy (4, 0, 0, 0, 5, 22, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Cycler}),
    Stables (5, 0, 0, 0, 20, 26, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Card_Advantage}), 
    Stash (5, 0, 2, 0, 75, 23, new DomCardType[]{DomCardType.Kingdom, DomCardType.Treasure}),
    Steward (3, 0, 2, 0, 27, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.Card_Advantage}),
    Swindler (3, 0, 2, 0, 25, 23, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Tactician (5, 0, 0, 0, 19, 27, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Duration, DomCardType.Terminal}),
    Talisman (4, 0, 1, 0, 80, 23, new DomCardType[]{DomCardType.Kingdom, DomCardType.Treasure}),
    Thief (4, 0, 0, 0, 30, 20, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),
    Throne_Room (4, 0, 0, 0, 7, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Multiplier}),
    Torturer (5, 0, 0, 0, 19, 27, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}),
    Tournament (4, 0, 1, 0, 8, 31, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Trader (4, 0, 0, 0, 35, 14, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher, DomCardType.Reaction, DomCardType.TrashForBenefit}),
    Trade_Route (3, 0, 0, 0, 25, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher}),
    Trading_Post (5, 0, 0, 0, 25, 24, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher}),
    Transmute (0, 1, 0, 0, 35, 19, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Trasher}),
    Treasure_Map (4, 0, 0, 0, 20, 35, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom}),
    Treasury (5, 0, 1, 0, 15, 30, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Tribute (5, 0, 0, 0, 24, 26, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Trusty_Steed (0, 0, 0, 0, 6, 45, new DomCardType[]{DomCardType.Action, DomCardType.Prize, DomCardType.Cycler, DomCardType.Village, DomCardType.Card_Advantage}), 
    Tunnel (3, 0, 0, 2, 100, 0, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom}),
    University (2, 1, 0, 0, 5, 25, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Village}),
    Upgrade (5, 0, 0, 0, 16, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Trasher, DomCardType.TrashForBenefit}),
    Vault (5, 0, 0, 0, 20, 26, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}),
    Venture (5, 0, 2, 0, 500, 29, new DomCardType[]{DomCardType.Kingdom, DomCardType.Treasure}),
    Village (3, 0, 0, 0, 5, 17, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Village}),
    Vineyard (0, 1, 0, 0, 100, 7, new DomCardType[]{DomCardType.Victory, DomCardType.Kingdom}),
    Walled_Village (4, 0, 0, 0, 5, 18, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler, DomCardType.Village}),
    Warehouse (3, 0, 0, 0, 10, 18, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Watchtower (3, 0, 0, 0, 27, 31, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Reaction, DomCardType.Card_Advantage}), 
    Wharf (5, 0, 0, 0, 19, 32, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Duration, DomCardType.Card_Advantage}),
    Wishing_Well (3, 0, 0, 0, 7, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Cycler}),
    Witch (5, 0, 0, 0, 18, 40, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal, DomCardType.Card_Advantage}),
    Woodcutter (3, 0, 2, 0, 29, 20, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}), 
    Worker$s_Village (4, 0, 0, 0, 3, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Village, DomCardType.Cycler}),
    Workshop (3, 0, 0, 0, 38, 22, new DomCardType[]{DomCardType.Action, DomCardType.Kingdom, DomCardType.Terminal}),
    Young_Witch (4, 0, 0, 0, 18, 26, new DomCardType[]{DomCardType.Action, DomCardType.Attack, DomCardType.Kingdom, DomCardType.Terminal}),

    ;
    
    public static final Comparator SORT_FOR_TRASHING = new Comparator<DomCardName>(){
        @Override
        public int compare( DomCardName aO1, DomCardName aO2 ) {
            if (aO1.getTrashPriority()< aO2.getTrashPriority())
                return -1;
            if (aO1.getTrashPriority() > aO2.getTrashPriority())
                return 1;
            return 0;
        }
      };

    private DomCost cost = null;  
    private final HashSet< DomCardType > types = new HashSet< DomCardType >();
    private int coinValue;
    private int victoryValue;
    private int playPriority;
    private int discardPriority; 
    
    DomCardName(int aCoinCost, int aPotionCost, int aCoinValue, int aVictoryValue, int aPlayPriority, int aDiscardPriority, DomCardType[] aTypes) {
      cost = new DomCost(aCoinCost, aPotionCost);
      coinValue = aCoinValue;
      victoryValue = aVictoryValue;
      playPriority = aPlayPriority;
      discardPriority = aDiscardPriority;
      for (DomCardType theType : aTypes) {
        types.add( theType );
      }
    }
    
    /**
     * @return
     */
    public DomCard createNewCardInstance() {
      //TODO look into Dynamic Class Loading (tried it, but took way too long)
      switch (this) {
        case Adventurer:
          return new AdventurerCard();
        case Alchemist:
          return new AlchemistCard();
        case Ambassador:
            return new AmbassadorCard();
        case Apprentice:
            return new ApprenticeCard();
        case Apothecary:
            return new ApothecaryCard();
        case Bag_of_Gold:
            return new Bag_of_GoldCard();
        case Bank:
            return new BankCard();
        case Baron:
            return new BaronCard();
        case Bazaar:
            return new BazaarCard();
        case Bishop:
            return new BishopCard();
        case Black_Market:
            return new Black_MarketCard();
        case Border_Village:
            return new Border_VillageCard();
        case Bridge:
            return new BridgeCard();
        case Bureaucrat:
            return new BureaucratCard();
        case Cache:
            return new CacheCard();
        case Caravan:
            return new CaravanCard();
        case Cartographer:
            return new CartographerCard();
        case Cellar:
            return new CellarCard();
        case Chancellor:
            return new ChancellorCard();
        case Chapel:
            return new ChapelCard();
        case City:
            return new CityCard();
        case Colony:
            return new ColonyCard();
        case Conspirator:
            return new ConspiratorCard();
        case Contraband:
            return new ContrabandCard();
        case Copper:
            return new CopperCard();
        case Coppersmith:
            return new CoppersmithCard();
        case Council_Room:
            return new Council_RoomCard();
        case Counting_House:
            return new Counting_HouseCard();
        case Courtyard:
            return new CourtyardCard();
        case Crossroads:
            return new CrossroadsCard();
        case Curse:
            return new CurseCard();
        case Cutpurse:
            return new CutpurseCard();
        case Develop:
            return new DevelopCard();
        case Diadem:
            return new DiademCard();
        case Duchess:
            return new DuchessCard();
        case Duchy:
            return new DuchyCard();
        case Duke:
            return new DukeCard();
        case Embargo:
            return new EmbargoCard();
        case Embassy:
            return new EmbassyCard();
        case Envoy:
            return new EnvoyCard();
        case Estate:
            return new EstateCard();
        case Expand:
            return new ExpandCard();
        case Explorer:
            return new ExplorerCard();
        case Fairgrounds:
            return new FairgroundsCard();
        case Farming_Village:
            return new Farming_VillageCard();
        case Farmland:
            return new FarmlandCard();
        case Familiar:
            return new FamiliarCard();
        case Feast:
            return new FeastCard();
        case Festival:
            return new FestivalCard();
        case Fishing_Village:
            return new Fishing_VillageCard();
        case Fool$s_Gold:
            return new Fool$s_GoldCard();
        case Followers:
            return new FollowersCard();
        case Forge:
            return new ForgeCard();
        case Fortune_Teller:
            return new Fortune_TellerCard();
        case Gardens:
            return new GardensCard();
        case Ghost_Ship:
            return new Ghost_ShipCard();
        case Golem:
            return new GolemCard();
        case Goons:
            return new GoonsCard();
        case Governor:
            return new GovernorCard();
        case Grand_Market:
            return new Grand_MarketCard();
        case Great_Hall:
            return new Great_HallCard();
        case Haggler:
            return new HagglerCard();
        case Hamlet:
            return new HamletCard();
        case Harem:
            return new HaremCard();
        case Harvest:
            return new HarvestCard();
        case Haven:
            return new HavenCard();
        case Herbalist:
            return new HerbalistCard();
        case Highway:
            return new HighwayCard();
        case Hoard:
            return new HoardCard();
        case Horn_of_Plenty:
            return new Horn_of_PlentyCard();
        case Horse_Traders:
            return new Horse_TradersCard();
        case Hunting_Party:
            return new Hunting_PartyCard();
        case Inn:
            return new InnCard();
        case Ill_Gotten_Gains:
            return new Ill_Gotten_GainsCard();
        case Ironworks:
            return new IronworksCard();
        case Jack_of_all_Trades:
            return new Jack_of_all_TradesCard();
        case Island:
            return new IslandCard();
        case Jester:
            return new JesterCard();
        case King$s_Court:
            return new King$s_CourtCard();
        case Laboratory:
            return new LaboratoryCard();
        case Library:
            return new LibraryCard();
        case Lighthouse:
            return new LighthouseCard();
        case Loan:
            return new LoanCard();
        case Lookout:
            return new LookoutCard();
        case Mandarin:
            return new MandarinCard();
        case Margrave:
            return new MargraveCard();
        case Market:
            return new MarketCard();
        case Masquerade:
            return new MasqueradeCard();
        case Menagerie:
            return new MenagerieCard();
        case Merchant_Ship:
            return new Merchant_ShipCard();
        case Militia:
            return new MilitiaCard();
        case Mine:
            return new MineCard();
        case Minion:
            return new MinionCard();
        case Mining_Village:
            return new Mining_VillageCard();
        case Mint:
            return new MintCard();
        case Moat:
            return new MoatCard();
        case Moneylender:
            return new MoneylenderCard();
        case Monument:
            return new MonumentCard();
        case Mountebank:
            return new MountebankCard();
        case Native_Village:
            return new Native_VillageCard();
        case Navigator:
            return new NavigatorCard();
        case Noble_Brigand:
            return new Noble_BrigandCard();
        case Nomad_Camp:
            return new Nomad_CampCard();
        case Nobles:
            return new NoblesCard();
        case Oasis:
            return new OasisCard();
        case Oracle:
            return new OracleCard();
        case Outpost:
            return new OutpostCard();
	    case Pawn:
	        return new PawnCard();
	    case Pearl_Diver:
	        return new Pearl_DiverCard();
	    case Peddler:
	        return new PeddlerCard();
        case Philosopher$s_Stone:
            return new Philosopher$s_StoneCard();
        case Pirate_Ship:
            return new Pirate_ShipCard();
        case Possession:
            return new PossessionCard();
        case Potion:
            return new PotionCard();
        case Princess:
            return new PrincessCard();
        case Province:
            return new ProvinceCard();
        case Quarry:
            return new QuarryCard();
        case Rabble:
            return new RabbleCard();
        case Remake:
            return new RemakeCard();
        case Remodel:
            return new RemodelCard();
        case Royal_Seal:
            return new Royal_SealCard();
        case Saboteur:
            return new SaboteurCard();
        case Salvager:
            return new SalvagerCard();
        case Scheme:
            return new SchemeCard();
        case Scout:
            return new ScoutCard();
        case Scrying_Pool:
            return new Scrying_PoolCard();
        case Sea_Hag:
            return new Sea_HagCard();
        case Secret_Chamber:
            return new Secret_ChamberCard();
        case Shanty_Town:
            return new Shanty_TownCard();
        case Silk_Road:
            return new Silk_RoadCard();
        case Smithy:
            return new SmithyCard();
        case Smugglers:
            return new SmugglersCard();
        case Spice_Merchant:
            return new Spice_MerchantCard();
        case Spy:
            return new SpyCard();
        case Stables:
            return new StablesCard();
        case Stash:
            return new StashCard();
        case Steward:
            return new StewardCard();
        case Swindler:
            return new SwindlerCard();
        case Tactician:
            return new TacticianCard();
        case Talisman:
            return new TalismanCard();
        case Thief:
            return new ThiefCard();
        case Throne_Room:
            return new Throne_RoomCard();
        case Torturer:
            return new TorturerCard();
        case Tournament:
            return new TournamentCard();
        case Trader:
            return new TraderCard();
        case Trade_Route:
            return new Trade_RouteCard();
        case Trading_Post:
            return new Trading_PostCard();
        case Transmute:
            return new TransmuteCard();
        case Treasure_Map:
            return new Treasure_MapCard();
        case Treasury:
            return new TreasuryCard();
        case Tribute:
            return new TributeCard();
        case Trusty_Steed:
            return new Trusty_SteedCard();
        case Tunnel:
            return new TunnelCard();
        case University:
            return new UniversityCard();
        case Upgrade:
            return new UpgradeCard();
        case Vault:
            return new VaultCard();
        case Venture:
            return new VentureCard();
        case Village:
            return new VillageCard();
        case Vineyard:
            return new VineyardCard();
        case Walled_Village:
            return new Walled_VillageCard();
        case Warehouse:
            return new WarehouseCard();
        case Watchtower:
            return new WatchtowerCard();
        case Wharf:
            return new WharfCard();
        case Wishing_Well:
            return new Wishing_WellCard();
        case Witch:
            return new WitchCard();
        case Woodcutter:
            return new WoodcutterCard();
        case Worker$s_Village:
            return new Worker$s_VillageCard();
        case Workshop:
            return new WorkshopCard();
        case Young_Witch:
            return new Young_WitchCard();
        }
        return new DomCard( this );
    }

    public DomCost getCost() { 
      return cost; 
    }
    public HashSet<DomCardType> types() { 
      return types; 
    }

    /**
     * @param aKingdom
     * @return
     */
    public boolean hasCardType( DomCardType aCardType ) {
      return types.contains( aCardType);
    }
    
    public Object[] getPlayStrategies() {
    	ArrayList<DomPlayStrategy> theStrategies = new ArrayList<DomPlayStrategy>();
    	switch (this) {
		case Ambassador:
			theStrategies.add(DomPlayStrategy.aggressiveTrashing);
			break;

    	case Apothecary:
			theStrategies.add(DomPlayStrategy.ApothecaryNativeVillage);
			break;

		case Chapel:
			theStrategies.add(DomPlayStrategy.aggressiveTrashing);
			break;

		case Governor:
			theStrategies.add(DomPlayStrategy.GoldEarlyTrashMid);
			break;

		case Native_Village:
			theStrategies.add(DomPlayStrategy.bigTurnBridge);
			theStrategies.add(DomPlayStrategy.bigTurnGoons);
			theStrategies.add(DomPlayStrategy.ApothecaryNativeVillage);
			break;

		case Pirate_Ship:
			theStrategies.add(DomPlayStrategy.attackUntil5Coins);
			break;

		case Smithy:
			theStrategies.add(DomPlayStrategy.playIfNotBuyingTopCard);
			break;

		case Spice_Merchant:
			theStrategies.add(DomPlayStrategy.FoolsGoldEnabler);
			break;

		case Tactician:
			theStrategies.add(DomPlayStrategy.playIfNotBuyingTopCard);
			break;

		default:
			break;
		}
		theStrategies.add(0,DomPlayStrategy.standard);
		return theStrategies.toArray();
    }

    /**
     * @return
     */
    public int getVictoryValue(DomPlayer aPlayer) {
      switch (this) {
		case Silk_Road:
	  	  if (aPlayer != null ) {
	 	      return aPlayer.count(DomCardType.Victory)/4;
		  }
	      break;

		case Gardens:
	  	  if (aPlayer != null ) {
	 	      return aPlayer.countAllCards()/10;
		  }
	      break;

		case Duke:
	  	  if (aPlayer != null ) {
	 	    return aPlayer.countInDeck(Duchy);
		  }
	      break;

		case Fairgrounds:
			if (aPlayer!=null) {
		      return aPlayer.countDifferentCardsInDeck()/5*2;
			}
		    break;

		case Vineyard:
			if (aPlayer!=null) {
		      return aPlayer.count(DomCardType.Action)/3;
			}
		    break;

		default:
	    	break;
	  }
      return victoryValue;
    }
    
    /**
     * @return
     */
    public int getCoinValue() {
      return coinValue;
    }

    /**
     * @param aActionsLeft 
     * @return
     */
    public int getPlayPriority() {
      return playPriority;
    }

    /**
     * @param aActionsLeft 
     * @return
     */
    public int getDiscardPriority(int aActionsLeft) {
      //TODO to review (warehouse draws village + Terminal...)
      if (aActionsLeft<1 && hasCardType( DomCardType.Action ) )
        return 1;
      return discardPriority;
    }

    /**
     * @param aDomPlayer 
     * @return
     */
    public int getTrashPriority() {
      return getDiscardPriority( 1 );
    }
    
    public String toString(){
      String theString = super.toString().replaceAll( "_", " " ).replaceAll( "\\$", "'" );
      if (this==DomCardName.Ill_Gotten_Gains)
    	  return "Ill-Gotten Gains";
	  return theString;
    }

    public final int getCoinCost( DomGame aDomGame) {
        int theCoins = getCost().getCoins();
        if (aDomGame!=null) {
          if (this==DomCardName.Peddler && aDomGame.isBuyPhase()) {
            theCoins-=aDomGame.countActionsInPlay()*2;
          }
          theCoins-=aDomGame.getBridgesPlayed();
          theCoins-=aDomGame.getPrincessesInPlay()*2;
          if (hasCardType(DomCardType.Action))
            theCoins-=aDomGame.getQuarriesPlayed()*2;
          theCoins-=aDomGame.getHighwaysInPlay();
        }
        return theCoins<0 ? 0 : theCoins;
    }

    /**
     * @param aDomPlayer
     * @return
     */
    public final DomCost getCost( DomGame aDomGame ) {
      return new DomCost( getCoinCost( aDomGame ), getPotionCost() );
    }

    /**
     * @return
     */
    public final int getPotionCost() {
      return getCost().getPotions();
    }

	public String getImageLocation() {
      StringBuilder theLocation = new StringBuilder();
      theLocation.append("images/");
      theLocation.append(super.toString()).append(".jpg");
      return theLocation.toString().toLowerCase().replaceAll( "_", "" ).replaceAll( "\\$", "" );
	}

	public int getTrashPriority(DomPlayer player) {
		//trash priorities depend on the owner of the card which is unknown in this enum
		//so we quickly make a DomCard object and assign it to the player 
		//this way we get a correct trash priority for that player
		DomCard theIntermediateCard = createNewCardInstance();
		theIntermediateCard.owner=player.getPossessor()==null ? player : player.getPossessor();
		return theIntermediateCard.getTrashPriority();
	}

	public static Object[] getPossibleBaneCards() {
	    ArrayList<DomCardName> possibleBanes = new ArrayList<DomCardName>();
		for (DomCardName cardName : values()) {
			if (cardName.getCost().compareTo(new DomCost(2, 0))==0
			 || cardName.getCost().compareTo(new DomCost(3, 0))==0){
				if (cardName.hasCardType(DomCardType.Kingdom))
			  	  possibleBanes.add(cardName);
			}
		}
		return possibleBanes.toArray();
	}

	public String toHTML() {
      String theString = toString();
   	  if (hasCardType(DomCardType.Curse))
        return "<FONT style=\"BACKGROUND-COLOR: #CC8BC7\">" + theString + "</FONT>";
   	  if (hasCardType(DomCardType.Treasure) && hasCardType(DomCardType.Victory))
      	return "<FONT style=\"BACKGROUND-COLOR: #A9E96E\">" + theString + "</FONT>";
   	  if (hasCardType(DomCardType.Action) && hasCardType(DomCardType.Victory))
        return "<FONT style=\"BACKGROUND-COLOR: #6EE9C2\">" + theString + "</FONT>";
   	  if (hasCardType(DomCardType.Reaction) && hasCardType(DomCardType.Treasure))
          return "<FONT style=\"BACKGROUND-COLOR: #CCFF99\">" + theString + "</FONT>";
   	  if (hasCardType(DomCardType.Reaction))
      	return "<FONT style=\"BACKGROUND-COLOR: #91A4FC\">" + theString + "</FONT>";
   	  if (hasCardType(DomCardType.Treasure))
      	return "<FONT style=\"BACKGROUND-COLOR: #F3F584\">" + theString + "</FONT>";
   	  if (hasCardType(DomCardType.Victory))
   		return "<FONT style=\"BACKGROUND-COLOR: #8EBF75\">" + theString + "</FONT>";
  	  if (hasCardType(DomCardType.Duration))
  	    return "<FONT style=\"BACKGROUND-COLOR: #F88017\">" + theString + "</FONT>";
   	  if (hasCardType(DomCardType.Action))
        return "<FONT style=\"BACKGROUND-COLOR: #D9D9D9 \">" + theString + "</FONT>";
	  return theString;
	}
	
	public DomSet getSet() {
		for (DomSet set : DomSet.values()){
			if (set.contains(this))
				return set;
		}
		return null;
	}

	public URL getImageURL() {
//		return null;
		//TODO this should be called in some other way
		return new EscapeDialog().getClass().getResource(getImageLocation());
	}
	public String getCompleteImageLocation() {
//		return null;
		//TODO this should be called in some other way
//		return new EscapeDialog().getClass().getResource(getImageLocation());
		return  "C:/Documents and Settings/djag492/My Documents/Jeroen/Dominion/" + getImageLocation();
//		return  "C:/Users/MEDION/Pictures/" + getImageLocation();
	}

	public static Object[] getKingdomCards() {
		ArrayList<DomCardName> theCards = new ArrayList<DomCardName>(); 
		for (DomCardName cardName : values()){
			if (!DomSet.Base.contains(cardName))
				theCards.add(cardName);
		}
		return theCards.toArray();
	}

	public int getOrderInBuyRules(DomPlayer owner) {
		int i=0;
		if (owner==null)
			return 1000;
		for (DomBuyRule rule : owner.getBuyRules()){
		  if (rule.getCardToBuy()==this && owner.wantsToGainOrKeep(this)){
		     return i;	  
		  }
		  i++;
		}
		return 1000;
	}
}