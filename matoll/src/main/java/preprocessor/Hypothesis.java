package preprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public String checkValidAndReturnRoot(HashMap<String,String> resource2heads) {
		
		int dangling = 0;
		String root = null;
		
		String head;
		
		for (String node: Nodes)
		{
			head = resource2heads.get(node);
			
			if (head!= null)
			{
				root = head;
			}
			else
			{
				if (!Nodes.contains(head)) dangling++;
			}
			
		}
		
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
