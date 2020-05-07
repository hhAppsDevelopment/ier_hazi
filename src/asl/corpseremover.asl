// Agent sample_agent in project ier_hazi

/* Initial beliefs and rules */

/* Initial goals */


/* Plans */

+step(_) : corpse(premise,tile) & pos(premise,tile)  <- clearCorpse.

+step(_) : corpse(premise,tile) & not pos(premise,tile) & pos(premise,_) & .findall(d,distToTile(_,dist),L) & .min(L,dist) & distToTile(tile,dist) <- setGoal(premise,tile).

+step(_) : corpse(premise,tile) & not pos(premise,tile) & pos(_,_)  <- setGoal(premise,tile).