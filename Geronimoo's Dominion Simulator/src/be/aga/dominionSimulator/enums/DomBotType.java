package be.aga.dominionSimulator.enums;

public enum DomBotType {
   Bot,
   Generated,
   UserCreated,
   BigMoney,
   Attacking,
   Engine,
   Combo, 
   SingleCard, 
   Province,
   Colony,
   TwoPlayer,
   ThreePlayer,
   FourPlayer,
   AnnotatedGames,
   Competitive,
   Casual,
   Fun,
   Optimized
   
   ;
   
   public String toString() {
       switch ( this ) {
       case SingleCard:
           return "Single Card";

       case Generated:
           return "Computer generated";

       case UserCreated:
           return "Created by user";

       case TwoPlayer:
           return "Two player";

       case ThreePlayer:
           return "Three player";

       case FourPlayer:
           return "Four player";

       case AnnotatedGames:
           return "Annotated Games";

       case BigMoney:
           return "Big Money";
           
       case Colony:
           return "Colony";

       case Combo:
           return "Combo";

       case Engine:
           return "Engine";

    default :
        return super.toString();
    }
       
   };
   
}
