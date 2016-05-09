package com.servlets;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.File;

import static com.servlets.PackageLocalMethods.*;

@Path("/delete")
public class DeleteRequests {
    @DELETE
    @Path("/file={fileName}")
    public Response delete(@PathParam("fileName") String fileName) {
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
