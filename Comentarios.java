   package data;

   public class Comentarios {
   
      private int rating;
      private String comentario;
   
   
      public Comentarios(int rating, String comentario){
      
         this.rating = rating;
         this.comentario = comentario;
      }
   
      public int getRating() {
      
         return rating;   
      }
   
      public void setRating(int rating) {
      
         this.rating = rating;
      }
   
      public String getComentario() {
      
         return comentario;
      }
   
      public void setComentario(String comentario) {
      
         this.comentario = comentario;
      }
   
   }
