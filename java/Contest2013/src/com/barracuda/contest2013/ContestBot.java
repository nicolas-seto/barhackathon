/**
 * sample Java implementation for 2013 Barracuda Networks Programming Contest
 *
 */
package com.barracuda.contest2013;

import java.io.IOException;
import java.util.Arrays;

public class ContestBot {
	private static final int RECONNECT_TIMEOUT = 15; // seconds

	private final String host;
	private final int port;
	private int game_id = -1;

	public ContestBot(String host, int port) {
		this.host = host;
		this.port = port;
	}

	private void run() {
		while (true) {
			// just reconnect upon any failure
			try {
				JsonSocket sock = new JsonSocket(host, port);
				try {
					sock.connect();
				}
				catch (IOException e) {
					throw new Exception("Error establishing connection to server: " + e.toString());
				}

				while (true) {
					Message message = sock.getMessage();
					PlayerMessage response = handleMessage(message);
		
					if (response != null) {
						sock.sendMessage(response);
					}
				}
			}
			catch (Exception e) {
				System.err.println("Error: " + e.toString());
				System.err.println("Reconnecting in " + RECONNECT_TIMEOUT + "s");
				try {
					Thread.sleep(RECONNECT_TIMEOUT * 1000);
				}
				catch (InterruptedException ex) {}
			}
		}
	}

	/** Return the category of which this card is in.  
	 *  0 = low
	 *  1 = medium
	 *  2 = high
	 */
	public int returnCategory(int value) {
	    if (value < 5) {
	        return 0;
	    } else if (5 <= value && value < 9) {
	        return 1;
	    } else {
	        return 2;
	    }
	}
	
	public PlayerMessage handleMessage(Message message) {
		if (message.type.equals("request")) {
			MoveMessage m = (MoveMessage)message;
			//System.out.println(m.toString());
			if (game_id != m.state.game_id) {
				game_id = m.state.game_id;
				System.out.println("new game " + game_id);
			}

			if (m.request.equals("request_card")) {
			    
			    int[] currentHand = m.state.hand;
                Arrays.sort(currentHand);
                
			    /* We are first to play and can play card or issue challenge*/
				if (m.state.can_challenge) {
				    if (m.state.your_tricks >= 3) {
				        return new OfferChallengeMessage(m.request_id);
				    } else {
				        return new PlayCardMessage(m.request_id, m.state.hand[currentHand.length - 1]);
				    }
				}
				else { /* We are second to play and can only play card */
				    int indexTie = -1;
				    int minWinIndex = -1;

                    System.out.println("Their hand: " + m.state.card);
                    

                    for(int i = 0; i < currentHand.length; i++) {
                        int difference = currentHand[i] - m.state.card;
                        
                        if (difference == 0) {
                            /* Stores index */
                            indexTie = i;
                        }
                        if (difference > 0) {
                            /* The first card we have that is higher, we save the index and break */
                            minWinIndex = i;
                            break;
                        } else if (i == currentHand.length - 1) {
                            /* All our cards are lower or can tie */
                            /* If we can tie */
                            if (indexTie != -1) {
                                /* If they have 2 tricks, we tie otherwise they would win */
                                if (m.state.their_tricks == 2) {
                                    return new PlayCardMessage(m.request_id, m.state.hand[indexTie]);
                                } else { //edit later
                                    return new PlayCardMessage(m.request_id, m.state.hand[0]);
                                }
                            } else { /* We can't tie, so play the lowest card */
                                return new PlayCardMessage(m.request_id,m.state.hand[0]);
                            }
                        }
                    }
                    
                    /* If we reach here, that means that we have at least one card that can beat the opponent's card. */
                    /* We can lose, tie, and win */
                    if (indexTie != -1) {
                        return new PlayCardMessage(m.request_id, m.state.hand[minWinIndex]);
                    } else { /* We can lose or win */
                        return new PlayCardMessage(m.request_id, m.state.hand[minWinIndex]);
                    }
				}
			} else if (m.request.equals("challenge_offered")) {
			    if (m.state.their_tricks > m.state.your_tricks) { // they have more tricks than us
			        if (m.state.their_tricks >= 3){ // if they have 3+ tricks already, it's automatic loss in point
	                    return new RejectChallengeMessage(m.request_id);
	                } else { // edit later: check our current hand
	                    return new RejectChallengeMessage(m.request_id);
	                }
			    } else if (m.state.their_tricks < m.state.your_tricks) { // we have more tricks than them
			        if (m.state.your_tricks >= 3){ // if we have 3+ tricks already, this is an instant win
	                    return new AcceptChallengeMessage(m.request_id);
	                } else { // edit later: check our current hand
	                    return new AcceptChallengeMessage(m.request_id);
	                }
			    } else { // we are tied in tricks
                    return new RejectChallengeMessage(m.request_id);
                    // edit later: check our current hand
                }
            }
		} else if (message.type.equals("result")) {
			ResultMessage r = (ResultMessage) message;
			System.out.println(r.toString());
		} else if (message.type.equals("error")) {
			ErrorMessage e = (ErrorMessage)message;
			System.err.println("Error: " + e.message);

			// need to register IP address on the contest server
			if (e.seen_host != null) {
				System.exit(1);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: java -jar ContestBot.jar <HOST> <PORT>");
			System.exit(1);
		}

		String host = args[0];
		Integer port = Integer.parseInt(args[1]);

		ContestBot cb = new ContestBot(host, port);
		cb.run();
	}
}
