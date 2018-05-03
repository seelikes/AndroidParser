package com.github.seelikes.android.parser;

import com.github.seelikes.android.dex.Parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 该类只是对 {@link Parser} 类的简封装，推荐直接操作Parser类
 */
public class AndroidParser {
    /**
     * 获取具有制定注解类型的类
     * @param entityClass 承接具体结果的实体类
     * @param annotationClass 目标注解
     * @param <T> 注解的类型
     * @param <C> 期望接受的目标的类型
     * @return 具有制定注解类型的类
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation, C> List<ClassAnnotationEntity<T, C>> getClassWith(Parser parser, Class<ClassAnnotationEntity<T, C>> entityClass, Class<T> annotationClass) {
        List<ClassAnnotationEntity<T, C>> list = new ArrayList<>();
        parser.getClass(classObject -> {
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
     * @param superClass 父类型
     * @param <T> 父类型
     * @return 指定类型的子类型
     */
    @SuppressWarnings("unchecked")
    public static <T> List<Class<? extends T>> getClassExtends(Parser parser, Class<T> superClass) {
        List<Class<? extends T>> list = new ArrayList<>();
        parser.getClass(classObject -> {
            if (classObject == null) {
                return;
            }
            if (superClass.isAssignableFrom(classObject) && classObject != superClass && (classObject.getModifiers() & Modifier.ABSTRACT) == 0) {
                list.add((Class<? extends T>) classObject);
            }
        });
        return list;
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
