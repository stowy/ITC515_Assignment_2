package crownandanchor.tests.bugs;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import crownandanchor.Dice;
import crownandanchor.DiceValue;
import crownandanchor.Game;
import crownandanchor.Player;

public class TestBug2BettingLimit {

	private static final String NAME = "Sam";
	private static final int STARTING_BALANCE = 10;
	private static final int BETTING_LIMIT = 0;
	private static final DiceValue PICK_CROWN = DiceValue.CROWN;
	private static final int BET_AMOUNT = 5;
	private static final int MAX_BALANCE = 300;
	private static final int MAX_TURNS = 1500;
	
	private Player player;
	private Dice d1;
	private Dice d2;
	private Dice d3;
	private Game game;
	
	@Before
	public void setUp() throws Exception {
		//Create player
		player = new Player(NAME, STARTING_BALANCE);
		player.setLimit(BETTING_LIMIT);
		//Create dice
		d1 = EasyMock.createMock(Dice.class);
		d2 = EasyMock.createMock(Dice.class);
		d3 = EasyMock.createMock(Dice.class);
		
		//Setup losing result to ensure we go below betting limit
		expect(d1.roll()).andReturn(DiceValue.ANCHOR).atLeastOnce();
		expect(d1.getValue()).andReturn(DiceValue.ANCHOR).atLeastOnce();
		
		expect(d2.roll()).andReturn(DiceValue.ANCHOR).atLeastOnce();
		expect(d2.getValue()).andReturn(DiceValue.ANCHOR).atLeastOnce();
		
		expect(d3.roll()).andReturn(DiceValue.ANCHOR).atLeastOnce();
		expect(d3.getValue()).andReturn(DiceValue.ANCHOR).atLeastOnce();
		
		replay(d1);
		replay(d2);
		replay(d3);
		
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
		int turn = 0;
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
            }
            else {
                System.out.printf("%s lost, balance now %d\n\n",
                		player.getName(), player.getBalance());
            }
            turn++;
            
		} while (player.balanceExceedsLimitBy(BET_AMOUNT) && player.getBalance() <= MAX_BALANCE && turn <= MAX_TURNS);
		
		verify(d1);
		verify(d2);
		verify(d3);
		
		//Check result
		assertEquals(BETTING_LIMIT, player.getBalance());
	}
	

}
