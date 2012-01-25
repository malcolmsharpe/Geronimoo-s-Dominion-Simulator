package be.aga.dominionSimulator.gui;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomPlayer;

/**
 */
public class DomBarChart {
	private ArrayList<DomPlayer> players;
    private ChartPanel myChartPanel;
    private DefaultCategoryDataset dataset;

    /**
     * @param players 
     * @param aDomPlayer 
     */
    public DomBarChart (ArrayList<DomPlayer> aPlayers) {
    	players = aPlayers;
        dataset = createBarDataset();
        JFreeChart chart = createChart(dataset);
        myChartPanel = new ChartPanel(chart);
    }

    private JFreeChart createChart(DefaultCategoryDataset defaultCategoryDataset) {
        String theTitle = "";
        if (!players.isEmpty())            
          theTitle = "Results (Average #turns = " + (players.get(0).getSumTurns()/DomEngine.NUMBER_OF_GAMES) + ")"; 

        final JFreeChart chart = ChartFactory.createBarChart3D(
             theTitle,  // chart title
             "",
             "",
             defaultCategoryDataset,           // data
             PlotOrientation.VERTICAL,
             true,             // include legend
             true,
             false
         );
        return chart;
   }

	/**
     * @return
     */
    private DefaultCategoryDataset createBarDataset() {
        DefaultCategoryDataset theDataset = new DefaultCategoryDataset();
        for (DomPlayer thePlayer : players) {
          String theLabel = thePlayer.toString() + " ("+ (thePlayer.getWins()*100/DomEngine.NUMBER_OF_GAMES) + "%)";
          theDataset.addValue( thePlayer.getWins()/DomEngine.NUMBER_OF_GAMES, theLabel, "");
        }
        if (!players.isEmpty()) {
          String theLabel = "Ties ("+ (players.get(0).getTies()*100/DomEngine.NUMBER_OF_GAMES) + "%)";
          theDataset.addValue( players.get( 0 ).getTies()/DomEngine.NUMBER_OF_GAMES, theLabel, "");
        }
        return theDataset;
    }

	/**
     * @return
     */
    public ChartPanel getChartPanel() {
       int theHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
       int theWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
//       myChartPanel.setPreferredSize( new Dimension(theWidth*10/28,theHeight*10/32) );
       return myChartPanel;
    }
}