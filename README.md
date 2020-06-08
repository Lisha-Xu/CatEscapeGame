# CatEscapeGame

Hanyang University-OOP class final project

Introduction: You are a cat and you chase mice in a house. The house has different floors, and higher floors have more mice! The house also contains obstacles which you must walk around, and stairs that go up and down. Finally, the house contains many dogs which will chase you. If you reach the top floor, there is a helicopter in which you can escape. If you reach the helicopter, then you save  the mice you collected in that house, and can advance to the next house. But if a dog reaches you first, then you lose the game!

There are five class in the project:

DogType: dogs move towards cat by random move along x-y axis. Dog is displayed as D.

MouseType: mices move away from cat by random move along x-y axis. Mouse is displayed as M.

LocationType: include XCoordinate and YCoordinate. It can return the location.

TrapType: If the cat was trapped, it cannot move one round. Trap is displayed as T.

CatDogMouse: includes main function. Player can control cat to move. It will build the house, move dogs and mouses and show the results.

I use Java Swing to display the game.
