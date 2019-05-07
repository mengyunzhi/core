package com.mengyunzhi.core.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by panjie on 17/7/6.
 * 公共服务类
 */
public interface CommonService {
    Logger logger = LoggerFactory.getLogger(CommonService.class);
    // 十六进制字符数组
    char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    ArrayList<Long> ids = new ArrayList<>();

    // 将字节转为字符串
    static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : bytes) {
            stringBuilder.append(HEXES[(b >> 4) & 0x0F]);
            stringBuilder.append(HEXES[b & 0x0F]);
        }

        return stringBuilder.toString();
    }

    /**
     * 根据指定的算法加密文件数据, 返回固定长度的十六进制小写哈希值
     *
     * @param multipartFile 需要加密的文件
     * @param algorithm     加密算法, 例如: MD5, SHA-1, SHA-256, SHA-512 等
     * @return 字符串
     * @throws Exception 异常
     */
    static String encrypt(InputStreamSource multipartFile, String algorithm) throws Exception {
        InputStream in = null;

        try {
            // 1. 根据算法名称获实现了算法的加密实例
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            in = multipartFile.getInputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                // 2. 文件数据通常比较大, 使用 update() 方法逐步添加
                digest.update(buf, 0, len);
            }

            // 3. 计算数据的哈希值, 添加完数据后 digest() 方法只能被调用一次
            byte[] cipher = digest.digest();

            // 4. 将结果转换为十六进制小写
            return bytesToString(cipher);

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // nothing
                }
            }
        }
    }

    /**
     * 获取所有的属性（包含其父类）
     *
     * @param aClass 类
     * @return 所有的字段
     */
    static List<Field> getAllModelFields(Class aClass) {
        List<Field> fields = new ArrayList<>();
        do {
            Collections.addAll(fields, aClass.getDeclaredFields());
            aClass = aClass.getSuperclass();
        } while (aClass != null);
        return fields;
    }

    /**
     * https://stackoverflow.com/questions/1555262/calculating-the-difference-between-two-java-date-instances
     * 获取两个时间的差
     * 示例代码 getDateDiff(date1, date2, TimeUnit.MINUTES);
     *
     * @param date1    时间1
     * @param date2    时间2
     * @param timeUnit 获取的两者时间差的单位（比如：天，小时，分，秒等，详见TimeUnit）
     * @return the diff value, in the provided unit
     */
    static Long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取一个所有字段均为null的对象
     *
     * @param tClass 类对象
     * @return Object
     */
    static Object getNullFieldsObject(Class<?> tClass) {
        Object object = null;
        try {
            object = Class.forName(tClass.getName()).newInstance();
        } catch (InstantiationException e) {
            logger.error("实例化对象发生InstantiationException异常:" + tClass.getName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error("实例化对象发生IllegalAccessException异常:" + tClass.getName());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.error("实例化对象发生ClassNotFoundException异常:" + tClass.getName());
            e.printStackTrace();
        }
        if (object != null)
            CommonService.setAllFieldsToNull(object);
        return object;
    }


    /**
     * 获取随机的INT数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    static int getRandomNumberInts(int min, int max) {
        Random random = new Random();
        OptionalInt number = random.ints(min, (max + 1)).findFirst();
        if (number.isPresent())
            return number.getAsInt();
        else {
            return Integer.parseInt(null);
        }
    }


    /**
     * 获取随机的LONG数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    static long getRandomNumberLongs(long min, long max) {
        Random random = new Random();
        OptionalLong asLong = random.longs(min, (max + 1)).findFirst();
        if (asLong.isPresent()) {
            return asLong.getAsLong();
        } else {
            return Long.parseLong(null);
        }
    }


    // 获取长度为length的随机字符串
    static String getRandomStringByLength(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    /**
     * 获取一个随机的唯一的ID
     * 注意：该方法只能在单元测试中使用
     *
     * @return 随机数
     */
    static Long getRandomUniqueId() {
        return CommonService.getRandomUniqueId(10000L, 20000L);
    }

    /**
     * 获取一个随机的唯一的ID
     * 注意：该方法只能在单元测试中使用
     * @param begin 最小值（不大于20000）
     * @return 随机数
     */
    static Long getRandomUniqueId(Long begin) {
        return CommonService.getRandomUniqueId(begin, 20000L);
    }

    /**
     * 获取一个随机的唯一的ID
     * 注意：该方法只能在单元测试中使用
     * @param begin 最小值
     * @param end 最大值
     * @return 随机ID
     */
    static Long getRandomUniqueId(Long begin, Long end) {
        Long id;
        do {
            id = CommonService.getRandomNumberLongs(begin, end);
        } while (CommonService.ids.contains(id));
        ids.add(id);
        return id;
    }

    // https://www.dexcoder.com/selfly/article/4026
    static String md5(String str) throws Exception {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new Exception("MD5加密出现错误");
        }
    }

    // 将所有的字段设置为null
    static Object setAllFieldsToNull(Object object) {
        List<Field> fields = getAllModelFields(object.getClass());
        try {
            for (Field field : fields) {
                String name = field.getName();
                if (name.equals("serialVersionUID")) {
                    continue;
                }

                if (Modifier.isFinal(field.getModifiers())) {
                    logger.debug("字段类型为final");
                    continue;
                }

                if (Modifier.isStatic(field.getModifiers())) {
                    logger.debug("字段类型为static");
                    continue;
                }

                logger.debug("将字段设置为设置为null");
                field.setAccessible(true);
                field.set(object, null);
            }
        } catch (IllegalAccessException e) {
            logger.info("设置字段值为null发生异常", object);
            e.printStackTrace();
        }

        return object;
    }

    // sha1加密
    static String sha1(String data) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");

            digest.update(data.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            IntStream.range(0, messageDigest.length).mapToObj(i -> Integer.toHexString(messageDigest[i] & 0xFF)).forEach(shaHex -> {
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            });
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将sql.Date 转换成Calendar
     *
     * @param date sql.Date
     * @return Calendar
     * @author panjie
     */
    static Calendar sqlDateToCalendar(java.sql.Date date) {
        Calendar calendar = null;
        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
        }

        return calendar;
    }

}
