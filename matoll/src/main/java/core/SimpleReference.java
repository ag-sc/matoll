package core;

public class SimpleReference implements Reference {

	String Reference;
	
	public SimpleReference(String reference)
	{
		Reference = reference;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Reference == null) ? 0 : Reference.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleReference other = (SimpleReference) obj;
		if (Reference == null) {
			if (other.Reference != null)
				return false;
		} else if (!Reference.equals(other.Reference))
			return false;
		return true;
	}


	public String toString()
	{
		return Reference;
	}

}


