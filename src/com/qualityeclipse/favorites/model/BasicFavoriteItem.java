package com.qualityeclipse.favorites.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.qualityeclipse.favorites.FavoritesActivator;

public abstract class BasicFavoriteItem implements IFavoriteItem {
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

}
