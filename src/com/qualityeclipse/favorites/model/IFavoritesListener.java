package com.qualityeclipse.favorites.model;

/**
 * Listener interface for broadcasting Favorites item property changes such as color.
 */
public interface IFavoritesListener
{
   void favoritesItemChanged(IFavoriteItem item);
}
