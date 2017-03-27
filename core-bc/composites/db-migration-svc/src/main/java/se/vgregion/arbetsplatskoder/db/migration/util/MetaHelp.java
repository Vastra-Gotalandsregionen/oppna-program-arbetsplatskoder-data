package se.vgregion.arbetsplatskoder.db.migration.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

public class MetaHelp {

  private static Map<Class<?>, BeanInfo> beanInfoCache = new HashMap<Class<?>, BeanInfo>();

  private static Map<BeanInfo, Map<String, PropertyDescriptor>> descriptors = new HashMap<BeanInfo, Map<String, PropertyDescriptor>>();

  public static BeanInfo getBeanInfo(Class<?> clazz) {
    if (clazz == null) throw new IllegalArgumentException("Argument cannot be null.");
    BeanInfo result = beanInfoCache.get(clazz);
    if (result != null) return result;
    try {
      result = Introspector.getBeanInfo(clazz);
    } catch (IntrospectionException e) {
      throw new RuntimeException(e);
    }
    beanInfoCache.put(clazz, result);

    return result;
  }

  public static Map<String, PropertyDescriptor> getDescriptors(BeanInfo key) {
    Map<String, PropertyDescriptor> result = descriptors.get(key);
    if (result == null) {
      result = new HashMap<String, PropertyDescriptor>();
      for (PropertyDescriptor pd : key.getPropertyDescriptors()) {
        result.put(pd.getName(), pd);
      }
    }
    return result;
  }

  public static Field getFieldValueImp(Class<?> clazz, String name) {
    Field result = null;
    try {
      result = clazz.getDeclaredField(name);
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      if (clazz.equals(Object.class)) return null;
      return getFieldValueImp(clazz.getSuperclass(), name);
    }
    return result;
  }

  public static Set<String> getFieldNamesByAnnotations(Class<?> beanClass, Set<Class<? extends Annotation>> lookingFor) {
    Set<String> result = new HashSet<String>();
    for (Field field : beanClass.getDeclaredFields()) {
      for (Class<? extends Annotation> annotation : lookingFor) {
        if (field.isAnnotationPresent(annotation)) {
          result.add(field.getName());
        }
      }
    }
    return result;
  }

  public static Set<String> getFieldNamesByAnnotations(Class<?> beanClass, Class<? extends Annotation>... lookingFor) {
    return getFieldNamesByAnnotations(beanClass, new HashSet<Class<? extends Annotation>>(Arrays.asList(lookingFor)));
  }

  public static List<Class> getGenerics(Class<?> clazz, String key) {
    // should GenericDeclaration be returned instead of Class:es?
    BeanInfo bi = getBeanInfo(clazz);
    Map<String, PropertyDescriptor> descs = getDescriptors(bi);

    Method method = descs.get(key).getReadMethod();

    List<Class> result = new ArrayList<Class>();

    Type returnType = method.getGenericReturnType();

    if (returnType instanceof ParameterizedType) {
      ParameterizedType type = (ParameterizedType) returnType;
      Type[] typeArguments = type.getActualTypeArguments();
      for (Type typeArgument : typeArguments) {
        if (!(typeArgument instanceof Class)) {

        } else {
          Class typeArgClass = (Class) typeArgument;
          result.add(typeArgClass);
        }
      }
    }

    return result;
  }

  public static Object getFirstFieldValue(Object graphRoot, String name) {
    try {
      return getFirstFieldValueImp(graphRoot, name, new IdentityHashMap());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object getFirstFieldValueImp(Object graphRoot, String name, IdentityHashMap passedThings) throws IllegalAccessException {
    if (passedThings.containsKey(graphRoot))
      return null;
    passedThings.put(graphRoot, graphRoot);
    List<Field> allFields = getAllFields(graphRoot.getClass());
    for (Field field : allFields) {
      if (name.equals(field.getName())) {
        field.setAccessible(true);
        return field.get(graphRoot);
      }
    }

    for (Field field : allFields) {
      field.setAccessible(true);
      Object value = field.get(graphRoot);
      if (value == null)
        continue;
      Class<? extends Object> clazz = value.getClass();
      if (passedThings.containsKey(value) || clazz.isPrimitive() || clazz.getName().startsWith("java.lang") || clazz.isArray())
        continue;
      // passedThings.put(value, value);
      Object possibleResult = getFirstFieldValueImp(value, name, passedThings);
      if (possibleResult != null)
        return possibleResult;
    }
    return null;
  }

  public static List<Field> getAllFields(Class<?> type) {
    return getAllFields(new ArrayList<Field>(), type);
  }

  public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
    fields.addAll(Arrays.asList(type.getDeclaredFields()));

    if (type.getSuperclass() != null) {
      fields = getAllFields(fields, type.getSuperclass());
    }

    return fields;
  }

  public static Object getFieldValue(Object from, String byFieldPath) {
    String fields[] = byFieldPath.split(Pattern.quote("/"));
    for (String field : fields) {
      from = getFieldValueImp(from, from.getClass(), field);
      if (from == null) {
        return null;
      }
    }

    return from;
  }

  private static Object getFieldValueImp(Object obj, Class<?> clazz, String fieldName) {
    for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
      try {
        Field field;
        field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
      } catch (Exception e) {
      }
    }

    return null;
  }

  public static String getBeanName(String fromEtterMethodName) {
    // Assume the method starts with either get or is.
    return Introspector.decapitalize(fromEtterMethodName.substring(fromEtterMethodName.startsWith("is") ? 2 : 3));
  }

}
