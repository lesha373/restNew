package com.servlets;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.*;

import static com.servlets.PackageLocalMethods.*;

/**
 * Created by Алексей on 05/09/2016.
 */
@Path("/post")
public class PostRequest {
    @POST
    @Path("/addFile")
    public Response someMethod(@Context HttpServletRequest req) {
        StringBuilder stringBuf = new StringBuilder(20);
        try {
            InputStream body = req.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(body));
            for (String str = ""; (str = reader.readLine()) != null; ) {
                stringBuf.append(str + "\n");
            }
        }
        catch (IOException e){
            String str = e.getMessage();
            return Response.serverError().entity(str).build();
        }



        String nameFile = (new java.util.Date ().toString ()).replaceAll(" ", "-").replaceAll(":", "-").replaceAll("Sat", "").replaceAll("LINT", "y");
        File file = new File(txtPath+"\\"+nameFile+".txt");
        try {
            FileWriter fr = new FileWriter(file);
            fr.write(stringBuf.toString());
            fr.flush();
            fr.close();
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
        return Response.ok(result.toString()+"\nname file = nameFile"+nameFile+".txt").build();
    }
}
