package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Cliente;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ControladorCrudClientes extends HttpServlet {

    private ArrayList<Cliente> arrayClientes = new ArrayList<Cliente>(); // Especificar tipo

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String opcion = request.getParameter("opcion");
        int rut = 0, telefono = 0;

        try {
            if (opcion.equals("Guardar")) {
                String rutA = request.getParameter("rut");
                char dv = request.getParameter("dv").charAt(0);
                String pNombre = request.getParameter("pNombre");
                String sNombre = request.getParameter("sNombre");
                String apPaterno = request.getParameter("apPaterno");
                String apMaterno = request.getParameter("apMaterno");
                String direccion = request.getParameter("direccion");
                String comuna = "Santiago"; // Default value
                String email = request.getParameter("email");
                String telefono1 = request.getParameter("telefono");
                
                String[] miselect = request.getParameterValues("comuna");
                if (miselect != null && miselect.length > 0) {
                    comuna = miselect[0]; // Usar el primer valor si hay varios
                }

                try {
                    rut = Integer.parseInt(rutA);
                    telefono = Integer.parseInt(telefono1);
                } catch (NumberFormatException e) {
                    out.println("<h1>Rut o teléfono no son válidos...</h1>");
                    return; // salir del método si hay un error
                }

                if (rut > 0 && !pNombre.isEmpty() && !sNombre.isEmpty() && 
                    !apPaterno.isEmpty() && !apMaterno.isEmpty() && 
                    !direccion.isEmpty() && !comuna.isEmpty() && 
                    telefono >= 0 && !email.isEmpty()) {

                    LocalDateTime ahora = LocalDateTime.now();
                    String hoy = String.format("%04d-%02d-%02d", ahora.getYear(), ahora.getMonthValue(), ahora.getDayOfMonth());
                    
                    Cliente nuevoCliente = new Cliente(hoy, rut, dv, pNombre, sNombre, apPaterno, apMaterno, direccion, comuna, telefono, email);
                    int estado = new ClienteDAO().agregarDatosPersona(nuevoCliente);
                    
                    if (estado > 0) {
                        out.println("<h1>Cliente agregado...</h1>");
                    } else {
                        out.println("<h1>Cliente NO agregado...</h1>");
                    }
                } else {
                    out.println("<h1>Ingrese todos los datos obligatorios...</h1>");
                }
            }

            // Similar manejo para las otras opciones (Buscar, Modificar, Eliminar)

        } catch (Exception ex) {
            response.sendRedirect("errorPagina.jsp");
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
