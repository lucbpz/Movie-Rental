   package data;
   
   import java.util.Date;
   import java.text.*;
   
   public class Pelicula {
   
      private int id;
      private String nombre;
      private String imagen;
      private String descripcion;
      private int ano;
      private String[] genero;
      private int valoracion;
      private String director;
      private String[] actor;
      
      private Date date;
      
   
      public Pelicula(int id, String nombre, String imagen, String descripcion, int ano, String[] genero, int valoracionmedia, String director, String actor[]){
      
         this.id = id;
         this.nombre = nombre;
         this.imagen = imagen;
         this.descripcion = descripcion;
         this.ano = ano;
         this.genero = genero;
         valoracion = valoracionmedia;
         this.director = director;
         this.actor = actor;
      
      }
      
      public Pelicula(String id){
      
      
         Principal principal = new Principal();
      
         Pelicula pelicula[] = principal.busquedaPeli("SELECT * FROM peliculas WHERE id="+id, 1);
         
         Pelicula peli = pelicula[0];
         
         setPelicula(peli);
      
      }
      
      public void setPelicula(Pelicula pelicula){
      
      
         this.id = pelicula.id;
         this.nombre = pelicula.nombre;
         this.imagen = pelicula.imagen;
         this.descripcion = pelicula.descripcion;
         this.ano = pelicula.ano;
         this.genero = pelicula.genero;
         this.valoracion = pelicula.valoracion;
         this.director = pelicula.director;
         this.actor = pelicula.actor;
      
      }
      
      public String getNombre(){
      
         return nombre;
      
      }
      
      public int getId(){
      
         return id;
      
      }
   	
      public String getImagen(){
      
         return imagen;
      
      }
      
      public String getDescripcion(){
      
         return descripcion;
      
      }
     
      public int getAno(){
      
         return ano;
      
      }
      
      public String getGenero(int num){
      
         return genero[num];
      
      }
   	
      public int getNumGeneros(){
      
         try{
            return genero.length;
         } 
            catch (NullPointerException npe){
               return 0;	
            }   
      }
      
      public String getDirector(){
      
         return director;
         
      }
      
      public String getActor(int num){
      
         return actor[num];
      
      }
   	
      public int getNumActores(){
      
         try{
            return actor.length;
         } 
            catch (NullPointerException npe){
               return 0;	
            }
         
      }
      
      public void setDate(Date date){
      
         this.date=date;
      
      }
   	
      public String getDate() throws ParseException{
      
         return toString(date);
      
      }
      
      private String toString(Date date) throws ParseException{
      
         try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
            return dateFormat.format(date);
         }
            catch (NullPointerException npe){
               return null;
            }
      
      }
   
   }
