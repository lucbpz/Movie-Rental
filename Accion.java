   package data;
   
   import java.sql.*;
   import java.text.*;
   import java.util.*; 
   import java.io.*;
   import java.net.URL;
   import java.net.URLConnection;
   
 
   public class Accion{
   
      public static final int precioAlq = 2;
      public static final int precioMitadAlq = 1;
      
   
      public static String ampliar(String user, String idString){
      
         Usuario usuario = new Usuario(user);
      
         int s = usuario.getSaldo();
      
         if (s >= precioAlq){
         
            usuario.setSaldo(s-precioAlq, user);
         
            int id = Integer.parseInt(idString);
         
            Connection conn = null;
            String query = "SELECT devolucion FROM UsuariosPeliculas WHERE peli="+ id +" AND dnid=" + user;
         
            Timestamp date = null;
         
            try {
            
               conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
            
               Statement statement = conn.createStatement();
               ResultSet rs = statement.executeQuery(query);
            
               if (rs.next())
                  date = rs.getTimestamp("devolucion");
                  
               conn.close();
            
            } 
               catch (SQLException ex) {
                  System.err.println("SQLException: " + ex.getMessage());
                  System.err.println("SQLState: " + ex.getSQLState());
                  System.err.println("VendorError: " + ex.getErrorCode());
               }
         
            long nuevaFecha = date.getTime() + 172800000;
            Timestamp nueva = new Timestamp(nuevaFecha);
         
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = formatter.format(nueva);
         
            query = "UPDATE UsuariosPeliculas SET devolucion=\""+ fecha +"\" WHERE peli=" + id + " AND dnid=" + user;
         
            try {
               conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
            
               Statement statement = conn.createStatement();
               statement.executeUpdate(query);
               
               conn.close();
            
            } 
               catch (SQLException ex) {
                  System.err.println("SQLException: " + ex.getMessage());
                  System.err.println("SQLState: " + ex.getSQLState());
                  System.err.println("VendorError: " + ex.getErrorCode());
               }
         
            return "<script>alert('Alquiler ampliado.')</script>\n<meta http-equiv='refresh' content='0;url=autent.jsp' />";
         
         }
         else{
         
            return "<script>alert('No tiene saldo para ampliar el alquiler.')</script>\n<meta http-equiv='refresh' content='0;url=autent.jsp' />";
         
         }
      
      }
   	
      public static void ejecutaQuery(String query){
        
         Connection conn = null;
      
         try {
            
            conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
            
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
                  
            conn.close();
            
         } 
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            } 
         
      }
      
      public static String registrar(String nombre, String apellidos, String password, String dni){
      
         int idni = Integer.parseInt(dni);
      
         String query = "INSERT INTO Usuarios VALUES ("+idni+", '"+nombre+"', '"+apellidos+"', '"+password+"', 0)";
      
         ejecutaQuery(query);
      
         return "<script>alert('Usuario registrado correctamente.')</script>\n<meta http-equiv='refresh' content='0;url=index.jsp' />";
      
      }
      
      public static String modificar(int dni, String nombre, String apellidos, String password){
      
         String query=null;
         if (!password.equals("")){
            query = "UPDATE Usuarios SET `nombre`='"+nombre+"', `apellidos`='"+apellidos+"', `password`='"+password+"' WHERE `dni`=" + dni;
         }
         else{
            query = "UPDATE Usuarios SET `nombre`='"+nombre+"', `apellidos`='"+apellidos+"' WHERE `dni`=" + dni;
         }
      
         ejecutaQuery(query);
      
         return "<script>alert('Usuario modificado correctamente.')</script>\n<meta http-equiv='refresh' content='0;url=autent.jsp' />";
      
      }
      
      public static String cancelar(String user, String idString){
      
         Usuario usuario = new Usuario(user);
         
         int id = Integer.parseInt(idString);
      
         Connection conn = null;
         String query = "SELECT reserva FROM UsuariosPeliculas WHERE peli="+ id +" AND dnid=" + user;
       
         Timestamp date = null;
      	
         try {
            
            conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
         
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
         
            if (rs.next())
               date = rs.getTimestamp("reserva");
            
            conn.close();
         
         } 
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            }
      
      
         java.util.Date actual = new java.util.Date();
         long resta = actual.getTime() - date.getTime();
      
         boolean devolver = false;
         
         if (resta < 14401000){
            int s = usuario.getSaldo();
            usuario.setSaldo(s+precioMitadAlq, user);
            devolver = true;
         }
      
      
         query = "DELETE FROM UsuariosPeliculas WHERE peli=" +id+ " AND dnid=" + user;
      
         try {
            conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
         
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            
            conn.close();
         
         } 
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            }
         if(devolver)
            return "<script>alert('Reserva anulada. Se le ha devuelto la mitad del alquiler.')</script>\n<meta http-equiv='refresh' content='0;url=autent.jsp' />";
         else
            return "<script>alert('Reserva anulada. No tiene derecho a la devolución del importe.')</script>\n<meta http-equiv='refresh' content='0;url=autent.jsp' />";
      }
      
      public static boolean login(String user, String pass){
      
         Connection conn = null;
      	
         boolean b = false;
         String us = null;
         String pa = null;
         
         try {
            conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
            Statement statement = conn.createStatement();
            ResultSet tabla = statement.executeQuery("select * from usuarios where dni='"+ user +"' ");
         
         
            while(tabla.next()){
            
               us = tabla.getString("dni");
               pa = tabla.getString("password");
               
               if(us.equals(user) && pa.equals(pass))
                  b = true;
               
               else
                  b = false;
            }
            
            conn.close();
         
         }
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            }
         
         return b;  
      
      }
      
      public static String reservar(String user, String idString){
      
         Usuario usuario = new Usuario(user);
      
         int s = usuario.getSaldo();
      
         if (s >= precioAlq){
         
            usuario.setSaldo(s-precioAlq, user);
         
            int id = Integer.parseInt(idString);
         
            java.util.Date actual = new java.util.Date();
         
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = formatter.format(actual);
         
            
         // Comprobando si ya tiene alquilada esta película
            String query = "SELECT * FROM UsuariosPeliculas WHERE dnid="+usuario.getDNI();
            
            Connection conn = null;
            String[] result = null;
         
            boolean alquilada = false;
            try {
            
               conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
            
               Statement statement = conn.createStatement();
               ResultSet rs = statement.executeQuery(query);
            
               while (rs.next()){
                  String string_peli = rs.getString("peli");
                  int peli = Integer.parseInt(string_peli);   
                  if(peli == id)
                     alquilada=true;
               }
            
               conn.close();
            
            } 
               catch (SQLException ex) {
                  System.err.println("SQLException: " + ex.getMessage());
                  System.err.println("SQLState: " + ex.getSQLState());
                  System.err.println("VendorError: " + ex.getErrorCode());
               }
         
         	
            if(alquilada)
               return "<script>alert('Ya tiene alquilada/reservada esa película.')</script>\n<meta http-equiv='refresh' content='0;url=autent.jsp' />";
            
            else
               ejecutaQuery("INSERT INTO UsuariosPeliculas VALUES (" + user + "," + id + ",'" + fecha + "',NULL)");
         
            return "<script>alert('Película reservada.')</script>\n<meta http-equiv='refresh' content='0;url=autent.jsp' />";
         
         }
         else
            return "<script>alert('No tiene saldo para reservar la película.')</script>\n<meta http-equiv='refresh' content='0;url=autent.jsp' />";
      }
      
      public static int anadirSaldo(String user, int saldo){
      
         Usuario usuario = new Usuario(user);
      
         int s = usuario.getSaldo();
      
         usuario.setSaldo(s+saldo, user);
         
         return s+saldo;
                  
      }
     
      
      public static boolean downloadPhoto(String surl, int id){
      /* Ejemplo descargado de http://chuwiki.chuidiang.org */
      
         try{
         
            URL url = new URL(surl);
         
         // establecemos conexion
            URLConnection urlCon = url.openConnection();
         
         // Sacamos por pantalla el tipo de fichero
            if(!urlCon.getContentType().equals("image/jpeg"))
               return false;
         
         // Se obtiene el inputStream de la foto web y se abre el fichero
         // local.
            InputStream is = urlCon.getInputStream();
            FileOutputStream fos = new FileOutputStream("../webapps/ROOT/img/"+id+".jpg");
         
         // Lectura de la foto de la web y escritura en fichero local
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            int leido = is.read(array);
            while (leido > 0) {
               fos.write(array, 0, leido);
               leido = is.read(array);
            }
         
         // cierre de conexion y fichero.
            is.close();
            fos.close();
         
         }
            catch (Exception e){
               return false;
            }
         
         return true;
      
      }
      
   }
