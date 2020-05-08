// Agent manager in project ier_hazi

/* Initial beliefs and rules */

/* Initial goals */



/* Plans */

+taskFinished(Premise,Tile) <- .broadcast(corpseRemoved(Premise,Tile)) .abolish(corpse(Premise,Tile)) .abolish(assigned(Premise,Tile)) .abolish(proposal(Premise,Tile)) .abolish(taskFinished(Premise,Tile)).

+proposal(Premise,Tile)[source(Src)] : not assigned(Premise,Tile,_) <- .broadcast(tell,task(Premise,Tile,Src)) .percept(assigned(Premise,Tile,Src)).

