# Invisi-Bomberman
A game of bomberman with multiplayer support. The game does not support single player.
Multiplayer is done over the network.

## Networking: Messenger, Server
Networking is achieved in a similar way as HW7. The core of the networking class is the Messenger class
and its subclasses. The messenger class has two private class members InputHandler and OutputHandler.
They do what you expect. InputHandler handles the incoming data from one socket. Output handler data
from multiple sockets. Each instance of InputHandler is ran in a different thread. There is only one OutputHandler
thread. They load and send messages to / from a buffer which is accessed via the getMessage and sendMessage methods.

The server makes the calculations of object updates and listens to keyboard input via the InputMessage. Upon recieving
input it takes appropriate actions such as placing a bomb or moving a player and tells the client how to display this.
Note the client does not do any update calculations.

The networking code is designed to send serializable objects, which are another aspect of my design.

## Inheritence and Subtyping for dynamic dispatch
I ended up not making power ups, because I changed my design such that I used dyn dispatch in Messages and Updatable
and also a bit in Destructible.

This is used for two things, so you can take your pick, but I think the implementation of Messages is neater, I do
also use dynamic dispatch from objects that implement Updatable: Bomb, Explosion.

The messages use dynamic dispatch every single time. There is a method called getType from which their type can
be deduced via enum. They are then cast to an appropriate type and thus their fields can be accessed. Messages
were originally supposed to be used to sync objects (ie create a serializable datapoint containing the same info
as the actuall objec) however for my purposes this was too much. Messages got from the Messenger class are processed
by the respective ClientModel or ServerModel, which calculate the appropriate response. For example,
only the client responds to the CreateMessage, since it would be unsafe (cheating (as if someone would cheat))
for the server to create objects upon clients request.

## 2D arary: Grid
The grid is the shared part of the client and server models. It is responsible for initializing the initial
state of the obstacles. There is really not much to it. The grid is just a fancy wrapper around an array of Tiles
which contain TileObjects. TileObjects can be null (duh) since not all Tiles are filled. The Grid class is just
a fancy wrapper, but it does not fully encapsulate everything. Everything is encapsulated at the model levels.
Some objects, like the player, mutate the grid. Like when placing a bomb, or moving, or respawning. The grid
also keeps track of all the updatable objects so that it does not have to iterate through itself every single time it
needs to move the player.

## Recursion: Explode
I mean it explodes. The design hasn't changed. When it explodes it creates a duplicate of itself, which creates a
duplicate and so forth until it reaches maxed range or blows something up. This is tail recursive since it
decrements the range before duplicating itself.