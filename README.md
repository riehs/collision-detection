# Collision Detection

These four classes are part of a larger program that uses openGL to construct a 3D mansion. Robots move randomly around the mansion, and the player must shoot the robots with bullets without touching the robots or the ricocheting bullets.

The program was part of a group project for a computer graphics class. My tasks included designing this collision detect system for the game.

Bullet, Player, and Robot all extend the CollisionDetect class. Methods in other parts of the program keep arrays of CollisionDetect instances. They incrementally call the moveOneStep methods for the bullets and robots, and call the collision detection methods to see if the objects are about to collide with any other objects or walls in the mansion.
