package com.servlets;

import javax.script.ScriptException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

import static com.servlets.PackageLocalMethods.*;

@Path("/get")
public class GetRequests {
    @GET
    @Path("/all")
    public Response showAll(){
        StringBuilder stream = new StringBuilder(500);//String which will store all files
        File directory = new File(txtPath);//String which get the path to the directory "txtPath"
        for (File e : directory.listFiles()) { //Iterative all files in the folder
            stream.append(e.getName()+"\n");//String "directory" append each file in the directory
        }
        return Response.ok(stream.toString()).build();//return string "directory"
    }

    @GET
    @Path("/all/file={fileName}")
    public Response showFile(@PathParam("fileName") String myFileName){
        File file = new File(txtPath+"\\"+myFileName);
        String stringBuf;
        try {
            stringBuf = readFile(file);
        }
        catch (IOException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }
        return Response.ok(stringBuf.toString()).build();
    }


    @GET
    @Path("/file={fileName}")
    public Response showScriptEngine(@PathParam("fileName") String myFileName) {
        File file = new File(txtPath+"\\"+myFileName);
        String stringBuf;
        try {
            stringBuf = readFile(file);
        }
        catch (IOException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }



        String result;
        try {
            result = runJSStream(stringBuf.toString());
        }
        catch(ScriptException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }
        return Response.ok(result.toString()).build();
    }


    @GET
    @Path("/file={fileName}/func={function}")
    public Response showJSFunction(@PathParam("fileName") String myFileName,
                                   @PathParam("function") String myFunction,
                                   @QueryParam("agr")final List<String> list) throws InputMismatchException {
        Object[]arg = new Object[list.size()];
        for(int i=0; i<arg.length; i++){
            String temp = list.get(i);
            if(list.get(i).charAt(0)=='\"'){
                arg[i] = list.get(i);
            }
            else if(Character.isDigit(list.get(i).charAt(0))==true){
                if (list.get(i).contains(".")) {
                    arg[i] = Double.valueOf(list.get(i));
                }
                else {
                    arg[i] = Integer.valueOf(list.get(i));
                }
            }
            else if(list.get(i).charAt(0)=='f' || list.get(i).charAt(0)=='t'){
                arg[i] = Boolean.valueOf(list.get(i));
            }
            else{
                throw new InputMismatchException("not valid input data");
            }
        }


        File file = new File(txtPath+"\\"+myFileName);
        String stringBuf;
        try {
            stringBuf = readFile(file);
        }
        catch (IOException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }



        String result;
        try {

            result = runInvokeFunctionJS(stringBuf,myFunction,arg);
        }
        catch(ScriptException | NoSuchMethodException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }
        return Response.ok(result).build();

    }

}
