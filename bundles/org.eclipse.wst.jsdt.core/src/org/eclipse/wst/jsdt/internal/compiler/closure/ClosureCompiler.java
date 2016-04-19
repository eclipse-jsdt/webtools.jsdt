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

package org.eclipse.wst.jsdt.internal.compiler.closure;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.BlockComment;
import org.eclipse.wst.jsdt.core.dom.ClosureCompilerASTConverter;
import org.eclipse.wst.jsdt.core.dom.JSdoc;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.LineComment;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblem;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemSeverities;

import com.google.javascript.jscomp.parsing.parser.Parser;
import com.google.javascript.jscomp.parsing.parser.Parser.Config;
import com.google.javascript.jscomp.parsing.parser.SourceFile;
import com.google.javascript.jscomp.parsing.parser.trees.Comment;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import com.google.javascript.jscomp.parsing.parser.util.SourcePosition;

/**
 * @author Gorkem Ercan
 *
 */
public class ClosureCompiler {
	
	private static class ErrorCollector extends com.google.javascript.jscomp.parsing.parser.util.ErrorReporter {
		
		private final String fileName;
		private final List<IProblem> problems = new ArrayList<IProblem>();
		

		public ErrorCollector(String file) {
			this.fileName = file;
		}
		
		@Override
		protected void reportError(SourcePosition location, String message) {
			addProblem(message,location,ProblemSeverities.Error);
		}

		protected void reportWarning(SourcePosition location, String message) {
			addProblem(message,location,ProblemSeverities.Warning);
		}

		IProblem[] problems(){
			return problems.toArray(new DefaultProblem[problems.size()]);
		}
		
		private void addProblem(String description, SourcePosition location, int severity){
			

			DefaultProblem result = new DefaultProblem(fileName.toCharArray(),
						description,
						0,
						null,
						severity,
						location.offset,
						-1,
						location.line,
						location.column);
			problems.add(result);
		}
	}
	
	private String rawContent;
	private IJavaScriptUnit unit;
	private boolean commentsEnabled;
	
	private ClosureCompiler(){
		super();
	}
	
	public static ClosureCompiler newInstance(){
		return new ClosureCompiler();
	}
	
	public ClosureCompiler setSource(String content){
		this.unit = null;
		this.rawContent = content;
		return this;
	}

	public ClosureCompiler setSource(IJavaScriptUnit content){
		this.rawContent = null;
		this.unit = content;
		return this;
	}
	
	public ClosureCompiler toggleComments( boolean enabled){
		this.commentsEnabled = enabled;
		return this;
	}

	public JavaScriptUnit parse() {
		Config config = new Config(com.google.javascript.jscomp.parsing.parser.Parser.Config.Mode.ES6);
		SourceFile source = getSourceFile(); 
		ErrorCollector errorCollector = new  ErrorCollector(source.name);
		Parser parser = new Parser(config, errorCollector, source);
		ProgramTree tree = parser.parseProgram();
		AST ast = AST.newAST(AST.JLS3);
		ast.setDefaultNodeFlag(ASTNode.ORIGINAL);
		ClosureCompilerASTConverter transformer = new ClosureCompilerASTConverter(ast, parser.getComments());
		JavaScriptUnit $ = (JavaScriptUnit) transformer.transform(null,tree);
		$.setLineEndTable(LineNumberComputer.computeLineTable(source.contents));
		$.setProblems(errorCollector.problems());
		if(commentsEnabled){
			$.setCommentTable(buildComments(parser.getComments(), ast));
		}
		ast.setDefaultNodeFlag(0);
		return $;
	}
	
	/**
	 * @param comments
	 */
	private org.eclipse.wst.jsdt.core.dom.Comment[] buildComments(List<Comment> comments, AST ast) {
		ArrayList<org.eclipse.wst.jsdt.core.dom.Comment> domComments = new ArrayList<org.eclipse.wst.jsdt.core.dom.Comment>();
		for (Comment comment : comments) {
			switch (comment.type) {
				case BLOCK :
					BlockComment bc = ast.newBlockComment();
					setSourceRange(bc,comment);
					domComments.add(bc);
					break;
				case SHEBANG:
				case LINE:
					LineComment lc = ast.newLineComment();
					setSourceRange(lc,comment);
					domComments.add(lc);
					break;
				case JSDOC:
					JSdoc jd = ast.newJSdoc();
					jd.setComment(comment.value);
					setSourceRange(jd,comment);
					domComments.add(jd);
					break;
			}
		}
		return domComments.toArray(new org.eclipse.wst.jsdt.core.dom.Comment[domComments.size()]);
	}
	
	private void setSourceRange(ASTNode node, Comment comment) {
		node.setSourceRange(comment.location.start.offset, comment.location.end.offset - comment.location.start.offset);
	}

	private SourceFile getSourceFile(){
		String content = rawContent;
		String filename = ""; //$NON-NLS-1$
		if(unit != null )
			try {
				content = unit.getSource();
				filename= unit.getDisplayName();
			}
			catch (JavaScriptModelException e) {
				e.printStackTrace();
			}
		return(new SourceFile(filename,content));
	}
	
}
