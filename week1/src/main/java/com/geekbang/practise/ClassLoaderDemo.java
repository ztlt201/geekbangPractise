package com.geekbang.practise;

import com.sun.deploy.util.ReflectionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * TODO Class Description
 * @since 2021-11-09
 */
public class ClassLoaderDemo {

    static class HelloClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {

            String classPath = ClassLoaderDemo.class.getClassLoader().getResource("Hello.xlass").getPath();
            File classFile = new File(classPath);
            byte[] bytes = new byte[(int) classFile.length()];
            try {
                new FileInputStream(classFile).read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            IntStream.range(0,bytes.length).forEach(a -> bytes[a] = (byte) (255 - bytes[a]));
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    public static void main(String[] args) throws Exception {
        Class<?> helloClazz = new HelloClassLoader().loadClass("Hello");
        Object hello = helloClazz.newInstance();
        Method helloMethod = helloClazz.getDeclaredMethod("hello");
        helloMethod.invoke(hello);
    }
}