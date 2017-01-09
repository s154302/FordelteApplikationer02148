package classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.Self;

public class Kitchen {
	protected String kitchenName;
	protected Node kitchenSpace;

	public Kitchen(String kitchenName) {
		this.kitchenName = kitchenName;

		kitchenSpace = new Node(kitchenName, new TupleSpace());
		kitchenSpace.addPort(Server.vp);
		// Agent kitchenAgent = new KitchenAgent(kitchenName);
		Agent monitor = new KitchenMonitor("kitchenMonitor");
		kitchenSpace.addAgent(monitor);
		kitchenSpace.start();

	}

	public class KitchenMonitor extends Agent {

		Tuple t;
		Template kitchenTemplate = new Template(new FormalTemplateField(String.class), new FormalTemplateField(Tuple.class));
		
		public KitchenMonitor(String who) {
			super(who);
		}

		@Override
		protected void doRun() {

			while (true) {

				try {

					t = get(kitchenTemplate, Self.SELF);
					Tuple data = t.getElementAt(Tuple.class, 1);
					String cmd = t.getElementAt(String.class, 0);

					exec(new KitchenAgent(cmd, data));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public class KitchenAgent extends Agent {
		protected PointToPoint p;
		
		Tuple data;
		String user, kitchen;
		String cmd;

		public KitchenAgent(String cmd, Tuple data) {
			super(cmd);
			this.data = data;
			this.cmd = cmd;
			this.user = data.getElementAt(String.class,0);
			this.kitchen = data.getElementAt(String.class, 1);
		}

		@Override
		protected void doRun() {
			
			switch (cmd){
				
			case "addDay":
				addDay(data);
				break;
			
			case "removeDay":
				removeDay(data);
				break;
			case "attendDay":
				//TODO - lav en attend day metode
				break;
			case "addChef":
				//TODO - lav en metode til at tilf�je en chef(kan evt virke som change chef)
				break;
			
			case "setPrice":
				//TODO - lav metode til at fort�lle hvor meget maden kostede p� en dag
				break;
			case "addBalance":
				//TODO - tager prisen for maden og l�gger det over i budget
				break;
			case "resetBalance":
				//TODO - Metode der bruges til at nulstille balance p� alle brugere n�r der skal betales
				break;
			}	
		}
		
		//Metode til at tilf�je ny dag
		private void addDay(Tuple data){			
					
			int day = data.getElementAt(Integer.class, 2);
			int month = data.getElementAt(Integer.class, 3);
			int year = data.getElementAt(Integer.class, 4);
			
		
			Template checkDayTemplate = new Template(new ActualTemplateField(""+day+""+month+""+year),
						new FormalTemplateField(Day.class));
			
			Tuple feedback;
			try{
				
				if(queryp(checkDayTemplate) == null){
					
					put(new Tuple(""+day+""+month+""+year,new Day(day, month, year)),Self.SELF);
					p = new PointToPoint(""+day+""+month+""+year, Server.vp.getAddress());
					get(new Template(new ActualTemplateField("dayCreated")),p);
				
					feedback = new Tuple(user, kitchen,"" + day + "" + month + "" + year + " was created");					
			
				}else{
					feedback = new Tuple(user, kitchen,"" + day + "" + month + "" + year + " was not created since the day already exits");
				}
				p = new PointToPoint("Server", Server.vp.getAddress());
				put(new Tuple("addDay Feedback", feedback),p);
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
		
		private void removeDay(Tuple data){
			String date = ""+data.getElementAt(Integer.class,1)+ "" + data.getElementAt(Integer.class,2) + "" + data.getElementAt(Integer.class,3);
			Template dayTemplate = new Template(new ActualTemplateField(date), new FormalTemplateField(Day.class));
			Tuple feedback;
			
			try{
				if(getp(dayTemplate)== null){
					feedback = new Tuple(user, kitchen, "Den valgte dag " + date + " findes ikke");
				}else{
					feedback = new Tuple(user, kitchen, "Dagen " + date + "Er blevet slettet");
				}
				p = new PointToPoint("Server", Server.vp.getAddress());
				put(new Tuple("addDay Feedback", feedback), p);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}		
	}
}
