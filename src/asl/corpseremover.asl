// Agent corpseremover in project ier_hazi

/* Initial beliefs and rules */


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

+corpse(Premise,Tile) : .count(assignedCorpse(_,_),0) & distToTile(Tile,Dist) & pos(Premise,_) <- .my_name(Me) .send(manager, tell, corpseBid(Dist,Premise,Tile,Me)).

+corpse(Premise,Tile) : .count(assignedCorpse(_,_),0) & distToPremise(Premise,Dist) & pos(MyPremise,_) & not(MyPremise == Premise) <- .my_name(Me) .send(manager, tell, corpseBid(Dist,Premise,Tile,Me)).

+corpse(Premise,Tile) : true <- .my_name(Me) .send(manager, tell, corpseBid(10000,Premise,Tile,Me)).

//remove corpse when reached

+step(_) : assignedCorpse(Premise,Tile) & pos(Premise,Tile) <-  -corpseRemoved(Premise,Tile); +corpseRemoved(Premise,Tile) .abolish(assignedCorpse(Premise,Tile)) .broadcast(tell,corpseRemoved(Premise,Tile)) clearCorpse.

//set assigned corpse as goal

+step(_) : assignedCorpse(Premise,Tile) <- !gotoCorpse(Premise,Tile).

+!gotoCorpse(Premise,Tile) : not goalSet <- setGoal(Premise,Tile) .print("Going to ",Premise,", ",Tile).

+!gotoCorpse(Premise,Tile) : true <-  .print("Can't yet go to ",Premise," ",Tile).

+corpseRemoved(Premise,Tile) <- .abolish(corpse(Premise,Tile)) .abolish(corpseRemoved(Premise,Tile)).