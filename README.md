# PebbleGame

In  this  assignment  you  are  to  implement  a  game  involving  multiple  competing  players,  in  a 
thread-safe fashion. The game being modelled has a strictly positive number of players. There 
are six bags of pebbles in the game, three white bags (A, B and C) and three black bags (X, Y and 
Z). At the start of the game the white bags are empty and the black bags are full. Each player 
takes 10 pebbles from a black bag (the black bag each player selects is chosen at random). Each 
pebble has an integer weight value. 
 
If  the  weight  of  pebbles  held  by  a  player  is  exactly  100,  then  they  have  won.  They  should 
immediately announce this fact to other players, and all other simulated players should stop 
playing. If a player does not hold a winning hand, they should discard a pebble to a white bag. 
They should then select one of the three black bags at random and draw a new pebble from this 
bag. This process should continue until either the player has won (has 10 pebbles with a total 
combined weight of 100), or until another player has won and the game has ended.  
 
On starting, the command-line program should request the number of players, and after this 
has been entered it should then request the location of three files in turn containing the weight.
