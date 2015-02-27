import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {


public final static int NUM_ROWS = 20;
public final static int NUM_COLS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs; //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );   

    buttons = new MSButton[NUM_ROWS][NUM_COLS]; 
    for(int i = 0; i < NUM_ROWS; i++)
    {
        for(int j = 0; j < NUM_COLS; j++)
        {
           buttons[i][j] = new MSButton(i,j);
        }
    }
    //declare and initialize
    bombs = new ArrayList <MSButton>();
    setBombs();
}
public void setBombs()
{
    for(int i = 0; i < 40; i++)
    {
        int row = (int)(Math.random()*NUM_ROWS);
        int col = (int)(Math.random()*NUM_COLS);
        if(!bombs.contains(buttons[row][col]))
        {
            bombs.add(buttons[row][col]);
        }
    }
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    int count = 0;
    for(int i = 0; i <bombs.size(); i++)
    {
        if(bombs.get(i).isMarked()){
            count++;
        }
    }
    if(count == bombs.size())
    {
        return true;
    }
    return false;
}
public void displayLosingMessage()
{
    buttons[8][4].setLabel("Y");
    buttons[8][5].setLabel("O");
    buttons[8][6].setLabel("U");
    buttons[8][7].setLabel(" ");
    buttons[8][8].setLabel("L");
    buttons[8][9].setLabel("O");
    buttons[8][10].setLabel("S");
    buttons[8][11].setLabel("E");
    stop();

}
public void displayWinningMessage()
{
    buttons[8][4].setLabel("Y");
    buttons[8][5].setLabel("O");
    buttons[8][6].setLabel("U");
    buttons[8][7].setLabel(" ");
    buttons[8][8].setLabel("W");
    buttons[8][9].setLabel("I");
    buttons[8][10].setLabel("N");
    stop();
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {    
        if(mouseButton == RIGHT){
            marked = !marked;
        } else if(bombs.contains(this)) {
            displayLosingMessage();
        } else if(countBombs(r, c) > 0) {
            setLabel("" + countBombs(r, c));
        } else if(!clicked) {
            clicked = true;
            if(isValid(r, c)){
                for (int i=-1; i<2; i++)
                {
                    for (int j=-1; j<2; j++)
                    {
                        if (isValid(r+i, c+j)==true)
                        {
                            if (buttons[r+i][c+j].isClicked()==false)
                            {
                                buttons[r+i][c+j].mousePressed();
                            }
                        }
                    }
                }
            }
        }

        clicked = true;
    }

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
            fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        return (r>=0 && r<NUM_ROWS && c>=0 && c<NUM_COLS);
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        for ( int i = -1; i<2; i++)
            for (int j = -1; j<2; j++)
                if (isValid(row + i, col + j))
                    if (bombs.contains(buttons[row+i][col+j]))
                        numBombs++;
        return numBombs;
    }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
