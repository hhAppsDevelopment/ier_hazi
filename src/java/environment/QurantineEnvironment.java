package environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import environment.model.Premise;
import environment.occupants.Agent;
import environment.occupants.Camera;
import environment.occupants.Occupant;
import environment.occupants.Person;
import environment.view.PlayField;
import environment.view.Tile;
import jason.NoValueException;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.TimeSteppedEnvironment;
import jason.environment.TimeSteppedEnvironment.OverActionsPolicy;

public class QurantineEnvironment extends TimeSteppedEnvironment{
	
	Map<Premise, Integer> premise2id = new HashMap<Premise, Integer>();
	Map<Integer, Premise> id2premise = new HashMap<Integer, Premise>();
	
	Map<Agent,String> ag2name = new HashMap<Agent,String>();
    Map<String,Agent> name2ag = new HashMap<String,Agent>();
    
    private Literal lstep; // current step
    
    PlayField field;
    
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
				ag.setGoal(id2premise.get((int)((NumberTerm)action.getTerm(0)).solve()).getTiles().get((int)((NumberTerm)action.getTerm(1)).solve()));
			} catch (NoValueException e) {
				e.printStackTrace();
			}
    	}
    	
    	return true;
    	
    }
    
    @Override
    public Collection<Literal> getPercepts(String agName) {
//        if (name2ag.get(agName) == null) {
//            updateAgPercept(addAgent(agName));
//        }
//        return super.getPercepts(agName);
    	System.out.println("agent name: "+agName);
    	return null;
    }
    
//    private Agent addAgent(String agName) {
//    	Agent  newAgent;
//        if(agName.contains("camera")) {
//        	newAgent = new Camera(field.getTileGraph(), /*random Tile*/);
//        } else if(agName.contains("foodtransporter")) {
//        	
//        }
//        
//        ag2name.put(newAgent, agName);
//        name2ag.put(agName, newAgent);
//    }
    
    @Override
    protected void stepStarted(int step) {
        //logger.info("start step "+step);
        lstep = ASSyntax.createLiteral("step", ASSyntax.createNumber(step+1));
        field.step();
    }
    
    @Override
    protected void stepFinished(int step, long time, boolean timeout) {
    	
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
    	
        addPercept(agName, lstep);
    }

}
