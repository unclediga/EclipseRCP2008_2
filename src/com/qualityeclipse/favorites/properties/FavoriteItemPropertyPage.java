package com.qualityeclipse.favorites.properties;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.qualityeclipse.favorites.model.BasicFavoriteItem;
import com.qualityeclipse.favorites.model.IFavoriteItem;

public class FavoriteItemPropertyPage extends FavoriteResourcePropertyPage {

	private ColorSelector colorSelector;

	@Override
	protected Control createContents(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		panel.setLayout(layout);
		Label label = new Label(panel, SWT.NONE);
		label.setLayoutData(new GridData());
		label.setText("Color of item in Favorites View:");
		colorSelector = new ColorSelector(panel);
		colorSelector.setColorValue(getColorPropertyValue());
		colorSelector.getButton().setLayoutData(new GridData(100, SWT.DEFAULT));
		Composite subpanel = (Composite) super.createContents(panel);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		subpanel.setLayoutData(gridData);
		panel.setLayoutData(gridData);
		return panel;
	}

	protected RGB getColorPropertyValue() {
		IFavoriteItem item = (IFavoriteItem) getElement();
		Color color = item.getColor();
		return color.getRGB();
	}

	protected void setColorPropertyValue(RGB rgb) {
		IFavoriteItem item = (IFavoriteItem) getElement();
		Color color = BasicFavoriteItem.getColor(rgb);
		if (color.equals(BasicFavoriteItem.getDefaultColor()))
			color = null;
		item.setColor(color);
	}

	public boolean performOk() {
		setColorPropertyValue(colorSelector.getColorValue());
		return super.performOk();
	}

	public FavoriteItemPropertyPage() {
		// TODO Auto-generated constructor stub
	}
}
