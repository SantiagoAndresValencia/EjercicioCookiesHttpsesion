import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/Servlet1")
public class Servlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		String n = request.getParameter("userName");
		String ln = request.getParameter("userLastname");
		String age = request.getParameter("userAge");
		String city = request.getParameter("userCity");
		String year = request.getParameter("userYear");

		if (n != null)    n = n.trim().replaceAll("\\s+", " ");
		if (ln != null)   ln = ln.trim().replaceAll("\\s+", " ");
		if (city != null) city = city.trim().replaceAll("\\s+", " ");


		int añoActual = Year.now().getValue();
		String error = null;

		try {
			int edad = Integer.parseInt(age.trim());
			int año = Integer.parseInt(year.trim());

			if (edad < 1 || edad > 110) {
				error = "La edad debe estar entre 1 y 110 años.";
			} else if (año < añoActual - 110 || año > añoActual) {
				error = "El año de nacimiento debe estar entre " + (añoActual - 110) + " y " + añoActual + ".";
			} else {
				int edadCalculada = añoActual - año;

				if (edad != edadCalculada && edad != edadCalculada - 1) {
					error = "La edad no coincide con el año de nacimiento. Si naciste en " + año
							+ ", debes tener " + (edadCalculada - 1) + " o " + edadCalculada + " años.";
				}
			}
		} catch (NumberFormatException | NullPointerException e) {
			error = "La edad y el año deben ser números enteros.";
		}


		String soloLetras = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+( [A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$";

		if (error == null) {
			if (n == null || !n.matches(soloLetras)) {
				error = "El nombre solo puede tener letras, sin números ni caracteres especiales.";
			} else if (ln == null || !ln.matches(soloLetras)) {
				error = "El apellido solo puede tener letras, sin números ni caracteres especiales.";
			} else if (city == null || !city.matches(soloLetras)) {
				error = "La ciudad solo puede tener letras, sin números ni caracteres especiales.";
			}
		}


		if (error != null) {
			out.println("<p style='color:red'>" + error + "</p>");
			out.println("<a href='index.html'>Volver</a>");
			return;
		}


		Cookie ckName     = new Cookie("uname",     URLEncoder.encode(n, StandardCharsets.UTF_8));
		Cookie ckLastname = new Cookie("ulastname", URLEncoder.encode(ln, StandardCharsets.UTF_8));
		Cookie ckAge      = new Cookie("uage",      age.trim());
		Cookie ckCity     = new Cookie("ucity",     URLEncoder.encode(city, StandardCharsets.UTF_8));
		Cookie ckYear     = new Cookie("uyear",     year.trim());

		int duracion = 60 * 60; 
		ckName.setMaxAge(duracion);
		ckLastname.setMaxAge(duracion);
		ckAge.setMaxAge(duracion);
		ckCity.setMaxAge(duracion);
		ckYear.setMaxAge(duracion);

		response.addCookie(ckName);
		response.addCookie(ckLastname);
		response.addCookie(ckAge);
		response.addCookie(ckCity);
		response.addCookie(ckYear);

		HttpSession session = request.getSession();
		session.setAttribute("usuario", n);

		response.sendRedirect("Servlet2");
	}
}