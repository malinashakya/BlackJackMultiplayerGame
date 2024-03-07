
import blackjackmultiplayergame.game_type.GameType;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author malin
 */
public class  GameTypeAJQKare10 extends GameType{
   public GameTypeAJQKare10()
    {
        System.out.println("You have selected the Win if A, J, K, Q are equal to 10 condition");
    }
    @Override
    public int evaluate (int card1,int card2, int card3)
    {
        return sumvalueJQKare10(card1, card2, card3);
    }
    @Override
    public int calculate(int card1,int card2, int card3)
    {
        return JQKare10(card1,card2,card3)?1:0;
    }
}
