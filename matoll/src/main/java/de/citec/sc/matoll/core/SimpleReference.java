package de.citec.sc.matoll.core;

public class SimpleReference implements Reference {

	String reference;
	
	public SimpleReference(String reference)
	{
		this.reference = reference;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((reference == null) ? 0 : reference.hashCode());
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
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

        public String getURI() {
               return reference;
        }

	public String toString()
	{
		return reference;
	}

}


