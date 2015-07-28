package br.lncc.sinapad.portengin.gui.jsp.converter.factory;

import br.lncc.sinapad.core.application.representation.Event;
import br.lncc.sinapad.core.application.representation.element.Checkbox;
import br.lncc.sinapad.core.application.representation.element.InputFile;
import br.lncc.sinapad.core.application.representation.element.ListBoxDouble;
import br.lncc.sinapad.core.application.representation.element.ListBoxInteger;
import br.lncc.sinapad.core.application.representation.element.OutputFile;
import br.lncc.sinapad.core.application.representation.element.Select;
import br.lncc.sinapad.core.application.representation.element.Table;
import br.lncc.sinapad.core.application.representation.element.Text;
import br.lncc.sinapad.core.application.representation.element.TextDouble;
import br.lncc.sinapad.core.application.representation.element.TextInteger;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.CheckboxConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.EventConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.InputFileConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.ListBoxDoubleConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.ListBoxIntegerConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.OutputFileConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.SelectConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.TableConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.TextConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.TextDoubleConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.impl.TextIntegerConverter;

public abstract class JSPConverterFactory {

	public static ElementConverter createParameterConverter(Class c) {
		if (Checkbox.class.equals(c)) {
			return new CheckboxConverter();
		} else if (Event.class.equals(c)) {
			return new EventConverter();
		} else if (InputFile.class.equals(c)) {
			return new InputFileConverter();
		} else if (ListBoxDouble.class.equals(c)) {
			return new ListBoxDoubleConverter();
		} else if (ListBoxInteger.class.equals(c)) {
			return new ListBoxIntegerConverter();
		} else if (OutputFile.class.equals(c)) {
			return new OutputFileConverter();
		} else if (Select.class.equals(c)) {
			return new SelectConverter();
		} else if (Table.class.equals(c)) {
			return new TableConverter();
		} else if (Text.class.equals(c)) {
			return new TextConverter();
		} else if (TextDouble.class.equals(c)) {
			return new TextDoubleConverter();
		} else if (TextInteger.class.equals(c)) {
			return new TextIntegerConverter();
		}
		return null;
	}

}
