package com.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Random;
import javax.script.*;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

@Path("/root") //Name root specifies path to the class
public class RestExecute {

    //Path must refer on the src\main\webapp\WEB-INF\classes
    private String txtPath = "C:\\rest12-hum\\src\\main\\webapp\\WEB-INF\\classes"; //Create the absolute path

    @GET
    @Path("/all")
    public Response All(){
        StringBuilder stream = new StringBuilder(500);//String which will store all files
        File directory = new File(txtPath);//get the path to the directory "txtPath"
        for (File e : directory.listFiles()) { //Iterative all files in the folder
            stream.append(e.getName()+"\n");//append each file in the directory
        }
        return Response.ok(stream.toString()).build();
    }
    @GET
    @Path("/file={fileName}")
    public Response getJS(@PathParam("fileName") String myFileName) {
        String filePath = myFileName+".txt";// Create full file name, for example "testFile1.txt";

        InputStream inputStream = RestExecute.class.getClassLoader().getResourceAsStream(filePath);

        StringBuffer stringBuf=new StringBuffer("");
        StringBuilder stream = new StringBuilder(500);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String strHelp;
            while((strHelp=br.readLine())!=null){
                stringBuf.append(strHelp+" ");
            }
        }
        catch (FileNotFoundException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }
        catch (IOException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }
        ScriptEngineManager factory = new ScriptEngineManager();// create a Nashorn script engine
        ScriptEngine engine = factory.getEngineByName("nashorn");// evaluate JavaScript statement
        //String sStringBuf = new String(stringBuf);

        StringWriter strwrit = new StringWriter();
        PrintWriter outPut = new PrintWriter(strwrit);
        engine.getContext().setWriter(outPut);

        try {
            engine.eval(stringBuf.toString());//
        }
        catch (ScriptException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }
        String result = strwrit.getBuffer().toString();
        stream.append(result);
        return Response.ok(stream.toString()).build();

    }


    @POST
    @Path("/addFile1")
    //JavaCode isn't get normal string
    public Response getBody(@Context HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        Scanner s = new Scanner(request.getInputStream(), "UTF-8");
        while (s.hasNext()) {
            buffer.append(s.nextLine());
        }
        String nameFile = (new java.util.Date ().toString ()).replaceAll(" ", "").replaceAll(":", "_").replaceAll("Sat", "");
        File file = new File(txtPath+"\\"+nameFile+".txt");
        try {
            FileWriter fr = new FileWriter(file);
            fr.write(buffer.toString());
            fr.flush();
            fr.close();
        }
        catch (IOException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }
        return Response.ok(nameFile).build();
    }


    @POST
    @Path("/addFile")
    @Produces(MediaType.APPLICATION_JSON)
    //"javaCode": "Your JavaScript text"
    //JavaCode isn't get normal string
    public Response someMethod(String javaCode) {
        System.out.println(javaCode);
        String nameFile = (new java.util.Date ().toString ()).replaceAll(" ", "").replaceAll(":", "_").replaceAll("Sat", "");
        File file = new File(txtPath+"\\"+nameFile+".txt");
        try {
            FileWriter fr = new FileWriter(file);
            fr.write(javaCode);
            fr.flush();
            fr.close();
        }
        catch (IOException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }
        return Response.ok(nameFile).build();
    }
    @DELETE
    @Path("/deleteFile")
    @Produces(MediaType.APPLICATION_JSON)
    //JavaCode isn't get normal txtPath
    public Response delete(String fileName) {
        File directory = new File(txtPath);
        for (File e : directory.listFiles()) {
            if(fileName.equals(e.getName())==true){
                e.delete();
                return Response.ok("file has been deleted").build();
            }
        }
        return Response.ok("file has not been deleted").build();
    }
}
