/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.preferences.formatter;
 
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wst.jsdt.core.formatter.DefaultCodeFormatterConstants;

public class BracesTabPage extends FormatterTabPage {
	
	/**
	 * Constant array for boolean selection 
	 */
	private static String[] FALSE_TRUE = {
		DefaultCodeFormatterConstants.FALSE,
		DefaultCodeFormatterConstants.TRUE
	};	
	
	private final String PREVIEW=
	createPreviewHeader(FormatterMessages.BracesTabPage_preview_header) + 
	"function foo(data) {\n" + //$NON-NLS-1$
	"\n" + //$NON-NLS-1$
	"    var abc = 1;\n" + //$NON-NLS-1$
	"    var xyz = 'one';\n" + //$NON-NLS-1$
	"    var arr1 = [ '1', '2', '3', '4' ];\n" + //$NON-NLS-1$
	"    var arr2 = [];\n" + //$NON-NLS-1$
	"    var car = { carMake: 'Amet', carModel: 'Porro', carYear: 2012 };\n" + //$NON-NLS-1$
	"    var car2 = {};\n" + //$NON-NLS-1$
	"\n" + //$NON-NLS-1$
	"    switch (data) {\n" + //$NON-NLS-1$
	"\n" + //$NON-NLS-1$
	"        case 0:\n" + //$NON-NLS-1$
	"            abc = 0;\n" + //$NON-NLS-1$
	"            xyz = 'zero';\n" + //$NON-NLS-1$
	"            break;\n" + //$NON-NLS-1$
	"\n" + //$NON-NLS-1$
	"        default:\n" + //$NON-NLS-1$
	"            abc = -1;\n" + //$NON-NLS-1$
	"            xyz = 'unknown';\n" + //$NON-NLS-1$
	"\n" + //$NON-NLS-1$
	"     }\n\n" + //$NON-NLS-1$
	"    if ( document.form1.year.value > 2000 ) {\n" + //$NON-NLS-1$
	"        abc += 27;\n" + //$NON-NLS-1$
	"    }\n" + //$NON-NLS-1$
	"    else if ( document.form1.year.value > 1900 ) {\n" + //$NON-NLS-1$
	"        abc += 19;\n" + //$NON-NLS-1$
	"    }\n" + //$NON-NLS-1$
	"    else {\n" + //$NON-NLS-1$
	"        abc = 0;\n" + //$NON-NLS-1$
	"    }\n" + //$NON-NLS-1$
	"\n" + //$NON-NLS-1$
	"}"; //$NON-NLS-1$

	
	
	private CompilationUnitPreview fPreview;
	
	
	private final String [] fBracePositions= {
	    DefaultCodeFormatterConstants.END_OF_LINE,
	    DefaultCodeFormatterConstants.NEXT_LINE,
	    DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED
	};
	
	private final String [] fExtendedBracePositions= {
		DefaultCodeFormatterConstants.END_OF_LINE,
	    DefaultCodeFormatterConstants.NEXT_LINE,
	    DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED, 
		DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP
	};
	
	private final String [] fBracePositionNames= {
	    FormatterMessages.BracesTabPage_position_same_line, 
	    FormatterMessages.BracesTabPage_position_next_line, 
	    FormatterMessages.BracesTabPage_position_next_line_indented
	};
	
	private final String [] fExtendedBracePositionNames= {
	    FormatterMessages.BracesTabPage_position_same_line, 
	    FormatterMessages.BracesTabPage_position_next_line, 
	    FormatterMessages.BracesTabPage_position_next_line_indented, 
		FormatterMessages.BracesTabPage_position_next_line_on_wrap
	};

	
	/**
	 * Create a new BracesTabPage.
	 * @param modifyDialog
	 * @param workingValues
	 */
	public BracesTabPage(ModifyDialog modifyDialog, Map workingValues) {
		super(modifyDialog, workingValues);
	}
	
	protected void doCreatePreferences(Composite composite, int numColumns) {
		
		final Group group= createGroup(numColumns, composite, FormatterMessages.BracesTabPage_group_brace_positions_title); 
//		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_class_declaration, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION); 
//		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_anonymous_class_declaration, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION); 
//		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_constructor_declaration, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION); 
		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_method_declaration, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION); 
//		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_enum_declaration, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ENUM_DECLARATION); 
//		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_enumconst_declaration, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ENUM_CONSTANT); 
//		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_annotation_type_declaration, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANNOTATION_TYPE_DECLARATION); 
		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_blocks, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK); 
		createExtendedBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_blocks_in_case, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK_IN_CASE); 
		createBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_switch_case, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH); 
		
		ComboPreference objectInitOption= createBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_object_initializer, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_OBJLIT_INITIALIZER);
		final CheckboxPreference objectInitCheckBox= createIndentedCheckboxPref(group, numColumns, FormatterMessages.BracesTabPage_option_keep_empty_object_initializer_on_one_line, DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_OBJLIT_INITIALIZER_ON_ONE_LINE, FALSE_TRUE); 
		objectInitOption.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				updateOptionEnablement((ComboPreference) o, objectInitCheckBox);
			}
		});
		updateOptionEnablement(objectInitOption, objectInitCheckBox);
		
		
		ComboPreference arrayInitOption= createBracesCombo(group, numColumns, FormatterMessages.BracesTabPage_option_array_initializer, DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ARRAY_INITIALIZER); 
		final CheckboxPreference arrayInitCheckBox= createIndentedCheckboxPref(group, numColumns, FormatterMessages.BracesTabPage_option_keep_empty_array_initializer_on_one_line, DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, FALSE_TRUE); 

		arrayInitOption.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				updateOptionEnablement((ComboPreference) o, arrayInitCheckBox);
			}
		});
		updateOptionEnablement(arrayInitOption, arrayInitCheckBox);
		
	}
	
	/**
	 * @param arrayInitOption
	 * @param arrayInitCheckBox
	 */
	protected final void updateOptionEnablement(ComboPreference arrayInitOption, CheckboxPreference arrayInitCheckBox) {
		arrayInitCheckBox.setEnabled(!arrayInitOption.hasValue(DefaultCodeFormatterConstants.END_OF_LINE));
	}

	protected void initializePage() {
	    fPreview.setPreviewText(PREVIEW);
	}
	
	protected JavaPreview doCreateJavaPreview(Composite parent) {
	    fPreview= new CompilationUnitPreview(fWorkingValues, parent);
	    return fPreview;
	}
	
	private ComboPreference createBracesCombo(Composite composite, int numColumns, String message, String key) {
		return createComboPref(composite, numColumns, message, key, fBracePositions, fBracePositionNames);
	}
	
	private ComboPreference createExtendedBracesCombo(Composite composite, int numColumns, String message, String key) {
		return createComboPref(composite, numColumns, message, key, fExtendedBracePositions, fExtendedBracePositionNames);
	}
	
	private CheckboxPreference createIndentedCheckboxPref(Composite composite, int numColumns, String message, String key, String [] values) {
		CheckboxPreference pref= createCheckboxPref(composite, numColumns, message, key, values);
		GridData data= (GridData) pref.getControl().getLayoutData();
		data.horizontalIndent= fPixelConverter.convertWidthInCharsToPixels(1);
		return pref;
	}
	

    protected void doUpdatePreview() {
    	super.doUpdatePreview();
        fPreview.update();
    }

}
