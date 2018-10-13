package com.until;

import com.google.gson.Gson;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Description:
 * Author:DongFang
 * Date:2018-08-28
 * Time:17:13
 */
public class BeanUtil {
    //通过反射，解析请求中的字段
    public static<T> T parseFromRequest(HttpServletRequest request, Class<T> aClass) throws IllegalAccessException, InstantiationException, ParseException {
        Field[] fields = aClass.getDeclaredFields();
        T instance = aClass.newInstance();
        for (Field field :
                fields) {
            field.setAccessible(true);
            if (StringUtils.isBlank(request.getParameter(field.getName()))){
                continue;
            }else if(field.getName().contains("Time")){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = simpleDateFormat.parse(request.getParameter(field.getName()));
                field.set(instance,date);
                continue;
            }else {
                //装换为该对象所在类的类型，调用转换工具包的方法，传入请求中字段的字段名，和字段类型
                Object convert = ConvertUtils.convert(request.getParameter(field.getName()), field.getType());
                field.set(instance,convert);
            }
        }
        return instance;
    }

    public static<T> T parseFromRequestMap(HttpServletRequest request, Class<T> aClass) throws IllegalAccessException, InstantiationException {
        Map parameterMap = request.getParameterMap();
        HashMap<String, String> map = new HashMap<>();
        Set<String> keySet = parameterMap.keySet();
        for (String key :
                keySet) {
            String[] values = (String[]) parameterMap.get(key);
            map.put(key,values[0]);
        }
        Gson gson = new Gson();
        T jsonInstance = gson.fromJson(gson.toJson(map), aClass);
        return jsonInstance;
    }
}
