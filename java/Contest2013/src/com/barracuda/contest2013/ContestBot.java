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
	private RemainingCardStats load;

	public ContestBot(String host, int port) {
		this.host = host;
		this.port = port;
		this.load = new RemainingCardStats();
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
	
	public PlayerMessage handleMessage(Message message) {
	    
	    
	    
		if (message.type.equals("request")) {
			MoveMessage m = (MoveMessage)message;
			//System.out.println(m.toString());
			if (game_id != m.state.game_id) {
				game_id = m.state.game_id;
				System.out.println("new game " + game_id);
			}

			if (m.state.hand_id % 10 == 1) {
	            load.reshuffle();
	        }
			
			int[] currentHand = m.state.hand;
            Arrays.sort(currentHand);
            
            boolean accept = ChallengeHelper.isChallengeAccepted(currentHand, m.state.your_tricks, m.state.their_tricks, m.state.your_points, m.state.their_points, m.state.card);
            
            /* It is our turn */
			if (m.request.equals("request_card")) {
			    
			    /* If our hand has 5 cards, decrement cards from remaining */
			    if (currentHand.length == 5) {
			        for (int i = 0; i < 5; i++) {
			            load.decrement(currentHand[i]);
			        }
			    }
			    
			    accept = ChallengeHelper.issueChallenge(currentHand, m.state.your_tricks, m.state.their_tricks, m.state.your_points, m.state.their_points, m.state.card);
			    
			    /* We are first to play */
				if (m.state.card == 0) {
				    /* No one has challenged yet and we can challenge */
				    if (m.state.can_challenge) {
				        /* If we have 3 tricks or more */
    				    if (m.state.your_tricks >= 3 || accept || (m.state.hand_id % 10 > 0 && m.state.hand_id % 10 < 4 && load.getHighPercentage() > .6 && m.state.total_tricks == 0)) { 
    				        return new OfferChallengeMessage(m.request_id);
    				    }
    				    if (currentHand.length % 2 == 1) { /* Pick middle */
    				        return new PlayCardMessage(m.request_id, currentHand[currentHand.length / 2]);
    				    } else if (currentHand.length % 2 == 0) { /* Card is even, then pick the lower half */
    				        return new PlayCardMessage(m.request_id, currentHand[(currentHand.length / 2) - 1]);
    				    }
				    } else { /* A challenge has been offered and we're playing */
				        if (currentHand.length % 2 == 1) {
                            return new PlayCardMessage(m.request_id, currentHand[currentHand.length / 2]);
                        } else if (currentHand.length % 2 == 0) {
                            return new PlayCardMessage(m.request_id, currentHand[(currentHand.length / 2) - 1]);
                        }
				    }
				}
				else { /* We are second to play */
				    int indexTie = -1;
				    int minWinIndex = -1;

                    System.out.println("Their hand: " + m.state.card);
                    load.decrement(m.state.card);
                    accept = ChallengeHelper.issueChallenge(currentHand, m.state.your_tricks, m.state.their_tricks, m.state.your_points, m.state.their_points, m.state.card);
                    if (m.state.can_challenge) {
                        if (m.state.your_tricks >= 3 || accept || (m.state.hand_id % 10 > 0 && m.state.hand_id % 10 < 4 && load.getHighPercentage() > .6 && m.state.total_tricks == 0)) { 
                            return new OfferChallengeMessage(m.request_id);
                        }
                    }
                    
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
                                    return new PlayCardMessage(m.request_id, currentHand[indexTie]);
                                } else { //edit later: maybe add more conditions
                                    return new PlayCardMessage(m.request_id, currentHand[0]);
                                }
                            } else { /* We can't tie, so play the lowest card */
                                return new PlayCardMessage(m.request_id, currentHand[0]);
                            }
                        }
                    }
                    
                    int lowestWin = currentHand[minWinIndex];
                    /* If we reach here, that means that we have at least one card that can beat the opponent's card. */
                    /* We can lose, tie, and win */
                    if (m.state.their_tricks == 2) {
                        return new PlayCardMessage(m.request_id, lowestWin);
                    }
                    /* If the card is within a difference of 3, beat it */
                    if (lowestWin - m.state.card <= 3) {
                        return new PlayCardMessage(m.request_id, lowestWin);
                    /* If our next highest winning card is much larger than opponent's card, drop lowest card */
                    } else if (lowestWin - m.state.card >= 4) {
                        return new PlayCardMessage(m.request_id, currentHand[0]);
                    }
                    return new PlayCardMessage(m.request_id, lowestWin);
				}
			} else if (m.request.equals("challenge_offered")) {
			    
			    if (m.state.their_tricks > m.state.your_tricks) { // they have more tricks than us
			        if (m.state.their_tricks >= 3){ // if they have 3+ tricks already, it's automatic loss in point
	                    return new RejectChallengeMessage(m.request_id);
	                } else { // edit later: check our current hand
	                    if(accept || load.getHighPercentage() < .45){
	                        return new AcceptChallengeMessage(m.request_id);
	                    }
	                    return new RejectChallengeMessage(m.request_id);
	                }
			    } else if (m.state.their_tricks < m.state.your_tricks) { // we have more tricks than them
			        if (m.state.your_tricks >= 3){ // if we have 3+ tricks already, this is an instant win
	                    return new AcceptChallengeMessage(m.request_id);
	                } else { // edit later: check our current hand
	                    if (m.state.your_points == 9){
	                        if (accept || load.getHighPercentage() < .45) {
	                            return new AcceptChallengeMessage(m.request_id);
	                        }
	                    } else if(accept){
	                        return new AcceptChallengeMessage(m.request_id);
	                    }
	                    return new RejectChallengeMessage(m.request_id);
	                }
			    } else { // we are tied in tricks
			        if (accept || load.getHighPercentage() < .45) {
			            return new AcceptChallengeMessage(m.request_id);
			        }
                    return new RejectChallengeMessage(m.request_id);
                }
            }
		} else if (message.type.equals("result")) {
			ResultMessage r = (ResultMessage) message;
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
