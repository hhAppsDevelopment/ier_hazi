// Agent foodtransporter in project ier_hazi

premiseToVisit(0).

maxPremiseID(ID) :- .findall(Premise,distToPremise(Premise,_),L) & .max(L,ID).

/* Plans */

+step(_) : not goalSet & premiseToVisit(X) <- gotoPremise(X).

+step(_) : pos(Premise,_) & premiseToVisit(Premise) & maxPremiseID(Premise) <- leaveFood; -+(premiseToVisit(0)).

+step(_) : pos(Premise,_) & premiseToVisit(Premise) <- leaveFood; -+(premiseToVisit(Premise+1)).

//+step(_) : true <- leaveFood.