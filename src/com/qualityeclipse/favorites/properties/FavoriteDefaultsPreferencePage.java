package com.qualityeclipse.favorites.properties;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.qualityeclipse.favorites.model.BasicFavoriteItem;

public class FavoriteDefaultsPreferencePage extends FavoriteItemPropertyPage
      implements IWorkbenchPreferencePage
{
   public void init(IWorkbench workbench) {
   }

   protected RGB getColorPropertyValue() {
      return BasicFavoriteItem.getDefaultColor().getRGB();
   }

   protected void setColorPropertyValue(RGB rgb) {
      BasicFavoriteItem.setDefaultColor(BasicFavoriteItem.getColor(rgb));
   }

   protected String getCommentPropertyValue() {
      return BasicFavoriteItem.getDefaultComment();
   }

   protected void setCommentPropertyValue(String comment) {
      BasicFavoriteItem.setDefaultComment(comment);
   }
}
