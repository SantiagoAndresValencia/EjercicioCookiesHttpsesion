import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Servlet2")
public class Servlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("usuario") == null) {
			response.sendRedirect("index.html");
			return;
		}

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String nombre = "", apellido = "", edad = "", ciudad = "", anio = "";

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				String valor = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
				switch (c.getName()) {
					case "uname":     nombre = valor;   break;
					case "ulastname": apellido = valor; break;
					case "uage":      edad = valor;     break;
					case "ucity":     ciudad = valor;   break;
					case "uyear":     anio = valor;     break;
				}
			}
		}
		
		out.println("<h2>Hola " + nombre + " " + apellido + "</h2>");
		out.println("<p>Edad: " + edad + "</p>");
		out.println("<p>Ciudad: " + ciudad + "</p>");
		out.println("<p>Año de nacimiento: " + anio + "</p>");


		out.println("<form action='CerrarSesion' method='post'>");
		out.println("<input type='submit' value='Cerrar Sesión'>");
		out.println("</form>");

		out.println("<script>");
		out.println("window.addEventListener('pageshow', function(e) {");
		out.println("  if (e.persisted) { location.reload(); }");
		out.println("});");
		out.println("</script>");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}