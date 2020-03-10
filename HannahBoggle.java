/*
 * The Boggle Game
 * Programmer: Hannah Hungerford 
 * Date Completed: January 21 2017
 * Boggle is a game where the player
 * tries to form words out of letters
 * on 16 dice suffled randomly.
 */

//import necessary packages 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.util.*;
import java.io.*;
import javax.swing.Timer;
import sun.audio.*; 

public class HannahBoggle extends JFrame implements ActionListener  //Boggle Game Class 
{
  
  //create GUI buttons
  static JButton instructions = new JButton ("Instructions");
  static JButton newGame = new JButton ("New Game");
  static JButton search = new JButton ("Search Dictionary");
  static JButton [] [] letterButtons = new JButton [4][4];
  static JButton submit = new JButton ("Submit");
  static JButton clear = new JButton ("Clear");
  
  //create GUI Labels 
  static JLabel title = new JLabel ("Boggle");
  static JLabel wordsSubmitted = new JLabel ("Submitted Words:   ");
  static JLabel guessLabel = new JLabel ("Your Guess: ");
  static JLabel timeLabel = new JLabel ("Time Left: ");
  static JLabel scoreLabel = new JLabel ("Score: ");
  
  //create GUI textFields 
  static JTextField guessFeild = new JTextField ("");
  static JTextField timeField = new JTextField ("");
  static JTextField scoreField = new JTextField ("");
  
  //create TextArea
  static JTextArea wordsList = new JTextArea (10,4);
  
  static boolean [][] enabledButtons = new boolean [4][4]; // a parallel array to hold wether or not the buttons are enabled 
  static boolean [][] usedDice = new boolean [4][4];       // a parallel array to hold wether or not the dice have been used 
  
  
  //instruction variables 
  static String instructionsLine1 = "The game of Boggle tests your word knowledge.\n";
  static String instructionsLine2 = "\n";
  static String instructionsLine3 = "At the beginning og a new game, sixteen dice, each containing 6 letters are shaken and then allowed to settle in a \n";
  static String instructionsLine4 = "4 by 4 grid leaving the player with 16 random letters to work with.\n";
  static String instructionsLine5 = "\n";
  static String instructionsLine6 = "The players task is to click on adjacent letters to form actual words without using the same die more than once.\n";
  static String instructionsLine7 = "The words formed must be at least three letters long, and they must show up in the dictionary. The same word\n";
  static String instructionsLine8 = "cannot be used more than once.\n";
  static String instructionsLine9 = "\n";
  static String instructionsLine10 = "Scoring is based on the length of the words as follows:\n";
  static String instructionsLine11 = "-3 letters long: 1 point\n";
  static String instructionsLine12 = "-4 letters long: 2 points\n";
  static String instructionsLine13 = "-5 letters long: 3 points\n";
  static String instructionsLine14 = "-6 letters long: 5 points\n";
  static String instructionsLine15 = "-7 letters long: 7 points\n";
  static String instructionsLine16 = "-8+ letters long: 11 points\n";
  static String instructionsLine17 = "\n";
  static String instructionsLine18 = "How many words can you create in 3 minutes (180 seconds)??\n";
  static String instructionsTxt = instructionsLine1 + instructionsLine2 + instructionsLine3 + instructionsLine4 + instructionsLine5 + instructionsLine6 + instructionsLine7 + instructionsLine8 + instructionsLine9 + instructionsLine10 + instructionsLine11 + instructionsLine12 +  instructionsLine13 + instructionsLine14  + instructionsLine15 + instructionsLine16 + instructionsLine17 + instructionsLine18;
  
  static String guessWord = "";  //the word the player creates 
  static int diceNumber = 1;     //used for assigning value to the die 
  static int score = 0;          //the players score 
  static int time = 180;
  
  static Vector <String> dictionary = new Vector<String>();   //holds the words in the dictionary to be searched 
  static Vector <String> usedWords = new Vector<String> ();   //holds the words the player has already guessed
  
  
  // a Timer object which will cause an ActionEvent every 1000 milleseconds
  Timer t = new Timer (1000, this);
  
  static boolean gameRunning = false; 
  
  
  
  public HannahBoggle () //game constructor 
  {
    t.start ();     // start the timer 
    
    //add two blank values to the usedWords vector in order to search through the list before any words have been used
    usedWords.add ("");
    usedWords.add ("");
    
    //set up GUI 
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    Container contentPane = getContentPane ();
    
    //create panels 
    JPanel mainPanel = new JPanel();
    JPanel titlePanel = new JPanel ();
    JPanel topPanel = new JPanel (new GridLayout (2,1));
    JPanel options = new JPanel (new GridLayout (1,3));
    JPanel rightPanel = new JPanel ();
    JPanel lettersPanel = new JPanel (new GridLayout (4,4));
    JPanel bottomPanel = new JPanel ();
    JPanel guessPanel = new JPanel(new GridLayout (1,4));
    JPanel submitPanel = new JPanel (new GridLayout (1,2));
    JPanel timePanel = new JPanel (new GridLayout (1,6));
    
    //customize panels 
    mainPanel.setLayout (new BorderLayout ());
    rightPanel.setLayout (new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS));
    bottomPanel.setLayout (new BoxLayout(bottomPanel,BoxLayout.PAGE_AXIS));
    
    //add actionlistners
    instructions.addActionListener (this);
    newGame.addActionListener (this);
    search.addActionListener (this);
    clear.addActionListener (this);
    submit.addActionListener (this);
    
    //add components to the submit panel 
    submitPanel.add (submit);
    submitPanel.add (clear);
    
    //add components to the guess panel 
    guessPanel.add (guessLabel);
    guessPanel.add (guessFeild);
    
    //add components to the time panel 
    timePanel.add (timeLabel);
    timePanel.add (timeField);
    timePanel.add (scoreLabel);
    timePanel.add (scoreField);
    
    //add components to the bottom panel 
    bottomPanel.add (guessPanel);
    bottomPanel.add (submitPanel);
    bottomPanel.add (timePanel);
    
    //add components to the right panel 
    rightPanel.add (wordsSubmitted);
    rightPanel.add (wordsList);
    
    //add components to the options panel 
    options.add (instructions);
    options.add (newGame);
    options.add (search);
    
    //add components to the title panel 
    title.setForeground(Color.RED); //customize title font colour 
    titlePanel.add (title);
    
    //add components to the top panel 
    topPanel.add(titlePanel);
    topPanel.add (options);
    
    //add all the letter buttons to the letterspanel 
    for (int a = 0; a < 4; a++)
    {
      for (int b = 0; b < 4; b++)
      {
        letterButtons [a][b] = new JButton (""); //set intial font to null 
        lettersPanel.add (letterButtons [a][b]); //add buttons to panel 
        letterButtons[a][b].addActionListener(this); //add actionlisteners to the buttons 
        letterButtons[a][b].setBackground(Color.LIGHT_GRAY); //set background to light gray 
        
        usedDice [a][b] = false; //set parallel array to false 
      } //end for 
    }//end for 
    
    //add all components to the main panel 
    mainPanel.add (lettersPanel, BorderLayout.CENTER);
    mainPanel.add (bottomPanel, BorderLayout.PAGE_END);
    mainPanel.add (topPanel, BorderLayout.PAGE_START);
    mainPanel.add (rightPanel, BorderLayout.LINE_END);
    
    //add the main panel to the content pain 
    contentPane.add(mainPanel);
    
    //set features of the GUI
    setSize (350,350);
    setVisible (true);  
    
    try
    {
      Scanner scanner = new Scanner(new File("larger_dict.txt")); //create scanner to read dictionary file 
      
      while (scanner.hasNextLine()) //go through all the lines in the file 
      {
        dictionary.addElement(new String (scanner.nextLine())); //add the files of the dictionary to the dictionary vector 
      } //end while loop 
      
      scanner.close (); //close the scanner 
    } //end try 
    
    catch (IOException e)
    {
    } //end catch 
    
  } //end constructor 
  
  public void actionPerformed (ActionEvent event)  //if an action is perfromed 
  {
    String wordSearch; //the word the user wants to search the dictionary for
    
    
    if (event.getSource () == instructions) // if the user clicks instructions button
    {
      JOptionPane.showMessageDialog (null, instructionsTxt, "Instructions", JOptionPane.INFORMATION_MESSAGE);    
    } //end if 
    
    
    if (event.getSource () == search) //if the user click the search button 
    {     
      try
      {
        String searchWord = JOptionPane.showInputDialog ("Enter the word you would like to search to dictionary for:  "); //prompt the user to enter a word to search for 
        
        int binaryResult = (binarySearch (dictionary, searchWord)); //the result of the search 
        
        if ( binaryResult == -1) //if the word was not found 
        {
          int yesOrNo = JOptionPane.showConfirmDialog(null, "That word was not found. Would you like to add it?", "Not Found", JOptionPane.YES_NO_OPTION);//output message to check 
          
          
          if (yesOrNo == JOptionPane.YES_OPTION)  //if they clicked yes 
          {
            dictionary.add(searchWord);  //add the word to the dictionary vector 
            
            try 
            {
              resaveDictionaryFile (); //run method to resave dictionary file with new word 
            } //end try 
            
            catch (IOException e)
            {
            }//end catch
            
          } //end if 
        }
        
        
        else  //if the word was not found 
        {
          JOptionPane.showMessageDialog (null, "That word was found!", "Found", JOptionPane.INFORMATION_MESSAGE);
        }//end if 
        
      }//end try 
      
      catch (NullPointerException e)
      {
      } //end catch
    } // end if
    
    
    if (event.getSource () == newGame) //if the user wants to play a new game 
    {
      //reset variables 
      time = 180; 
      score = 0; 
      diceNumber = 1; 
      guessWord = "";
      
      //reset fields 
      guessFeild.setText ("");
      scoreField.setText (String.valueOf (score));
      wordsList.setText ("");
      timeField.setText ("180");
      
      //reset usedWords vector 
      usedWords.clear();
      usedWords.add ("");
      usedWords.add ("");
      
      gameRunning = true; //a new game is being played 
      
      for (int c = 0; c < 4; c++)
      {
        for (int d = 0; d < 4; d++)
        {
          letterButtons [c][d].setText(rollDice (diceNumber)); //use rolldice method to assign a random letter to each dice 
          
          diceNumber++; //increase the number of the dice 
          
          //set back parallel arrays 
          usedDice[c][d] = false; 
          enabledButtons[c][d] = true;
          
          //re-enable all buttons 
          letterButtons[c][d].setEnabled (true);
          
          //set all button colors back to light gray 
          letterButtons[c][d].setBackground (Color.LIGHT_GRAY);
        } //end for 
      } //end for 
      
    } //end if 
    
    
    
    if (gameRunning) //if there is a game running 
    {
      if (event.getSource () == t) //and a second has passed 
      {
        timeField.setText (String.valueOf(time)); //set the time feild to remaining time 
        time = time - 1; //decrease remaining time by one 
        
        if (time == 0) //if there is no more time 
        {
          JOptionPane.showMessageDialog (null,"You are out of time. Your final score is " + score, "Try Again", JOptionPane.INFORMATION_MESSAGE); //game over message 
          
          //reset variables  
          time = 180; 
          score = 0; 
          guessWord = "";
          
          gameRunning = false;  //set game runnning to false 
          
          for (int o = 0; o< 4; o++)
          {
            for (int p = 0; p < 4; p++)
            {
              
              letterButtons[o][p].setBackground (Color.LIGHT_GRAY); //set background colours back to gray 
              
              letterButtons [o][p].setText(""); //set buttons text back to null 
              
              letterButtons [o][p].setEnabled (true); //re-enable all buttons 
              
            } //end for 
          } //end for 
          
          //reset fields to blank 
          guessFeild.setText ("");
          scoreField.setText (String.valueOf (score));
          wordsList.setText ("");
          timeField.setText ("180");
        } //end if 
      } //end if  
    } //end if 
    
    //check if any letter buttons have been clicked 
    for (int e = 0; e < 4; e++)
    {
      for (int f = 0; f < 4; f++)
      {
        if (event.getSource () == letterButtons [e][f])
        {
          
          usedDice [e][f] = true; //set this letters place in parallel to used
          
          enableButtons (e,f); //run method to enable buttons 
          
          letterButtons[e][f].setBackground(Color.blue); //set clicked buttons background to blue 
          
          guessWord = guessWord + letterButtons[e][f].getText(); //add the letter to the guessWord 
          
          guessFeild.setText (guessWord); //place the guessWord in the guess text field 
          
          try 
          {
            playSound (1); //run method to play sound
          } // end try
          
          catch (IOException er)
          {
          } //end catch
        } //end if 
      } //end for 
    } //end for 
    
    
    if (event.getSource () == clear) //if the user clicks clear 
    {
      for (int g = 0; g < 4; g++)
      {
        for (int h = 0; h < 4; h++)
        {
          //reset colours
          letterButtons[g][h].setBackground (Color.LIGHT_GRAY);
          letterButtons[g][h].setForeground (Color.BLACK);
          
          //reset usedDice array 
          usedDice[g][h] = false; 
          
          //enable all buttons 
          enabledButtons[g][h] = true;
          letterButtons[g][h].setEnabled (true);
        } //end for 
      } //end for 
      
      //reset guess word 
      guessWord = "";
      guessFeild.setText ("");
      
    } //end if 
    
    
    if (event.getSource () == submit) //if user click submit 
    {
      int binaryResult = (binarySearch (dictionary, guessWord)); //the result of searching the dictionary for the guessed word 
      int usedWordResult = (binarySearch (usedWords, guessWord));//the result of searching the used words vecotr for the guessed word 
      
      
      if ( binaryResult == -1) //if the word was not found in the dictionary 
      {
        JOptionPane.showMessageDialog (null,"That word was not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
      } // end if
      
      else if (usedWordResult != -1) //if the word has already been used
      {
        JOptionPane.showMessageDialog (null,"You have already used this word.", "Used", JOptionPane.INFORMATION_MESSAGE);
      } //end if 
      
      else if (guessWord.length() < 3) //if the word is less than 3 letters 
      {
        JOptionPane.showMessageDialog (null,"The word must be 3 letters long.", "Too Short", JOptionPane.INFORMATION_MESSAGE);
      } //end if 
      
      
      else //if the word is a valid guess 
      {
        usedWords.add(guessWord); //add it to list of used words 
        
        wordsList.append(guessWord + "\n"); //add to word list text area on GUI 
        
        calculatePoints (guessWord); //run method to calculate points 
        
        try 
        {
          playSound (2); //run method to play sound
        } // end try
        
        catch (IOException er)
        {
        } //end catch
        
      } //end if 
      
      //reset variables and buttons
      for (int i = 0; i < 4; i++)
      {
        for (int j = 0; j< 4; j++)
        {
          letterButtons[j][i].setBackground (Color.LIGHT_GRAY); //reset background colour to gray 
          
          usedDice[j][i] = false; //reset parallel array to determine if the dice as already been clicked 
          
          //re-enable buttons
          enabledButtons[j][i] = true; 
          letterButtons[j][i].setEnabled (true);
        } //end for 
      } //end for 
      
      
      //reset guess field 
      guessWord = "";
      guessFeild.setText ("");
      
    } //end if 
    
  } //end actionperformed method 
  
  
  public static void enableButtons (int row, int column) //method to enable appropriate buttons after a letter button has been clicked 
  {
    //set all the elements of parallel array of enabled buttons to false to begin with 
    for (int k = 0; k < 4; k++)
    {
      for (int l = 0; l< 4; l++)
      {
        enabledButtons [k][l] = false;
      } //end for 
    }//end for 
    
    //set place in parallel array to enabled for all adjacent buttons 
    if (row-1 >= 0) //beneth the selected button 
    {
      enabledButtons [row-1][column] = true;  
    }
    
    if ( row + 1 < 4)//above the selected button
    {
      enabledButtons [row+1][column] = true;
    }
    
    if (column-1 >= 0)//to the left of the selected button
    {
      enabledButtons [row][column - 1] = true;
    }
    
    if (column+ 1 < 4)//to the right of the selected button
    {     
      enabledButtons [row][column + 1] = true;
    }
    
    if (column + 1 < 4 && row + 1 < 4) //to the right of and above the selected button
    {
      enabledButtons [row + 1][column + 1] = true;
    }
    
    if (column - 1 > -1 && row - 1 > -1)//to the left of and below the selected button
    {
      enabledButtons [row - 1][column - 1] = true;
    }
    
    if (column - 1 > -1 && row + 1 < 4)//to the left of and above the selected button
    {
      enabledButtons [row + 1][column - 1] = true;
    }
    
    if (column + 1 < 4 && row - 1 > -1)//to the right of and below the selected button
    {
      enabledButtons [row - 1][column + 1] = true;
    }
    
    
    for (int m = 0; m < 4; m++)
    {
      for (int n = 0; n< 4; n++)
      {
        
        if (usedDice [m][n] == true) //if the letter has already been used for this word then disable it 
        {
          enabledButtons [m][n] = false;
        } //end if 
        
        if (enabledButtons [m][n] == true) //if the parallel aray place is true for each button then set the to enabled 
        {
          letterButtons[m][n].setEnabled (true);
        } //end if 
        
        else //if not keep them as false 
        {
          letterButtons[m][n].setEnabled (false);
        } //end if 
        
       letterButtons[m][n].setForeground (Color.BLACK); //change lettering of disabled buttons to black 
      }//end for 
    }//end for 
     
  }//end enable buttons method 
  
  
  
  public static void calculatePoints (String word) //method to calculate points each word has earned 
  {    
    if (word.length() == 3) //if the word is three letters long the players gets 1 point
    {
      score = score + 1; //add 1 points to total score     
    }
    
    else if (word.length() == 4)//if the word is four letters long the players gets 2 points
    {
      score = score + 2; //add 2 points to total score       
    }
    
    else if (word.length() == 5)//if the word is five letters long the players gets 3 points
    {
      score = score + 3; //add 3 points to total score       
    }
    
    else if (word.length() == 6)//if the word is six letters long the players gets 5 points
    {
      score = score + 5; //add 5 points to total score       
    }
    
    else if (word.length() == 7)//if the word is seven letters long the players gets 7 points
    {
      score = score + 7; //add 7 points to total score      
    }
    
    else if (word.length() > 7)//if the word is longer than 7 the players gets 11 points
    {
      score = score + 11; //add 11 points to total score      
    }
    
    scoreField.setText (String.valueOf(score)); //update score field 
  }
  
  
  public static int binarySearch (Vector <String> array, String target) //search method to search for a specific word through a vector 
  {
    int high = array.size(); //the number of the highest element 
    int low = 0; //lowest element stating at 0 
    int middle; //middle varaible 
    
    while (high - low > 1) //while there is still elements left to search through 
    {
      
      middle = (high + low) / 2; //calculate the middle position of the vector 
      
      
      if (((String)array.get(middle)).compareToIgnoreCase (target) > 0) //if the middle element is greater than the target element 
        high = middle; //the high position becomes the middle 
      
      else //if the target element is greater than the middle element 
        low = middle; //the low position becomes the middle 
      
      
    }//end while 
    
    if (low == -1 || ! ((String) array.get(low)).equalsIgnoreCase(target)) //if low is less than zero or the last element does not eqaul te target 
      return -1; //the word was not found 
    
    else  //if the last element was equal to the target 
      return low; //the word was found and its position is returned 
  } //end search method 
  
  
  
  public static void resaveDictionaryFile () throws IOException //method to resave the dictionary file 
  {
    quickSort (dictionary, 0, dictionary.size() - 1); //sort the new vector with the added word 
    
    PrintWriter fileOut = new PrintWriter (new FileWriter ("larger_dict.txt")); //create the printWriter 
    
    //print new vector to the file 
    for (int s = 0; s < dictionary.size(); s++)
    {
      fileOut.println (dictionary.get(s));
    } //end for 
    
    fileOut.close (); //close the file 
  } //end the method 
  
  
  public static void quickSort (Vector <String> list, int left, int right) //method to sort a vector 
  {
    swap (list, left, (left + right) /2); //call the swap method 
    
    int lastSmall = left; //last small variable is equal to the left parameter 
    
    for (int i = left + 1; i <= right; i++)
    {
      if ((list.get(i)).compareTo(list.get (left)) < 0) //if the next element is smaller than the elemnts to its left
      {
        lastSmall++; //increase last small 
        swap (list,lastSmall, i); //swap the elements 
      }//end if 
    }//end for 
    
    swap (list,left,lastSmall); //call swap method 
    
    int pivotPlace = lastSmall; //pivot place is the last smallest element 
    
    //determine if recursion is necessary 
    if (left < pivotPlace -1)
      quickSort (list, left, pivotPlace - 1);
    
    if (pivotPlace+1 < right)
      quickSort (list,pivotPlace+1, right);
    
  } //end quicksort method 
  
  public static void swap (Vector <String> array3 , int first, int second) //swap method to swap two elements in a vector 
  {
    String hold; //temporary variable 
    
    //swap elements 
    hold = array3.get(first);
    array3.setElementAt (array3.get (second), first);
    array3.setElementAt (hold, second);
    
  }
      
 public static void playSound (int sound) throws IOException //method to play sounds 
  {
    //create the blip sound 
    InputStream in = new FileInputStream("blip.wav");
    AudioStream blip = new AudioStream(in); 
    
    //create the boing sound 
    InputStream in2 = new FileInputStream("boing.wav");
    AudioStream boing = new AudioStream(in2); 
    
    //play the blip sound if 1 is entered as a parameter 
    if (sound == 1)
    {
      AudioPlayer.player.start(blip);            
    } //end if 
    
    //play the boing sound if 2 is entered as a parameter 
    if (sound == 2)
    {
      AudioPlayer.player.start (boing);
    } //end if 
  }//end play sound method    
  
 
  public static String rollDice (int diceNum) //method to "roll" dice creating random letter for each of the dice faces 
  {
    //letterRolled variable starting at null 
    String letterRolled = ""; 
    
    int faceNum = (int) (Math.random () * 6) + 1; //pick a random face number 
    
    if (diceNum == 1) //if the first dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "P";
      
      else if (faceNum == 2)
        letterRolled = "C";
      
      else if (faceNum == 3)
        letterRolled = "H";
      
      else if (faceNum == 4)
        letterRolled = "O";
      
      else if (faceNum == 5)
        letterRolled = "A";
      
      else if (faceNum == 6)
        letterRolled = "S";       
    }//end if 
    
    
    
    else if (diceNum == 2)//if the second dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "O";
      
      else if (faceNum == 2)
        letterRolled = "A";
      
      else if (faceNum == 3)
        letterRolled = "T";
      
      else if (faceNum == 4)
        letterRolled = "T";
      
      else if (faceNum == 5)
        letterRolled = "O";
      
      else if (faceNum == 6)
        letterRolled = "W";       
    }//end if 
    
    else if (diceNum == 3)//if the third dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "L";
      
      else if (faceNum == 2)
        letterRolled = "Y";
      
      else if (faceNum == 3)
        letterRolled = "R";
      
      else if (faceNum == 4)
        letterRolled = "T";
      
      else if (faceNum == 5)
        letterRolled = "T";
      
      else if (faceNum == 6)
        letterRolled = "E";       
    } //end if
    
    
    else if (diceNum == 4)//if the fourth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "V";
      
      else if (faceNum == 2)
        letterRolled = "T";
      
      else if (faceNum == 3)
        letterRolled = "H";
      
      else if (faceNum == 4)
        letterRolled = "R";
      
      else if (faceNum == 5)
        letterRolled = "W";
      
      else if (faceNum == 6)
        letterRolled = "E";       
    } //end if
    
    
    else if (diceNum == 5)//if the fifth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "E";
      
      else if (faceNum == 2)
        letterRolled = "G";
      
      else if (faceNum == 3)
        letterRolled = "H";
      
      else if (faceNum == 4)
        letterRolled = "W";
      
      else if (faceNum == 5)
        letterRolled = "N";
      
      else if (faceNum == 6)
        letterRolled = "E";       
    } //end if 
    
    
    
    else if (diceNum == 6)//if the sixth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "S";
      
      else if (faceNum == 2)
        letterRolled = "E";
      
      else if (faceNum == 3)
        letterRolled = "O";
      
      else if (faceNum == 4)
        letterRolled = "T";
      
      else if (faceNum == 5)
        letterRolled = "Y";
      
      else if (faceNum == 6)
        letterRolled = "S";       
    } //end if 
    
    
    else if (diceNum == 7)//if the seventh dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "A";
      
      else if (faceNum == 2)
        letterRolled = "N";
      
      else if (faceNum == 3)
        letterRolled = "A";
      
      else if (faceNum == 4)
        letterRolled = "E";
      
      else if (faceNum == 5)
        letterRolled = "E";
      
      else if (faceNum == 6)
        letterRolled = "G";       
    }//end if 
    
    else if (diceNum == 8)//if the eigth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "I";
      
      else if (faceNum == 2)
        letterRolled = "D";
      
      else if (faceNum == 3)
        letterRolled = "S";
      
      else if (faceNum == 4)
        letterRolled = "Y";
      
      else if (faceNum == 5)
        letterRolled = "T";
      
      else if (faceNum == 6)
        letterRolled = "T";       
    } //end if 
    
    
    else if (diceNum == 9)//if the ninth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "M";
      
      else if (faceNum == 2)
        letterRolled = "T";
      
      else if (faceNum == 3)
        letterRolled = "O";
      
      else if (faceNum == 4)
        letterRolled = "I";
      
      else if (faceNum == 5)
        letterRolled = "C";
      
      else if (faceNum == 6)
        letterRolled = "U";       
    } //end if 
    
    
    
    else if (diceNum == 10)//if the tenth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "A";
      
      else if (faceNum == 2)
        letterRolled = "F";
      
      else if (faceNum == 3)
        letterRolled = "P";
      
      else if (faceNum == 4)
        letterRolled = "K";
      
      else if (faceNum == 5)
        letterRolled = "F";
      
      else if (faceNum == 6)
        letterRolled = "S";       
    }//end if 
    
    
    
    else if (diceNum == 11)//if the elenventh dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "X";
      
      else if (faceNum == 2)
        letterRolled = "L";
      
      else if (faceNum == 3)
        letterRolled = "D";
      
      else if (faceNum == 4)
        letterRolled = "E";
      
      else if (faceNum == 5)
        letterRolled = "R";
      
      else if (faceNum == 6)
        letterRolled = "I";       
    } //end if 
    
    
    
    else if (diceNum == 12)//if the twelth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "E";
      
      else if (faceNum == 2)
        letterRolled = "N";
      
      else if (faceNum == 3)
        letterRolled = "S";
      
      else if (faceNum == 4)
        letterRolled = "I";
      
      else if (faceNum == 5)
        letterRolled = "E";
      
      else if (faceNum == 6)
        letterRolled = "U";       
    }//end if
    
    else if (diceNum == 13)//if the thirteenth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "Y";
      
      else if (faceNum == 2)
        letterRolled = "L";
      
      else if (faceNum == 3)
        letterRolled = "D";
      
      else if (faceNum == 4)
        letterRolled = "E";
      
      else if (faceNum == 5)
        letterRolled = "V";
      
      else if (faceNum == 6)
        letterRolled = "R";       
    }//end if 
    
    
    else if (diceNum == 14)//if the fourteenth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "Z";
      
      else if (faceNum == 2)
        letterRolled = "N";
      
      else if (faceNum == 3)
        letterRolled = "R";
      
      else if (faceNum == 4)
        letterRolled = "N";
      
      else if (faceNum == 5)
        letterRolled = "H";
      
      else if (faceNum == 6)
        letterRolled = "L";       
    }//end if 
    
    
    else if (diceNum == 15)//if the fifthteenth dice is being rolled 
    {
      //pick a letter based on the random integer determining which "face it lands on" 
      if (faceNum == 1)
        letterRolled = "N";
      
      else if (faceNum == 2)
        letterRolled = "M";
      
      else if (faceNum == 3)
        letterRolled = "I";
      
      else if (faceNum == 4)
        letterRolled = "Q";
      
      else if (faceNum == 5)
        letterRolled = "H";
      
      else if (faceNum == 6)
        letterRolled = "U";       
    }
    
    
    else if (diceNum == 16)//if the sixteenth dice is being rolled 
    {
      if (faceNum == 1)
        letterRolled = "O";
      
      else if (faceNum == 2)
        letterRolled = "B";
      
      else if (faceNum == 3)
        letterRolled = "B";
      
      else if (faceNum == 4)
        letterRolled = "O";
      
      else if (faceNum == 5)
        letterRolled = "A";
      
      else if (faceNum == 6)
        letterRolled = "J";       
    }//end if 
    
    return letterRolled; //return what letter was picked 
  }//end roll Dice 
  
}//end Hannah Boggle Class 