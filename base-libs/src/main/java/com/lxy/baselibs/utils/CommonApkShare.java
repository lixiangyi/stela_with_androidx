package com.lxy.baselibs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author yuxh
 * @createTime 2014年9月11日 上午10:05:27
 */
public class CommonApkShare {
    /** 加密密钥 **/
    public final static String APK_DES_KEY = "key!12@3";
    /****/
    public final static String KEY_COMMON_SHAREDPREFERENCES = "KEY_COMMON_SHAREDPREFERENCES";
    /** 自动记录的登录账户 */
    public final static String KEY_COMMON_LOGINNAME = "KEY_COMMON_LOGINNAME";
    /** 是否记录登录密码 */
    public final static String KEY_ISREMEMBER_LOGINPASSWORD = "KEY_ISREMEMBER_LOGINPASSWORD";
    /** 自动记录的登录密码 */
    public final static String KEY_COMMON_LOGINPASSWORD = "KEY_COMMON_LOGINPASSWORD";
    /** 是否开启推送通知 */
    public final static String KEY_OPEN_PUSH = "KEY_OPEN_PUSH";
    /** 是否开启免打扰 */
    public final static String KEY_OPEN_DISTURB = "KEY_OPEN_DISTURB";
    /** 是否开启铃声 */
    public final static String KEY_OPEN_RING = "KEY_OPEN_RING";
    /** 是否开启震动 */
    public final static String KEY_OPEN_SHAKE = "KEY_OPEN_SHAKE";
    /** 是否已登录 */
    public final static String KEY_IS_LOGINED = "KEY_IS_LOGINED";
    /** 用户登录信息 */
    public final static String KEY_LOGIN_INFO = "KEY_LOGIN_INFO";
    /** apk是否有更新 */
    public final static String KEY_IS_APK_UPDATE = "KEY_IS_APK_UPDATE";
    /** 是否下次更新 */
    public final static String KEY_APK_UPDATE_NEXT_TIME = "KEY_APK_UPDATE_NEXT_TIME";
    /** 存在[资源]更新，是否下次再下载更新 */
    public final static String KEY_IS_DOWLOAD_NEXT_TIME = "KEY_IS_DOWLOAD_NEXT_TIME";
    /** 是否第一次进入App, 1表示已经登陆过 */
    public final static String KEY_IS_FIRST_IN = "KEY_IS_FIRST_IN";
    /** 引导 */
    public final static String KEY_GUIDE_FRAGMENT = "KEY_GUIDE_FRAGMENT";
    /** 手势密码 */
    public final static String KEY_GESTURE_PWD = "KEY_GESTURE_PWD";
    /** 重试时间戳 */
    public final static String KEY_GESTURE_RETRY = "KEY_GESTURE_RETRY";

    /** 登录用户 */
    public final static String KEY_LOGIN_USER = "KEY_LOGIN_USER";

    /** KEY_TOKEN */
    public final static String KEY_TOKEN = "KEY_TOKEN";
    /** KEY_USER_ID */
    public final static String KEY_USER_ID = "KEY_USER_ID";
    /** KEY_DEEP_LINK */
    public final static String KEY_DEEP_LINK = "KEY_DEEP_LINK";

    /**
     * 设备号
     */
    private final static String DEVICE_ID = "DEVICE_ID_V2";
    /**
     * 日活接口是否传送过
     */
    public final static String USERACTIVE_DIALY = "useractive_dialy";

    /**
     * 获取操作系统版本
     *
     * @return
     * @author yuxh
     * @createTime 2014年9月28日 下午10:33:16
     */
    public static String getOS() {
        return DeviceUuidFactory.getOS();
    }

    /**
     * 获取手机型号
     *
     * @return
     * @author yuxh
     * @createTime 2014年9月28日 下午10:32:57
     */
    public static String getModel() {
        return DeviceUuidFactory.getModel();
    }

    public static String getDeviceId(Context context) {
        String deviceid = getCommonShared(context).getString(DEVICE_ID, "");
        if (deviceid.equals("")) {
            deviceid = DeviceUuidFactory.GetCombineDeviceID(context, APK_DES_KEY);
            getCommonSharedEditor(context).putString(DEVICE_ID, deviceid).commit();
        }
        return deviceid;
    }

    /**
     * 获得通用的数据存储对象
     *
     * @param context
     * @return
     * @author yuxh
     * @createTime 2014年9月11日 上午10:06:04
     */
    public static SharedPreferences getCommonShared(Context context) {
        return context.getSharedPreferences("KEY_COMMON_SHAREDPREFERENCES", Context.MODE_PRIVATE);
    }

    /**
     * 获得通用的数据存储编辑对象
     *
     * @param context
     * @return
     * @author yuxh
     * @createTime 2014-7-31 下午12:12:46
     */
    public static Editor getCommonSharedEditor(Context context) {
        return getCommonShared(context).edit();
    }

    /**
     * 读取存储的String对象
     *
     * @param context
     * @param key
     * @return
     * @author yuxh
     * @createTime 2014-7-31 下午12:13:27
     */
    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = getCommonShared(context);
        return sharedPreferences.getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = getCommonShared(context);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * 存储String对象
     *
     * @param context
     * @param key
     * @param value
     * @author xxj
     * @createTime 2014-7-31 下午12:13:45
     */
    public static void putString(Context context, String key, String value) {
        Editor editor = getCommonSharedEditor(context);
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 存储boolean对象
     *
     * @param context
     * @param key
     * @param value
     * @author xxj
     * @createTime 2014-7-31 下午12:14:02
     */
    public static void putBoolean(Context context, String key, boolean value) {
        Editor editor = getCommonSharedEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putFloat(Context context, String key, float value) {
        Editor editor = getCommonSharedEditor(context);
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void putInt(Context context, String key, int value) {
        Editor editor = getCommonSharedEditor(context);
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putLong(Context context, String key, long value) {
        Editor editor = getCommonSharedEditor(context);
        editor.putLong(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getCommonShared(context);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences sharedPreferences = getCommonShared(context);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = getCommonShared(context);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sharedPreferences = getCommonShared(context);
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 存储对象数据
     *
     * @param context
     * @param key
     * @param value
     * @return
     * @author xxj
     * @createTime 2014-7-31 下午12:11:16
     */
    public static boolean saveObj(final Context context, final String key, final Object value) {
        ValidateSerializable.beginValidate(value);
        try {
            Editor editor = getCommonSharedEditor(context);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            String objString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            if (!getDeviceId(context).equals("") && !objString.equals(""))
                objString = objString + "$" + getDeviceId(context);
            if (!objString.equals("")) {
                try {
                    objString = EncryptTool.desEncrypt(objString, APK_DES_KEY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            editor.putString(key, objString).commit();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 读取对象数据
     *
     * @param context
     * @param key
     * @return
     * @author xxj
     * @createTime 2014-7-31 下午12:11:31
     */
    public static Object getObj(Context context, String key) {
        Object obj = null;
        try {
            SharedPreferences sharedPreferences = getCommonShared(context);
            String str = sharedPreferences.getString(key, "");
            if (str == null || "".equals(str.trim())) {
                return obj;
            } else {
                try {
                    str = EncryptTool.desDecrypt(str, APK_DES_KEY).split("[$]")[0];
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        // 兼容旧版app obj的解密方式
                        str = EncryptTool.desDecrypt_Deprecated(str, APK_DES_KEY).split("[$]")[0];
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            byte[] mobileBytes = Base64.decode(str.toString().getBytes(), Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
            ObjectInputStream objectInputStream;
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            obj = objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            return obj;
        }
        return obj;
    }



    /**
     * 根据键值名删除相应数据
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        Editor editor = getCommonSharedEditor(context);
        editor.remove(key);
        editor.commit();
    }

    /**
     * 1，判断saveObj中的object 是否设置了serialVersionUID<br>
     * 2，如果是String、基础类型数据。 则不进行serialVersionUID判断！<br>
     * 3，如果是数组，则判断数组里面的元素是否符合1，2，3<br>
     * <p>
     * 函数职责：
     * saveObj需要序列化对象，如果对象没有设置serialVersionUID，则对象字段变更时，可能会造成旧版本【已序列化】的对象无法【反序列化】成新版本的模型。<br>
     * 因此必须校验所涉及到的所有对象的serialVersionUID<br>
     * <p>
     * 校验难点<br>
     * 1，object本身为 【数组、集合】 怎么办？<br>
     * 2，object字段中 如果有【对象类型】怎么办？<br>
     * 3，object字段中 如果有【数组、集合】 怎么办？<br>
     * 4，object字段中 对象类型的字段 更复杂怎么办？<br>
     *
     * @param value
     * @author zqs
     * @createTime 2016年6月15日 下午12:25:17
     */
    private static class ValidateSerializable {

        private static final boolean DEBUG = false;

        public static void beginValidate(Object object) {
            Class classT = getObjectClass(object);
            if (classT != null) {
                validateClass(classT, null);
            }
        }

        /**
         * 获取数组、集合内的真实【对象类型】
         *
         * @param value 注意：参数是对象，不是class
         * @return
         * @author zqs
         * @createTime 2016年6月16日 上午11:48:53
         */
        private static Class getObjectClass(Object value) {
            if (value == null) {
                return null;
            }
            if (value instanceof Iterable) {// value是集合类型
                Iterable<?> items = (Iterable<?>) value;
                if (items.iterator().hasNext()) {
                    return getObjectClass(items.iterator().next());
                } else {
                    return null; // value是集合类型，但是里面没有任何元素
                }
            } else if (value.getClass().isArray()) { // value是数组类型
                return value.getClass().getComponentType();
            }
            return value.getClass(); // 非数组、集合类型 返回object本身
        }

        /**
         * 递归查找模型里面套的模型，检查所有模型是否符合规范
         *
         * @param classT 注意：参数是class
         * @param array
         * @author zqs
         * @createTime 2016年6月15日 下午7:28:41
         */
        private static void validateClass(Class classT, ArrayList<Class> arrayCacheClass) {

            if (arrayCacheClass == null) {
                arrayCacheClass = new ArrayList<Class>(); // array缓存已判断过的对象class，防止对象循环引用死循环
            }

            // 判断classT是否是数组、array类型。到数组、array等类型。直接进入泛型判断
            Class arryGenericClass = genericTypeInArray(classT);
            if (arryGenericClass != null) {
                validateClass(arryGenericClass, arrayCacheClass);
                return;
            }

            // 当前判断的classT是否是对象类型。否则不继续递归判断
            if (isFieldObject(classT) && !arrayCacheClass.contains(classT)) {
                if (DEBUG) {
                    Log.e("ValidateSerializable", "----------------------------------------");
                    Log.e("ValidateSerializable", "类型：" + classT.getName() + "是对象\n");
                    System.out.println("----------------------------------------");
                    System.out.println("类型：" + classT.getName() + "是对象\n");
                }
                arrayCacheClass.add(classT);
                isSerialVersionUIDConfig(classT); // 对象类型检查是否符合序列化规范
                validateField(classT, arrayCacheClass);
            } else if (!isFieldObject(classT)) {
                if (DEBUG) {
                    Log.e("ValidateSerializable", classT.getName() + "不是对象\n");
                    System.out.println("类型：" + classT.getName() + "不是对象\n");
                }
            }
        }

        private static void validateField(Class classT, ArrayList<Class> arrayCacheClass) {
            // 递归判断开始
            Field[] fs = classT.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field field = fs[i];
                Class filedClass = field.getType();
                Class arryGenericType = genericTypeInArray(field); // field是否存在泛型
                if (arryGenericType != null) {
                    validateClass(arryGenericType, arrayCacheClass);
                    continue; // 遇到数组、array等类型。直接进入泛型判断
                }
                if (DEBUG) {
                    Log.e("ValidateSerializable", "字段名：" + field.getName() + "");
                    System.out.println("字段名：" + field.getName() + "");
                }
                validateClass(filedClass, arrayCacheClass);
            }
        }

        /**
         * 获取Collection ，ArrayList、LinkedList等配置的泛型类型（通过field取出）
         *
         * @param field 注意：参数是field
         * @return
         * @author zqs
         * @createTime 2016年6月16日 上午10:14:11
         */
        private static Class genericTypeInArray(Field field) {
            Class classT = field.getType();
            if (classT.isArray()) {
                return classT.getComponentType();
            } else if (Collection.class.isAssignableFrom(classT)) {
                try {
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    return (Class) pt.getActualTypeArguments()[0]; // 获取到的ArrayList的泛型类型
                } catch (Exception e) {
                    // List可能未配置泛型类型
                }
            }
            return null;
        }

        /**
         * 数组泛型可以通过class取出
         * <p>
         * Collection ，ArrayList、LinkedList等配置的泛型类型 无法通过class取出
         *
         * @param classT
         * @return
         * @author zqs
         * @createTime 2016年6月16日 上午10:14:11
         */
        private static Class genericTypeInArray(Class classT) {
            if (classT.isArray()) {
                return classT.getComponentType();
            } else if (Collection.class.isAssignableFrom(classT)) {
                return String.class; // classT 是集合类型，无法取出其泛型类型，暂时默认为可序列化
            }
            return null;
        }

        /**
         * 判断模型是否设置了 serialVersionUID，使用序列化保存对象，必须配置该值
         *
         * @param classT
         * @author zqs
         * @createTime 2016年6月15日 下午7:32:17
         */
        private static void isSerialVersionUIDConfig(Class object) {
            if (Serializable.class.isAssignableFrom(object)) { // 只有继承自Serializable的类才需要判断
                // 不是包装类------则要判断对象是否设置了serialVersionUID
                try {
                    Field field = object.getDeclaredField("serialVersionUID");
                    if (field == null || field.getModifiers() != 26) {
                        // TODO: 2019/9/18 反射有问题
                        throw new RuntimeException("\n=======" + object.getName() + " 必须配置：private static final long serialVersionUID 序列化码========");
                    }
                } catch (NoSuchFieldException | SecurityException e1) {
                    // TODO: 2019/9/18 反射有问题
                    throw new RuntimeException("\n=======" + object.getName() + " 必须配置：private static final long serialVersionUID 序列化码========");
                }
            }
        }

        /**
         * 判断Class [是8中基础数据类型+String] 之外的对象！
         *
         * @param classT
         * @author zqs
         * @createTime 2016年6月15日 下午5:33:06
         */
        private static boolean isFieldObject(Class classT) {
            boolean isPrimitive = classT.isPrimitive(); // 是否是基础类型数据
            if (isPrimitive) {
                return false;
            }
            try {
                Class clz = ((Class) classT.getField("TYPE").get(null)); // 是否是基础类型的包装类。 只有包装类getField("TYPE").get(null) 有值，且无异常
                isPrimitive = clz.isPrimitive();
                if (!isPrimitive) {
                    throw new Exception("非包装类，但包含TYPE字段的复杂对象");
                }
            } catch (Exception e) {
                if (classT == String.class) {
                    isPrimitive = true;
                } else {
                    isPrimitive = false;
                }
            }
            return !isPrimitive;
        }
    }
}
