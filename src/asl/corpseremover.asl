// Agent corpseremover in project ier_hazi

/* Initial beliefs and rules */

/* Initial goals */


/* Plans */

+step(_) : corpse(Premise,Tile) & pos(Premise,Tile) & task(Premise,Tile)  <- .send(manager,tell,corpseRemoved(Premise,Tile)) clearCorpse.

+step(_) : corpse(Premise,Tile) & pos(_,_) & distToTile(Tile,Dist) <-  setGoal(Premise,Tile).

+step(_) : corpse(Premise,Tile) & pos(_,_)  & not corpseClaimed(Premise,Tile) <- setGoal(Premise,Tile).

+step(_) : corridorID(Corridor) & not pos(Corridor,_) & not (pos(Premise,_) & task(Premise,_)) <- returnToCorridor.

+corpseRemoved(Premise,Tile) <- .abolish(corpse(Premise,Tile)) .abolish(corpseClaimed(Premise,Tile)) .abolish(corpseRemoved(Premise,Tile)) .abolish(task(Premise,Tile)).