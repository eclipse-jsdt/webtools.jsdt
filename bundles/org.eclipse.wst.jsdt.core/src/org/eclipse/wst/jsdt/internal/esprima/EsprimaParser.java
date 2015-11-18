/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
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
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.wst.jsdt.internal.core.CompilationUnit;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ECMAException;

/**
 * @author Gorkem Ercan
 *
 */
public class EsprimaParser {
	/**
	 * 
	 */
	private static final String ESPRIMA_OPT_RANGE = "range"; //$NON-NLS-1$

	/**
	 * "tolerant" option for esprima
	 */
	private static final String ESPRIMA_OPT_TOLERANT = "tolerant"; //$NON-NLS-1$
	
	private static ScriptEngine engine;
	private static CompiledScript compiledEsprima;
	private Bindings bindings;
	private String rawContent;
	private IJavaScriptUnit unit;
	
	private boolean tolerant = Boolean.TRUE;
	private boolean range = Boolean.TRUE;
	private boolean errorReporting = Boolean.TRUE;

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
					if (in == null) {
						throw new RuntimeException("Failed to load esprima.js file"); //$NON-NLS-1$
					}
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
		if(unit != null ){
			try {
				content = unit.getSource();
			}
			catch (JavaScriptModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		AST ast = AST.newAST(AST.JLS3);
		JavaScriptUnit result = ast.newJavaScriptUnit();
		try{
			ScriptObjectMirror jsObject = internalParse(content);
			translate(jsObject,result);
			if(errorReporting && tolerant){
				reportErrors(jsObject, result);
			}
		}catch(ECMAException e)
		{
			result.setProblems(new IProblem[]{createProblem((ScriptObjectMirror)e.getEcmaError())});
		}
		return result;
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
	

	/**
	 * @param jsObject
	 * @return
	 */
	private JavaScriptUnit translate(final ScriptObjectMirror jsObject, final JavaScriptUnit jsunit) {
		DOMASTConverter visitor = new DOMASTConverter(jsunit);
		return visitor.convert(jsObject);
	}

	private ScriptObjectMirror internalParse(String content) {
		final ScriptObjectMirror esprima = (ScriptObjectMirror) this.bindings.get("esprima");
		if (esprima == null) {
			throw new RuntimeException("Esprima parser was not loaded correctly"); //$NON-NLS-1$
		}
		HashMap<String, Boolean> options = getOptions(); 
		final ScriptObjectMirror tree = (ScriptObjectMirror) esprima.callMember("parse", content, options);
		return tree;
	}

	private HashMap<String, Boolean> getOptions() {
		HashMap<String , Boolean> options = new HashMap<String, Boolean>();
		options.put(ESPRIMA_OPT_RANGE, range);
		options.put(ESPRIMA_OPT_TOLERANT, tolerant);
		return options;
	}
	
	/**
	 * @param jsObject
	 * @param result
	 */
	private void reportErrors(final ScriptObjectMirror jsObject, final JavaScriptUnit result) {
		ScriptObjectMirror errors = (ScriptObjectMirror) jsObject.getMember("errors");
		IProblem[] problems = new IProblem[errors.size()];
		for(int i = 0; i < errors.size(); i++){
			ScriptObjectMirror obj = (ScriptObjectMirror) errors.getSlot(i);
			problems[i]=createProblem(obj);
		}
		result.setProblems(problems);
	}
	
	private IProblem createProblem(final ScriptObjectMirror error){
		String description = (String) error.getMember("description");
		Number index = (Number) error.getMember("index");
		Number line = (Number) error.getMember("lineNumber");
		
		char[] fileName = null;
		if(unit != null){
			CompilationUnit cu = (CompilationUnit)unit;
			fileName = cu.getFileName();
		}
		
		DefaultProblem result = new DefaultProblem(fileName,
					description,
					0, 
					null, 
					ProblemSeverities.Fatal, 
					0, 
					0,
					line.intValue(),
					index.intValue());
		return result;
					
	}
	

}
