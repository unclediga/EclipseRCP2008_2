package com.qualityeclipse.favorites.model;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Color;

public interface IFavoriteItem
   extends IAdaptable
{
   String getName();
   void setName(String newName);
   String getLocation();
   boolean isFavoriteFor(Object obj);
   FavoriteItemType getType();
   String getInfo();
   Color getColor();
   void setColor(Color color);

   static IFavoriteItem[] NONE = new IFavoriteItem[] {};
   
}