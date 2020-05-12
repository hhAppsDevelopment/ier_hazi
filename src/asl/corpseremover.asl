// Agent corpseremover in project ier_hazi

/* Initial beliefs and rules */


//~haveTask.
//~goalSet.

/* Initial goals */


/* Plans */

//+step(_) : corpse(Premise,Tile) & pos(Premise,Tile) & task(Premise,Tile)  <- .send(manager,tell,corpseRemoved(Premise,Tile)) clearCorpse.
//
//+step(_) : corpse(Premise,Tile) & pos(_,_) & distToTile(Tile,Dist) <-  setGoal(Premise,Tile).
//
//+step(_) : corpse(Premise,Tile) & pos(_,_)  & not corpseClaimed(Premise,Tile) <- setGoal(Premise,Tile).
//
//+step(_) : corridorID(Corridor) & not pos(Corridor,_) & not (pos(Premise,_) & task(Premise,_)) <- returnToCorridor.

//bidding

+corpse(Premise,Tile) : not haveTask & distToTile(Tile,Dist) & pos(Premise,_) <- .my_name(Me) .send(manager, tell, corpseBid(Dist,Premise,Tile,Me)).

+corpse(Premise,Tile) : not haveTask & distToPremise(Premise,Dist) & pos(MyPremise,_) & not(MyPremise == Premise) <- .my_name(Me) .send(manager, tell, corpseBid(Dist,Premise,Tile,Me)).

+corpse(Premise,Tile) : true <- .my_name(Me) .send(manager, tell, corpseBid(10000,Premise,Tile,Me)).

//set assigned corpse as goal

+assignedCorpse(Premise,Tile)  <- +haveTask !gotoCorpse(Premise,Tile).

+!gotoCorpse(Premise,Tile) : not goalSet <- +goalSet setGoal(Premise,Tile) .print("Going to ",Premise,", ",Tile).

+!gotoCorpse(Premise,Tile) : true <-  !gotoCorpse(Premise,Tile).

//remove corpse when reached

+step(_) : assignedCorpse(Premise,Tile) & pos(Premise,Tile) <-  +corpseRemoved(Premise,Tile) .abolish(goalSet)  .abolish(haveTask) .broadcast(tell,corpseRemoved(Premise,Tile)) clearCorpse.

+corpseRemoved(Premise,Tile) <- .abolish(corpse(Premise,Tile)) .abolish(corpseRemoved(Premise,Tile)) .abolish(assignedCorpse(Premise,Tile)).