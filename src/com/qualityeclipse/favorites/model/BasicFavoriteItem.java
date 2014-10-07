package com.qualityeclipse.favorites.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.qualityeclipse.favorites.FavoritesActivator;

public abstract class BasicFavoriteItem implements IFavoriteItem,
		IPropertySource2 {
	private Color color;
	private static Color defaultColor;

	public static final QualifiedName COMMENT_PROPKEY = new QualifiedName(
			FavoritesActivator.PLUGIN_ID, "comment");

	public static final String COMMENT_PREFKEY = "defaultComment";

	public static String getDefaultComment() {
		return FavoritesActivator.getDefault().getPreferenceStore()
				.getString(COMMENT_PREFKEY);
	}

	public static void setDefaultComment(String comment) {
		FavoritesActivator.getDefault().getPreferenceStore()
				.setValue(COMMENT_PREFKEY, comment);
	}

	public Color getColor() {
		if (color == null)
			return getDefaultColor();
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		FavoritesManager.getManager().fireFavoritesItemChanged(this);
	}

	public static Color getDefaultColor() {
		if (defaultColor == null)
			defaultColor = getColor(new RGB(0, 0, 0));
		return defaultColor;
	}

	public static void setDefaultColor(Color color) {
		defaultColor = color;
	}

	// / COLOR UTILS ///////////
	private static final Map<RGB, Color> colorCache = new HashMap<RGB, Color>();

	public static Color getColor(RGB rgb) {
		Color color = colorCache.get(rgb);
		if (color == null) {
			Display display = Display.getCurrent();
			color = new Color(display, rgb);
			colorCache.put(rgb, color);
		}
		return color;
	}

	public static void disposeColors() {
		Iterator<Color> iter = colorCache.values().iterator();
		while (iter.hasNext())
			iter.next().dispose();
		colorCache.clear();
	}

	private static final String COLOR_ID = "favorite.color";
	private static final ColorPropertyDescriptor COLOR_PROPERTY_DESCRIPTOR = new ColorPropertyDescriptor(
			COLOR_ID, "Color");

	private static final String HASH_ID = "favorite.hash";
	private static final TextPropertyDescriptor HASH_PROPERTY_DESCRIPTOR = new TextPropertyDescriptor(
			HASH_ID, "Hash Code");
	static {
		HASH_PROPERTY_DESCRIPTOR.setCategory("Other");
		HASH_PROPERTY_DESCRIPTOR
				.setFilterFlags(new String[] { IPropertySheetEntry.FILTER_ID_EXPERT });
		HASH_PROPERTY_DESCRIPTOR.setAlwaysIncompatible(true);
	}

	private static final IPropertyDescriptor[] DESCRIPTORS = {
			COLOR_PROPERTY_DESCRIPTOR, HASH_PROPERTY_DESCRIPTOR };

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return DESCRIPTORS;
	}

	public Object getPropertyValue(Object id) {
		if (id.equals(COLOR_ID)) {
			return getColor().getRGB();
		} else if (id.equals(HASH_ID)) {
			return new Integer(hashCode());
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if (COLOR_ID.equals(id))
			return getColor() != getDefaultColor();
		if (HASH_ID.equals(id)) {
			// return true for indicating that hash
			// does not have a meaningful default value
			return true;
		}
		return false;
	}

	public boolean isPropertyResettable(Object id) {
		if (COLOR_ID.equals(id))
			return true;
		return false;
	}
	
	public void resetPropertyValue(Object id) {
		if (COLOR_ID.equals(id))
			setColor(null);
	}	

	public void setPropertyValue(Object id, Object value) {
		if (COLOR_ID.equals(id))
			setColor(getColor((RGB) value));
	}

}
