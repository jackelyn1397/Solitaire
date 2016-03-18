
/**
 * Creates a Card object for a Solitaire game.
 * 
 * @author Jackelyn Shen
 * @version May 26, 2014
 */
public class Card
{
    // instance variables - replace the example below with your own
    private int rank;
    private String suit;
    private boolean isFaceUp;

    /**
     * Constructor for objects of class Card
     */
    public Card(int r, String s)
    {
        rank=r;
        suit=s;
    }
    public int getRank()
    {
        return rank;
    }
    public String getSuit()
    {
        return suit;
    }
    public boolean isFaceUp()
    {
        return isFaceUp;
    }
    public void turnUp()
    {
        isFaceUp=true;
    }
    public void turnDown()
    {
        isFaceUp=false;
    }
    public String getFileName()
    {
        if(isFaceUp==false)
        {
            return "cards/back.gif";
        }
        else
        {
            if(getRank()>1 && getRank()<10)
            {
                return "cards/"+getRank()+getSuit()+".gif";
            }
            if(getRank()==1)
            {
                return "cards/a"+getSuit()+".gif";
            }
            if(getRank()==10)
            {
                return "cards/t"+getSuit()+".gif";
            }
            if(getRank()==11)
            {
                return "cards/j"+getSuit()+".gif";
            }
            if(getRank()==12)
            {
                return "cards/q"+getSuit()+".gif";
            }
            else
            {
                return "cards/k"+getSuit()+".gif";
            }
        }
    }
}
