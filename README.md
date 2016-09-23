# Platformer

This project was my submission for the final coursework of the "Object Oriented Programming with Java" unit at the University of Bristol.

The application is a proof of concept platform game in the spirit of Mario, with the following features:

* Sound effects and music
* Sprite animation
* Live graphics rendering
* A physics model; entities are moved around the game by applying a force (such as gravity or drag)

This version contains only one level for demonstration purposes. However; it is designed so that level designers can easily created new levels by extending the abstract `World` class, and overriding abstract methods to provide initial state and winning conditions. For this assignment I received **76%**
