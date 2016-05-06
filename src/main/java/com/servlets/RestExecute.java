package com.servlets;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Random;
import javax.script.*;
import java.util.Date;
import java.util.regex.Pattern;

@Path("/root")
public class RestExecute {

    //must refer on the src\main\webapp\WEB-INF\classes
    private String txtPath = "C:\\rest12-hum\\src\\main\\webapp\\WEB-INF\\classes";

    @GET
    @Path("/all")
    public Response All(){

        StringBuilder stream = new StringBuilder(500);
        File directory = new File(txtPath);
        for (File e : directory.listFiles()) {
            stream.append(e.getName()+"\n");
        }
        return Response.ok(stream.toString()).build();
    }
    @GET
    @Path("/file={fileName}")
    public Response getJS(@PathParam("fileName") String myFileName) {
        String filePath = myFileName+".txt";// "testFile1.txt";
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
        String sStringBuf = new String(stringBuf);

        StringWriter strwrit = new StringWriter();
        PrintWriter outPut = new PrintWriter(strwrit);
        engine.getContext().setWriter(outPut);

        try {
            engine.eval(sStringBuf);
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
    @Path("/addFile")
    @Produces(MediaType.APPLICATION_JSON)
    //"javaCode": "Your JavaScript text"
    //JavaCode isn't get normal string
    public Response someMethod(String javaCode) {
        System.out.println(javaCode);
        String nameFile = (new java.util.Date ().toString ()).replaceAll(" ", "").replaceAll(":", "");
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
        StringBuilder stream = new StringBuilder(20);
        stream.append(nameFile);
        return Response.ok(stream.toString()).build();
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
