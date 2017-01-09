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
	protected static Node kitchenSpace;
	protected PointToPoint p = new PointToPoint("Server", Server.vp.getAddress());

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

		/*protected void addDay(Tuple tupleData) {
			try {
				addDay.initialize(tupleData);
				exec(addDay);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		protected void getDays(Tuple tupleData) {
			try {
				exec(getDays);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
*/
	}

	public class KitchenAgent extends Agent {

		Tuple data;
		String user;
		String cmd;

		public KitchenAgent(String cmd, Tuple data) {
			super(cmd);
			this.data = data;
			this.cmd = cmd;
			this.user = data.getElementAt(String.class,0);
		}

		@Override
		protected void doRun() {
			
			switch (cmd){
				
			case "addDay":
				// TODO - lav en add day metode
				break;
			
			case "removeDay":
				//Todo - lav en remove day metode
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
		
		public void addDay(Tuple data){			
			int day, month, year;
			day = data.getElementAt(Integer.class, 2);
			month = data.getElementAt(Integer.class, 3);
			year = data.getElementAt(Integer.class, 4);
			try {
				
				put(new Tuple(new Day(day, month, year)),Self.SELF);
				get(new Template(new ActualTemplateField("dayCreated")),""+day+""+month+""+year);
				put(new Tuple("addDay Feedback", data),"Server");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//			int day = tupleData.getElementAt(Integer.class, 2);
//			int month = tupleData.getElementAt(Integer.class, 3);
//			int year = tupleData.getElementAt(Integer.class, 4);
//
//			Template date = new Template(new ActualTemplateField(day), new ActualTemplateField(month),
//					new ActualTemplateField(year));
//
//			try {
//				String feedback;
//
//				// check if this date exists in this kitchen already
//				if (null == queryp(date)) {
//					// add date
//					put(new Tuple(day, month, year), Self.SELF);
//					days.add(new Day(day, month, year));
//
//					feedback = "Date " + day + "/" + month + "/" + year + " added successfully to " + kitchenName;
//				} else {
//					feedback = "Date " + day + "/" + month + "/" + year + " already exists in " + kitchenName;
//				}
//
//				Tuple feedbackData = new Tuple(user, feedback, kitchenName);
//				put(new Tuple("addDay Feedback", feedbackData), p);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		void initialize(Tuple t) {
//			tupleData = t;
//		}
	}

	/*public class GetDaysAgent extends Agent {

		public GetDaysAgent(String who) {
			super(who);
		}

		@Override
		protected void doRun() {
			try {
				put(new Tuple("getDays", days), p);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		*/
}
