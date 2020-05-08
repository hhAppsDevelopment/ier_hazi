// Agent camera in project ier_hazi

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */

+step(_) : corpse(Premise,Tile) & pos(Premise,_)  <- .broadcast(tell,corpse(Premise,Tile)).
