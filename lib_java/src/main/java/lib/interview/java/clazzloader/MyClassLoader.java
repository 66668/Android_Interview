package lib.interview.java.clazzloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author:sjy
 * @date: 2021/1/4
 * Description：自定义ClassLoader,绕过双亲委派机制
 */
public class MyClassLoader extends ClassLoader{
    private final static String DEFAULT_DIR = "/Users/sjy/a_my_repository/Android_Interview/lib_java/src/main/java/lib/interview/java/clazzloader";//mac个人路径
    private String dir = DEFAULT_DIR;
    private String classLoaderName;

    public MyClassLoader() {
        super();
    }

    public MyClassLoader(String classLoaderName) {
        super();
        this.classLoaderName = classLoaderName;
    }
    public MyClassLoader(ClassLoader parent, String dir, String classLoaderName) {
        super(parent);
        this.dir = dir;
        this.classLoaderName = classLoaderName;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;
        if(name.startsWith("java.")){
            //没有走这里
            System.out.println("loadClass startsWith java.*");
            try {
                ClassLoader sys = ClassLoader.getSystemClassLoader();
                clazz =sys.loadClass(name);
                if(clazz!=null){
                    if(resolve){
                        resolveClass(clazz);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            clazz = findClass(name);
        } catch (Exception e) {

        }
        if(clazz==null && getParent()!=null){
            //结果测试走这里了
            clazz= getParent().loadClass(name);
        }
        System.out.println("loadClass is null:"+(clazz==null));
        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        String classPath = name.replaceAll("\\.", "/");
        File classFile = new File(dir, (classPath + ".class"));

        if(!classFile.exists()) {
            throw new ClassNotFoundException("The class " + name + " not found under " + dir);
        }
        byte[] classBytes = loadClassBytes(classFile);
        if(null == classBytes || classBytes.length == 0) {
            throw new ClassNotFoundException("load the class " + name + " failed");
        }

        return this.defineClass(name, classBytes, 0, classBytes.length);
    }

    private byte[] loadClassBytes(File classFile) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(classFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getClassLoaderName() {
        return classLoaderName;
    }

}