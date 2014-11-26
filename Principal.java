   package data;
  
   import java.sql.*;
   import java.util.*;
   
  
   public class Principal {
   
      public static final String serv_addr = "http://www.metropolis.dimper.org/";
   
      public static final String url = "jdbc:mysql://localhost:3306/metropolis";
      public static final String def_user = "0";
      public static final String def_pass = "invitado";
   	
      public static final String paypal_url_1 = "https://api-3t.sandbox.paypal.com/nvp";
      public static final String paypal_url_2 = "https://www.sandbox.paypal.com/cgi-bin/webscr";
   	
      public static final String paypal_user = "metro_1336588272_biz_api1.lopezsamaniego.es";
      public static final String paypal_password = "1336588301";
      public static final String paypal_signature = "AlhMGG-eKgqNjmMXpZa9t3SsE0RoA6pocVyR90hz32sOsebh2e-TLbNr";
      public static final String paypal_version = "88.0";
      
      private int id; 
      
   		
      public Pelicula[] busquedaPeli(String query, int num){
      
         Connection conn = null;
         Pelicula[] result = null;
      	
         try {
         
            conn = DriverManager.getConnection(url, def_user, def_pass);
         
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            result = new Pelicula[num];
            
            int count=0;
            
            while (rs.next()) {
            
               int id = rs.getInt("id");
               String nombre = rs.getString("titulo");
               String imagen = rs.getString("imagen");
               String descripcion = rs.getString("descripcion");
               Timestamp date = rs.getTimestamp("anyo");
               int valoracionmedia = rs.getInt("valoracionmedia");
            
               int ano=0;
               try{
                  Calendar calendar = Calendar.getInstance();  
                  calendar.setTime(date);  
                  ano = calendar.get(Calendar.YEAR);
               }
                  catch (NullPointerException npe){}
            
               String query_generos = "SELECT genero FROM Generos INNER JOIN PeliculasGeneros ON PeliculasGeneros.gen = Generos.id INNER JOIN Peliculas ON Peliculas.id = PeliculasGeneros.peli WHERE Peliculas.id="+id;
               Statement statement_generos = conn.createStatement();
               ResultSet rsg = statement_generos.executeQuery(query_generos);
            
               ArrayList<String> generos = new ArrayList<String>();
               while (rsg.next()) 
                  generos.add(rsg.getString("genero"));
               
               String[] genero = generos.toArray(new String[0]);
               
            	
               String query_actores = "SELECT actor FROM Actores INNER JOIN PeliculasActores ON PeliculasActores.act = Actores.id INNER JOIN Peliculas ON Peliculas.id = PeliculasActores.peli WHERE Peliculas.id="+id;
               Statement statement_actores = conn.createStatement();
               ResultSet rsa = statement_actores.executeQuery(query_actores);
            
               ArrayList<String> actores = new ArrayList<String>();
               while (rsa.next())
                  actores.add(rsa.getString("actor"));
               
               String[] actor = actores.toArray(new String[0]);
              
              
               String query_director = "SELECT director FROM Directores INNER JOIN PeliculasDirectores ON PeliculasDirectores.dire = Directores.id INNER JOIN Peliculas ON Peliculas.id = PeliculasDirectores.peli WHERE Peliculas.id="+id;
               Statement statement_director = conn.createStatement();
               ResultSet rsd = statement_director.executeQuery(query_director);
            
               String director = null;
               if (rsd.next()) 
                  director = rsd.getString("director");
              
            
               result[count] = new Pelicula(id, nombre, imagen, descripcion, ano, genero, valoracionmedia, director, actor);
               count++;
            }
         
         
            if (count < num){
            
               Pelicula[] result2 = new Pelicula[count];
            
               for (int i=0; i<count; i++)
                  result2[i] = result[i];
            
               return result2;
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
      
      public Dato[] busquedaDato(String query, int num){
          
         Connection conn = null;
         Dato[] result = null;
         ResultSet rs = null;
       	
         try {
         
            conn = DriverManager.getConnection(url, def_user, def_pass);
          
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(query);
          
            result = new Dato[num];
          
            int count=0;
            while (rs.next()) {
             
               int id = rs.getInt("dnid");
               int peli = rs.getInt("peli");
               java.util.Date reserva = null;
               java.util.Date devolucion = null;
               reserva = rs.getDate("reserva");
               devolucion = rs.getDate("devolucion");
                
               result[count] = new Dato(id, peli, reserva, devolucion);
               count++;
            }
            
         	
            if (count < num){
            
               Dato[] result2 = new Dato[count];
            
               for (int i=0; i<count; i++)
                  result2[i] = result[i];
            
               return result2;
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
      
      private String[] busquedaString(String query){
      
         Connection conn = null;
         String[] result = null;
      	
         try {
            
            conn = DriverManager.getConnection(url, def_user, def_pass);
         
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
         
         
            ArrayList<String> generos = new ArrayList<String>();
         
            while (rs.next()) 
               generos.add(rs.getString("genero"));
               
            result = generos.toArray(new String[0]);
            
            conn.close();
         
         } 
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            }
      
         return result;
      
      }
      
      public int getNewId(){
      
         Connection conn = null;
         String id = new String(); 
      	
         try {
            
            conn = DriverManager.getConnection(url, def_user, def_pass);
         
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SHOW TABLE STATUS LIKE 'Peliculas'");
         
            if (rs.next()) 
               id = rs.getString("Auto_increment");
            
            conn.close();
         
         } 
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            }
      
         return Integer.parseInt(id);
      
      }
      
      public int busquedaId(String query){
      
         Connection conn = null;
         String id = new String();
         int res = 0; 
      	
         try {
            
            conn = DriverManager.getConnection(url, def_user, def_pass);
         
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
         
            if (rs.next()){
               id = rs.getString("id");
               res = Integer.parseInt(id);
            }
            
            conn.close();
         
         } 
            catch (SQLException ex) {
               System.err.println("SQLException: " + ex.getMessage());
               System.err.println("SQLState: " + ex.getSQLState());
               System.err.println("VendorError: " + ex.getErrorCode());
            }
      
         return res;
      
      }
      
      public Comentarios[] busquedaComentarios(String query){
          
         Connection conn = null;
         Comentarios[] result = null;
         ResultSet rs = null;
         int num=10;
        	
         try {
          
            conn = DriverManager.getConnection(url, def_user, def_pass);
           
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(query);
           
            result = new Comentarios[num];
           
            int count=0;
            while (rs.next()) {
              
               int rating = rs.getInt("rating");
               String comentario = rs.getString("comentario");
                 
               result[count] = new Comentarios(rating, comentario);
               count++;
            }
             
          	
            if (count < num){
             
               Comentarios[] result2 = new Comentarios[count];
             
               for (int i=0; i<count; i++)
                  result2[i] = result[i];
             
               return result2;
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
      
      public Pelicula[] getUltimosLanzamientos(int num){
      
      //System.out.println("Servidor web corriendo en "+ serv_addr);
      
         return busquedaPeli("SELECT * FROM Peliculas ORDER BY anyo DESC LIMIT " + num, num);
      
      }
   	
      public Pelicula[] getMasPopulares(int num){
      
         return busquedaPeli("SELECT * FROM Peliculas ORDER BY valoracionmedia DESC LIMIT " + num, num);
      
      }
      
      public Dato[] getHistorial(int num, String user){
          
         return busquedaDato("SELECT * FROM UsuariosPeliculas WHERE dnid=" + user + " ORDER BY reserva DESC LIMIT " + num, num);
       
      }
   	
      public String[] getGeneros(){
      
         return busquedaString("SELECT genero FROM generos");
      
      }
      
      public Pelicula[] getPeliculasGenero(int num, String genero){
      
         return busquedaPeli ("SELECT * FROM Peliculas INNER JOIN PeliculasGeneros ON Peliculas.id=PeliculasGeneros.peli INNER JOIN Generos ON Generos.id=PeliculasGeneros.gen WHERE genero='"+genero+"' LIMIT "+num, num);
      
      }
      
      public Pelicula[] getBusquedaPeliculas(int num, String busq){
      
         return busquedaPeli ("SELECT * FROM Peliculas WHERE titulo LIKE '%"+ busq +"%' LIMIT "+num, num);
      
      }
      
      public Comentarios[] getComent(String peli){
          
         return busquedaComentarios("SELECT * FROM PeliculasValoracion WHERE peli=" + peli);
        
      }
      
      public boolean anadirPeliculaACatalogo(Pelicula peli) throws java.text.ParseException{
      
      
         Pelicula[] peliculaEncontrada = busquedaPeli("SELECT * FROM Peliculas WHERE titulo LIKE '"+ peli.getNombre() +"'", 1);
         
      	
         if(peliculaEncontrada.length == 0){
         
            id = getNewId();
            
            String date = peli.getDate();
            if(date == null)
               date = "NULL";
            else date = "'"+date+"'";
            
            String titulo = peli.getNombre();
         	
            String desc = peli.getDescripcion();
            if(desc == null)
               desc = "NULL";
            else{
               if(desc.length()>601)
                  desc=desc.substring(0, 595)+"[...]"; 
               desc = "'"+desc+"'";
            }
            
            String imagen = new String();
            if (Accion.downloadPhoto(peli.getImagen(), id)){
               imagen = id + ".jpg";
               imagen = "'"+imagen+"'";
            }
            else
               imagen = "NULL";
         	
         
            Accion.ejecutaQuery("INSERT INTO Peliculas VALUES (NULL, '"+titulo+"', "+date+", "+desc+", "+imagen+", 0)");
            
            anadirGeneros(peli, id);
            
            anadirActores(peli, id);
         	
            if(peli.getDirector() != null)
               anadirDirector(peli.getDirector(), id);
         
         }
         else{
         
            id = peliculaEncontrada[0].getId();
            return false;
         }
         
         return true;
                  
      }
      
      private void anadirGeneros (Pelicula peli, int id){
      
         int num_generos = peli.getNumGeneros();
         
         for(int i=0; i<num_generos; i++){
         
            int gener = busquedaId("SELECT * FROM Generos WHERE genero LIKE '"+ peli.getGenero(i) +"'");
         	
            if(gener == 0){ // No existe
               Accion.ejecutaQuery("INSERT INTO Generos VALUES (NULL, '"+ peli.getGenero(i) +"')");
               gener = busquedaId("SELECT * FROM Generos WHERE genero LIKE '"+ peli.getGenero(i) +"'");
            }
            
            Accion.ejecutaQuery("INSERT INTO PeliculasGeneros VALUES ("+ id +", "+ gener +")");
         
         }  
      
      }
      
      private void anadirDirector (String director, int id){
      
      
         int dir = busquedaId("SELECT * FROM Directores WHERE director LIKE '"+ director +"'");
         	
         if(dir == 0){ // No existe
            Accion.ejecutaQuery("INSERT INTO Directores VALUES (NULL, '"+ director +"')");
            dir = busquedaId("SELECT * FROM Directores WHERE director LIKE '"+ director +"'");
         }
            
         Accion.ejecutaQuery("INSERT INTO PeliculasDirectores VALUES ("+ id +", "+ dir +")"); 
      
      }
      
      private void anadirActores (Pelicula peli, int id){
      
         int num_actores = peli.getNumActores();
         
         for(int i=0; i<num_actores; i++){
         
            int act = busquedaId("SELECT * FROM Actores WHERE actor LIKE '"+ peli.getActor(i) +"'");
         	
            if(act == 0){ // No existe
               Accion.ejecutaQuery("INSERT INTO Actores VALUES (NULL, '"+ peli.getActor(i) +"')");
               act = busquedaId("SELECT * FROM Actores WHERE actor LIKE '"+ peli.getActor(i) +"'");
            }
            
            Accion.ejecutaQuery("INSERT INTO PeliculasActores VALUES ("+ id +", "+ act +")");
         
         }  
      
      }
      
      public int getId(){
      
         return id;
      
      }
   
   }
