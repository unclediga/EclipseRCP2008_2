package com.qualityeclipse.favorites.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.qualityeclipse.favorites.FavoritesLog;

public class PropertiesEditor extends MultiPageEditorPart {

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (!(input instanceof IFileEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		super.init(site, input);
	}

	private TreeViewer treeViewer;
	private TextEditor textEditor;

	@Override
	protected void createPages() {
		createPropertiesPage();
		createSourcePage();
		updateTitle();
	}

	private TreeColumn keyColumn;
	private TreeColumn valueColumn;
	
	private void createPropertiesPage() {

		Composite treeContainer = new Composite(getContainer(), SWT.NONE);
		TreeColumnLayout layout = new TreeColumnLayout();
		treeContainer.setLayout(layout);
		
		treeViewer = new TreeViewer(treeContainer, SWT.MULTI
				| SWT.FULL_SELECTION);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		
		keyColumn = new TreeColumn(tree,SWT.NONE);
		keyColumn.setText("Key");
		layout.setColumnData(keyColumn, new ColumnWeightData(2));

		valueColumn = new TreeColumn(tree,SWT.NONE);
		valueColumn.setText("Value");
		layout.setColumnData(valueColumn, new ColumnWeightData(3));
		
		int index = addPage(treeContainer);
		setPageText(index, "Properties");
		
		
		
		
	}

	private void createSourcePage() {

		textEditor = new TextEditor();
		int index;
		try {
			index = addPage(textEditor, getEditorInput());
			setPageText(index, "Source");
		} catch (PartInitException e) {
			FavoritesLog.logError("Error creating nested text editor", e);
		}
	}

	private void updateTitle() {

		IEditorInput editorInput = getEditorInput();
		setPartName(editorInput.getName());
		setTitleToolTip(editorInput.getToolTipText());

	}

	public void setFocus() {
		switch (getActivePage()) {
		case 0:
			treeViewer.getTree().setFocus();
			break;
		case 1:
			textEditor.setFocus();
			break;
		}
	}

	public void gotoMarker(IMarker marker) {
		setActivePage(1);
		((IGotoMarker) textEditor.getAdapter(IGotoMarker.class))
				.gotoMarker(marker);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		textEditor.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		textEditor.doSaveAs();
		setInput(textEditor.getEditorInput());
		updateTitle();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

}
