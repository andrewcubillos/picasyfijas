/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.escuelaing.edu.PicasFamas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App {
     static ArrayList<Integer> numero;
     ArrayList<Integer> numerousuario;
     static int picas=0;
     static int famas=0;
     static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8080; 
    


 }
    public static void main(String[] args) {
        picas=0;
        famas=0;
        numero = new ArrayList<>();
        numero.add((int)(Math.random()*(9-0+1)+0));
        numero.add((int)(Math.random()*(9-0+1)+0));
        numero.add((int)(Math.random()*(9-0+1)+0));
        numero.add((int)(Math.random()*(9-0+1)+0));
        SpringApplication app = new SpringApplication(App.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", getPort()));
        app.run(args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "0000") String name) {
        
        String output =  "<DOCTYPE html>\r\n"
                +"<html>\r\n"
                +"<body>\r\n"
                +"<form action=\"/hello\"> \r\n"
                +"<label for=\"fname\">Número:</label><br> \r\n"
                +"<input type=\"text\" id=\"name\" name=\"name\" ><br> \r\n"
                +"<input type=\"submit\" value=\"Submit\"> \r\n"
                +"</form> \r\n" 
                +"</body> \r\n"
                +"</html> \r\n";
         
    String n=inttoString(name);	
    name="0000";
    return output +String.format("%s", n);
    }
    
    private String comparacion(ArrayList<Integer> name){          
            picas=0;
            famas=0;
            int cont=0;
            for(int i = 0; i <= 3; i++){
                    if(numero.get(i)==numerousuario.get(i)){
                        famas++;
                    }
                    else if(contiene(cont)) picas++;
                    cont++;
                    
            }
            
            return"  Picas:  "+ String.valueOf(picas)+"  Famas:  "+String.valueOf(famas);
                
            
    }
    private boolean contiene(int h){
        return (numero.contains(numerousuario.get(h)));
    }
    private  String  inttoString(String name){
            String pf;
            numerousuario = new ArrayList<>();
            for(int i = 0; i <= 3; i++){
                numerousuario.add(Character.getNumericValue(name.charAt(i)));
            
            }
           
            String str = "";
            String str2 = "";
            for (int n : numero) {
			str+= String.valueOf(n)+",";
		}
            for (int m : numerousuario) {
			str2+= String.valueOf(m)+",";
		}
            if(name=="0000") pf= "  Picas:  0  Famas:  0";
            else pf=comparacion(numerousuario);
        return "Tu número: "+str2+pf;
    }
}
