/*
 * Created on 2007 jan 28
 */
package se.vxu.msi.malteval.util;

import java.io.Serializable;

/**
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public class Pair<T> implements Serializable {
	private static final long serialVersionUID = -6814604177578430813L;

	private T first_;
	private T second_;

	public Pair(T first, T second) {
		first_ = first;
		second_ = second;
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (o instanceof Pair) {
			return ((Pair<T>) o).getFirst().equals(getFirst()) && ((Pair<T>) o).getSecond().equals(getSecond());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return (getFirst() + "_" + getSecond()).hashCode();
	}

	public T getFirst() {
		return first_;
	}

	public T getSecond() {
		return second_;
	}
	
	public String toString() {
		return "<" + first_.toString() + "," + second_.toString() + ">";
	}
}
