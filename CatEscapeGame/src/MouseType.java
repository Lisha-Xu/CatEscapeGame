// MouseType.java
// This class describes a mouse

import java.util.Random;

public class MouseType extends LocationType
{

    int myNumber;
    char mouseChar;
    char [] [] house;
    LocationType player;
    Random randomNum;
    static int numMice = 0;

    // Define an axis to be either vertical (NORTHSOUTH)
    // or horizontal (EASTWEST)
    static final int NORTHSOUTH = 0;
    static final int EASTWEST = 1;

    /////////////////////////////////////////////////////////////////////////
    //
    // This is the constructor for the MouseType class
    //
    /////////////////////////////////////////////////////////////////////////


    public MouseType(int newMouseNumber, char [] [] newHouse,
                   LocationType newPlayer, char newMouseChar)
    {
        super();
        myNumber = newMouseNumber;
        mouseChar = newMouseChar;
        house = newHouse;
        player = newPlayer;
        randomNum= new Random();
        numMice++;
    }


    /////////////////////////////////////////////////////////////////////////
    //
    // This function returns a count of how many mouses were created
    //
    /////////////////////////////////////////////////////////////////////////



    public static int getNumMouses()
    {
        return numMice;
    }

    /////////////////////////////////////////////////////////////////////////
    //
    // This function changes the location of the mouse to x,y
    //
    /////////////////////////////////////////////////////////////////////////

    public void changeLocation(int x, int y)
    {
        house[this.getX()][this.getY()] = ' ';
        house[x][y] = mouseChar;
        this.setXY(x,y);
    }

    /////////////////////////////////////////////////////////////////////////
    //
    // This is a boolean function.  It can return true or false
    //
    // This function checks if the mouse is at location x,y.
    // If the mouse is at this location it returns true
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

    /////////////////////////////////////////////////////////////////////////
    //
    // This function moves the mouse either North or South
    //
    // The mouse always moves North or South towards the player
    // unless there is an obstacle in that location.  If there is an obstacle
    // blocking the way then the mouse does not move
    //
    // This function returns true if the mouse actually moved
    //
    /////////////////////////////////////////////////////////////////////////


    public boolean moveNS()
    {
        int newX = this.getX();
        int newY = this.getY();

        if (newX < player.getX())
        {
            newX --;
        }
        else if (newX > player.getX())
        {
            newX++;
        }

        if (CatDogMouse.emptyLocation(newX, newY) ||
                CatDogMouse.playerLocation(newX, newY))
        {
            this.changeLocation(newX, newY);
            return true;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //
    // This function moves the mouse either East or West
    //
    // The mouse always moves East or West towards the player
    // unless there is an obstacle in that location.  If there is an obstacle
    // blocking the way then the mouse does not move
    //
    // This function returns true if the mouse actually moved
    //
    /////////////////////////////////////////////////////////////////////////

    public boolean moveEW()
    {
        int newX = this.getX();
        int newY = this.getY();

        if (newY < player.getY())
        {
            newY--;
        }
        else if (newY > player.getY())
        {
            newY++;
        }

        if (CatDogMouse.emptyLocation(newX, newY) ||
                CatDogMouse.playerLocation(newX, newY))
        {
            this.changeLocation(newX, newY);
            return true;
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////////////
    //
    // This function moves the mouse either along the specified axis.
    // The axis can be NORTHSOUTH or EASTWEST
    //
    // The mouse always moves along that axis towards the player
    // unless there is an obstacle in that location.  If there is an obstacle
    // blocking the way then the mouse does not move
    //
    // This function returns true if the mouse actually moved
    //
    /////////////////////////////////////////////////////////////////////////

    public boolean moveOnAxis(int axis)
    {
        boolean moved = false;

        if (axis == NORTHSOUTH)
        {
            moved=moveNS();
        }
        else if (axis == EASTWEST)
        {
            moved=moveEW();
        }

        return moved;

    }
    /////////////////////////////////////////////////////////////////////////
    //
    // This function moves the mouse towards the player.
    // The direction is chosen to be North-South or
    // East-West at random.  If the mouse cannot move in that direction
    // then the other direction is attempted.  If the mouse cannot move in
    // either direction, then it does not move.
    //
    /////////////////////////////////////////////////////////////////////////

    public void moveMouse()
    {
        boolean moved;
        int axis;

        // Choose the vertical or horizontal axis at random
        axis = randomNum.nextInt(2);

        moved = moveOnAxis(axis);

        // If the mouse was not successfully moved
        // try again in the other direction
        if (!moved)
        {
            axis ++;
            axis %= 2;
            moveOnAxis(axis);
        }
    }


}