package classes;
import classes.User;
import classes.Kitchen;
import org.cmg.resp.behaviour.Agent;
import org.cmg.resp.comp.Node;
import org.cmg.resp.knowledge.ActualTemplateField;
import org.cmg.resp.knowledge.FormalTemplateField;
import org.cmg.resp.knowledge.Template;
import org.cmg.resp.knowledge.Tuple;
import org.cmg.resp.knowledge.ts.TupleSpace;
import org.cmg.resp.topology.PointToPoint;
import org.cmg.resp.topology.Self;
import org.cmg.resp.topology.VirtualPort;


public class DinnerClub {
	
	public static VirtualPort vp = new VirtualPort(1337);
	
	public static void main(String[] argv) {
		
		Kitchen kitchen = new Kitchen("Kitchen6");
		User user1 = new User("Emilie", "Kitchen6");
		//User user2 = new User("Mathias", "Kitchen6");
	}
}
