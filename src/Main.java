import java.util.Random;
import java.util.Scanner;

/**
 * 
 * AI for Games
 * 
 * @author Laurence Budd
 *
 */

public class Main {

	static boolean statChange = false;
	static boolean creatureChange = true;
	static boolean alive = true;
	static int turnCount = 0;
	static String charName;
	static int charSpeed;
	static int charWep;
	static int charInt;
	static int creatureSpeed;
	static int creatureWep;
	static int creatureInt;
	String[] creatureArray = { "Scared", "Curious", "Aggressive" };
	static String creatureState;
	int jetpackPow;
	int flashgunPow;
	int textbookPow;
	static int noOfJetpacks;
	static int noOfFlashguns;
	static int noOfTextbooks;
	static int specialCount;

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to the AI's World of Adventures! Please type a name and press enter to begin.");
		String name = scan.nextLine();

		Main m = new Main();
		m.adventurer(name);

		System.out.println("Welcome " + charName + ". You're first encounter shall commence shortly!");
		System.out.println("Remember your stats are as follows, Speed: " + charSpeed + ", Weapon: " + charWep
				+ ", Intelligence: " + charInt);
		System.out.println("");

		while (alive == true) { // When alive becomes false (when the ai dies), the game will stop running this
								// loop.
			m.gameFacts(); // Increases the turnCount / number of monster encounters survived statistic by
							// 1.
			m.creature(); // Randomises and generates a new creature.
			if (creatureChange == false) { // For when the creature is curious but the AI chooses to do nothing.
				System.out.println("The same monster is still watching you except now it is " + creatureState + "!");
			} else {
				System.out.println(
						"A monster is approaching! It is '" + creatureState + "' and has the following stats, Speed: "
								+ creatureSpeed + ", Weapon: " + creatureWep + ", Intelligence: " + creatureInt);
			}

			m.chooseAction(); // AI goes through the process of decision what to do.
			System.out.println("");

			if (statChange == true) { // If a special is used, this sets all AI stats back to normal.
				charWep = 2;
				charInt = 4;
				charSpeed = 3;
				statChange = false;
			}

		}
		System.out.println("You have died.");
		System.out.println("Unlucky " + charName + ", you survived encounters with " + (turnCount - 1) + " creatures.");
		// Tells the user the AI has finally died, how many turns it survived for (-1 as
		// the last turn is a loss and shouldn't increase the score)
		specialCount = noOfJetpacks + noOfFlashguns + noOfTextbooks;
		System.out.println("You had " + specialCount + " gadgets left."); // Outputs how many gadgets were available
																			// when the AI dies. Ideally this will
																			// always be 0 to show that it has
																			// effectively tried to survive for as long
																			// as possible.
	}

	public void creature() { // Generates the creatures.
		Random num = new Random();
		if (creatureChange == true) { // If the creature is curious and the AI does nothing, the stats do not change
										// as the creature doesn't leave but its temperament is still able to change or
										// stay the same.
			creatureSpeed = num.nextInt(5) + 1;
			creatureWep = num.nextInt(5) + 1;
			creatureInt = num.nextInt(5) + 1;
		}
		creatureState = creatureArray[num.nextInt(3)];
	}

	public void adventurer(String name) { // Where all the information about the AI's stats will be saved
		charName = name;
		charSpeed = 3;
		charWep = 2;
		charInt = 4;
		noOfJetpacks = 1; // The Quantity of them remaining.
		noOfFlashguns = 1;
		noOfTextbooks = 1;
		jetpackPow = 2; // The amount it increases the stat by
		flashgunPow = 2;
		textbookPow = 2;
	}

	public void gameFacts() {
		turnCount += 1;
		// holds the number of turns the user has played for.
	}

	public void chooseSpecial() { // This will work out what special ability is best to use against the current
									// creature
		String choice = "";
		int wepDifference = charWep - creatureWep;
		int intDifference = charInt - creatureInt;
		int speedDifference = charSpeed - creatureSpeed;
		boolean flashgunWin = false;
		boolean jetpackWin = false;
		boolean textbookWin = false;
		System.out.println("The AI has chosen to use a special power!");

		// checking to see which specials are available and also will provide a 100% win
		// chance
		if ((noOfFlashguns > 0) && ((wepDifference + flashgunPow >= 2) || (wepDifference + flashgunPow == 0))) {
			flashgunWin = true;
		}
		if ((noOfTextbooks > 0) && ((intDifference + textbookPow >= 2) || (intDifference + textbookPow == 0))) {
			textbookWin = true;
		}
		if ((noOfJetpacks > 0) && (speedDifference + jetpackPow >= 0)) {
			jetpackWin = true;
		}
		// Now depending on which specials will make sure the AI wins, the program will
		// then output to the user to see which ones are available.
		if (noOfJetpacks == 1 && jetpackWin == true) {
			System.out.println(
					"You have a jetpack available to use that will guarantee you a win by increasing your speed by +2 ");
		}
		if (noOfFlashguns == 1 && flashgunWin == true) {
			System.out.println(
					"You have a flashgun available to use that will guarantee you a win by increasing your attack by +2 ");
		}
		if (noOfTextbooks == 1 && textbookWin == true) {
			System.out.println(
					"You have a texbook available to use that will guarantee you a win by increasing your intelligence by +2");
		}

		// Depending on which gadgets are available the AI will use them in this order. 
		// This is because I determined the flashgun was worst, the textbook was in the middle and the jetpack is the best.
		if ((flashgunWin == true && noOfFlashguns > 0)) {
			choice = "flashgun";
		} else if ((textbookWin == true && noOfTextbooks > 0)) {
			choice = "textbook";
		}  else if (jetpackWin == true && noOfJetpacks >= 0) {
			choice = "jetpack";
		}

		if (choice.equals("jetpack") && noOfJetpacks > 0) {
			charSpeed += 2;
			noOfJetpacks--;
			statChange = true;
			System.out.println("You have chosen the jetpack! You will now flee from the monster!");
			this.resolveFlee();
		} else if (choice.equals("flashgun") && noOfFlashguns > 0) {
			charWep += 2;
			noOfFlashguns--;
			statChange = true;
			System.out.println("You have chosen the flashgun! You will now fight the monster!");
			this.resolveFight();
		} else if (choice.equals("textbook") && noOfTextbooks > 0) {
			charInt += 2;
			noOfTextbooks--;
			statChange = true;
			System.out.println("You have chosen the textbook! You will now attempt to hypnotise the monster!");
			this.resolveHypnotise();
		}
	}

	public void chooseAction() { // This method decides what action is best for the AI to do.
		if (creatureState.equals(creatureArray[0])) { // For a scared creature, the AI should always 'do nothing' as its
														// the safest option. Although it is possible to make the AI
														// choose to fight some creatures that are scared if there is a
														// 100% chance of winning, but that wouldn't change the outcome
														// scores at all.
			creatureChange = true;
			this.doNothing();
		}

		if (creatureState.equals(creatureArray[1])) { // The selection of decisions available if the creatures
														// temperament is of curious.
			int wepDifference = charWep - creatureWep;
			int intDifference = charInt - creatureInt;
			int speedDifference = charSpeed - creatureSpeed;

			if ((wepDifference >= 2) || (wepDifference == 0)) { // 100% chance user victory (AI wins or creature runs
																// away for weapon fight)
				creatureChange = true;
				this.resolveFight();
			} else if ((intDifference >= 2) || (intDifference == 0)) { // 100% chance user victory (AI wins or creature
																		// runs away for intelligence hypnotising)
				creatureChange = true;
				this.resolveHypnotise();
			} else if (speedDifference >= 0) { // 100% chance user victory (AI will be able to successfull flee)
				creatureChange = true;
				this.resolveFlee();
			} 
			
//			After testing I found that removing this code makes the ai more efficient
//			
//			else if ((noOfFlashguns > 0) 
//					&& ((wepDifference + flashgunPow >= 2) || (wepDifference + flashgunPow == 0))) {
			// If none of the above options are possible then the ai should try to use a special.
//				this.chooseSpecial();
//				statChange = true;
//				creatureChange = true;
//			} else if ((noOfTextbooks > 0) // This code is replicated in the chooseSpecial() method to help chose which
//					&& ((intDifference + textbookPow >= 2) || (intDifference + textbookPow == 0))) { // is the right
//																										// special to
//																										// use.
//				this.chooseSpecial();
//				statChange = true;
//				creatureChange = true;
//			} else if ((noOfJetpacks > 0) && (speedDifference + jetpackPow > 0)) {
//				this.chooseSpecial();
//				statChange = true;
//				creatureChange = true;
//			} 
			
			
			else if (wepDifference == 1 || intDifference == 1 || wepDifference == -1 || intDifference == -1) { 
				// With the curious monster if the AI cannot kill it
				// with 100% success rate then it will 'do nothing'
				// until its temperament changes and the AI is
				// forced to attack or the creature becomes scared
				// and runs away.
				statChange = false;
				creatureChange = false;
				this.doNothing();
			}
		}
		if (creatureState.equals(creatureArray[2])) { // if creature is aggressive the AI will go through this choice
														// path.
			creatureChange = true;
			int wepDifference = charWep - creatureWep;
			int intDifference = charInt - creatureInt;
			int speedDifference = charSpeed - creatureSpeed;

			if (wepDifference >= 2 || wepDifference == 0) { // 100% chance user victory (AI wins or creature runs away
															// for weapon fight)
				this.resolveFight();
			} else if (intDifference >= 2 || intDifference == 0) { // 100% chance user victory(AI wins or creature runs
																	// away for intellect hypnotise)
				this.resolveHypnotise();
			} else if (speedDifference >= 0) { //// 100% chance of user victor with flee if difference is greater than
												//// or equal to 0
				this.resolveFlee();
			} else if ((noOfFlashguns > 0)
					&& ((wepDifference + flashgunPow >= 2) || (wepDifference + flashgunPow == 0))) {
				this.chooseSpecial();
			} else if ((noOfTextbooks > 0)
					&& ((intDifference + textbookPow >= 2) || (intDifference + textbookPow == 0))) {
				this.chooseSpecial();
			} else if ((noOfJetpacks > 0) && (speedDifference + jetpackPow >= 0)) {
				this.chooseSpecial();
			} else if (wepDifference == 1 || wepDifference == -1) {
				// This is the last thing the AI will choose to do asit only gives a 50% chance
				// of victory the AI will
				// avoid it where possible.
				this.resolveFight();
			} else if (intDifference == 1 || intDifference == -1) { // It can be 1 or -1 depending on who initially had higher stats
				this.resolveHypnotise();
			}
		}
	}

	public void resolveFight() { // If resolveFight() is called then this is where the program will depend who
									// the winner of the fight is.
		int y = charWep - creatureWep;
		if (y == 0) {
			System.out.println("The fight is a draw and the creature runs away!");
			// AI wins, next turn
			alive = true;
		} else if (y <= -2) {
			System.out.println("You fight honourably but the creature is too strong!");
			// AI loses, game ends
			alive = false;
		} else if (y >= 2) {
			System.out.println("You fight and defeat the creature!");
			// AI wins, next turn
			alive = true;
		} else if (y == 1 || y == -1) {
			Random num = new Random();
			int win = num.nextInt(2) + 1;
			if (win == 1) {
				System.out.println("You fight, its very close but win!");
				alive = true;
				// AI wins the 50/50, next turn
			} else if (win == 2) {
				System.out.println("You fight, the creature is just too strong and defeats you.");
				alive = false;
				// AI loses 50/50, game ends
			}
		}
	}

	public void resolveHypnotise() { // Depending on computer choice from the chooseAction method, this is chosen and
										// executed
		int y = charInt - creatureInt;

		if (y == 0) {
			System.out.println("You attempt to hypnotise the creature, it runs away!");
			// AI wins, next turn
			alive = true;
		} else if (y <= -2) {
			System.out.println("The creature is more intelligent than you and defeats you.");
			// AI loses, game over
			alive = false;
		} else if (y >= 2) {
			System.out.println("You successfully hypnotise the creature!");
			// AI wins, next turn
			alive = true;
		} else if (y == 1 || y == -1) {
			// System.out.println("50% chance hypno adventurer winning");
			Random num = new Random();
			int win = num.nextInt(2) + 1;
			if (win == 1) {
				System.out.println("You're only hope is to try and hypnotise the creature, it works!");
				alive = true;
				// AI wins the 50/50, next turn
			} else if (win == 2) {
				System.out.println("You attempt to hypnotise the creature but run out of energy and it defeats you.");
				alive = false;
				// AI loses the 50/50, game over
			}
		}
	}

	public void resolveFlee() { // The program will determine if the flee is successful or not.
		int x = charSpeed - creatureSpeed;
		if (x >= 0) {
			System.out.println("You manage to outrun the creature!");
			alive = true;
			// AI escapes, next turn
		} else if (x < 0) {
			System.out.println("The creature caught you!");
			alive = false;
			// AI is caught, game over
			// This situation will never occur as it is pointless for the AI to go along
			// this route.
		}
	}

	public void doNothing() { // What happens when the AI chooses to 'do nothing'.
		if (creatureState.equals(creatureArray[0])) { // What happens if the creature is 'scared'
			System.out.println("You do nothing.");
			System.out.println("The creature was scared so ran away!");
			alive = true;
		} else if (creatureState.equals(creatureArray[1])) {
			// What happens if the creature is curious.
			System.out.println("You're only hope is to do nothing and hope it doesn't become aggressive.");
			System.out.println("The curious creature doesn't move and continues to watch you from a distance.");
			alive = true;
		} else {
			System.out.println("The creature viciously mauls you to death.");
			alive = false;
		}
	}

}