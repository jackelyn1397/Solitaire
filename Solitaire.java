import java.util.*;
/**
 * Plays a game of Solitaire.
 * 
 * @author Jackelyn Shen
 * @version May 26, 2014
 */
public class Solitaire
{
    //creates a new game
    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;
    private int shuffle=0;

    //sets up the initial solitaire display and runs the game
    public Solitaire()
    {
        foundations = new Stack[4];
        piles = new Stack[7];

        //INSERT CODE HERE

        for(int i=0; i<7; i++)
            piles[i]=new Stack();

        for(int i=0; i<4; i++)
            foundations[i]=new Stack();

        waste=new Stack();
        stock=new Stack();
        createStock();
        deal();
        display = new SolitaireDisplay(this);
    }

    //returns the card on top of the stock,
    //or null if the stock is empty
    public Card getStockCard()
    {
        if(stock.isEmpty())
        {
            return null;
        }
        else
        {
            return stock.peek();
        }
    }

    //returns the card on top of the waste,
    //or null if the waste is empty
    public Card getWasteCard()
    {
        if(waste.isEmpty())
        {
            return null;
        }
        else
        {
            return waste.peek();
        }
    }

    //precondition:  0 <= index < 4
    //postcondition: returns the card on top of the given
    //               foundation, or null if the foundation
    //               is empty
    //param index  the foundation number selected
    public Card getFoundationCard(int index)
    {
        if(foundations[index].isEmpty())
        {
            return null;
        }
        else
        {
            return foundations[index].peek();
        }
    }

    //precondition:  0 <= index < 7
    //postcondition: returns a reference to the given pile
    //param index  the pile number selected
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    }

    //creates a deck of 52 cards and randomly puts them in the stock
    public void createStock()
    {
        ArrayList<Card> deck=new ArrayList<Card>();
        for(int i=1; i<14; i++)
        {
            deck.add(new Card(i,"h"));
        }
        for(int i=1; i<14; i++)
        {
            deck.add(new Card(i,"d"));
        }
        for(int i=1; i<14; i++)
        {
            deck.add(new Card(i,"c"));
        }
        for(int i=1; i<14; i++)
        {
            deck.add(new Card(i,"s"));
        }
        int count=52;
        while(deck.size()>0)
        {
            Card x=deck.remove((int)(Math.random()*count));
            stock.push(x);
            count--;
        }
    }

    //sets up the initial number of cards in each of the piles, turns the top card face up
    public void deal()
    {

        for(int i=1; i<8; i++)
        {
            for(int j=0; j<i; j++)
            {
                Card x=piles[i-1].push(stock.pop());
                if(j+1==i)
                {
                    x.turnUp();
                }
            }
        }
    }

    //places three cards into the waste from the stock
    public void dealThreeCards()
    {
        for(int i=0; i<3; i++)
        {
            if(!stock.isEmpty())
            {
                Card x=stock.pop();
                waste.push(x);
                x.turnUp();
            }
        }
    }

    //without shuffling, transfers all card from the waste to the stock, face down
    public void resetStock()
    {
        while(!waste.isEmpty())
        {
            Card x=waste.pop();
            stock.push(x);
            x.turnDown();
        }
    }

    //called when the stock is clicked
    public void stockClicked()
    {
        //IMPLEMENT ME
        if(!stock.isEmpty())
        {
            if(!display.isWasteSelected() && !display.isPileSelected())
            {
                dealThreeCards();
            }
        }
        else
        {
            resetStock();
        }
        System.out.println("stock clicked");
    }

    //called when the waste is clicked
    public void wasteClicked()
    {
        //IMPLEMENT ME
        System.out.println("waste clicked");
        if(display.isWasteSelected())
        {
            display.unselect();
        }
        else if(!waste.isEmpty())
        { 
            if(!display.isWasteSelected() && !display.isPileSelected())
            {
                display.selectWaste();
            }
        }
        else if(waste.isEmpty())
        {
            shuffle++;
            if(shuffle>=4)
            {
                shuffle();
                shuffle=0;
            }
        }
    }

    //precondition:  0 <= index < 4
    // param index  the foundation number clicked
    //called when given foundation is clicked
    public void foundationClicked(int index)
    {
        //IMPLEMENT ME
        if(display.isPileSelected())
        {
            Stack<Card> x=getPile(display.selectedPile());
            if(!x.isEmpty())
            {
                if(canAddToFoundation(x.peek(), index))
                {
                    foundations[index].push(x.pop());
                    display.unselect();
                }
            }
        }
        else if(display.isWasteSelected())
        {
            if(canAddToFoundation(getWasteCard(), index))
            {
                foundations[index].push(waste.pop());
                display.unselect();
            }
        }
        System.out.println("foundation #" + index + " clicked");
        int count=0;
        while(count!=-1)
        {
            if(getFoundationCard(count)!=null)
            {
                if(getFoundationCard(count).getRank()==13)
                {
                    if(count>=3)
                    {
                        System.out.println("Congratulations! You win!");
                        count=-1;
                    }
                    else
                    {
                        count++;
                    }
                }
                else
                {
                    count=-1;
                }
            }
            else
            {
                count=-1;
            }
        }
    }

    //precondition:  0 <= index < 7
    //param index  the pile number clicked
    //called when given pile is clicked
    public void pileClicked(int index)
    {
        //IMPLEMENT ME

        if(!display.isPileSelected() && !display.isWasteSelected())
        {
            if(!getPile(index).isEmpty())
            {
                if(getPile(index).peek().isFaceUp())
                {
                    display.selectPile(index);
                }
                else
                {
                    getPile(index).peek().turnUp();
                }
            }
        }
        else if(display.isWasteSelected() && canAddToPile(getWasteCard(),index))
        {
            Card x=waste.pop();
            getPile(index).push(x);
            display.unselect();
        }
        else if(display.isPileSelected() && display.selectedPile()!=index)
        {
            Stack<Card> x=removeFaceUpCards(display.selectedPile());
            if(!x.isEmpty())
            {
                if(canAddToPile(x.peek(),index))
                {
                    addToPile(x,index);
                }
                else
                {
                    addToPile(x, display.selectedPile());
                }
            }
        }
        else if(display.isPileSelected() && display.selectedPile()==index)
        {
            display.unselect();
        }

        System.out.println("pile #" + index + " clicked");
    }

    //precondition:  0 <= index < 7
    //postcondition: Returns true if the given card can be
    //               legally moved to the top of the given
    //               pile
    //param card  the card for which adding it to the pile will be determined
    //param index  the pile number selected
    private boolean canAddToPile(Card card, int index)
    {
        if(card!=null && !getPile(index).isEmpty())
        {
            Card pile=getPile(index).peek();
            if(pile.isFaceUp() && card.getRank()+1==pile.getRank())
            {
                if(pile.getSuit().equals("h") || pile.getSuit().equals("d"))
                {
                    return card.getSuit().equals("c") || card.getSuit().equals("s");
                }
                else
                {
                    return card.getSuit().equals("h") || card.getSuit().equals("d");
                }
            }
        }
        else if(card!=null && card.getRank()==13)
        {
            return true;
        }
        return false;
    }

    //precondition:  0 <= index < 7
    //postcondition: Removes all face-up cards on the top of
    //               the given pile; returns a stack
    //               containing these cards
    //param index  the pile number selected
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> cards=new Stack<Card>();
        if(!getPile(index).isEmpty())
        {
            while(getPile(index).peek().isFaceUp())
            {
                cards.push(getPile(index).pop());
                if(getPile(index).isEmpty())
                {
                    return cards;
                }
            }
        }
        return cards;
    }

    //precondition:  0 <= index < 7
    //postcondition: Removes elements from cards, and adds
    //               them to the given pile.
    //param card  the stack of cards to be added to the pile
    //param index  the pile number selected
    private void addToPile(Stack<Card> cards, int index)
    {
        for(int i=0; i<cards.size(); i++)
        {
            if(!cards.isEmpty())
            {
                getPile(index).push(cards.pop());
                i--;
            }
        }
    }

    //precondition:  0 <= index < 4
    //postcondition: Returns true if the given card can be
    //               legally moved to the top of the given
    //               foundation
    //param card  the card for which addint to the foundation will be determined
    //param index  the foundation number selected
    private boolean canAddToFoundation(Card card, int index)
    {
        if(foundations[index].size()==0)
        {
            if(card.getRank()==1)
            {
                return true;
            }
        }
        else
        {
            Card top=getFoundationCard(index);
            if(top.getRank()+1==card.getRank() && top.getSuit().equals(card.getSuit()))
            {
                return true;
            }
        }
        return false;
    }

    public void shuffle()
    {
        ArrayList<Card> shuffle=new ArrayList();
        while(!stock.isEmpty())
        {
            Card x=stock.pop();
            shuffle.add(x);
        }
        while(shuffle.size()>0)
        {
            int num=(int)(Math.random()*shuffle.size());
            stock.push(shuffle.get(num));
            shuffle.remove(num);
        }
        System.out.println("Stock shuffled.");
    }

}