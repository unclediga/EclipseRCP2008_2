package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;

public class MyOpenFavoriteViewHandler extends org.eclipse.core.commands.AbstractHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event)
			throws org.eclipse.core.commands.ExecutionException {
		// TODO Auto-generated method stub
		System.out.println("Hello from "+this.getClass().getName());
		return null;
	}

}
