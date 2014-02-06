package it.polimi.dima.dacc.mountainroutes.commons;

/**
 * Holds a reference to a value.
 * 
 * @param <E>
 *            - the type of the value.
 */
public class Holder<E> {

	private E value;

	public E getValue() {
		return value;
	}

	public void setValue(E value) {
		this.value = value;
	}

}
