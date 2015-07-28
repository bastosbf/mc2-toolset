package br.lncc.sinapad.portengin.gui.jsp.converter.utils;

import java.util.List;

import br.lncc.sinapad.core.application.representation.Event;
import br.lncc.sinapad.core.application.representation.Parameter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.EventConverter;

public abstract class EventUtils {

	public static String getEvents(Parameter p) {
		List<Event> events = p.getEvents();

		EventConverter eventConverter = new EventConverter();
		String event = "";
		if (!events.isEmpty()) {
			for (Event e : events) {
				event += eventConverter.convert(e, null) + " ";
			}
		}
		return event;
	}

}
