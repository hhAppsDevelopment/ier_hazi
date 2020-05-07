// Agent sample_agent in project ier_hazi

/* Initial beliefs and rules */

/* Initial goals */


/* Plans */

+step(_) : corpse(_,tile) & (pos(A,_,_) | A == -1) <- removeCorpse.
