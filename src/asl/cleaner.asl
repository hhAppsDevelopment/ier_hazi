// Agent cleaner in project ier_hazi

/* Initial beliefs and rules */




/* Initial goals */

!start.

/* Plans */

+!start <- for(distToPremise(Premise,_)){
				+cleanness(0,Premise);
			}.
		
@coughProcessing[atomic]	
+cough(Premise,Person) : cleanness(Old,Premise) <- -cleanness(_,Premise); +cleanness(Old+1,Premise); .abolish(cough(Premise,Person)).

@corpseProcessing[atomic]
+corpse(Premise,Tile) : cleanness(Old,Premise) <- -cleanness(_,Premise); +cleanness(Old+5,Premise); .abolish(corpse(Premise,Tile)).

+step(_) : true <- !cleanCurrent; !gotoMostInfected; !age.

@aging[atomic]
+!age <- for(cleanness(Old,Premise)){
				-cleanness(_,Premise);
				+cleanness(Old*0.9,Premise);
			}.


@cleaning[atomic]			
+!cleanCurrent : pos(Premise,Tile)  <-  cleanPremise; -cleanness(_,Premise); +cleanness(0,Premise).

+!gotoMostInfected : not goalSet & .findall(cleanness(X,Y),cleanness(X,Y),L) & .max(L,cleanness(Biggest,Premise)) & Biggest > 0 <- .print(Premise) ; gotoPremise(Premise).

+!gotoMostInfected <- true.