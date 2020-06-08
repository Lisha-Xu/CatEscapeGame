// TrapType.java
// This Class Describes a Trap

import java.util.Random;

public class TrapType extends LocationType
{

    int myNumber;
    char trapChar;
    char [] [] house;
    LocationType player;
    Random randomNum;
    static int numTraps = 0;

    // Define an axis to be either vertical (NORTHSOUTH) 
    // or horizontal (EASTWEST)
    static final int NORTHSOUTH = 0;
    static final int EASTWEST = 1;

    /////////////////////////////////////////////////////////////////////////
    //
    // This is the constructor for the TrapType class
    //
    /////////////////////////////////////////////////////////////////////////


    public TrapType(int newTrapNumber, char [] [] newHouse,
                   LocationType newPlayer, char newTrapChar)
    {
        super();
        myNumber = newTrapNumber;
        trapChar = newTrapChar;
        house = newHouse;
        player = newPlayer;
        randomNum= new Random();
        numTraps++;
    }


    /////////////////////////////////////////////////////////////////////////
    //
    // This function returns a count of how many traps were created
    //
    /////////////////////////////////////////////////////////////////////////



    public static int getNumTraps()
    {
        return numTraps;
    }


    /////////////////////////////////////////////////////////////////////////
    //
    // This is a boolean function.  It can return true or false
    //
    // This function checks if the trap is at location x,y.  
    // If the trap is at this location it returns true
    //
    /////////////////////////////////////////////////////////////////////////


    public boolean atLocation(int x, int y)
    {
        if (x == this.getX() && y == this.getY())
        {
            return true;
        }
        return false;
    }
}
