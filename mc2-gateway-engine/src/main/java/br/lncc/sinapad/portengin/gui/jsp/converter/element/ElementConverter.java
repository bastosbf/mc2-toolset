package br.lncc.sinapad.portengin.gui.jsp.converter.element;

import java.util.Properties;

public interface ElementConverter<T, E> {

	String VARIABLE = "args";

	T convert(E e, Properties locale);

}
