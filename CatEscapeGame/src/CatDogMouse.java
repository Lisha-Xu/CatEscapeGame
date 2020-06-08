// CatDogMouse.java                          Written by David Wagner && Lisha Xu
// Project for ObjectOrientedProgramming

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.util.Formatter;
import java.util.Random;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CatDogMouse extends JFrame
{

	//////////////////////
	// Constant Values  //
	//////////////////////

	// The house is a square and this is the length of each side
	static final int HOUSESIZE = 30;

	// This is the smallest possible size of the house
	static final int MINHOUSESIZE=18;

	// This is the maximum number of dogs and mice that can appear in the house
	static final int MAXDOGS=20;
	static final int MAXMICE=20;

	static final int MAXTRAPS=3;
	// This is the starting floor of the house
	static final int MINFLOOR=1;

	// A location in the house can contain an obsatcle, player, dog or mouse
	static final char OBSTACLECHAR='.';
	static final char CATCHAR='C';
	static final char DOGCHAR='D';
	static final char MOUSECHAR='M';
	static final char UPCHAR='U';
	static final char DOWNCHAR='S';
	static final char EXITCHAR='H';
	static final char TRAPCHAR='T';


	// The name of the high score file
	static String HIGHSCOREFILE="CatDogMouseHighScore.txt";

	// If this is the first time you run the program, this is the high score
	static final int STARTHIGHSCORE=0;

	// Define the size of an obstacle free center region
	// This also defines the region where the cat must start
	// And where no dogs can start
	static final int TOOCLOSE=4;

	// Define the size of an obstructed external region on the top floor
	static final int TOOFAR=8;

	static final int NOMOUSEINDEX=-1;
	static final int NOTRAPINDEX=-1;

	// Define an axis to be either vertical (NORTHSOUTH) or horizontal (EASTWEST)
	static final int NORTHSOUTH = 0;
	static final int EASTWEST = 1;

	//////////////////////
	// Global Variables //
	//////////////////////

	// This is the house variable
	// It is a 2 dimensional array of characters
	static char [][] house;

	// This is the count of how many dogs and mice are currently in the house
	static int numDogs;
	static int numMice;

	static int numTraps;


	// This is the current size of the house
	static int houseSize;

	// These are the current min and max coordinates of the house
	static int houseMinCoord;
	static int houseMaxCoord;
	static float houseMidCoord;

	// This is how much smaller the house becomes after every level
	static int houseSizeIncrement;

	// This is the current number of the house
	static int houseNumber;

	// This is the current floor number where the player is located
	static int floorNum;

	// This is the highest floor reached by the player
	static int maxFloorNum;

	// This variable contains the location of the player
	static LocationType player;

	// This variable conatains the locations of the dogs
	static DogType [] dogs;

	// This variable contains the locations of the mice
	static MouseType [] mouse;

	static TrapType [] traps;

	// This variable contains the location of the up and down stairs
	static LocationType upStairs, downStairs;

	// This variable contains the location of the exit
	static LocationType houseExit;

	// This is how many mice you are carrying
	static int miceCarried;

	// This is how many mice you have removed
	static int miceRemoved;

	// This is your high score, which is the tallest house you have reached so far
	static int highScore;

	// Random number generator
	static Random randomNum;

	// Scanner to read from a file or from the command line
	static Scanner input;

	// Formatter to write to a file
	static Formatter output;

	private JTextField num1TextField;
	private JTextField num2TextField;
	private JTextArea printText;
	private JTextArea HouseText;

	private JButton continueButton;
	private JButton upButton;
	private JButton downButton;
	private JButton leftButton;
	private JButton rightButton;
	private JButton rushButton;
	private JButton startButton;
	private JPanel buttonRow1;
	private JPanel buttonRow2;
	private JPanel buttonRow3;

	int flag =100;
	static int flag_rush = 0;
	static int flag_trap =0;
	static int sum =0;
	static int sum2 =0;
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				if (event.getSource() == startButton) {
					startHouseNumber(1);
					miceCarried=0;
					miceRemoved=0;
					starthouse();
					flag =0;
				}
				if(flag == 0) {
					if (event.getSource() == upButton) {
						movePlayerAndDogsAndMice('N');
						printHouseAndKey();
					}
					if (event.getSource() == downButton) {
						movePlayerAndDogsAndMice('S');
						printHouseAndKey();
					}
					if (event.getSource() == leftButton) {
						movePlayerAndDogsAndMice('W');
						printHouseAndKey();
					}
					if (event.getSource() == rightButton) {
						movePlayerAndDogsAndMice('E');
						printHouseAndKey();
					}
					if(event.getSource() == rushButton)
					{
						if(miceCarried-1>=0)
						{
							miceCarried=miceCarried-1;
							flag_rush =1;
						}
					}
				}
				else if(flag ==1){
					if (event.getSource() == continueButton)
					{
						starthouse();
						num2TextField.setText("");
						flag=0;
					}

				}
				else
				{

				}

			}
			catch(Exception ex)
			{

			}
		}
	}
	/////////////////////////////////////////////////////////////////////////
	//
	// Print out the house
	//
	/////////////////////////////////////////////////////////////////////////

	public void printHouse()
	{
		int i,j;
		HouseText.setText("");
		for (i=0; i<HOUSESIZE; i++)
		{
			for (j=0;j<HOUSESIZE;j++)
			{
				if (house[i][j]==' ')
					HouseText.append("   ");
				else if(house[i][j]=='.')
					HouseText.append(" . ");
				else if (house[i][j]=='-')
					HouseText.append("- -");
				else
				HouseText.append(String.valueOf(house[i][j]));
			}
			HouseText.append("\n");
		}
	}

	public CatDogMouse(String title)
	{
		//Set the title of JFrame with the superclass constructor
		super(title);

		//Set the JFrame to be a 3X2 Grid, with a gap of 5 between each row and column
		setLayout(new GridLayout(1,2,5,5));
		initVariables();
		HouseText = new JTextArea();
		HouseText.setBounds(0,0, HOUSESIZE, HOUSESIZE);
		printText = new JTextArea();

		buttonRow1 = getButtonPanel1();
		buttonRow3 = getsumPanel();

		add(HouseText);
		add(buttonRow3);
	}


	private JPanel getButtonPanel1()
	{
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(2,2,5,5));

		//Create an instance of each button
		upButton = new JButton("N");
		downButton = new JButton("S");
		leftButton = new JButton("W");
		rightButton = new JButton("E");

		//Add ActionListeners to all the buttons
		ButtonListener listener= new ButtonListener();

		upButton.addActionListener(listener);
		downButton.addActionListener(listener);
		leftButton.addActionListener(listener);
		rightButton.addActionListener(listener);

		//Add the 3 buttons to myPanel
		myPanel.add(upButton);
		myPanel.add(downButton);
		myPanel.add(leftButton);
		myPanel.add(rightButton);

		return myPanel;
	}

	private JPanel getButtonPanel2()
	{
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new FlowLayout());

		//Create an instance of each button
		continueButton = new JButton("Continue");
		startButton = new JButton("Start");
		rushButton = new JButton("rush");
		ButtonListener listener= new ButtonListener();

		continueButton.addActionListener(listener);
		startButton.addActionListener(listener);
		rushButton.addActionListener(listener);
		//myPanel.add(confirmButton);
		myPanel.add(startButton);
		myPanel.add(continueButton);
		myPanel.add(rushButton);

		return myPanel;
	}

	private JPanel getsumPanel()
	{
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(5,1,5,5));
		num1TextField = new JTextField("0",5);
		num2TextField = new JTextField("0",5);


		//Add the 3 buttons to myPanel
		myPanel.add(num1TextField);

		myPanel.add(printText);
		myPanel.add(getButtonPanel1());
		myPanel.add(getButtonPanel2());
		myPanel.add(num2TextField);
		return myPanel;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// Print out the key, explaining what the various letters are in the house
	//
	/////////////////////////////////////////////////////////////////////////

	public void printKey()
	{
		num1TextField.setText("House: "+String.format("%d",houseNumber)+" Floor: "+String.format("%d",floorNum)+" High Score: "+String.format("%d",highScore)
		+"\nMice Carried "+String.format("%d",miceCarried)+"   Mice Removed "+String.format("%d",miceRemoved));

		printText.setText("");
		printText.append("Cat (Player) = C\n");
		printText.append("Guard Dog = D\n");
		printText.append("Mouse = M\n");
		printText.append("Up Stairs = U\n");
		printText.append("Down Stairs = S\n");
		printText.append("Helicopter = H\n");
		printText.append("When the cat meet trap, it can't move in one step\n");
		printText.append("The cat can eat a mouse to rush\n");
		printText.append("After eating, the cat can move faster in three steps\n");


	}

	/////////////////////////////////////////////////////////////////////////
	//
	// Print out the house and the key
	//
	/////////////////////////////////////////////////////////////////////////

	public void printHouseAndKey()
	{
		printHouse();
		printKey();
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// Add walls to the four sides of the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void buildHouseWalls()
	{
		int i;
		for (i=houseMinCoord; i<houseMaxCoord; i++)
		{
			house[houseMinCoord][i] = '-';
			house[houseMaxCoord-1][i] = '-';
			house[i][houseMinCoord] = '|';
			house[i][houseMaxCoord-1] = '|';

		}
	}



	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// Return true if the given coordinates are inside the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean insideHouse(int i, int j)
	{
		if (i > houseMinCoord && i < houseMaxCoord-1 && j > houseMinCoord && j < houseMaxCoord-1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}



	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// Return true if the given coordinates are outside the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean outsideHouse(int i, int j)
	{
		if (i < houseMinCoord || i > houseMaxCoord-1 || j < houseMinCoord || j > houseMaxCoord-1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}



	/////////////////////////////////////////////////////////////////////////
	//
	// Return the square of the distance from the coords to the house center
	//
	/////////////////////////////////////////////////////////////////////////

	public static float centerDistSquared(int i, int j)
	{
		float iDist = i - houseMidCoord;
		float jDist = j - houseMidCoord;

		return (iDist * iDist) + (jDist * jDist);
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This funcation returns true if the coords (i,j) are closer
	// to the house center than the given distance
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean closerToCenter(int i, int j, float distance)
	{
		if (centerDistSquared(i,j) < distance * distance)
		{
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This funcation returns true if the player is on the bottom floor
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean bottomFloor()
	{
		if (floorNum <= MINFLOOR)
		{
			return true;
		}
		return false;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This funcation returns true if the player is on the top floor of the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean topFloor()
	{
		if (houseSize <= MINHOUSESIZE)
		{
			return true;
		}
		return false;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// Add some obstacles and empty spaces to the house at random
	// The center of the house should have no obstacles
	// The region away from the center of the top floor should be all obstacles
	//
	/////////////////////////////////////////////////////////////////////////

	public static void buildHouseObstacles()
	{
		int i, j;
		for (i=0; i<HOUSESIZE; i++)
		{
			for (j=0; j<HOUSESIZE; j++)
			{
				if (outsideHouse(i,j))
				{
					house[i][j] = ' ';
				}

				else if (insideHouse(i,j))
				{
					if (closerToCenter(i,j, TOOCLOSE))
					{
						house[i][j] = ' ';
					}
					else if (topFloor() &&
						!closerToCenter(i,j, TOOFAR))
					{
						house[i][j] = OBSTACLECHAR;
					}
					else if (randomNum.nextInt(5) == 0)
					{
						house[i][j] = OBSTACLECHAR;
					}
					else
					{
						house[i][j] = ' ';
					}
				}
			}
		}
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// Create a new high score file
	//
	/////////////////////////////////////////////////////////////////////////

	public static void createHighScoreFile()
	{

		System.out.printf("Creating High Score file\n");

		try {
			output = new Formatter(HIGHSCOREFILE);
			output.format("%d\n", STARTHIGHSCORE);
		}
		catch (Exception error)
		{
			System.out.printf("Error creating new high score file\n");
			return;
		}

		output.close();

	}


	/////////////////////////////////////////////////////////////////////////
	//
	// Load the high score from a file
	//
	/////////////////////////////////////////////////////////////////////////

	public static void loadHighScore()
	{

		try {
			// read the high score from the file
			input = new Scanner(new File(HIGHSCOREFILE));
			highScore = input.nextInt();
		}
		catch(Exception error)
		{
			// the file does not exist yet so create it
			highScore = STARTHIGHSCORE;
			createHighScoreFile();
			return;
		}

		input.close();
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// Save the high score to a file
	//
	/////////////////////////////////////////////////////////////////////////

	public static void saveHighScore()
	{

		try {
			output = new Formatter(HIGHSCOREFILE);
			output.format("%d\n", highScore);
		}

		catch (Exception error)
		{
			System.out.printf("Error saving the high score to a file\n");
			return;
		}

		output.close();

	}

	/////////////////////////////////////////////////////////////////////////
	//
	// Check if the high score has been beaten
	// If yes, then save the new high score
	//
	/////////////////////////////////////////////////////////////////////////

	public static void checkAndUpdateHighScore()
	{

		if (miceRemoved > highScore)
		{
			highScore = miceRemoved;
			saveHighScore();
		}
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// Pick a random location in the house, and assign that location to myLocation
	//
	// This function changes the value of myLocation in the parameter
	//
	/////////////////////////////////////////////////////////////////////////

	public static void chooseRandomHouseLocation(LocationType myLocation)
	{
		int randomX = randomNum.nextInt(houseMaxCoord-houseMinCoord);
		int randomY = randomNum.nextInt(houseMaxCoord-houseMinCoord);
		myLocation.setXY(houseMinCoord + randomX, houseMinCoord + randomY);
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if the house is empty at location x,y
	// If the house is empty at this location it returns true
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean emptyLocation(int x, int y)
	{
		if (house[x][y] == ' ')
		{
			return true;
		}
		return false;
	}

	public static boolean emptyLocation(LocationType myLocation)
	{
		return emptyLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if the player is at location x,y
	// If the player is at this location it returns true
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean playerLocation(int x, int y)
	{
		if (x == player.getX() && y == player.getY())
		{
			return true;
		}
		return false;
	}

	public static boolean playerLocation(LocationType myLocation)
	{
		return playerLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if the UP stairs is at location x,y
	// If the UP stairs is at this location it returns true
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean upStairsLocation(int x, int y)
	{
		if (x == upStairs.getX() && y == upStairs.getY())
		{
			return true;
		}
		return false;
	}

	public static boolean upStairsLocation(LocationType myLocation)
	{
		return upStairsLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if the DOWN stairs is at location x,y
	// If the UP stairs is at this location it returns true
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean downStairsLocation(int x, int y)
	{
		if (x == downStairs.getX() && y == downStairs.getY())
		{
			return true;
		}
		return false;
	}

	public static boolean downStairsLocation(LocationType myLocation)
	{
		return downStairsLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if the DOWN stairs is at location x,y
	// If the UP stairs is at this location it returns true
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean exitLocation(int x, int y)
	{
		if (x == houseExit.getX() && y == houseExit.getY())
		{
			return true;
		}
		return false;
	}

	public static boolean exitLocation(LocationType myLocation)
	{
		return exitLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if a specific mouse, with the index mouseNum,
	// is at location x,y.  If the mouse is at this location it returns true.
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean mouseNumberAtLocation(int x, int y, int mouseNum)
	{
		if (x == mouse[mouseNum].getX() && y == mouse[mouseNum].getY())
		{
			return true;
		}
		return false;
	}

	public static boolean mouseNumberAtLocation(LocationType myLocation, int mouseNum)
	{
		return mouseNumberAtLocation(myLocation.getX(), myLocation.getY(), mouseNum);
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is an integer function.  It can return an integer
	//
	// This function checks if any mouse is at location x,y
	// If any mouse is at this location it returns the index of that mouse.
	// If there is no mouse then this returns -1.
	//
	/////////////////////////////////////////////////////////////////////////

	public static int mouseIndexAtLocation(int x, int y)
	{
		int mouseNum;

		for (mouseNum = 0; mouseNum < numMice; mouseNum++)
		{
			if (mouseNumberAtLocation(x, y, mouseNum))
			{
				return mouseNum;
			}
		}
		return NOMOUSEINDEX;
	}

	public static int mouseIndexAtLocation(LocationType myLocation)
	{
		return mouseIndexAtLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if any mouse is at location x,y
	// If any mouse is at this location it returns true.
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean mouseLocation(int x, int y)
	{
		if (mouseIndexAtLocation(x, y) == NOMOUSEINDEX)
		{
			return false;
		}
		return true;
	}

	public static boolean mouseLocation(LocationType myLocation)
	{
		return mouseLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if the specific dog, with the number dogNumber,
	// is at location x,y.  If that dog is at this location it returns true
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean dogNumberAtLocation(int x, int y, int dogNumber)
	{
//		if (x == dogs[dogNumber].getX() &&
//				y == dogs[dogNumber].getY())
//		{
//			return true;
//		}
//		return false;
		return dogs[dogNumber].atLocation(x,y);
	}

	public static boolean dogNumberAtLocation(LocationType myLocation, int dogNumber)
	{
		return dogNumberAtLocation(myLocation.getX(), myLocation.getY(), dogNumber);
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if any dog is at location x,y
	// If any dog is at this location it returns true
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean dogLocation(int x, int y)
	{
		int dogNum;

		for (dogNum = 0; dogNum < numDogs; dogNum++)
		{
			if (dogNumberAtLocation(x, y, dogNum))
			{
				return true;
			}
		}
		return false;
	}


	public static boolean dogLocation(LocationType myLocation)
	{
		return dogLocation(myLocation.getX(), myLocation.getY());
	}



	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if a specific mouse, with the index mouseNum,
	// is at location x,y.  If the mouse is at this location it returns true.
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean trapNumberAtLocation(int x, int y, int trapNum)
	{
		if (x == traps[trapNum].getX() && y == traps[trapNum].getY())
		{
			return true;
		}
		return false;
	}

	public static boolean trapNumberAtLocation(LocationType myLocation, int trapNum)
	{
		return trapNumberAtLocation(myLocation.getX(), myLocation.getY(), trapNum);
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is an integer function.  It can return an integer
	//
	// This function checks if any mouse is at location x,y
	// If any mouse is at this location it returns the index of that mouse.
	// If there is no mouse then this returns -1.
	//
	/////////////////////////////////////////////////////////////////////////

	public static int trapIndexAtLocation(int x, int y)
	{
		int trapNum;

		for (trapNum = 0; trapNum < numTraps; trapNum++)
		{
			if (trapNumberAtLocation(x, y, trapNum))
			{
				return trapNum;
			}
		}
		return NOTRAPINDEX;
	}

	public static int trapIndexAtLocation(LocationType myLocation)
	{
		return trapIndexAtLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function checks if any mouse is at location x,y
	// If any mouse is at this location it returns true.
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean trapLocation(int x, int y)
	{
		if (trapIndexAtLocation(x, y) == NOTRAPINDEX)
		{
			return false;
		}
		return true;
	}

	public static boolean trapLocation(LocationType myLocation)
	{
		return trapLocation(myLocation.getX(), myLocation.getY());
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// Pick an empty location in the house at random,
	// and assign that location to myLocation
	//
	// This function changes the value of myLocation in the parameter
	//
	/////////////////////////////////////////////////////////////////////////

	public static void chooseEmptyHouseLocation(LocationType myLocation)
	{
		chooseRandomHouseLocation(myLocation);

		while (!emptyLocation(myLocation))
		{
			chooseRandomHouseLocation(myLocation);
		}
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation starts by putting the stairs and the mouse outside the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void initializeStairsAndExit()
	{
		houseExit.setXY(-1,-1);
		upStairs.setXY(-1,-1);
		downStairs.setXY(-1,-1);
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds the exit to an empty location in the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addExit()
	{
		LocationType myLocation = new LocationType();

		chooseEmptyHouseLocation(myLocation);
		houseExit.setLocation(myLocation);
		house[myLocation.getX()][myLocation.getY()] = EXITCHAR;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds an up stairs to an empty location in the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addUpStairs()
	{
		LocationType myLocation = new LocationType();

		chooseEmptyHouseLocation(myLocation);
		upStairs.setLocation(myLocation);
		house[myLocation.getX()][myLocation.getY()] = UPCHAR;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds a down stairs to an empty location in the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addDownStairs()
	{
		LocationType myLocation = new LocationType();

		chooseEmptyHouseLocation(myLocation);
		downStairs.setLocation(myLocation);
		house[myLocation.getX()][myLocation.getY()] = DOWNCHAR;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds an exit and stairs to the house if necessary
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addExitandStairs()
	{
		initializeStairsAndExit();
		if (topFloor()) addExit();
		if (!bottomFloor()) addDownStairs();
		if (!topFloor()) addUpStairs();

	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds the player to an empty location in the house
	// The player is always added close to the center of the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addPlayer()
	{
		LocationType myLocation = new LocationType();

		chooseEmptyHouseLocation(myLocation);
		while(!closerToCenter(myLocation.getX(),myLocation.getY(),TOOCLOSE)) chooseEmptyHouseLocation(myLocation);
		player.setLocation(myLocation);
		house[myLocation.getX()][myLocation.getY()] = CATCHAR;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds a specific dog, with the number dogNumber,
	// to an empty location in the house
	// The dog is never added close to the center of the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addOneDog(int dogNumber)
	{
		DogType myLocation = new DogType(dogNumber, house, player, DOGCHAR);

		chooseEmptyHouseLocation(myLocation);
		while(closerToCenter(myLocation.getX(),myLocation.getY(),TOOCLOSE)) chooseEmptyHouseLocation(myLocation);
		dogs[dogNumber].setLocation(myLocation);
		house[myLocation.getX()][myLocation.getY()] = DOGCHAR;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds all dogs to empty locations in the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addManyDogs()
	{
		int dogNumber;

		for (dogNumber = 0; dogNumber < numDogs; dogNumber++)
		{
			addOneDog(dogNumber);
		}
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds one mouse to an empty location in the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addOneMouse(int mouseNumber)
	{
		MouseType myLocation = new MouseType(mouseNumber, house, player, MOUSECHAR);

		chooseEmptyHouseLocation(myLocation);
		mouse[mouseNumber].setLocation(myLocation);
		house[myLocation.getX()][myLocation.getY()] = MOUSECHAR;
	}


	/////////////////////////////////////////////////////////////////////////////
	//
	// This funcation copies one mouse from one position of the array to another
	//
	/////////////////////////////////////////////////////////////////////////////

	public static void copyMouse(int toIndex, int fromIndex)
	{
		int x, y;

		mouse[toIndex].setLocation(mouse[fromIndex]);
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This funcation adds all mouse to empty locations in the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void addManyMice()
	{
		int mouseNumber;

		for (mouseNumber = 0; mouseNumber < numMice; mouseNumber++)
		{
			addOneMouse(mouseNumber);
		}
	}

	public static void addOneTrap(int trapNumber)
	{
		TrapType myLocation = new TrapType(trapNumber, house, player, TRAPCHAR);

		chooseEmptyHouseLocation(myLocation);
		while(closerToCenter(myLocation.getX(),myLocation.getY(),TOOCLOSE)) chooseEmptyHouseLocation(myLocation);
		traps[trapNumber].setLocation(myLocation);
		house[myLocation.getX()][myLocation.getY()] = TRAPCHAR;
	}

	public static void addManyTraps()
	{
		int trapNumber;

		for (trapNumber = 0; trapNumber < numTraps; trapNumber++)
		{
			addOneTrap(trapNumber);
		}
	}
	public static void copyTraps(int toIndex, int fromIndex)
	{
		int x, y;

		traps[toIndex].setLocation(traps[fromIndex]);
	}


	/////////////////////////////////////////////////////////////////////////
	//
	//  This function prints out the menu, asking the user to give a direction
	//
	/////////////////////////////////////////////////////////////////////////

	public static void printMenu()
	{
		System.out.printf("Which direction (N)orth, (S)outh, (E)ast, (W)est or (Q)uit: ");
	}


	/////////////////////////////////////////////////////////////////////////
	//
	//  This function reads one character from the user
	//
	//  The character read is converted to uppercase and is returned
	//
	/////////////////////////////////////////////////////////////////////////

	public static char getCommand()
	{
		String commandStr;
		char commandChar = 'x';

		input = new Scanner(System.in);

		commandStr = input.nextLine();
		if (commandStr.length() > 0) commandChar = commandStr.charAt(0);
		commandChar = Character.toUpperCase(commandChar);

		return Character.toUpperCase(commandChar);
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This function waits for the user to press ENTER
	//
	/////////////////////////////////////////////////////////////////////////

/*	public static void pause()
	{
		String commandStr;
		char commandChar = 'x';

		System.out.printf ("Press ENTER to continue...");
		commandStr = input.nextLine();

	}*/


	/////////////////////////////////////////////////////////////////////////
	//
	// This function changes the location of the player to x,y
	//
	/////////////////////////////////////////////////////////////////////////

	public static void changePlayerLocation(int x, int y)
	{
		house[player.getX()][player.getY()] = ' ';

		house[x][y] = CATCHAR;

		player.setXY(x,y);
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This function moves the player according to the command
	// The command should be 'N', 'S', 'E', or 'W'
	//
	// The player will move North, South, East, or West according to the
	// command character unless there is an obstacle in that location.
	// If there is an obstacle in that location then the player does not move
	//
	/////////////////////////////////////////////////////////////////////////

	public static void movePlayer(char directionChar)			//use changePlayerLocation check the location is empty
	{
	//	int i=0;
		int x = player.getX();
		int y = player.getY();
		if(flag_trap==0){
		switch (directionChar) {
			case 'N':x = x - 1;break;
			case 'S':x = x + 1;break;
			case 'E':y = y + 1;break;
			case 'W':y = y - 1;break;
			default:break;
			}
		if(emptyLocation(x,y)||(house[x][y]==DOGCHAR)||(house[x][y]==MOUSECHAR)||(house[x][y]==UPCHAR)||(house[x][y]==DOWNCHAR)||(house[x][y]==EXITCHAR)||(house[x][y]==TRAPCHAR))
		{
			changePlayerLocation(x,y);
			if(flag_rush==1){
				switch (directionChar) {
					case 'N':x = x - 1;break;
					case 'S':x = x + 1;break;
					case 'E':y = y + 1;break;
					case 'W':y = y - 1;break;
					default:break;
				}
				if(emptyLocation(x,y)||(house[x][y]==DOGCHAR)||(house[x][y]==MOUSECHAR)||(house[x][y]==UPCHAR)||(house[x][y]==DOWNCHAR)||(house[x][y]==EXITCHAR)||(house[x][y]==TRAPCHAR))
					changePlayerLocation(x,y);
				sum = sum+1;
				if(sum ==3)
				{
					flag_rush=0;
					sum=0;
				}
			}
		}
		}
		if(flag_trap==1)
		{
			switch (directionChar) {
				case 'N':
					x = x - 1;
					break;
				case 'S':
					x = x + 1;
					break;
				case 'E':
					y = y + 1;
					break;
				case 'W':
					y = y - 1;
					break;
				default:
					break;
			}
			sum2++;
			if(sum2==1)
			{
				flag_trap=0;
				sum2=0;
			}
		}

	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This function moves the specific dog, with the number dogNumber,
	// towards the player.  The direction is chosen to be North-South or
	// East-West at random.  If the dog cannot move in that direction
	// then the other direction is attempted.  If the dog cannot move in
	// either direction, then it does not move.
	//
	/////////////////////////////////////////////////////////////////////////

	public static void moveOneDog(int dogNumber)
	{
		dogs[dogNumber].moveDog();
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This function moves all of the dogs
	//
	/////////////////////////////////////////////////////////////////////////

	public static void moveAllDogs()
	{
		int i;

		for (i=0; i<numDogs; i++)
		{
			moveOneDog(i);
		}
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This function moves the mouse away from the player.
	//
	// The direction is chosen to be North-South or
	// East-West at random.  If the mouse cannot move in that direction
	// then the other direction is attempted.  If the mouse cannot move in
	// either direction, then it does not move.
	//
	/////////////////////////////////////////////////////////////////////////

	public static void moveOneMouse(int mouseNum)
	{
		mouse[mouseNum].moveMouse();
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This function moves all of the mice
	//
	/////////////////////////////////////////////////////////////////////////

	public static void moveAllMice()
	{
		int i;
		for (i=0; i<numMice; i++)
		{
			moveOneMouse(i);
		}

	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This function collects the mouse if the player has found a mouse
	//
	/////////////////////////////////////////////////////////////////////////

	public static void collectMouse()
	{
		int mouseIndex = mouseIndexAtLocation(player);

		// If there is no mouse here then quit this function
		if (mouseIndex == NOMOUSEINDEX) return;

		// Swap the last mouse in the array into this index
		copyMouse(mouseIndex, numMice-1);
		numMice--;
		miceCarried++;

	}

	public static void TrapFunction()
	{
		int trapIndex = trapIndexAtLocation(player);

		// If there is no mouse here then quit this function
		if (trapIndex == NOTRAPINDEX) return;
		flag_trap=1;
		// Swap the last mouse in the array into this index
		copyTraps(trapIndex, numTraps-1);
		numTraps--;
	}

	public void starthouse()
	{
		buildHouseWalls();
		buildHouseObstacles();
		// Add the dogs, mice, cat, exit, and stairs to the house
		addManyDogs();
		addManyMice();
		addManyTraps();

		addPlayer();
		addExitandStairs();
		printHouseAndKey();
	}
	/////////////////////////////////////////////////////////////////////////
	//
	// This function moves the player and the dogs and the mice.
	//
	/////////////////////////////////////////////////////////////////////////

	public void movePlayerAndDogsAndMice(char directionChar)
	{
		// Move the player according to the direction entered
		movePlayer(directionChar);
		collectMouse();
		TrapFunction();
		// Move the dogs and mouse according to their moving strategy
		moveAllDogs();
		// Move the mouse only if we are in house 3 or later
		if (houseNumber >= 3) moveAllMice();
		// If the player is reached the up stairs, climb up
		if (upStairsLocation(player))
		{
			climbUpStairs();
			starthouse();
		}

		// If the player is reached the down stairs, climb down
		if (downStairsLocation(player))
		{
			climbDownStairs();
			starthouse();
		}

		if (houseFinished())
		{
			printHouseAndKey();
			printWinMessage();
		//	pause();

			startHouseNumber(houseNumber + 1);

		}

		// If the player loses, then quit the program
		if (gameLost())
		{
			flag=2;
			printHouseAndKey();
			printLoseMessage();

			return;
		}
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This function changes the size of the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void changeHouseSize(int newHouseSize)
	{
		houseSize=newHouseSize;
		houseMinCoord = (HOUSESIZE-houseSize)/2;
		houseMaxCoord = HOUSESIZE-(HOUSESIZE-houseSize)/2;
		houseMidCoord = (houseMinCoord + houseMaxCoord-1)/2;
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This function climbs up the stairs to the
	// next higher level of the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void climbUpStairs()
	{
		floorNum++;
		// Increase the size of the house
		changeHouseSize(houseSize-houseSizeIncrement);
		// Add one dog for the next level
		if (numDogs < MAXDOGS) numDogs++;
		if (numTraps < MAXTRAPS) numTraps++;
		// If we have not visited this level yet, then add mouse
		if (maxFloorNum < floorNum){
			maxFloorNum = floorNum;
			numMice = numDogs * houseNumber;
		} else {
			// If we have already visited this level, then there
			// are no mice on this level
			numMice = 0;
		}
	}


	/////////////////////////////////////////////////////////////////////////
	//
	// This function climb down the stairs to the
	// next lower level of the house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void climbDownStairs()
	{
		floorNum--;
		// Decrease the size of the house
		changeHouseSize(houseSize+houseSizeIncrement);
		// Remove one dog for the next level
		numDogs--;
		numTraps--;

		numMice = 0;
	}



	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function returns true if the player has reached the exit,
	// meaning that the house is finished.
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean houseFinished()
	{
		if (exitLocation(player))
		{
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This is a boolean function.  It can return true or false
	//
	// This function returns true if a dog has reached the player,
	// meaning that the game is lost.
	//
	/////////////////////////////////////////////////////////////////////////

	public static boolean gameLost()
	{
		if (dogLocation(player))
		{
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This function prints a message informing the user that he has won
	//
	/////////////////////////////////////////////////////////////////////////

	public void printWinMessage()
	{
		flag =1;
		if(miceCarried==1)
			num2TextField.setText("*** You Escaped with "+String.format("%d",miceCarried)+" mouse! *** press \"continue\" to continue");
		else
			num2TextField.setText("*** You Escaped with "+String.format("%d",miceCarried)+" mice! ***");
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This function prints a message informing the user that he has lost
	//
	/////////////////////////////////////////////////////////////////////////

	public void printLoseMessage()
	{
		num2TextField.setText(" You were Caught... T.T "+"\r\n"+"    Your score is "+String.format("%d",miceRemoved));
	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This function starts the player in a new house
	//
	/////////////////////////////////////////////////////////////////////////

	public static void startHouseNumber(int number)
	{
		houseNumber=number;
		houseSizeIncrement = (HOUSESIZE - MINHOUSESIZE) / number;
		// Start each house with the biggest possible house
		changeHouseSize(HOUSESIZE);
		// Start each house with 1 dog
		numDogs=1;
		numTraps = 1;
		// Start each house with the same amount of mice as the house number
		numMice=houseNumber;
		// Start on the bottom floor, and reset the highest visited floor
		floorNum=1;
		maxFloorNum=floorNum;

		// Remove all mice carried
		miceRemoved += miceCarried;
		miceCarried = 0;

		// Update the high score with the new mice removed
		checkAndUpdateHighScore();

	}

	/////////////////////////////////////////////////////////////////////////
	//
	// This function initializes the global variables
	//
	/////////////////////////////////////////////////////////////////////////

	public static void initVariables()
	{
		int i;
		// Seed the random number generator
		randomNum = new Random();
		house = new char [HOUSESIZE][HOUSESIZE];
		player = new LocationType();
 		dogs = new DogType [MAXDOGS];
 		traps = new TrapType[MAXTRAPS];
		for (i=0; i<MAXDOGS; i++) dogs[i] = new DogType(i, house, player, DOGCHAR);
		mouse = new MouseType [MAXMICE];
		for (i=0; i<MAXMICE; i++) mouse[i] = new MouseType(i,house,player,MOUSECHAR);
		for (i=0; i<MAXTRAPS; i++) traps[i] = new TrapType(i, house, player, TRAPCHAR);
		upStairs = new LocationType();
		downStairs = new LocationType();
		houseExit = new LocationType();
	}


	///////////////////
	// main function //
	///////////////////


	public static void main(String [] args)
	{
		//char command;
		CatDogMouse frame1 = new CatDogMouse("CatDogMouse");


		// Load the previous high score from the high score file
		loadHighScore();

		// Start the player in the first house
		startHouseNumber(1);


		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(800,800);
		frame1.setVisible(true);
	}


}
