package com.qualityeclipse.favorites.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.qualityeclipse.favorites.FavoritesLog;
import com.qualityeclipse.favorites.views.AltClickCellEditListener;

public class PropertiesEditor extends MultiPageEditorPart {

	private final PropertyFileListener propertyFileListener = new PropertyFileListener() {
		public void keyChanged(PropertyCategory category, PropertyEntry entry) {
			treeViewer.refresh(entry);
			treeModified();
		}

		public void valueChanged(PropertyCategory category, PropertyEntry entry) {
			treeViewer.refresh(entry);
			treeModified();
		}

		public void nameChanged(PropertyCategory category) {
			treeViewer.refresh(category);
			treeModified();
		}

		public void entryAdded(PropertyCategory category, PropertyEntry entry) {
			treeViewer.refresh();
			treeModified();
		}

		public void entryRemoved(PropertyCategory category, PropertyEntry entry) {
			treeViewer.refresh();
			treeModified();
		}

		public void categoryAdded(PropertyCategory category) {
			treeViewer.refresh();
			treeModified();
		}

		public void categoryRemoved(PropertyCategory category) {
			treeViewer.refresh();
			treeModified();
		}
	};

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
		initTreeContent();
		initTreeEditors();
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

		keyColumn = new TreeColumn(tree, SWT.NONE);
		keyColumn.setText("Key");
		layout.setColumnData(keyColumn, new ColumnWeightData(2));

		valueColumn = new TreeColumn(tree, SWT.NONE);
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

	private PropertiesEditorContentProvider treeContentProvider;
	private PropertiesEditorLabelProvider treeLabelProvider;

	void initTreeContent() {
		treeContentProvider = new PropertiesEditorContentProvider();
		treeViewer.setContentProvider(treeContentProvider);
		treeLabelProvider = new PropertiesEditorLabelProvider();
		treeViewer.setLabelProvider(treeLabelProvider);

		// Reset the input from the text editor’s content
		// after the editor initialization has completed.
		treeViewer.setInput(new PropertyFile(""));
		treeViewer.getTree().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				updateTreeFromTextEditor();
			}
		});
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
	}

	void updateTreeFromTextEditor() {
		PropertyFile propertyFile = (PropertyFile) treeViewer.getInput();
		propertyFile.removePropertyFileListener(propertyFileListener);
		propertyFile = new PropertyFile(textEditor.getDocumentProvider()
				.getDocument(textEditor.getEditorInput()).get());
		treeViewer.setInput(propertyFile);
		propertyFile.addPropertyFileListener(propertyFileListener);
	}

	private void initTreeEditors() {
		TreeViewerColumn column1 = new TreeViewerColumn(treeViewer, keyColumn);
		TreeViewerColumn column2 = new TreeViewerColumn(treeViewer, valueColumn);

		column1.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return treeLabelProvider.getColumnText(element, 0);
			}
		});
		column2.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return treeLabelProvider.getColumnText(element, 1);
			}
		});

		column1.setEditingSupport(new EditingSupport(treeViewer) {
			TextCellEditor editor = null;

			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				if (editor == null) {
					Composite tree = (Composite) treeViewer.getControl();
					editor = new TextCellEditor(tree);
					editor.setValidator(new ICellEditorValidator() {
						public String isValid(Object value) {
							if (((String) value).trim().length() == 0)
								return "Key must not be empty string";
							return null;
						}
					});
					editor.addListener(new ICellEditorListener() {
						public void applyEditorValue() {
							setErrorMessage(null);
						}

						public void cancelEditor() {
							setErrorMessage(null);
						}

						public void editorValueChanged(boolean oldValidState,
								boolean newValidState) {
							setErrorMessage(editor.getErrorMessage());
						}

						private void setErrorMessage(String errorMessage) {
							getEditorSite().getActionBars()
									.getStatusLineManager()
									.setErrorMessage(errorMessage);
						}
					});
				}
				return editor;
			}

			protected Object getValue(Object element) {
				return treeLabelProvider.getColumnText(element, 0);
			}

			protected void setValue(Object element, Object value) {
				if (value == null)
					return;
				String text = ((String) value).trim();
				if (element instanceof PropertyCategory)
					((PropertyCategory) element).setName(text);
				if (element instanceof PropertyEntry)
					((PropertyEntry) element).setKey(text);
			}
		});

		column2.setEditingSupport(new EditingSupport(treeViewer) {
			TextCellEditor editor = null;

			protected boolean canEdit(Object element) {
				return element instanceof PropertyEntry;
			}

			protected CellEditor getCellEditor(Object element) {
				if (editor == null) {
					Composite tree = (Composite) treeViewer.getControl();
					editor = new TextCellEditor(tree);
				}
				return editor;
			}

			protected Object getValue(Object element) {
				return treeLabelProvider.getColumnText(element, 1);
			}

			protected void setValue(Object element, Object value) {
				String text = ((String) value).trim();
				if (element instanceof PropertyEntry)
					((PropertyEntry) element).setValue(text);
			}
		});

		 treeViewer.getColumnViewerEditor().addEditorActivationListener(
		 new AltClickCellEditListener());

	}

	private boolean isPageModified;
	
	public void treeModified() {
		boolean wasDirty = isDirty();
		isPageModified = true;
		if (!wasDirty)
			firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	protected void handlePropertyChange(int propertyId) {
		if (propertyId == IEditorPart.PROP_DIRTY)
			isPageModified = isDirty();
		super.handlePropertyChange(propertyId);
	}	
	
	@Override
	public boolean isDirty() {
		return isPageModified || super.isDirty();
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
