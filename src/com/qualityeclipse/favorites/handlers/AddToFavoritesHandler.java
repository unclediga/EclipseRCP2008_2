package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class AddToFavoritesHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("HELLO FROM HANDLER!!!");
		return null;
	}
}
