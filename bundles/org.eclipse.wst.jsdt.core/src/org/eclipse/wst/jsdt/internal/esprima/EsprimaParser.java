/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.esprima;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.Comment;
import org.eclipse.wst.jsdt.core.dom.JSdoc;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.wst.jsdt.internal.core.CompilationUnit;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ECMAException;

/**
 * A wrapper around EsprimaParser that uses nashorn to execute the parser.
 *
 * @author Gorkem Ercan
 * @since 2.0
 */
public class EsprimaParser {
	/**
	 * "attachComment" option for parser
	 */
	private static final String ESPRIMA_OPT_ATTACH_COMMENT = "attachComment"; //$NON-NLS-1$

	/**
	 * "range" option for parser
	 */
	private static final String ESPRIMA_OPT_RANGE = "range"; //$NON-NLS-1$

	/**
	 * "tolerant" option for esprima
	 */
	private static final String ESPRIMA_OPT_TOLERANT = "tolerant"; //$NON-NLS-1$
	
	/**
	 * "sourceType" option for esprima
	 */
	private static final String ESPRIMA_OPT_SOURCE_TYPE = "sourceType"; //$NON-NLS-1$

	private static ScriptEngine engine;
	private static CompiledScript compiledEsprima;
	private Bindings bindings;
	private String rawContent;
	private IJavaScriptUnit unit;

	private boolean tolerant = Boolean.TRUE;
	private boolean range = Boolean.TRUE;
	private boolean errorReporting = Boolean.TRUE;
	private boolean includeJsdocs = Boolean.FALSE;
	private String sourceType;

	/**
	 *
	 */
	private EsprimaParser() {
		this.bindings = engine.createBindings();		
		try {
			compiledEsprima.eval(this.bindings);
		}
		catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static EsprimaParser newParser() {
		final ScriptEngineManager manager = new ScriptEngineManager();
		synchronized (EsprimaParser.class) {
			if (engine == null) {
				engine = manager.getEngineByName("nashorn"); //$NON-NLS-1$
				Compilable compilable = (Compilable) engine;
				try {
					InputStream in = EsprimaParser.class.getResourceAsStream("esprima.js"); //$NON-NLS-1$
					if (in == null)
						throw new RuntimeException("Failed to load esprima.js file"); //$NON-NLS-1$
					compiledEsprima = compilable.compile(new InputStreamReader(in));
				}
				catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new EsprimaParser();
	}


	public EsprimaParser setSource(String content){
		this.unit = null;
		this.rawContent = content;
		return this;
	}

	public EsprimaParser setSource(IJavaScriptUnit content){
		this.rawContent = null;
		this.unit = content;
		return this;
	}

	public JavaScriptUnit parse() {
		String content = rawContent;
		if(unit != null )
			try {
				content = unit.getSource();
			}
			catch (JavaScriptModelException e) {
				e.printStackTrace();
			}
		AST ast = AST.newAST(AST.JLS3);
		ast.setDefaultNodeFlag(ASTNode.ORIGINAL);
		JavaScriptUnit $ = ast.newJavaScriptUnit();
		try{
			ScriptObjectMirror jsObject = internalParse(content);
			translate(jsObject,$);
			buildLineEndTable($, content);
			if(errorReporting && tolerant)
				reportErrors(jsObject, $);
			if(includeJsdocs)
				buildComments(jsObject, $, ast);
			ast.setDefaultNodeFlag(0);
		}catch(ECMAException e)
		{
			if(!(e.getEcmaError() instanceof ScriptObjectMirror))
				e.printStackTrace();
			$.setProblems(new DefaultProblem[]{createProblem(((ScriptObjectMirror) e.getEcmaError()))});
			$.setFlags(ASTNode.MALFORMED);
		}
		return $;
	}

	/**
	 * Sets the tolerant parsing option to false.
	 * The default value for tolerant is true.
	 *
	 * @return parser
	 */
	public EsprimaParser noTolerantParsing(){
		this.tolerant = false;
		return this;
	}

	/**
	 * Sets the range based location option to false.
	 * The default is true;
	 *
	 * @return parser
	 */
	public EsprimaParser noRangeLocationInfo(){
		this.range = false;
		return this;
	}

	/**
	 * Turns the error reporting off causing the return JavaScriptUnit
	 * not to include the errors.
	 *
	 * @return
	 */
	public EsprimaParser noErrorReporting(){
		this.errorReporting = false;
		return this;
	}

	public EsprimaParser includeComments(){
		this.includeJsdocs = true;
		return this;
	}
	
	public EsprimaParser setSourceType(String value) {
		this.sourceType = value;
		return this;
	}

	/**
	 * @param jsObject
	 * @return
	 */
	private JavaScriptUnit translate(final ScriptObjectMirror jsObject, final JavaScriptUnit jsunit) {
		return (new DOMASTConverter(jsunit)).convert(jsObject);
	}

	private ScriptObjectMirror internalParse(String content) {
		final ScriptObjectMirror esprima = (ScriptObjectMirror) this.bindings.get("esprima"); //$NON-NLS-1$
		if (esprima == null)
			throw new RuntimeException("Esprima parser was not loaded correctly"); //$NON-NLS-1$
		return (ScriptObjectMirror) esprima.callMember("parse", content, getOptions()); //$NON-NLS-1$
	}

	private HashMap<String, Object> getOptions() {	
		HashMap<String, Object> $ = new HashMap<String, Object>();
		$.put(ESPRIMA_OPT_RANGE, range);
		$.put(ESPRIMA_OPT_TOLERANT, tolerant);
		$.put(ESPRIMA_OPT_ATTACH_COMMENT, includeJsdocs);
		$.put(ESPRIMA_OPT_SOURCE_TYPE, sourceType);
		return $;
	}

	/**
	 * Add the errors reported on the "errors" array to
	 * result.
	 *
	 * @param jsObject
	 * @param result
	 */
	private void reportErrors(final ScriptObjectMirror jsObject, final JavaScriptUnit result) {
		ScriptObjectMirror errors = (ScriptObjectMirror) jsObject.getMember("errors"); //$NON-NLS-1$
		if(errors == null || errors.size() < 1) return;
		DefaultProblem[] problems = new DefaultProblem[errors.size()];
		for(int i = 0; i < errors.size(); ++i)
			problems[i] = createProblem(((ScriptObjectMirror) errors.getSlot(i)));
		result.setFlags(ASTNode.MALFORMED);
		result.setProblems(problems);
	}

	private DefaultProblem createProblem(final ScriptObjectMirror error){
		String description = (String) error.getMember("description"); //$NON-NLS-1$
		Number index = (Number) error.getMember("index"); //$NON-NLS-1$
		Number line = (Number) error.getMember("lineNumber"); //$NON-NLS-1$
		Number column = (Number) error.getMember("column"); //$NON-NLS-1$

		char[] fileName = null;
		if(unit != null){
			CompilationUnit cu = (CompilationUnit)unit;
			fileName = cu.getFileName();
		}

		DefaultProblem result = new DefaultProblem(fileName,
					description,
					0,
					null,
					ProblemSeverities.Error,
					index.intValue(),
					-1,
					line.intValue(),
					column.intValue());
		return result;
	}

	/**
	 * @param jsObject
	 * @param result
	 */
	private void buildComments(ScriptObjectMirror jsObject, JavaScriptUnit result, AST t) {
		ScriptObjectMirror comments = (ScriptObjectMirror) jsObject.getMember("comments"); //$NON-NLS-1$

		int commentSize = comments.size();
		Comment[] resultComments = new Comment[commentSize];
		for(int i = 0; i< commentSize; ++i){
			ScriptObjectMirror obj = (ScriptObjectMirror) comments.getSlot(i);
			Comment newComment = createComment(obj, t);
			newComment.setAlternateRoot(result);
			resultComments[i] = newComment;
		}
		result.setCommentTable(resultComments);
	}

	/**
	 * @param m
	 * @return
	 */
	static Comment createComment(ScriptObjectMirror m, AST t) {
		String type = (String) m.getMember("type"); //$NON-NLS-1$
		String value = (String) m.getMember("value"); //$NON-NLS-1$
		Comment $ = "Line".equals(type) ? t.newLineComment() : !value.startsWith("*") ? t.newBlockComment() : t.newJSdoc();  //$NON-NLS-1$//$NON-NLS-2$
		if($.isDocComment()) ((JSdoc)$).setComment("/*"+value+"*/");  //$NON-NLS-1$//$NON-NLS-2$
		ScriptObjectMirror r = (ScriptObjectMirror) m.getMember("range"); //$NON-NLS-1$
		Number x = (Number) r.getSlot(0);
		Number y = (Number) r.getSlot(1);
		$.setSourceRange(x.intValue(), y.intValue()-x.intValue());
		return $;
	}

	/**
	 * Calculates line ends for the given source and sets this table for given
	 * JavaScriptUnit.
	 *
	 * @param jsunit
	 *            unit to assign calculated LineEndTable
	 * @param content
	 *            content to calculate line ends from
	 */
	private void buildLineEndTable(JavaScriptUnit jsunit, String content) {
		int[] lineEnds = new int[250];
		int linePtr = 0;
		for (int i = 0, e = content.length(); i < e; ++i)
			if ( content.charAt(i) == '\n' || content.charAt(i) == '\r') {
				if(content.length() > i+1 && content.charAt(i) == '\r' && content.charAt(i+1) == '\n' ) ++i;//Skip to next character
				int length = lineEnds.length;
				if (linePtr >= length)
					System.arraycopy(lineEnds, 0, lineEnds = new int[length + 250], 0, length);
				lineEnds[linePtr++] = i;
			}
		System.arraycopy(lineEnds, 0, lineEnds = new int[linePtr], 0, linePtr);
		jsunit.setLineEndTable(lineEnds);
	}

}
