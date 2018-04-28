package com.github.seelikes.android.parser;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

public class AndroidParser {
    /**
     * 获取具有制定注解类型的类
     * @param context 应用上下文
     * @param entityClass 承接具体结果的实体类
     * @param annotationClass 目标注解
     * @param basePackage 扫描的基础包
     * @param <T> 注解的类型
     * @param <C> 期望接受的目标的类型
     * @return 具有制定注解类型的类
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation, C> List<ClassAnnotationEntity<T, C>> getClassWith(Context context, Class<ClassAnnotationEntity<T, C>> entityClass, Class<T> annotationClass, String basePackage) {
        List<ClassAnnotationEntity<T, C>> list = new ArrayList<>();
        getClass(context, basePackage, classObject -> {
            T annotation = classObject.getAnnotation(annotationClass);
            if (annotation != null) {
                try {
                    ClassAnnotationEntity<T, C> entity = entityClass.newInstance();
                    entity.setAnnotation(annotation);
                    entity.setClassEntity((Class<C>) classObject);
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return list;
    }

    /**
     * 获取指定类型的子类型
     * @param context 应用上下文
     * @param superClass 父类型
     * @param basePackage 扫描的基础包
     * @param <T> 父类型
     * @return 指定类型的子类型
     */
    @SuppressWarnings("unchecked")
    public static <T> List<Class<? extends T>> getClassExtends(Context context, Class<T> superClass, String basePackage) {
        List<Class<? extends T>> list = new ArrayList<>();
        getClass(context, basePackage, classObject -> {
            if (superClass.isAssignableFrom(classObject) && classObject != superClass && (classObject.getModifiers() & Modifier.ABSTRACT) == 0) {
                list.add((Class<? extends T>) classObject);
            }
        });
        return list;
    }

    /**
     * 类扫描封装
     * @param context 应用上下文
     * @param basePackage 扫描的基础包
     * @param checker 类检查器
     */
    public static void getClass(@NonNull Context context, String basePackage, ClassChecker checker) {
        try {
            DexFile dex = new DexFile(context.getPackageCodePath());
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()) {
                String className = entries.nextElement();
                if (basePackage == null || basePackage.isEmpty() || className.startsWith(basePackage)) {
                    try {
                        checker.check(context.getClassLoader().loadClass(className));
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 类检查器
     */
    public interface ClassChecker {
        /**
         * 检查给定类是否满足现行判断标准
         * @param classObject 要检查的类型
         */
        void check(Class<?> classObject);
    }

    /**
     * 注解类型实体类
     * @param <T> 注解类型
     * @param <C> 期望的类型，通常我们只需要加注解的某一类型的类
     */
    public interface ClassAnnotationEntity<T extends Annotation, C> {
        void setAnnotation(T annotation);
        void setClassEntity(Class<C> classEntity);
    }
}
