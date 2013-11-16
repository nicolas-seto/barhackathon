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

	public PlayerMessage handleMessage(Message message) {
		if (message.type.equals("request")) {
			MoveMessage m = (MoveMessage)message;
			//System.out.println(m.toString());
			if (game_id != m.state.game_id) {
				game_id = m.state.game_id;
				System.out.println("new game " + game_id);
			}

			if (m.request.equals("request_card")) {
			    /* We are first to play and can play card or issue challenge*/
				if (m.state.can_challenge) {
				   
				}
				else { /* We are second to play and can only play card */
				    int indextie = -1;
				    int minWinIndex = -1;
				    int minLoseIndex = 0;

                    System.out.println("Their hand: " + m.state.card);
                    int[] currentHand = m.state.hand;
                    Arrays.sort(currentHand);

                    for(int i = 0; i < currentHand.length; i++) {
                        int difference = currentHand[i] - m.state.card;
                        
                        if (difference == 0) {
                            /* Stores index */
                            indextie = i;
                        }
                        if (difference > 0) {
                            /* The first card we have that is higher, we play it */
                            return new PlayCardMessage(m.request_id,m.state.hand[i]);
                        } else if (i == currentHand.length - 1) {
                            /* All our cards are lower or can tie, just play lowest card because the highest of our lowest can still win
                             * further hands
                             */
                            return new PlayCardMessage(m.request_id,m.state.hand[0]);
                        }
                    }
                    
				}
			}
			else if (m.request.equals("challenge_offered")) {
				return (Math.random() < 0.5)
						? new AcceptChallengeMessage(m.request_id)
						: new RejectChallengeMessage(m.request_id);
			}
		}
		else if (message.type.equals("result")) {
			ResultMessage r = (ResultMessage)message;
			System.out.println(r.toString());
		}
		else if (message.type.equals("error")) {
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
