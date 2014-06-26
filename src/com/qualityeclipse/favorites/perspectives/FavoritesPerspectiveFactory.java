package com.qualityeclipse.favorites.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class FavoritesPerspectiveFactory implements IPerspectiveFactory {

	private static final String FAVORITES_VIEW_ID = "com.qualityeclipse.favorites.views.FavoritesView";
	private static final String FAVORITES_ACTION_ID = "com.qualityeclipse.favorites.workbenchActionSet";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Get the editor area.
		String editorArea = layout.getEditorArea();
		// Put the Outline view on the left.
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.LEFT, 0.25f,
				editorArea);
		// Put the Favorites view on the bottom with
		// the Tasks view.
		IFolderLayout bottom = layout.createFolder("bottom",
				IPageLayout.BOTTOM, 0.66f, editorArea);
		bottom.addView(FAVORITES_VIEW_ID);
		bottom.addView(IPageLayout.ID_TASK_LIST);
		bottom.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);
		// Add the Favorites action set.
		layout.addActionSet(FAVORITES_ACTION_ID);
		
		// что-то не действует, когда Workbenсh запускаешь
		layout.setFixed(true);
	}

}
