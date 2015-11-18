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

import java.util.Map;
import java.util.Stack;

import org.eclipse.core.runtime.Assert;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * @author Gorkem Ercan
 *
 */
public class EStreeVisitor {
	
	public enum VisitOptions{ 
		/**
		 * Traverse children
		 */
		CONTINUE, 
		/**
		 * Skip children
		 */
		SKIP,
		/**
		 * Break traversal
		 */
		BREAK }
	
	class WorkElement{
		
		ScriptObjectMirror node;
		boolean endVisit;
		String key;
		
		/**
		 * 
		 */
		public WorkElement(ScriptObjectMirror node,String key, boolean endVisit) {
			this.node = node;
			this.key = key;
			this.endVisit = endVisit;
		}
	}
	
	final private Stack<WorkElement> workStack = new Stack<WorkElement>();
	
	public void traverse(ScriptObjectMirror root) {
		workStack.push(new WorkElement(root,null, false));

		while (!workStack.empty()) {
			WorkElement element = workStack.pop();
			VisitOptions vo = callVisit(element);
			if (vo == VisitOptions.BREAK) {
				return;
			}
			// No breaks push endVisit to stack
			if (!element.endVisit)
				workStack.push(new WorkElement(element.node, element.key, true));

			if (element.endVisit || vo == VisitOptions.SKIP) { // skip children
				continue;
			}
			
			// Find children and add them to worklist
			ESTreeNodeTypes nodeType = getNodeType(element);
			Assert.isNotNull(nodeType);
			String[] keys = nodeType.getVisitorKeys();
			for (int i = keys.length - 1; i > -1; i--) {
				Object o = element.node.getMember(keys[i]);
				if (o instanceof ScriptObjectMirror) {
					ScriptObjectMirror candidate = (ScriptObjectMirror) o;
					if (candidate.isArray()) {
						// Create WorkElements for Array in reverse order 
						// to keep the order coming out of stack
						Object[] arrayElements = candidate.entrySet().toArray();
						for (int j = arrayElements.length-1; j > -1; j--) {
							Map.Entry<String, Object> entry = (java.util.Map.Entry<String, Object>) arrayElements[j];
							Object result = entry.getValue();
							if (result instanceof ScriptObjectMirror) {
								workStack.push(new WorkElement((ScriptObjectMirror) result, keys[i] ,false));
							}
						}
					}
					else {
						workStack.push(new WorkElement(candidate, keys[i], false));
					}
				}
			}
		}
	}
	
	private VisitOptions callVisit(WorkElement element){
		ESTreeNodeTypes nodeType = getNodeType(element);
		Assert.isNotNull(nodeType);
		if(element.endVisit){
			return endVisit(element.node, nodeType, element.key);
		}
		return visit(element.node, nodeType, element.key);
	}

	private ESTreeNodeTypes getNodeType(WorkElement element) {
		String type = (String)element.node.get("type");
		ESTreeNodeTypes nodeType = ESTreeNodeTypes.valueOf(type);
		return nodeType;
	}
	
	public VisitOptions visit(ScriptObjectMirror object, ESTreeNodeTypes nodeType, String key){
		return VisitOptions.CONTINUE;
	}
	
	public VisitOptions endVisit(ScriptObjectMirror object, ESTreeNodeTypes nodeType, String key){
		return VisitOptions.CONTINUE;
	}	

}
