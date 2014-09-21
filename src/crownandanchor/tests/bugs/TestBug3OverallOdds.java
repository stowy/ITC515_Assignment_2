package crownandanchor.tests.bugs;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import crownandanchor.Dice;
import crownandanchor.DiceValue;
import crownandanchor.Game;
import crownandanchor.Player;

public class TestBug3OverallOdds {

	private static final String NAME = "Sam";
	private static final int STARTING_BALANCE = 10;
	private static final int BETTING_LIMIT = 0;
	private static final DiceValue PICK_CROWN = DiceValue.CROWN;
	private static final int BET_AMOUNT = 5;
	private static final int MAX_BALANCE = 300;
	private static final int MAX_TURNS = 200;
	
	private static final double MAX_WIN_LOSS_RATIO = 0.43;
	private static final double MIN_WIN_LOSS_RATIO = 0.41;
	
	private Player player;
	private Dice d1;
	private Dice d2;
	private Dice d3;
	private Game game;
	
	@Before
	public void setUp() throws Exception {
		//Create dice
		d1 = new Dice();
		d2 = new Dice();
		d3 = new Dice();
		
	}

	@After
	public void tearDown() throws Exception {
		player = null;
		d1 = null;
		d2 = null;
		d3 = null;
		game = null;
	}

	@Test
	public void test() {
		//Create game
		game = new Game(d1, d2, d3);
		
		//Play round
		int winCount = 0;
		int lossCount = 0;
		
		int numGames = 100;
		
		for (int gameNo = 1; gameNo <= numGames; gameNo++) {
			int turn = 0;
			//Create player
			player = new Player(NAME, STARTING_BALANCE);
			player.setLimit(BETTING_LIMIT);
			
			do {
				System.out.printf("Turn %d: %s bet %d on %s\n",
	        			turn, player.getName(), BET_AMOUNT, PICK_CROWN); 
	        	
	        	int winnings = game.playRound(player, PICK_CROWN, BET_AMOUNT);
	            List<DiceValue> cdv = game.getDiceValues();
	            
	            System.out.printf("Rolled %s, %s, %s\n",
	            		cdv.get(0), cdv.get(1), cdv.get(2));
	            
	            if (winnings > 0) {
	                System.out.printf("%s won %d, balance now %d\n\n",
	                		player.getName(), winnings, player.getBalance());
	                winCount++;
	            }
	            else {
	                System.out.printf("%s lost, balance now %d\n\n",
	                		player.getName(), player.getBalance());
	                lossCount++;
	            }
	            turn++;
	            
			} while (player.balanceExceedsLimitBy(BET_AMOUNT) && player.getBalance() <= MAX_BALANCE && turn <= MAX_TURNS);
			
			player = null;
		}
		
		double winLossRatio = (winCount * 1.0)/(lossCount*1.0);
		assertTrue(String.format("Win loss ratio too great: %.2f", winLossRatio), winLossRatio <= MAX_WIN_LOSS_RATIO);
		assertTrue(String.format("Win loss ratio too little: %.2f", winLossRatio), winLossRatio >= MIN_WIN_LOSS_RATIO);
		
		//Check result
		
	}
	

}
