package se.vgregion.arbetsplatskoder.db.migration.util;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BeanMap implements Map<String, Object> {

    public final static Map<Class<?>, Object> defaultPrimitiveValues = new HashMap<Class<?>, Object>();

    static {
        defaultPrimitiveValues.put(Byte.TYPE, new Byte((byte) 0));
        defaultPrimitiveValues.put(Short.TYPE, new Short((short) 0));
        defaultPrimitiveValues.put(Integer.TYPE, new Integer(0));
        defaultPrimitiveValues.put(Long.TYPE, 0l);
        defaultPrimitiveValues.put(Float.TYPE, 0f);
        defaultPrimitiveValues.put(Double.TYPE, 0d);
        defaultPrimitiveValues.put(Boolean.TYPE, false);
        defaultPrimitiveValues.put(Character.TYPE, new Character((char) 0));
    }

    private Object bean;

    BeanInfo beanInfo;

    Map<String, PropertyDescriptor> properties;

    Set<String> keys;

    public BeanMap(Object bean) {
        this.bean = bean;
        beanInfo = MetaHelp.getBeanInfo(bean.getClass());
        properties = MetaHelp.getDescriptors(beanInfo);
        keys = Collections.unmodifiableSet(properties.keySet());
    }

    @Override
    public void clear() {
        for (PropertyDescriptor pd : getWriteableDescs()) {
            Class<?> type = pd.getPropertyType();
            if (type.isPrimitive()) {
                put(pd.getName(), defaultPrimitiveValues.get(type));
            } else {
                put(pd.getName(), null);
            }
        }
    }

    public Set<PropertyDescriptor> getWriteableDescs() {
        Set<PropertyDescriptor> result = new HashSet<PropertyDescriptor>();
        for (PropertyDescriptor pd : properties.values()) {
            if (pd.getWriteMethod() != null) {
                result.add(pd);
            }
        }
        return result;
    }

    public PropertyDescriptor getPropertyDesc(String key) {
        return properties.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return properties.entrySet().stream().map(stringPropertyDescriptorEntry -> {
            Entry<String, Object> stringObjectEntry;
            try {
                String key = stringPropertyDescriptorEntry.getKey();
                Object value = stringPropertyDescriptorEntry.getValue().getReadMethod().invoke(bean);

                stringObjectEntry = new AbstractMap.SimpleEntry<>(key, value);
                return stringObjectEntry;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());
    }

    @Override
    public Object get(Object name) {
        Object r = getImp(name);
        if (r == null && name.toString().contains(".")) {
            final String[] firstKeyAndTailKeys = name.toString().split(Pattern.quote("."), 2);
            r = getImp(firstKeyAndTailKeys[0]);
            if (r != null) {
                BeanMap bm = new BeanMap(r);
                r = bm.get(firstKeyAndTailKeys[1]);
            }
        }
        return r;
    }

    private Object getImp(Object key) {
        String name = (String) key;
        PropertyDescriptor pd = properties.get(name);
        assert pd != null : "Property descriptor for " + bean.getClass().getSimpleName() + "." + key + " where null!";
        try {
            return pd.getReadMethod().invoke(bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getIgnoringException(Object key) {
        String name = (String) key;
        PropertyDescriptor pd = properties.get(name);
        try {
            return pd.getReadMethod().invoke(bean);
        } catch (Exception e) {
            // throw new RuntimeException(e); as I said ignore...
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return keys;
    }

    @Override
    public Object put(String key, Object value) {
        PropertyDescriptor pd = properties.get(key);
        Object oldValue = get(key);
        try {
            try {
                pd.getWriteMethod().invoke(bean, value);
            } catch (IllegalArgumentException iae) {
                pd.getWriteMethod().invoke(bean, convert(value, pd.getPropertyType()));
            }
        } catch (Exception e) {
            System.out.println("Key " + key + " failed. Type is " + pd.getPropertyType().getName() + " value is " + value.getClass().getName());
            throw new RuntimeException(e);
        }
        return oldValue;
    }

    private <Ot> Ot convert(Object value, Class<Ot> type) throws IllegalAccessException {
        if (!converters.containsKey(type)) {
            throw new IllegalAccessException("argument type mismatch. " + value.getClass().getName() + " -> " + type);
        }
        return (Ot) converters.get(type).go(value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        for (String key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public Object remove(Object key) {
        return put((String) key, null);
    }

    @Override
    public int size() {
        return beanInfo.getPropertyDescriptors().length;
    }

    @Override
    public Collection<Object> values() {
        final Collection<Object> result = new HashSet<Object>();
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            result.add(get(pd.getName()));
        }
        return result;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Object getBean() {
        return bean;
    }

    @Override
    public String toString() {
        TreeMap<String, Object> tm = new TreeMap<String, Object>();
        for (String key : keySet()) {
            tm.put(key, get(key));
        }
        return tm.toString();
    }

    public boolean isReadOnly(String key) {
        if (!keySet().contains(key)) {
            return true;
        }
        return getPropertyDesc(key).getWriteMethod() == null;
    }

    public List<Class> getGenerics(String key) {
        return MetaHelp.getGenerics(bean.getClass(), key);
    }

    public Method[] getMethods() {
        return bean.getClass().getMethods();
    }

    public Set<String> getMethodNames() {
        Set<String> result = new HashSet<String>();
        for (Method method : getMethods()) result.add(method.getName());
        return result;
    }

    public Object invoke(Method method, Object... args) {
        try {
            return method.invoke(bean, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object invoke(String name, Object... args) {
        try {
            return invokeImp(name, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object invokeImp(String name, Object... args) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class[] types = new Class[args.length];

        for (int i = 0; i < types.length; i++) {
            Class<? extends Object> clazz = args[i].getClass();
            if (clazz.isPrimitive()) {
                Field field = clazz.getField("TYPE");
                Class<?> type = (Class<?>) field.get(clazz);
                types[i] = type;
            } else {
                types[i] = args[i].getClass();
            }
        }
        return bean.getClass().getMethod(name, types).invoke(bean, args);
    }

    public Map<String, Object> makeLenientMassInvokation(String methodPattern, Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (Method method : getMethods()) {
            String name = method.getName();
            if (name.matches(methodPattern)
                    && method.getParameterTypes().length == args.length) {
                try {
                    result.put(name, invoke(method, args));
                } catch (RuntimeException re) {
                    //result.put(name, re.getCause());
                }
            }
        }
        return result;
    }

    private interface Function<It, Ot> {
        Ot go(It it);
    }

    private Map<Class, Function> converters = new HashMap<>();

    {
        converters.put(Integer.class, s -> Integer.parseInt(s + ""));
        converters.put(Short.class, s -> Short.parseShort(s + ""));
        converters.put(Boolean.class, s -> Boolean.parseBoolean(s + ""));
        converters.put(Byte[].class, (Object s) -> {
            if (s instanceof Byte[] || s == null) {
                return s;
            }
            String[] parts = s.toString().split("[^0-9]");
            List<Byte> result = new ArrayList<>();
            for (String part : parts) {
                if(!part.trim().isEmpty()){
                    result.add(Byte.parseByte(part));
                }
            }
            Byte[] array = new Byte[result.size()];
            array = result.toArray(array);
            return array;
        });
    }

}
