// Agent sample_agent in project ier_hazi

/* Initial beliefs and rules */

/* Initial goals */


/* Plans */

+step(_) : corpse(Premise,Tile) & pos(Premise,Tile)  <- clearCorpse.

//+step(_) : corpse(Premise,Tile) & pos(Premise,_) & .findall(D,corpse(Premise,D),L) & .findall(Dist, distToTile(CorpseTile,Dist) & .member(CorpseTile,L),L2) & .min(L2,D) & distToTile(Tile,D) <- setGoal(Premise,Tile).

+step(_) : corpse(Premise,Tile) & pos(Premise,_) & distToTile(Tile,Dist) & not (corpse(Premise,OtherTile) & distToTile(OtherTile,OtherDist) & OtherDist < Dist) <- setGoal(Premise,Tile).

//+step(_) : corpse(Premise,Tile) & pos(_,_)  <- setGoal(Premise,Tile).