package environment;

import java.util.HashMap;
import java.util.Map;

import environment.occupants.Agent;
import environment.occupants.Occupant;
import environment.occupants.Person;
import environment.view.Tile;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.TimeSteppedEnvironment;

public class QurantineEnvironment extends TimeSteppedEnvironment{
	
	Map<Agent,String> ag2name = new HashMap<Agent,String>();
    Map<String,Agent> name2ag = new HashMap<String,Agent>();
    
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
