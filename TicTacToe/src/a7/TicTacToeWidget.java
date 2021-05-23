package a7;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToeWidget extends JPanel implements ActionListener, SpotListener {

	/* Enum to identify player. */
	
	private enum Player {BLACK, WHITE};
	
	private JSpotBoard _board;		/* SpotBoard playing area. */
	private JLabel _message;		/* Label for messages. */
	private boolean _game_won;		/* Indicates if games was been won already.*/
	private boolean _game_draw;		/* Indicates if games was been drawn.*/
	private Spot _secret_spot;		/* Secret spot which wins the game. */
	private Color _secret_spot_bg;  /* Needed to reset the background of the secret spot. */
	private Player _next_to_play;	/* Identifies who has next turn. */
	
	public TicTacToeWidget() {
		
		/* Create SpotBoard and message label. */
		
		_board = new JSpotBoard(3,3);
		_message = new JLabel();
		
		/* Set layout and place SpotBoard at center. */
		
		setLayout(new BorderLayout());
		add(_board, BorderLayout.CENTER);

		/* Create subpanel for message area and reset button. */
		
		JPanel reset_message_panel = new JPanel();
		reset_message_panel.setLayout(new BorderLayout());

		/* Reset button. Add ourselves as the action listener. */
		
		JButton reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_message_panel.add(reset_button, BorderLayout.EAST);
		reset_message_panel.add(_message, BorderLayout.CENTER);

		/* Add subpanel in south area of layout. */
		
		add(reset_message_panel, BorderLayout.SOUTH);

		/* Add ourselves as a spot listener for all of the
		 * spots on the spot board.
		 */
		_board.addSpotListener(this);

		/* Reset game. */
		resetGame();
	}

	/* resetGame
	 * 
	 * Resets the game by clearing all the spots on the board,
	 * picking a new secret spot, resetting game status fields, 
	 * and displaying start message.
	 * 
	 */

	private void resetGame() {
		/* Clear all spots on board. Uses the fact that SpotBoard
		 * implements Iterable<Spot> to do this in a for-each loop.
		 */

		for (Spot s : _board) {
			s.clearSpot();
			s.setSpotColor(Color.BLACK); //empty
		}
			
		
		/* Reset game won 
		 * displays the next game 
		 * who starts the game */
		
		_game_won = false;
		_game_draw = false;
		_next_to_play = Player.WHITE;		
		
		/* Display game start message. */
	
		
		_message.setText("Welcome to TicTacToe. White to play");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* Handles reset game button. Simply reset the game. */
				resetGame();
	}

	/* Implementation of SpotListener below. Implements game
	 * logic as responses to enter/exit/click on spots.
	 */
	
	@Override
	public void spotClicked(Spot s) {
		
		/* If game already won, do nothing. */
		if (_game_won) {
			return;
		}


		/* Set up player and next player name strings,
		 * and player color as local variables to
		 * be used later.
		 */
		
		String player_name = null;
		String next_player_name = null;
		Color player_color = null;
		
		if (_next_to_play == Player.BLACK) {
			player_color = Color.BLACK;
			player_name = "Black";
			next_player_name = "White";
			_next_to_play = Player.WHITE;
		} else {
			player_color = Color.WHITE;
			player_name = "White";
			next_player_name = "Black";
			_next_to_play = Player.BLACK;			
		}
				
		
		/* Set color of spot clicked and toggle. */
		if (s.isEmpty()) {
			s.setSpotColor(player_color);
			s.toggleSpot();
			
		} else {
			
			if (_next_to_play == Player.BLACK) {
				player_color = Color.WHITE;
				player_name = "White";
				next_player_name = "White";
				_next_to_play = Player.WHITE;
				
			} else {
				
				player_color = Color.BLACK;
				player_name = "Black";
				next_player_name = "Black";
				_next_to_play = Player.BLACK;			
				
			}
		}
		
		System.out.println("Win Checker " + checkWin());

		/* Check if spot clicked is secret spot.
		 * If so, mark game as won and update background
		 * of spot to show that it was the secret spot.
		 */
		

		/* Update the message depending on what happened.
		 * If spot is empty, then we must have just cleared the spot. Update message accordingly.
		 * If spot is not empty and the game is won, we must have
		 * just won. Calculate score and display as part of game won message.
		 * If spot is not empty and the game is not won, update message to
		 * report spot coordinates and indicate whose turn is next.
		 */
		
		if (s.isEmpty()) {
			_message.setText(next_player_name + " to play.");
		} else {
			if (_game_won)  {

				_message.setText(player_name + " wins! ");
			} else {
				
				if (checkDraw() && (_game_won == false))  {

					_message.setText(" Draw game.");
					
				} else {
				
				_message.setText(next_player_name + " to play.");
				}
			}
		}
	}

	
	
	@Override

	public void spotEntered(Spot s) {
		/* Highlight spot if game still going on. */
		
		if (_game_won) {
			return;
		}
		s.highlightSpot();
	}

	@Override
	public void spotExited(Spot s) {
		/* Unhighlight spot. */
		
		s.unhighlightSpot();
	}

//Tic tac toe logic
	public boolean checkWin() {
		
		if (_game_won) {
			return true;
		}
		
		int countBlacks = 0;
		int countWhites = 0;
		
		
		for (int x = 0; x < 3; x++) {
			
			countBlacks = 0;
			countWhites = 0;
			
			for (int y = 0; y < 3; y++) {

			
				if (_board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
					
					
					countWhites++;
				}
				
				else if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK && (_board.getSpotAt(x, y).isEmpty() == false)) {
					
					countBlacks++;
				}
				
				
				
			}
			
	// 	Checks straight line 		
			
			System.out.println("Blacks " + countBlacks);
			System.out.println("Whites " + countWhites);
			
			
			if (countWhites == 3 || countBlacks == 3) {
				System.out.println("Straight Line, Whites = " + countWhites
						+ " Blacks: " + countBlacks);
				_game_won = true;

				return true;
			
				
			}

		}
		
		countBlacks = 0;
		countWhites = 0;
		
		
		for (int y = 0; y < 3; y++) {
			
			countBlacks = 0;
			countWhites = 0;

			for (int x = 0; x < 3; x++) {
				
				if (_board.getSpotAt(x, y).getSpotColor() == Color.WHITE  && (_board.getSpotAt(x, y).isEmpty() == false)) {
				
					countWhites++;
				}
				
				else if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK && (_board.getSpotAt(x, y).isEmpty() == false)) {
					
					countBlacks++;
				}
				

				
				
			}
			
			
	// checks straight down		
			System.out.println("Blacks " + countBlacks);
			System.out.println("Whites " + countWhites);
			
			
			if (countWhites == 3 || countBlacks == 3) {
				
				_game_won = true;

				return true;
				
				//break;
				
			}
			
			
		
		}
		
		countBlacks = 0;
		countWhites = 0;
	

		for (int i = 0, j = 2; i < 3; i++, j--) {
			
			

			
			if (_board.getSpotAt(i, j).getSpotColor() == Color.WHITE  && (_board.getSpotAt(i, j).isEmpty() == false)) {
				
				countWhites++;
			}
			
			else if (_board.getSpotAt(i, j).getSpotColor() == Color.BLACK && (_board.getSpotAt(i, j).isEmpty() == false)) {
				countBlacks++;
			}
		}
		// diagonal right
		if (countWhites == 3 || countBlacks == 3) {
			
			
			_game_won = true;
			return true;
		} 
		
		countBlacks = 0;
		countWhites = 0;
		
		
		for (int i = 3 - 1, j = 3 - 1; i >= 0; i--, j--) {
			if (_board.getSpotAt(i, j).getSpotColor() == Color.WHITE  && (_board.getSpotAt(i, j).isEmpty() == false)) {
				countWhites++;
			}
			else if (_board.getSpotAt(i, j).getSpotColor() == Color.BLACK && (_board.getSpotAt(i, j).isEmpty() == false)) {
				countBlacks++;
		}
		}
		// // check diagonal right
		if (countBlacks == 3 || countWhites == 3) {
			
			_game_won = true;
			return true;
		}
		
		return false;

	}
	



public boolean checkDraw() {
	int countBlacks = 0;
	int countWhites = 0;
	
	for (int x = 0; x < 3; x++) {
		
		for (int y = 0; y < 3; y++) {
			
			if (_board.getSpotAt(x, y).getSpotColor() == Color.WHITE) {
				countWhites++;
			}
			
			else if (_board.getSpotAt(x, y).getSpotColor() == Color.BLACK && (_board.getSpotAt(x, y).isEmpty() == false)) {
				
				countBlacks++;
			}
			
		}
		
		System.out.println("Blacks " + countBlacks);
		System.out.println("Whites " + countWhites);
		
	// draw	
		if (countWhites == 5 && countBlacks == 4) {

			_game_draw = true;
			_game_won = false;

			return true;
			
			
		}
		
		
		
	
	}
	return false;
	

}

}