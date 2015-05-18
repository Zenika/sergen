package com.zenika.sergen.classgenerator;

import com.zenika.sergen.jsonParser.SG_Configuration;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Zenika on 07/04/2015.
 */
public class ResourceGenerator {

    public static Class generate(SG_Configuration sg_configClass) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass(sg_configClass.getResourceName());
        ClassFile ccFile = cc.getClassFile();
        ConstPool constPool = ccFile.getConstPool();

        //Create Fields with Autowired Annotation

        for (Map.Entry<String, String> entry : sg_configClass.getResourceAttributs().entrySet()) {
            SG_Fields_Setting.createField(entry.getValue().toString(), pool, constPool, cc);
        }

        // Create Methode with annotations

        SG_Methods_Setting.createMethod(sg_configClass.getResourceFonctions(), constPool, cc);

        //Class annotation

        SG_ClassAnnotations.addClassAnnotations(sg_configClass, constPool, ccFile);


        return cc.toClass();
    }


    // To get CtClass
    public static CtClass resolveCtClass(String name) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        return pool.get(name);
    }


}
