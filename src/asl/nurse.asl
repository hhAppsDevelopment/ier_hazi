// Agent nurse in project ier_hazi

/* Initial goals */

!start.

/* Plans */

+!start <- for(distToPremise(Premise,Dist)){
	+roomSickness(0,Premise);
}.

+medicine(X) <- +medicine(X).

@personProcessing[atomic]
+person(Id,Premise) : person(Id,OldPremise) & not (OldPremise == Premise) & roomSickness(A,Premise) & roomSickness(B,OldPremise) & sickness(C,Id) 
<- .abolish(person(Id,OldPremise)); -roomSickness(A,Premise); -roomSickness(B,OldPremise); +roomSickness(A+C,Premise); +roomSickness(B-C,OldPremise).

@personInit[atomic]
+person(Id,Premise) : not sickness(_,Id) <- +sickness(0,Id).


@coughprocessing[atomic]
+cough(Premise,Id) : sickness(Old,Id) & person(Id,Premise) & roomSickness(A,Premise)
<- -sickness(_,Id); +sickness(Old+1,Id); -roomSickness(_,Premise); +roomSickness(A+1,Premise) .abolish(cough(Premise,Id)).

+step(_) : true <- !giveMedicine; !gotoMostInfected; !age.

@aging[atomic]
+!age <- for(roomSickness(Old,Premise)){
				-roomSickness(_,Premise);
				+roomSickness(Old*0.9,Premise);
			}.

@giveMed[atomic]
+!giveMedicine : pos(Premise,_) & roomSickness(Val,Premise) & Val>5  & medicine(_) <- leaveMedicine; -roomSickness(_,Premise); +roomSickness(0,Premise); for(person(Id,Premise)){
	-sickness(_,Id);
	+sickness(0,Id);
}.

+!giveMedicine <- true.

@goto[atomic]
+!gotoMostInfected : not goalSet & .findall(roomSickness(X,Y),roomSickness(X,Y),L) & .max(L,roomSickness(Biggest,Premise)) & Biggest > 1 <- gotoPremise(Premise).

+!gotoMostInfected <- true.