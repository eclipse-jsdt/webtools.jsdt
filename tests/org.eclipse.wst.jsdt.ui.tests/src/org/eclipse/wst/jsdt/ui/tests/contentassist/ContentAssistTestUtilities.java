/*******************************************************************************
 * Copyright (c) 2011, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc. - refactoring
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.wst.jsdt.internal.ui.text.html.HTML2TextReader;
import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;
import org.eclipse.wst.jsdt.ui.text.IJavaScriptPartitions;
import org.eclipse.wst.jsdt.ui.text.JavaScriptSourceViewerConfiguration;
import org.junit.Assert;

/**
 * <p>
 * Helpful utilities for running content assist tests.
 * </p>
 *
 * @see org.ecliplse.wst.jsdt.ui.tests.contentassist.ContentAssistUtilities
 * @see org.eclipse.wst.jsdt.web.ui.tests.contentassist.ContentAssistUtilities
 */
public class ContentAssistTestUtilities {
	/**
	 * <p>
	 * Run a proposal test by opening the given file and invoking content assist for each expected
	 * proposal count at the given line number and line character offset and then compare the number
	 * of proposals for each invocation (pages) to the expected number of proposals.
	 * </p>
	 *
	 * @param testProject
	 * @param filePath
	 * @param lineNum - subtract 1 from what the Eclipse Text Editor shows for line number
	 * @param lineRelativeCharOffset - subtract 1 from what the Eclipse Text Editor shows for column number
	 * @param expectedProposalCounts
	 * @throws Exception
	 */
	public static void runProposalTest(TestProjectSetup testProject, String filePath, int lineNum, int lineRelativeCharOffset,
			String[][] expectedProposals) throws Exception {

		runProposalTest(testProject, filePath, lineNum, lineRelativeCharOffset, expectedProposals, false, true);
	}

	/**
	 * <p>
	 * Run a proposal test by opening the given file and invoking content assist for each expected
	 * proposal count at the given line number and line character offset and then compare the number
	 * of proposals for each invocation (pages) to the expected number of proposals.
	 * </p>
	 *
	 * @param testProject
	 * @param filePath
	 * @param lineNum
	 * @param lineRelativeCharOffset
	 * @param expectedProposalCounts
	 * @param negativeTest
	 *            <code>true</code> if expectedProposals should not be found, <code>false</code> if
	 *            they should be found
	 * @param exactMatch
	 *            <code>true</code> if expectedProposals should be compared to actual proposals with
	 *            {@link String#equals(Object)}, <code>false</code> if {@link String#indexOf(int)}
	 *            should be used
	 * @throws Exception
	 */
	public static void runProposalTest(TestProjectSetup testProject, String filePath, int lineNum, int lineRelativeCharOffset,
			String[][] expectedProposals, boolean negativeTest, boolean exactMatch) throws Exception {

		ICompletionProposal[][] pages = getProposals(testProject, filePath, lineNum, lineRelativeCharOffset,
				expectedProposals.length);
		verifyExpectedProposal(pages, expectedProposals, negativeTest, exactMatch, false);
	}

	/**
	 * <p>
	 * Run a proposal test by opening the given file and invoking content assist for each expected
	 * proposal count at the given line number and line character offset and then compare the number
	 * of proposals for each invocation (pages) to the expected number of proposals.
	 * </p>
	 *
	 * @param testProject
	 * @param filePath
	 * @param lineNum
	 * @param lineRelativeCharOffset
	 * @param expectedProposalCounts
	 * @param negativeTest
	 *            <code>true</code> if expectedProposals should not be found, <code>false</code> if
	 *            they should be found
	 * @param exactMatch
	 *            <code>true</code> if expectedProposals should be compared to actual proposals with
	 *            {@link String#equals(Object)}, <code>false</code> if {@link String#indexOf(int)}
	 *            should be used
	 * @param inOrder
	 *            <code>true</code> if <code>expectedProposals</code> should be found in the order
	 *            they are given, <code>false</code> if order does not matter
	 *
	 * @throws Exception
	 */
	public static void runProposalTest(TestProjectSetup testProject, String filePath, int lineNum, int lineRelativeCharOffset,
			String[][] expectedProposals, boolean negativeTest, boolean exactMatch, boolean inOrder) throws Exception {

		ICompletionProposal[][] pages = getProposals(testProject, filePath, lineNum, lineRelativeCharOffset,
				expectedProposals.length);
		verifyExpectedProposal(pages, expectedProposals, negativeTest, exactMatch, inOrder);
	}

	/**
	 * <p>
	 * Run a proposal info test by opening the given file and invoking content assist for each
	 * expected proposal count at the given line number and line character offset and then compare
	 * the proposal Info for each invocation (pages) with its expected Proposal Info .
	 * </p>
	 *
	 * @param testProject
	 * @param filePath
	 * @param lineNum
	 * @param lineRelativeCharOffset
	 * @param expectedProposalInfo
	 * @throws Exception
	 */
	public static void runProposalInfoTest(TestProjectSetup testProject, String filePath, int lineNum, int lineRelativeCharOffset,
			String[][] expectedProposals, String[][] expectedProposalInfo) throws Exception {

		ICompletionProposal[][] pages = getProposals(testProject, filePath, lineNum, lineRelativeCharOffset,
				expectedProposals.length);
		verifyExpectedProposalInfo(pages, expectedProposals, expectedProposalInfo);
	}

	/**
	 * <p>
	 * Runs a test for finding whether duplicate proposals are generated when content assist is
	 * invoked.
	 * </p>
	 *
	 * @param testProject
	 * @param filePath
	 * @param lineNum
	 * @param lineRelativeOffset
	 */
	public static void verifyNoDuplicates(TestProjectSetup testProject, String filePath, int lineNum, int lineRelativeCharOffset) throws Exception {

		int numOfPages = 1;
		ICompletionProposal[][] pages = getProposals(testProject, filePath, lineNum, lineRelativeCharOffset, numOfPages);
		checkForDuplicates(pages, numOfPages);
	}

	/**
	 * <p>
	 * Returns the content assist proposals when invoked at the offset provided.
	 * </p>
	 *
	 * @param fileNum
	 * @param lineNum
	 * @param lineRelativeCharOffset
	 * @param numOfPages
	 */
	private static ICompletionProposal[][] getProposals(TestProjectSetup testProject, String filePath, int lineNum, int lineRelativeCharOffset,
			int numOfPages) throws Exception {

		IFile file = testProject.getFile(filePath);
		JavaEditor editor = testProject.getEditor(file);
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		int offset = doc.getLineOffset(lineNum) + lineRelativeCharOffset;

		ICompletionProposal[][] pages = getProposals(editor, offset, numOfPages);
		return pages;
	}

	/**
	 * <p>
	 * Invoke content assist on the given editor at the given offset, for the given number of pages
	 * and return the results of each page
	 * </p>
	 */
	private static ICompletionProposal[][] getProposals(JavaEditor editor, int offset, int pageCount) throws Exception {
		// setup the viewer
		JavaScriptSourceViewerConfiguration configuration = new JavaScriptSourceViewerConfiguration(
				JavaScriptPlugin.getDefault().getJavaTextTools().getColorManager(),
				JavaScriptPlugin.getDefault().getCombinedPreferenceStore(), editor,
				IJavaScriptPartitions.JAVA_PARTITIONING);
		ISourceViewer viewer = editor.getViewer();
		ContentAssistant contentAssistant = (ContentAssistant) configuration.getContentAssistant(viewer);

		// get the processor
		String partitionTypeID = viewer.getDocument().getPartition(offset).getType();
		IContentAssistProcessor processor = contentAssistant.getContentAssistProcessor(partitionTypeID);

		// fire content assist session about to start
		Method privateFireSessionBeginEventMethod = ContentAssistant.class.getDeclaredMethod("fireSessionBeginEvent",
				new Class[] { boolean.class });
		privateFireSessionBeginEventMethod.setAccessible(true);
		privateFireSessionBeginEventMethod.invoke(contentAssistant, new Object[] { Boolean.TRUE });

		// get content assist suggestions
		ICompletionProposal[][] pages = new ICompletionProposal[pageCount][];
		for(int p = 0; p < pageCount; ++p) {
			pages[p] = processor.computeCompletionProposals(viewer, offset);
		}

		// fire content assist session ending
		Method privateFireSessionEndEventMethod = ContentAssistant.class.getDeclaredMethod("fireSessionEndEvent", null);
		privateFireSessionEndEventMethod.setAccessible(true);
		privateFireSessionEndEventMethod.invoke(contentAssistant, null);

		return pages;
	}

	/**
	 * @param pages
	 * @param expectedProposals
	 * @param negativeTest
	 *            <code>true</code> if <code>expectedProposals</code> should not be found,
	 *            <code>false</code> if
	 *            they should be found
	 * @param exactMatch
	 *            <code>true</code> if expectedProposals should be compared to actual proposals with
	 *            {@link String#equals(Object)}, <code>false</code> if {@link String#indexOf(int)}
	 *            should be used
	 * @param inOrder
	 *            <code>true</code> if <code>expectedProposals</code> should be found in the order
	 *            they are given, <code>false</code> if order does not matter
	 */
	private static void verifyExpectedProposal(ICompletionProposal[][] pages, String[][] expectedProposals,
			boolean negativeTest, boolean exactMatch, boolean inOrder) {

		StringBuffer error = new StringBuffer();
		int lastFoundIndex = -1;
		String lastFoundProposal = ""; //$NON-NLS-1$
		for(int page = 0; page < expectedProposals.length; ++page) {
			for(int expected = 0; expected < expectedProposals[page].length; ++expected) {
				String expectedProposal = expectedProposals[page][expected];
				boolean found = false;
				int suggestion = 0;
				for( ; (suggestion < pages[page].length) && !found; ++suggestion) {
					String displayString = pages[page][suggestion].getDisplayString();
					found = found || (exactMatch ?
							displayString.equals(expectedProposal) : displayString.indexOf(expectedProposal) != -1);
				}

				/* error if checking for in order and this expected proposal was
				 * found at an index lower then the last found proposal */
				if(inOrder && found && (suggestion < lastFoundIndex)) {
					error.append("\nProposal was found out of order: " + expectedProposal + " found before " + lastFoundProposal);
				}

				if(found && negativeTest) {
					error.append("\nUnexpected proposal was found on page " + page + ": '" + expectedProposal + "'");
				} else if(!found && !negativeTest) {
					error.append("\n Expected proposal was not found on page " + page + ": '" + expectedProposal + "'");
				}

				if(found) {
					lastFoundProposal = expectedProposal;
					lastFoundIndex = suggestion;
				}
			}
		}
		if (negativeTest) {
//			System.out.println(error.length());
		}
		// if errors report them
		if(error.length() > 0) {
			StringBuffer expected = new StringBuffer();
			for (String[] expectedProposal : expectedProposals) {
				for (String element : expectedProposal) {
					expected.append(element);
					expected.append('\n');
				}
			}
			StringBuffer actual = new StringBuffer();
			for (ICompletionProposal[] page : pages) {
				for (ICompletionProposal element : page) {
					actual.append(element.getDisplayString());
					actual.append('\n');
				}
			}
			Assert.assertEquals(error.toString(), expected.toString(), actual.toString());
		}
	}

	/**
	 *
	 * <p>
	 * Compares the expected proposal Info with the one generated from the JavaDoc
	 * </p>
	 *
	 * @param pages
	 * @param expectedProposalInfo
	 */

	private static void verifyExpectedProposalInfo(ICompletionProposal[][] pages, String[][] expectedProposals,
			String[][] expectedProposalsInfo) {
		StringBuffer error = new StringBuffer();
		String proposalInfo = new String();
		for(int page = 0; page < expectedProposals.length; ++page) {
			for(int expected = 0; expected < expectedProposals[page].length; ++expected) {
				String expectedProposal = expectedProposals[page][expected];
				String expectedProposalInfo = expectedProposalsInfo[page][expected];
				boolean found = false;
				for(int suggestion = 0; (suggestion < pages[page].length) && !found; ++suggestion) {
					found = pages[page][suggestion].getDisplayString().equals(expectedProposal);
					if(found) {
						proposalInfo = pages[page][suggestion].getAdditionalProposalInfo();

						if((proposalInfo == null) || (proposalInfo.indexOf(expectedProposalInfo) < 0)) {
							error.append("\n" + "Required proposal info for " + expectedProposal + " does not exist.");
						}
						break;
					}

				}
			}

		}

		// if errors report them
		if(error.length() > 0) {
			StringBuffer expected = new StringBuffer();
			for (String[] element : expectedProposalsInfo) {
				for (String element2 : element) {
					expected.append(element2);
					expected.append('\n');
				}
			}
			StringBuffer actual = new StringBuffer();
			for (ICompletionProposal[] page : pages) {
				for (ICompletionProposal element : page) {
					try {
						String rawAdditionalProposalInfo = element.getAdditionalProposalInfo();
						if (rawAdditionalProposalInfo != null) {
							actual.append(new HTML2TextReader(new StringReader(rawAdditionalProposalInfo), null).getString().trim());
						}
					} catch(IOException e) {
						e.printStackTrace();
					}
					actual.append('\n');
				}
			}
			Assert.assertEquals(error.toString(), expected.toString(), actual.toString());
		}
	}

	/**
	 * <p>
	 * Checks for Duplicates and reports an error if found.
	 * </p>
	 *
	 * @param pages
	 * @param numOfPages
	 */
	private static void checkForDuplicates(ICompletionProposal[][] pages, int numOfPages) {
		Set set = new HashSet();
		StringBuffer error = new StringBuffer();

		for(int suggestion = 0; suggestion < pages[0].length; ++suggestion) {
			if(set.contains(pages[0][suggestion].toString())) {
				error.append("\nDuplicate proposal found: '" + pages[0][suggestion] + "'");
			} else {
				set.add(pages[0][suggestion].toString());
			}
		}

		if(error.length() > 0) {
			Assert.fail(error.toString());
		}

		set.clear();
	}

	/**
	 * Get a proposal and test by inserting computed proposal into the Editor.
	 */
	public static void runProposalandInertTest(TestProjectSetup testProject, String filePath, int lineNum, int lineRelativeCharOffset, String expectedResult) throws Exception{

			System.out.println("TEST PROJECT _ > " + testProject);
			IFile file = testProject.getFile(filePath);
			JavaEditor editor  = testProject.getEditor(file);
			IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			int offset = doc.getLineOffset(lineNum) + lineRelativeCharOffset;

			ICompletionProposal[][] pages = getProposals(editor, offset, 1);

			verifyInsertProposalToEditor(editor, offset, pages, expectedResult);
	}

	/**
	 * Verify insert proposal to Editor between parenthesis.
	 *
	 * @param editor JavaEditor of Content Assist invoked
	 * @param offset Location of Content Assist invoked.
	 * @param pages computed CompletionProposals
	 * @param expectedResult Expected result after proposal inserted to Editor
	 * @throws Exception
	 */
	public static void verifyInsertProposalToEditor(JavaEditor editor, int offset, ICompletionProposal[][] pages, String expectedResult) throws Exception {
		ISourceViewer viewer = editor.getViewer();
		Field fContentAssistant = SourceViewer.class.getDeclaredField("fContentAssistant");
		fContentAssistant.setAccessible(true);
		ContentAssistant contentassistant = (ContentAssistant)fContentAssistant.get(viewer);

		Field fProposalPopup = ContentAssistant.class.getDeclaredField("fProposalPopup");
		fProposalPopup.setAccessible(true);
		Object objPopup = fProposalPopup.get(contentassistant);

		Class proposalPopupClass = Class.forName("org.eclipse.jface.text.contentassist.CompletionProposalPopup");
		Method privateInsertProposalMethod = proposalPopupClass.getDeclaredMethod("insertProposal", new Class[]{ICompletionProposal.class,char.class,int.class,int.class});
		privateInsertProposalMethod.setAccessible(true);

		// Set selected range to properly compute ReplacementLength in AbstractJavaCompletionProposal.
		viewer.setSelectedRange(offset, 0);

		// Invoke insertProposal of CompletionProposalPopup
		privateInsertProposalMethod.invoke(objPopup, new Object[]{pages[0][0], Character.valueOf((char) 0), Integer.valueOf(524288), Integer.valueOf(offset)});

		// Get result of inserted proposal in the Editor
		String strAfterInsert = viewer.getDocument().get();

		if (!expectedResult.equals(strAfterInsert)) {
			Assert.fail("\n<ExpectedResult>\n" + expectedResult + "\n<The result after inserting to Editor>\n" + strAfterInsert);
		}
	}

}