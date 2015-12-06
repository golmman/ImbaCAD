package imbacad.model;

/**
 * 
 * @author Dirk Kretschmann
 *
 * @param <T>
 */
public interface CopyFactory<T> {
	public T copy(T type);
}
