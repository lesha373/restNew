package com.servlets;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;

/**
 * Created by Алексей on 05/09/2016.
 */
public class PackageLocalMethods {
    private PackageLocalMethods(){}
    //Path must refer on the src\main\webapp\WEB-INF\classes, this way works only on OS Windows!!!!
    static String txtPath = "C:\\rest12-hum\\src\\main\\webapp\\WEB-INF\\classes";
    static String runJSStream(String rawCode) throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();// create a Nashorn script engine
        ScriptEngine engine = factory.getEngineByName("nashorn");// evaluate JavaScript statement

        StringWriter strwrit = new StringWriter();
        PrintWriter outPut = new PrintWriter(strwrit);
        engine.getContext().setWriter(outPut);
        engine.eval(rawCode);//
        String result = strwrit.getBuffer().toString();
        return result;
    }

    static String runInvokeFunctionJS(String rawCode, String fun, Object... arg) throws ScriptException, NoSuchMethodException{
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");

        engine.eval(rawCode);
        Invocable jsInvoke = (Invocable) engine;
        Object result = jsInvoke.invokeFunction(fun, arg);
        System.out.println(result.toString());
        return result.toString();
    }

    static String readFile(File file) throws IOException {
        StringBuffer stringBuf=new StringBuffer(500);
        FileReader fr = new FileReader(file);
        BufferedReader reader = new BufferedReader(fr);
        for(String str="";(str = reader.readLine())!=null;){
            stringBuf.append(str+"\n");
        }
        return stringBuf.toString();
    }
}
