package de.citec.sc.matoll.core;

public class Restriction implements Reference {

        String uri;
	String hasValue;
	String onProperty;
	
	public Restriction(String uri, String value, String property)
	{
                this.uri   = uri;
		hasValue   = value;
		onProperty = property;
	}
        
        public String getURI() {
               return uri;
        }

	@Override
	public String toString() {
		return "Restriction [hasValue=" + hasValue 
                                + ", onProperty=" + onProperty + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hasValue == null) ? 0 : hasValue.hashCode());
		result = prime * result
				+ ((onProperty == null) ? 0 : onProperty.hashCode());
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
		Restriction other = (Restriction) obj;
		if (hasValue == null) {
			if (other.hasValue != null)
				return false;
		} else if (!hasValue.equals(other.hasValue))
			return false;
		if (onProperty == null) {
			if (other.onProperty != null)
				return false;
		} else if (!onProperty.equals(other.onProperty))
			return false;
		return true;
	}

	public String getValue() {
		return hasValue;
	}

	public void setValue(String hasValue) {
		this.hasValue = hasValue;
	}

	public String getProperty() {
		return onProperty;
	}

	public void setProperty(String onProperty) {
		this.onProperty = onProperty;
	}
	
	
	
}
