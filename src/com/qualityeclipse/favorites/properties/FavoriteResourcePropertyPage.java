package com.qualityeclipse.favorites.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.qualityeclipse.favorites.FavoritesLog;
import com.qualityeclipse.favorites.model.BasicFavoriteItem;

public class FavoriteResourcePropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	private Text textField;

	@Override
	protected Control createContents(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		panel.setLayout(layout);
		Label label = new Label(panel, SWT.NONE);
		label.setLayoutData(new GridData());
		label.setText("Comment that appears as hover help in the Favorites view:");
		textField = new Text(panel, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		textField.setLayoutData(new GridData(GridData.FILL_BOTH));
		textField.setText(getCommentPropertyValue());
		return panel;
	}

	protected String getCommentPropertyValue() {
		IResource resource = (IResource) getElement().getAdapter(
				IResource.class);
		try {
			String value = resource
					.getPersistentProperty(BasicFavoriteItem.COMMENT_PROPKEY);
			if (value == null)
				return BasicFavoriteItem.getDefaultComment();
			return value;
		} catch (CoreException e) {
			FavoritesLog.logError(e);
			return e.getMessage();
		}
	}

	protected void setCommentPropertyValue(String comment) {
		IResource resource = (IResource) getElement().getAdapter(
				IResource.class);
		String value = comment;
		if (value.equals(BasicFavoriteItem.getDefaultComment()))
			value = null;
		try {
			resource.setPersistentProperty(BasicFavoriteItem.COMMENT_PROPKEY,
					value);
		} catch (CoreException e) {
			FavoritesLog.logError(e);
		}
	}
	
	@Override
	public boolean performOk() {
		setCommentPropertyValue(textField.getText());
		return super.performOk();
	}

	public FavoriteResourcePropertyPage() {
		// TODO Auto-generated constructor stub
	}

}