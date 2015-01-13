package preprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hypothesis {
	
	List<String> Nodes;
	
	public Hypothesis()
	{
		Nodes = new ArrayList<String>();
	}
	
	public Hypothesis(Hypothesis hypothesis) {
		
		Nodes = new ArrayList<String>();
		
		for (String node: hypothesis.getNodes())
		{
			Nodes.add(node);
		}
		
	}

	public void add(String node)
	{
		Nodes.add(node);
	}
	
	public List<Hypothesis> expand(List<String> Nodes)
	{
		List<Hypothesis> hypotheses = new ArrayList<Hypothesis>();
		
		Hypothesis hypothesis;
		
		for (String node: Nodes)
		{
			hypothesis = new Hypothesis(this);
			
			hypothesis.add(node);
			
			hypotheses.add(hypothesis);
		}
		
		return hypotheses;
	}
	
	public List<String> getNodes()
	{
		return Nodes;
	}

	public String checkValidAndReturnRoot(HashMap<String,String> resource2heads, HashMap<String,String> resource2deps) {
		
		int dangling = 0;
		String root = null;
		Set<String> pos = new HashSet<String>();
		pos.add("prep");
		pos.add("appos");
		pos.add("nn");
		pos.add("dobj");
		
		String head;
		
		for (String node: Nodes)
		{
			
			// System.out.print("Checking:"+node+" "+resource2deps.get(node)+"\n");
			head = resource2heads.get(node);
			
			if ((pos.contains(resource2deps.get(node))) && Nodes.contains(head))
			{
				// System.out.print("Node: "+node+" is in good shape\n");
			}
			else
			{
				dangling++;
				root = node;
			}
			
		}
		
		// System.out.print("Dangling: "+dangling+"\n");
		
		if (dangling == 1)
			return root;
		else
			return null;
		
	}
	
	public String toString()
	{
		String string = "Hypothesis: ";
		string += Nodes.toString();
		return string+"\n";
	}

}
