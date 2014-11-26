   package data;
   
   import java.util.*;
   import java.text.*;
   import java.sql.*;

   public class Dato {
   
      private int id;
      private int peli;
      private java.util.Date reserva;
      private java.util.Date devolucion;
   
      public Dato(int id, int peli, java.util.Date reserva, java.util.Date devolucion){
      
         this.id = id;
         this.peli = peli;
         this.reserva = reserva;
         this.devolucion = devolucion;
         
      }
   
      public int getId() {
      
         return id;
         
      }
   
      public void setId(int id) {
      
         this.id = id;
         
      }
   
      public int getPeli() {
      
         return peli;
         
      }
   
      public void setPeli(int peli) {
      
         this.peli = peli;
         
      }
   
      public String getReserva() {
      
         DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
         String fecha = formatter.format(reserva);
      
         return fecha;
         
      }
   
      public void setReserva(java.util.Date reserva) {
      
         this.reserva = reserva;
         
      }
   
      public String getDevolucion() {
      
         try{
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String fecha = formatter.format(devolucion);
         
            return fecha;
         }
            catch (NullPointerException npe){
               return "-";
            }
      }
   
      public void setDevolucion(java.util.Date devolucion) {
      
         this.devolucion = devolucion;
         
      }
      
      public String getNombrePeli() {
      
         String query = "SELECT titulo FROM Peliculas WHERE id="+peli;
         
         return busquedaPeli(query);
         
      }
      
      public String busquedaPeli(String query){
      
         String result = null;
         Connection conn = null;
      	
         try {
            
            conn = DriverManager.getConnection(Principal.url, Principal.def_user, Principal.def_pass);
         
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
         
            if (rs.next()) 
            
               result = rs.getString("titulo");
               
            conn.close();
         
         } 
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            }
            
         return result;
            
      }
   
   }
