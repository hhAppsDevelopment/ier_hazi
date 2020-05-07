package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import environment.model.Premise;
import environment.occupants.Agent;
import environment.occupants.Occupant;
import environment.occupants.Person;
import environment.view.Tile;
import jason.NoValueException;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.TimeSteppedEnvironment;
import jason.environment.TimeSteppedEnvironment.OverActionsPolicy;

public class QurantineEnvironment extends TimeSteppedEnvironment{
	
	List<Premise> allPremises = new ArrayList<Premise>();
	
	Map<Agent,String> ag2name = new HashMap<Agent,String>();
    Map<String,Agent> name2ag = new HashMap<String,Agent>();
    
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(new String[] { "1000" } ); // set step timeout
        setOverActionsPolicy(OverActionsPolicy.ignoreSecond);
    }
    
    @Override
    public boolean executeAction(String agName, Structure action) {
    	String actId = action.getFunctor();
    	Agent ag=name2ag.get(agName);
    	
    	if(actId.equals("cleanPremise")) {
    		ag.getCurrentTile().getPremise().getContagious().clear();
    	} else if(actId.equals("clearCorpse")) {
    		ag.getCurrentTile().getOccupants().forEach(occupant -> {
                if(occupant.isDead()) ag.getCurrentTile().unregisterOccupant(occupant);
            });
    	} else if(actId.equals("leaveFood")) {
    		ag.getCurrentTile().getPremise().getTiles().forEach(tile -> tile.getOccupants().forEach(Occupant::resetFood));
    	} else if(actId.equals("leaveMedicine")) {
    		ag.getCurrentTile().getPremise().getTiles().forEach(tile -> tile.getOccupants().forEach(Occupant::giveMedicine));
    	} else if(actId.equals("lockPremise")) {
    		ag.getCurrentTile().getPremise().setLocked(true);
    	} else if(actId.equals("unlockPremise")) {
    		ag.getCurrentTile().getPremise().setLocked(false);
    	} else if(actId.equals("setGoal")) {
    		try {
				ag.setGoal(allPremises.get((int)((NumberTerm)action.getTerm(0)).solve()).getTiles().get((int)((NumberTerm)action.getTerm(1)).solve()));
			} catch (NoValueException e) {
				e.printStackTrace();
			}
    	}
    	
    	return true;
    	
    }
    
    @Override
    protected void updateAgsPercept() {
        for (Agent ag: ag2name.keySet()) {
            updateAgPercept(ag);
        }
    }
    
    protected void updateAgPercept(Agent ag) {
    	String agName=ag2name.get(ag);
    	clearPercepts(agName);
    	
    	// all Tiles of the agent's current premise
    	for(Tile tile: ag.getCurrentTile().getPremise().getTiles()) {
    		int tileID=ag.getCurrentTile().getPremise().getTiles().indexOf(tile);
    		for(Occupant oc: tile.getOccupants()) {
    			if(oc.isDead()) {
    				Literal lcorpse= ASSyntax.createLiteral("corpse", ASSyntax.createNumber(tileID));
    				addPercept(agName, lcorpse);
    			}
    			if(oc instanceof Person) {
    				Literal lperson=ASSyntax.createLiteral("person", ASSyntax.createNumber(tileID));
    				addPercept(agName, lperson);
    			}
    		}
    	}
    }

}
