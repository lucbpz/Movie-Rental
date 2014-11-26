package data;

import java.util.*; 
import java.io.*;
import java.net.*;


public class Paypal{

   private String error;

   public String paso1(String saldo) throws Exception{
   	
      String str = new String();
      str = str + "&USER=" + URLEncoder.encode(Principal.paypal_user, "UTF-8");
      str = str + "&PWD=" + URLEncoder.encode(Principal.paypal_password, "UTF-8");
      str = str + "&SIGNATURE=" + URLEncoder.encode(Principal.paypal_signature, "UTF-8");
      str = str + "&VERSION=" + URLEncoder.encode(Principal.paypal_version, "UTF-8");
      str = str + "&PAYMENTREQUEST_0_PAYMENTACTION=" + URLEncoder.encode("Sale", "UTF-8");
      str = str + "&PAYMENTREQUEST_0_AMT=" + URLEncoder.encode(saldo, "UTF-8");
      str = str + "&PAYMENTREQUEST_0_CURRENCYCODE=" + URLEncoder.encode("EUR", "UTF-8");
      str = str + "&RETURNURL=" + URLEncoder.encode(Principal.serv_addr + "ppreturn.jsp", "UTF-8");
      str = str + "&CANCELURL=" + URLEncoder.encode(Principal.serv_addr + "ppcancel.jsp", "UTF-8");
      str = str + "&BRANDNAME=" + URLEncoder.encode("Metrópolis", "UTF-8");
      str = str + "&L_PAYMENTREQUEST_0_ITEMAMT=" + URLEncoder.encode(saldo, "UTF-8");
      str = str + "&L_PAYMENTREQUEST_0_ITEMCATEGORY0=" + URLEncoder.encode("Digital", "UTF-8");
      str = str + "&L_PAYMENTREQUEST_0_NAME0=" + URLEncoder.encode("Recarga de " + saldo + " euros del videoclub", "UTF-8");
      str = str + "&L_PAYMENTREQUEST_0_AMT0=" + URLEncoder.encode(saldo, "UTF-8");
      str = str + "&L_PAYMENTREQUEST_0_QTY0=" + URLEncoder.encode("1", "UTF-8");
      str = str + "&METHOD=" + URLEncoder.encode("SetExpressCheckout", "UTF-8");
      
   /*	<input type="hidden" name="USER" value="<%=Principal.paypal_user %>" /> 
   <input type="hidden" name="PWD" value="<%=Principal.paypal_password %>" /> 
   <input type="hidden" name="SIGNATURE" value="<%=Principal.paypal_signature %>" /> 
   <input type="hidden" name="VERSION" value="<%=Principal.paypal_version %>" /> 
   <input type="hidden" name="PAYMENTREQUEST_0_PAYMENTACTION" value="Sale" /> 
   <input type="hidden" name="PAYMENTREQUEST_0_AMT" value="10" /> 
   <input type="hidden" name="PAYMENTREQUEST_0_CURRENCYCODE" value="EUR" /> 
   <input type="hidden" name="RETURNURL" value="http://localhost/ppreturn.jsp" /> 
   <input type="hidden" name="CANCELURL" value="http://localhost/ppcancel.jsp" /> 
   <input type="hidden" name="BRANDNAME" value="Metropolis" />
   <input type="hidden" name="L_PAYMENTREQUEST_0_ITEMAMT" value="10" />
   <input type="hidden" name="L_PAYMENTREQUEST_0_ITEMCATEGORY0" value="Digital" />
   <input type="hidden" name="L_PAYMENTREQUEST_0_NAME0" value="Recarga de 10 euros del videoclub" />
   <input type="hidden" name="L_PAYMENTREQUEST_0_AMT0" value="10" />
   <input type="hidden" name="L_PAYMENTREQUEST_0_QTY0" value="1" />
   <input type="submit" name="METHOD" value="SetExpressCheckout" /> */
   
   
      URL u = new URL(Principal.paypal_url_1);
      URLConnection uc = u.openConnection();
      uc.setDoOutput(true);
      uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
      PrintWriter pw = new PrintWriter(uc.getOutputStream());
      pw.println(str);
      pw.close();
   
      BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
      String res = in.readLine();
      in.close();
   
   	
      String[] param = getParameters(res);
            
      String ack = getParamByName(param, "ACK");
      String token = getParamByName(param, "TOKEN");
      
      
      if(ack.equals("Failure"))
         return null;
   	
      if(ack.equals("Success"))
         return token;
   	
      throw new Exception("Invalid ACK");
   	  
   }
	
   public int paso2(String token) throws Exception{
   
      String str = new String();
   
      str = str + "&USER=" + URLEncoder.encode(Principal.paypal_user, "UTF-8");
      str = str + "&PWD=" + URLEncoder.encode(Principal.paypal_password, "UTF-8");
      str = str + "&SIGNATURE=" + URLEncoder.encode(Principal.paypal_signature, "UTF-8");
      str = str + "&VERSION=" + URLEncoder.encode(Principal.paypal_version, "UTF-8");
      str = str + "&TOKEN=" + URLEncoder.encode(token, "UTF-8");
      str = str + "&METHOD=" + URLEncoder.encode("GetExpressCheckoutDetails", "UTF-8");
   	
   	/*<form method=post action=https://api-3t.sandbox.paypal.com/nvp>
   	<input type=hidden name=USER value=API_username>
   	<input type=hidden name=PWD value=API_password>
   	<input type=hidden name=SIGNATURE value=API_signature>
   	<input type=hidden name=VERSION value=XX.0>
   	<input name=TOKEN value=EC-1NK66318YB717835M>
   	<input type=submit name=METHOD value=GetExpressCheckoutDetails>
   	</form>*/
   
      URL u = new URL(Principal.paypal_url_1);
      URLConnection uc = u.openConnection();
      uc.setDoOutput(true);
      uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
      PrintWriter pw = new PrintWriter(uc.getOutputStream());
      pw.println(str);
      pw.close();
      
      BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
      String res = in.readLine();
      in.close();
      
   
      String[] param = getParameters(res);
            
      String ack = getParamByName(param, "ACK");
      
      if(ack.equals("Failure")){
         error = URLDecoder.decode(res, "UTF-8");
         return 0;
      }
   	
      String recarg, divisa;
      int recarga;
      try{
         recarg = getParamByName(param, "PAYMENTREQUEST_0_AMT");
         divisa = getParamByName(param, "PAYMENTREQUEST_0_CURRENCYCODE");
      
         double double_recarga = Double.parseDouble(recarg);
         recarga = (int)double_recarga;
      }
      catch (Exception e){
         error = e.getMessage();
         return 0;
      }
   
   	
      if(ack.equals("Success") && divisa.equals("EUR"))
         return recarga;
   	
      throw new Exception("Invalid ACK");
   
   }
   
   public int paso3(String token, String PayerID, String saldo) throws Exception{
   
      String str = new String();
   
      str = str + "&USER=" + URLEncoder.encode(Principal.paypal_user, "UTF-8");
      str = str + "&PWD=" + URLEncoder.encode(Principal.paypal_password, "UTF-8");
      str = str + "&SIGNATURE=" + URLEncoder.encode(Principal.paypal_signature, "UTF-8");
      str = str + "&VERSION=" + URLEncoder.encode(Principal.paypal_version, "UTF-8");
      str = str + "&PAYMENTREQUEST_0_PAYMENTACTION=" + URLEncoder.encode("Sale", "UTF-8");
      str = str + "&PAYERID=" + URLEncoder.encode(PayerID, "UTF-8");
      str = str + "&TOKEN=" + URLEncoder.encode(token, "UTF-8");
      str = str + "&PAYMENTREQUEST_0_AMT=" + URLEncoder.encode(saldo, "UTF-8");
      str = str + "&PAYMENTREQUEST_0_CURRENCYCODE=" + URLEncoder.encode("EUR", "UTF-8");
      str = str + "&METHOD=" + URLEncoder.encode("DoExpressCheckoutPayment", "UTF-8");
   	
   	/*<form method=post action=https://api-3t.sandbox.paypal.com/nvp> 
   	<input type=hidden name=USER value=API_username> 
   	<input type=hidden name=PWD value=API_password> 
   	<input type=hidden name=SIGNATURE value=API_signature> 
   	<input type=hidden name=VERSION value=XX.0> 
   	<input type=hidden name=PAYMENTREQUEST_0_PAYMENTACTION value=Authorization> 
   	<input type=hidden name=PAYERID value=7AKUSARZ7SAT8> 
   	<input type=hidden name=TOKEN value= EC%2d1NK66318YB717835M> 
   	<input type=hidden name=PAYMENTREQUEST_0_AMT value= 19.95> 
   	<input type=submit name=METHOD value=DoExpressCheckoutPayment> 
   	</form>*/
   
      URL u = new URL(Principal.paypal_url_1);
      URLConnection uc = u.openConnection();
      uc.setDoOutput(true);
      uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
      PrintWriter pw = new PrintWriter(uc.getOutputStream());
      pw.println(str);
      pw.close();
      
      BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
      String res = in.readLine();
      in.close();
      
   
      String[] param = getParameters(res);
            
      String ack = getParamByName(param, "ACK");
      
      if(ack.equals("Failure")){
         error = URLDecoder.decode(res, "UTF-8");
         return 0;
      }
      
      String last_trid;
      try{
         BufferedReader lee = new BufferedReader(new FileReader("tran.log")); 
         last_trid = lee.readLine();
         lee.close();
      }
      catch(FileNotFoundException fnfe){
         last_trid="";
      }
      
   	
      boolean hack = false;
      String recarg, divisa, trid;
      int recarga;	
      try{
         recarg = getParamByName(param, "PAYMENTINFO_0_AMT");
         divisa = getParamByName(param, "PAYMENTINFO_0_CURRENCYCODE");
         trid = getParamByName(param, "PAYMENTINFO_0_TRANSACTIONID");
         
         if(trid.equals(last_trid))
            hack = true;
         
         BufferedWriter out = new BufferedWriter(new FileWriter("tran.log"));
         out.write(trid);
         out.close();
      
         double double_recarga = Double.parseDouble(recarg);
         recarga = (int)double_recarga;
      }
      catch (Exception e){
         error = e.getMessage();
         return 0;
      }
         
      if(hack)
         return -1;
   
   	
      if(ack.equals("Success") && divisa.equals("EUR"))
         return recarga;
   	
      throw new Exception("Invalid ACK");
   
   }
   
   private String[] getParameters(String res) throws UnsupportedEncodingException{
   
      List<String> list = new ArrayList<String>();
      int pos1 = 0;
      boolean salir = false;
      while(!salir){
      
         int pos2 = res.indexOf("=", pos1);
         String s = res.substring(pos1, pos2);
         list.add(URLDecoder.decode(s, "UTF-8"));
         pos1=pos2+1;
         pos2 = res.indexOf("&", pos1);
         if(pos2==-1){
            s = res.substring(pos1);
            salir=true;
         }
         else
            s = res.substring(pos1, pos2);
         list.add(URLDecoder.decode(s, "UTF-8"));
         pos1=pos2+1;
         
      }
      
      return list.toArray(new String[0]);
         
   }
   
   private String getParamByName(String[] param, String name){
   
      for (int i=0; i<param.length; i=i+2){
      
         if(param[i].equalsIgnoreCase(name))
            return param[i+1];
         
      
      }  
      return null;    
   }
   
   public String getError(){
   
      return error;
   
   }

}
