   package data;
   
   import java.sql.*;

   public class Usuario {
   
      private int DNI;
      private String nombre;
      private String apellidos;
      private String password;
      private int saldo;
      
   
      public Usuario(String user){
      
         String query = "SELECT * FROM Usuarios WHERE dni=" + user;
      
         Usuario us = busquedaUsuario(query, user);
      
         setUsuario(us);
      
      }
      
      public Usuario(int DNI, String nombre, String apellidos, String password, int saldo){
      
         this.DNI = DNI;
         this.nombre = nombre;
         this.apellidos = apellidos;
         this.password = password;
         this.saldo = saldo;
      
      
      }
      
      private Usuario busquedaUsuario(String query, String user){
      
         Connection conn = null;
         Usuario result = null;
      	
         try {
            
            conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
         
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
         
            if (rs.next()) {
            
               int DNI = rs.getInt("dni");
               String nombre = rs.getString("nombre");
               String apellidos = rs.getString("apellidos");
               String password = rs.getString("password");
               int saldo = rs.getInt("saldo");
            
               result = new Usuario(DNI, nombre, apellidos, password, saldo);
            }
            
            conn.close();
         
         } 
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            }
      
         return result;
      
      }
      
      public void setUsuario(Usuario usuario){
      
         DNI = usuario.getDNI();
         nombre = usuario.getNombre();
         apellidos = usuario.getApellidos();
         password = usuario.getPassword();
         saldo = usuario.getSaldo();
      
      }
   	
      public int getDNI(){
      
         return DNI;
      
      }
      
      public String getNombre(){
      
         return nombre;
      
      }
   	      
      public String getApellidos(){
      
         return apellidos;
      
      }
      
      public String getPassword(){
      
         return password;
      
      }
      
      public int getSaldo(){
      
         return saldo;
      
      }
      
      public void setSaldo(int s, String user){
      
         String query = "UPDATE Usuarios SET saldo="+ s +" WHERE dni=" + user;
         
         Accion.ejecutaQuery(query);  
      	
         saldo = s;
      
      }
      
   }
