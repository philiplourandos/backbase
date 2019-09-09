This module contains the mankala game. The `Game.java` contains the logic for the game.

The state of the game is maintained in this class. That being:

* The current player
* Stones in the pits
* Stones in the house


Once the game is created via the constructor, Player1 is set to start. The 
`playTurn` method takes the `startingPitIndex` which is where the stones will be 
taken from. Validation is performed to confirm that the player can take stones 
from the required pit. If so, we are then able to seed the stones based on the 
games rules.
The `playTurn` method returns a boolean indicating if the 'turn was played', if
not it indicates an error in the selection of the pit, otherwise the player will
be able to play. 
Based on the spec there is no REST API call to determine if the game is complete.
When the game is complete the `playTurn` method will also indicate the turn has 
not been played. The method to determine the game is over should rather be exposed
and a REST method available to determine if the game can progress.