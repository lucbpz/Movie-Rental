   package data;
   
   import java.util.*; 
   import java.io.*;
   import java.net.*;
   import java.text.*;
    

   public class Mdb{
   
   
      private String url1 = "http://api.themoviedb.org/3/search/movie";
      private String url2 = "http://api.themoviedb.org/3/movie";
   
      private String api_key = "ae07a7c79c165890c77232c22ceee073";
      
      private String img_route = "http://cf2.imgobject.com/t/p/original";
      private String img_small = "http://cf2.imgobject.com/t/p/w185";
      
   	
      private String[][] results;
   
   
      public int busqueda(String query, int num_res) throws Exception{
      
         if(num_res>20) num_res=20;
      	
         String str = new String();
         str = str + "?api_key=" + URLEncoder.encode(api_key, "UTF-8");
         str = str + "&query=" + URLEncoder.encode(query, "UTF-8");
         str = str + "&language=" + URLEncoder.encode("es", "UTF-8");
      	
      
         URL u = new URL(url1+str);
         URLConnection uc = u.openConnection();
         uc.setRequestProperty("Accept","application/json");
      
      
         BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
         String res = in.readLine();
         in.close();
         
         int num = getTotalResults(res);
         
         if(num_res < num)
            num = num_res;
         
         results = new String[num][4];  
         
         setBusquedaValues(res, results);
         	
         return num;
      	  	  
      }
      
      public Pelicula peli(int id) throws Exception{
      
         String str = new String();
         str = str + "/" + URLEncoder.encode(String.valueOf(id), "UTF-8");
         str = str + "?api_key=" + URLEncoder.encode(api_key, "UTF-8");
         str = str + "&language=" + URLEncoder.encode("es", "UTF-8");
         
         URL u = new URL(url2+str);
         URLConnection uc = u.openConnection();
         uc.setRequestProperty("Accept","application/json");
      
      
         BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
         String res = in.readLine();
         in.close();
         
         String[] generos = setMultipleValues(res, "genres");
         
         String titulo = setIntValue(res, "title");
         
         String descripcion = setIntValue(res, "overview");
         	
         Date fecha = toDate(setIntValue(res, "release_date"));
         
         String caratulas[] = setIntValues(res, "poster_path");
         
         String imagen;
         if(caratulas[caratulas.length-1]==null)
            imagen = null;
         else
            imagen = img_route.concat(caratulas[caratulas.length-1]);
      	
         str = new String();
         str = str + "/" + URLEncoder.encode(String.valueOf(id), "UTF-8") + "/casts";
         str = str + "?api_key=" + URLEncoder.encode(api_key, "UTF-8");
         
         u = new URL(url2+str);
         uc = u.openConnection();
         uc.setRequestProperty("Accept","application/json");
      
      
         in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
         res = in.readLine();
         in.close();
      
         String[] actor = setMultipleValues(res, "cast");
            
         String director = setDirector(res);
      	   
         int ano;
         try{
            Calendar calendar = Calendar.getInstance();  
            calendar.setTime(fecha);  
            ano = calendar.get(Calendar.YEAR);
         }
            catch(NullPointerException npe) {
               ano=0;
            }
      	
         Pelicula peli = new Pelicula(0, titulo, imagen, descripcion, ano, generos, 0, director, actor);
      	
         peli.setDate(fecha);
      
         return peli;
      
      }
      
      private int getTotalResults(String res){
      
         int pos = res.indexOf("total_results");
         
         pos += 15;
      	
         String r = res.substring(pos, res.indexOf("}", pos));
         
         int tr = Integer.parseInt(r);
         
         if(tr>20)
            tr=20;
      
         return tr;
      
      }
      
      private String setDirector(String res){
      
         int pos1 = res.indexOf("crew");
         pos1 =  res.indexOf("[", pos1);
         int pos2 = res.indexOf("]", pos1);
      	
         String st = res.substring(pos1, pos2+1);
            
         List<String> list = new ArrayList<String>();
         
      	
         int first=0;
         while(true){
         
            int pos3 = st.indexOf("{", first);
            if(pos3==-1)
               break;
            int pos4 = st.indexOf("}", pos3);
            
            String secc = st.substring(pos3, pos4+1);
         
            list.add(secc);
            
            first += secc.length();
         
         }
         
         String[] crew = list.toArray(new String[0]);
      	
         
         for(int i=0; i<crew.length; i++){
         
            String ret = setIntValue(crew[i], "name");
         
            if (setIntValue(crew[i], "job").equals("Director"))
               return ret;
         
         }
         
         return null;
      	
      }
      
   	//Actores (cast), Generos (genres)
      private String[] setMultipleValues(String res, String topic){
      
         int pos1 = res.indexOf("\""+topic+"\"");
         pos1 =  res.indexOf("[", pos1);
         int pos2 = res.indexOf("]", pos1);
      	
         String st = res.substring(pos1, pos2+1);
      
         return setIntValues(st, "name");
      
      }
      
      private void setBusquedaValues(String res, String[][] rellenar){
      
         setIntValuesNum(res, rellenar, "id", 0);
         setIntValues(res, rellenar, "title", 1);
         setIntValues(res, rellenar, "release_date", 2);
         setIntValues(res, rellenar, "poster_path", 3);
      
      }
      
   	// Para vote_count, reemplezar , con }
      private void setIntValues(String res, String[][] rellenar, String topic, int j){
      
         int pos = res.indexOf("\""+topic+"\"");
         
         for (int i=0; i<rellenar.length; i++){
         
            pos += topic.length()+4;
            String r = res.substring(pos, res.indexOf("\"", pos));
            if(r.equals("ull,"))
               r=null;
            rellenar[i][j] = r;
            pos = res.indexOf("\""+topic+"\"", pos+2);
         }
         
      }
      
      private String setIntValue(String res, String topic){
      
         int pos = res.indexOf("\""+topic+"\"");
         if(pos==-1)
            return null;
         
         pos += topic.length()+4;
         
         int Final = res.indexOf("\"", pos);
         int falsas_comillas = res.indexOf("\\\"", pos);
         while(falsas_comillas != -1){
            Final = res.indexOf("\"", falsas_comillas+2);
            falsas_comillas = res.indexOf("\\\"", falsas_comillas+2);
         }
         
         String r = res.substring(pos, Final);
         if(r.equals("ull,"))
            return null;
            
         return r.replace("\\", "");
      }
      
      private String[] setIntValues(String res, String topic){
      
         List<String> list = new ArrayList<String>();
      
      
         int pos = res.indexOf("\""+topic+"\"");
         if(pos==-1)
            return null;
         
         boolean salir=false;
         while (!salir){
         
            pos += topic.length()+4;
            
            int Final = res.indexOf("\"", pos);
            int falsas_comillas = res.indexOf("\\\"", pos);
            while(falsas_comillas != -1){
               Final = res.indexOf("\"", falsas_comillas+2);
               falsas_comillas = res.indexOf("\\\"", falsas_comillas+2);
            }
         	
            String r = res.substring(pos, Final);
            if(r.equals("ull,")){
               r=null;
               list.add(r);   
            }
            else
               list.add(r.replace("\\", ""));
            pos = res.indexOf("\""+topic+"\"", pos+2);
            if(pos==-1)
               salir=true;
         }
         
         return list.toArray(new String[0]);
         
      }
      
      private void setIntValuesNum(String res, String[][] rellenar, String topic, int j){
      
         int pos = res.indexOf("\""+topic+"\"");
         
         for (int i=0; i<rellenar.length; i++){
         
            pos += topic.length()+3;
            String r = res.substring(pos, res.indexOf(",", pos));
            if(r.equals("null"))
               r=null;
            rellenar[i][j] = r;
            pos = res.indexOf("\""+topic+"\"", pos+2);
         }
         
      }
      
      private Date toDate(String dateString) throws ParseException{
      
         try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
         }
            catch (NullPointerException npe){
               return null;
            }
      
      }
      
      public String getTitulo(int i){
      
         return results[i][1];
      
      }
      
      public Date getFecha(int i) throws ParseException{
      
         return toDate(results[i][2]);
      
      }
      
      public int getAno(int i) {
      
         try{      
            Calendar calendar = Calendar.getInstance();  
            calendar.setTime(getFecha(i));  
            return calendar.get(Calendar.YEAR);
         }
            catch (Exception npi){
               return 0;
            }
               
      }
      
      public String getImagenGrande(int i){
      
         if(results[i][3] == null)
            return "";
      
         return img_route.concat(results[i][3]);
      
      }
      
      public String getImagenPequena(int i){
      
         if(results[i][3] == null)
            return "";
      
         return img_small.concat(results[i][3]);
      
      }
      
      public int getId(int i){
      
         String id = results[i][0];
      
         return Integer.parseInt(id);
      
      }
   
   }
