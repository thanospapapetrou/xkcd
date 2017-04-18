package com.github.thanospapapetrou.xkcd.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet redirecting application context root to current comic.
 * 
 * @author thanos
 */
@WebServlet("")
public class RootRedirector extends HttpServlet {
	private static final String CURRENT_COMIC = "comic/current";
	private static final long serialVersionUID = 0L;

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		response.sendRedirect(CURRENT_COMIC);
	}
}
