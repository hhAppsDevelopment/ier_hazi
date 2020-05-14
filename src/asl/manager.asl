// Agent manager in project ier_hazi

/* Plans */

//bidding related rules

+cough(Premise,_) <- lockPremise(Premise); -cough(Premise,_); +locked(Premise).

+corpse(Premise,_) <- lockPremise(Premise); +locked(Premise).

+canOpen(Premise) : locked(Premise) <- unlockPremise(Premise); -locked(Premise); -canOpen(Premise).

+corpseBid(Dist,Premise,Tile,Ag) :  .count(corpseBid(_,Premise,Tile,_),4) <- .print("Last bid ",Dist," from ", Ag) !assignCorpse(Premise,Tile).

+corpseBid(Dist,Premise,Tile,Ag) <- .print("Bid ",Dist," from ", Ag). 

+!assignCorpse(Premise,Tile) <- .findall(option(Dist,Ag),corpseBid(Dist,Premise,Tile,Ag),LD); .min(LD,option(DistCloser,Closer)); .send(Closer,tell,assignedCorpse(Premise,Tile)) .print("Assigned ", Premise, " ",Tile, " to ",Closer," for ",DistCloser).

-!assignCorpse(Premise,Tile) <- .print("Could not assign ", Premise, " ",Tile).

+corpseRemoved(Premise,Tile) <- .abolish(corpse(Premise,Tile)) .abolish(corpseRemoved(Premise,Tile)) .abolish(corpseBid(_,Premise,Tile,_)).