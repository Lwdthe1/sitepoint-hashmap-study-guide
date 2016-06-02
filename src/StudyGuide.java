package com.lincolnwdaniel.hashmapStudyGuide;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Lincoln W Daniel
 */
public class StudyGuide {
    //for seperating output from our app
    public static final String OUTPUT_SEPERATOR_LINE = "\n------------------------------------\n";
    //values that the user can enter to quit the app deliminated by a space
    private static final String USER_QUIT_INPUTS = "x quit";
    //initialize a HashMap to hold String-Meaning (key-value) pairs.
    private static final HashMap<String, Meaning> studyGuide = new HashMap<>();
    /*The total possible points the user can obtain. 
    Equal to the number of concepts in the study guide.*/
    private static int possiblePoints;
    
    //our main method to start our app
    public static void main(String[] args) {
        populateStudyGuide();
        printInstructions();       
        study();
    }

    /**
     * Starts the study session
     */
    private static void study() {
        //for scanning the user's keyboard for input
        Scanner userInputScanner = new Scanner(System.in);
        //to store the user's input
        String userInput;
        
        /*the points obtained by the user 
        for getting the meanings of concepts correct.*/
        int userPoints = 0;
        /*how many rounds the user takes 
        to get all of the concept meanings correct*/
        int rounds = 1;
        
        //set the default user input because the while loop needs to check it
        userInput = "startInConsole";
        
        /*we will let the user quit at any point in the app,
        so we must make sure they didn't quit yet*/
        while(userPoints < possiblePoints && !userQuit(userInput, userPoints)) {
            //store the keys of the study guide in a immutable Set
            final Set<String> concepts = studyGuide.keySet();
            
            for (String concept : concepts) {
                Meaning currentConceptMeaning = studyGuide.get(concept);
                
                //make sure currentConceptMeaning is not null
                if(currentConceptMeaning == null) {
                    studyGuide.remove(concept);
                    //move to next iteration
                    continue;
                }
                
                if(!currentConceptMeaning.userGotCorrect()) {
                    System.out.printf("\n\t\t" + OUTPUT_SEPERATOR_LINE 
                            + "\n\n> What is %s?\n\n\n", concept);

                    if(userInputScanner.hasNextLine()) {
                        userInput = userInputScanner.nextLine();
                    }
                    
                    if (!userQuit(userInput, userPoints)) {
                        if (currentConceptMeaning.userInputMatches(userInput)) {
                            currentConceptMeaning.markUserGotCorrect();
                            userPoints++;
                            System.out.printf("\n> CORRECT! %s means: %s",
                                    concept, currentConceptMeaning.getMeaning());
                        } else {
                            System.out.printf("\n> WRONG! %s means: %s",
                                    concept, currentConceptMeaning.getMeaning());
                        }
                    }
                }
            }

            System.out.println(OUTPUT_SEPERATOR_LINE);
            System.out.printf("\n> You have %d of %d possible points at the end of round %d. ", 
                    userPoints, possiblePoints, rounds);
            
            //make sure the user hasn't scored all of the possible points
            if(userPoints < possiblePoints) {
                System.out.println("\n> Type anything to continue "
                        + "OR remove to remove a concept \"x\" or \"quit\" to quit?");
                if(userInputScanner.hasNextLine()) {
                    userInput = userInputScanner.nextLine();
                    if(userInput.toLowerCase().equals("remove")) {
                        System.out.println("\n> Remove which concept?");
                        if(userInputScanner.hasNextLine()) {
                            removeConcept(userInputScanner.nextLine());
                        }
                    }
                }
            } else break; //break out of the loop because the user is done
        }
        
        System.out.println(OUTPUT_SEPERATOR_LINE);
        System.out.println("Congrats! You got all the meanings correct.");
    }

    /**
     * Prints our program's instructions for the user.
     */
    private static void printInstructions() {
        System.out.println("> Let's Study Java Concepts!");
        System.out.println("> Try to give the meaning to each concept.");
        System.out.println("\tIf you are right, you will get a point " +
                "and the concept will be marked as correct.");
        System.out.println("> The game will continue until you get each concept right.");
        System.out.println("> To quit, enter \"x\" or \"quit\" when prompted.");
        System.out.printf("\n>Let's begin...\n%s\n", OUTPUT_SEPERATOR_LINE);
    }
    
    /**
     * Checks if the user quit or not.
     * @param userInput
     * @return Returns true if userInput is contained 
     * in USER_QUIT_INPUTS and false otherwise.
     */
    private static boolean userQuit(String userInput, int userPoints) {
        //check if the user quit inputs contains the user's input
        if(userInput != null && USER_QUIT_INPUTS.toLowerCase().contains(userInput)) {
            System.out.println("You quit with " 
                    + userPoints + " of " + possiblePoints + " possible points. "
                    + "See you later!");
            //end the program now
            System.exit(100);
            //return true. This line is not necessary because the program ends before its reached
        }
        
        //return false because the user didn't quit
        return false;
    }

    /**
     * Populates the study guide with Java concepts.
     * @param studyGuide The HashMap representation of the study guide
     */
    private static void populateStudyGuide() {
        //add a bunch of 
        studyGuide.put("Variable", new Meaning(
                "Used to store a single value for later use."));
        studyGuide.put("String", new Meaning(
                "A class for representing character strings."));
        studyGuide.put("double", new Meaning(
                "A primitive datatype for representing floating point numbers."));
        studyGuide.put("Double", new Meaning(
                "A class for wrapping a double in an Object with convenient methods."));
        studyGuide.put("zero", new Meaning(
                "The number zero. The first index in arrays and the first position of lists."));
        
        //set the possible points
        updatePossiblePoints();
    }

    /**
     * This allows the user to remove a concept they give up on from the study guide. 
     * Removes the pair from the HashMap with a key that matches the concept.
     * @param studyGuide 
     */
    private static void removeConcept(String concept) {
        Meaning conceptMeaning = studyGuide.get(concept);
        if(conceptMeaning != null) {
            //make sure the user hasn't already gotten the meaning correct
            if(!conceptMeaning.userGotCorrect()){
                //remove the concept's key-value pair from the study guide
                studyGuide.remove(concept);
                System.out.println("Removed \"" + concept + "\" from your study guide.");

                /*update the possible points so that it matches 
                the number of concepts left in the study guide*/
                updatePossiblePoints();
            } else {
                //don't let the user remove a concept they already know
                System.out.println("You know \"" + concept + "\", silly. Can't remove.");
            }
        } else {
            System.out.println("\"" + concept + "\" isn't in your study guide.");
        }
    }

    /**
     * Sets the possible points to the user can obtain
     */
    private static void updatePossiblePoints() {
        possiblePoints = studyGuide.size();
        System.out.println("There are " + possiblePoints + " concepts to study.");
    }
}

