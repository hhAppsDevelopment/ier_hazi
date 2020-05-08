import environment.model.Premise;
import environment.occupants.*;
import environment.view.MainFrame;
import environment.view.PlayField;
import environment.view.Tile;
import jason.NoValueException;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.TimeSteppedEnvironment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class QuarantineEnvironment extends TimeSteppedEnvironment{
	
	private Logger logger = Logger.getLogger("ier_hazi.mas2j."+QuarantineEnvironment.class.getName());
	
	Map<Premise, Integer> premise2id = new HashMap<Premise, Integer>();
	Map<Integer, Premise> id2premise = new HashMap<Integer, Premise>();
	
	Map<Agent,String> ag2name = new HashMap<Agent,String>();
    Map<String,Agent> name2ag = new HashMap<String,Agent>();
    
    private Literal lstep; // current step
    
    PlayField field;
        
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        
        
        //initializing MainFrame with path to map
		try {
			MainFrame frame = new MainFrame(args[0]);
			field = frame.getPlayField();
			int i = 0;
			for (Premise premise : field.getTileGraph().getCorridors()) {
				premise2id.put(premise, i);
				id2premise.put(i++, premise);
			}
			for (Premise premise : field.getTileGraph().getCabins()) {
				premise2id.put(premise, i);
				id2premise.put(i++, premise);
			}
			for (Premise premise : field.getTileGraph().getSmokingRooms()) {
				premise2id.put(premise, i);
				id2premise.put(i++, premise);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		super.init(new String[] { "10" } ); // set step timeout
        setOverActionsPolicy(OverActionsPolicy.ignoreSecond);
        		
		logger.info("init finished");

    }
    
    @Override
    public synchronized boolean executeAction(String agName, Structure action) {
    	String actId = action.getFunctor();
    	Agent ag=name2ag.get(agName);
    	
    	if(actId.equals("cleanPremise")) {
    		ag.getCurrentTile().getPremise().getContagious().clear();
    	} else if(actId.equals("clearCorpse")) {
    		List<Occupant> toRemove = new ArrayList<Occupant>();
    		ag.getCurrentTile().getOccupants().forEach(occupant -> {
                if(occupant.isDead()) toRemove.add(occupant); 
            });
    		toRemove.forEach(occupant -> {ag.getCurrentTile().unregisterOccupant(occupant);});
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
    	} else if(actId.equals("returnToCorridor")) {   		
			ag.setGoal(field.getTileGraph().getCorridors().get(0).getRandomTile());
    	}
    	
    	return true;
    	
    }
    

    @Override
    protected int requiredStepsForAction(String agName, Structure action) {
        if (action.getFunctor().equals("setGoal")) {
            return 1;
        } else if (action.getFunctor().equals("clearCorpse")) {
        	return 1;
        } else if (action.getFunctor().equals("returnToCorridor")) {
        	return 1;
        }
        return super.requiredStepsForAction(agName, action);
    }
    
    @Override
    public Collection<Literal> getPercepts(String agName) {
        if (name2ag.get(agName) == null) {
            updateAgPercept(addAgent(agName));
        }
        
    	  	
    	return super.getPercepts(agName);
    }
    
    private Agent addAgent(String agName) {
    	Agent newAgent = null;
        if(agName.contains("camera")) {
        	newAgent = new Camera(field, getNextEmptyRoomTile());
        } else if(agName.contains("cleaner")) {
        	newAgent = new Cleaner(field, field.getTileGraph().getCorridors().get(0).getRandomTile());
        } else if(agName.contains("corpseremover")) {
        	newAgent = new CorpseTransporter(field, field.getTileGraph().getCorridors().get(0).getRandomTile());
        } else if(agName.contains("foodtransporter")) {
        	newAgent = new FoodTransporter(field, field.getTileGraph().getCorridors().get(0).getRandomTile());
        } else if(agName.equals("manager")) {
        	newAgent = new Manager(field, field.getTileGraph().getCorridors().get(0).getRandomTile());
        }
        
        ag2name.put(newAgent, agName);
        name2ag.put(agName, newAgent);
        
        logger.info(agName+" joined");
        return newAgent;
    }

    int next = 0;
	private Tile getNextEmptyRoomTile() {
		assert (field.getTileGraph().getCabins().size() > next);
		return field.getTileGraph().getCabins().get(next++).getTiles().get(0);
	}

	@Override
    protected void stepStarted(int step) {
        //logger.info("start step "+step);
        lstep = ASSyntax.createLiteral("step", ASSyntax.createNumber(step+1));
    }
    
    @Override
    protected void stepFinished(int step, long time, boolean timeout) {
    	logger.info("time: "+time);
    	if(time<field.getStepTime()) {
    		try {
    			Thread.sleep(field.getStepTime()-time);
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
        field.step();
    }
    
    @Override
    protected void updateAgsPercept() {
    	updateGlobalKnowledge();
        for (Agent ag: ag2name.keySet()) {
            updateAgPercept(ag);
        }
    }
    
    protected void updateGlobalKnowledge() {
    	Literal lcorridor=ASSyntax.createLiteral("corridorID", ASSyntax.createNumber(premise2id.get(field.getTileGraph().getCorridors().get(0))));
    	addPercept(lcorridor);
    }
    
    protected void updateAgPercept(Agent ag) {
    	String agName=ag2name.get(ag);
    	clearPercepts(agName);
    	
    	Literal lpos= ASSyntax.createLiteral("pos", ASSyntax.createNumber(premise2id.get(ag.getCurrentTile().getPremise())),ASSyntax.createNumber(ag.getCurrentTile().getPremise().getTiles().indexOf(ag.getCurrentTile())));
		addPercept(agName, lpos);
    	
		Tile currentTile=ag.getCurrentTile();
    	// all Tiles of the agent's current premise
    	for(Tile tile: currentTile.getPremise().getTiles()) {
    		boolean interestingTile=false;
			int tileID=currentTile.getPremise().getTiles().indexOf(tile);
    		
    		for(Occupant oc: tile.getOccupants()) {
    			if(oc.isDead()) {
    				Literal lcorpse= ASSyntax.createLiteral("corpse", ASSyntax.createNumber(premise2id.get(currentTile.getPremise())),ASSyntax.createNumber(tileID));
    				addPercept(agName, lcorpse);
    				interestingTile=true;
    			}
//    			if(oc instanceof Person) {
//    				Literal lperson=ASSyntax.createLiteral("person", ASSyntax.createNumber(tileID));
//    				addPercept(agName, lperson);
//    				interestingTile=true;
//    			}
    		}
    		
    		if(interestingTile) {
        		int distToTile=field.getTileGraph().getDijkstraShortestPath().getPath(currentTile, tile).getLength();
        		Literal ldistToTile=ASSyntax.createLiteral("distToTile", ASSyntax.createNumber(tileID), ASSyntax.createNumber(distToTile));
        		addPercept(agName, ldistToTile);
    		}
    	}
    	
    	
    	addPercept(agName, lstep);
    }

}
