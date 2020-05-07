// Agent sample_agent in project ier_hazi

/* Initial beliefs and rules */

/* Initial goals */


/* Plans */

+step(_) : corpse(Premise,Tile) & pos(Premise,Tile)  <- clearCorpse.

+step(_) : corpse(Premise,Tile) & pos(Premise,_) & .findall(D,distToTile(_,D),L) & .min(L,D) & distToTile(Tile,D) <- setGoal(Premise,Tile).

+step(_) : corpse(Premise,Tile) & pos(_,_)  <- setGoal(Premise,Tile).