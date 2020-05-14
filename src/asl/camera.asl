// Agent camera in project ier_hazi

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */


+step(_) : corpse(Premise,Tile) & pos(Premise,_)  <- .broadcast(tell,corpse(Premise,Tile)).

+cough(Premise,Person) <- .abolish(cough(Premise,Person)) .send(cleaner,tell,cough(Premise,Person)) .send(nurse,tell,cough(Premise,Person)).

+person(Id,Premise) <- .abolish(person(Id,Premise)) .send(nurse,tell,person(Id,Premise)).

+corpseRemoved(Premise,Tile) <- .abolish(corpse(Premise,Tile)) .abolish(corpseRemoved(Premise,Tile)).
