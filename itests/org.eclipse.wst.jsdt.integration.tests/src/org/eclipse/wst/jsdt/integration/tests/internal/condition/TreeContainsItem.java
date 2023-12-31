/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.wst.jsdt.integration.tests.internal.condition;

import java.util.List;

import org.hamcrest.Matcher;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;

/**
 * 
 * @author Pavol Srna
 *
 */
public class TreeContainsItem extends AbstractWaitCondition {

	private Tree tree;
	private Matcher matcher;
	private boolean recursive = true;

	/**
	 * Constructs TreeContainsItem wait condition. Condition is met when the
	 * specified tree contains the tree item with specified text.
	 * 
	 * @param tree tree where to look for an item
	 * @param matcher to match the text of the item
	 */
	public TreeContainsItem(Tree tree, Matcher matcher) {
		this.tree = tree;
		this.matcher = matcher;
	}
	
	public TreeContainsItem(Tree tree, Matcher matcher, boolean recursive) {
		this.tree = tree;
		this.matcher = matcher;
		this.recursive = recursive;
	}

	@Override
	public boolean test() {
		try{
			List<TreeItem> list = recursive ? tree.getAllItems() : tree.getItems();
			for (TreeItem i : list) {
				if (matcher.matches(i.getText())) {
					return true;
				}
			}
		}catch (CoreLayerException e) {
			return false;
		}
		return false;
	}

	@Override
	public String description() {
		return "tree contains item '" + matcher.toString();
	}

}