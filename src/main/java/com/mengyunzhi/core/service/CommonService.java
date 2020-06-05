package com.mengyunzhi.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 公共服务类
 *
 * @author panjie on 17/7/6.
 */
public interface CommonService {
    Logger logger = LoggerFactory.getLogger(CommonService.class);

    /**
     * 字符串格式化
     */
    String dataPattern = "yyyy年M月d日";
    /**
     * 加密算法
     */
    String SHA_256 = "SHA-256";

    /**
     * 十六进制字符数组
     */
    char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    ArrayList<Long> IDS = new ArrayList<>();

    /**
     * 将字节转为字符串
     *
     * @param bytes 字节
     * @return 字符串
     */
    static String bytesToString(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        final StringBuilder stringBuilder = new StringBuilder();

        for (final byte b : bytes) {
            stringBuilder.append(HEXES[(b >> 4) & 0x0F]);
            stringBuilder.append(HEXES[b & 0x0F]);
        }

        return stringBuilder.toString();
    }

    /**
     * 将字节数组转换为无符号十六进制字符串
     *
     * @param bytes 数组
     * @return java.lang.String
     * @author htx
     * @date 上午11:40 19-7-20
     **/
    static String convertByteArrayToUnsignedHexString(final byte[] bytes) {
        // Create Hex String
        final StringBuilder hexString = new StringBuilder();
        // 字节数组转换为 十六进制 数
        IntStream.range(0, bytes.length).mapToObj(i -> Integer.toHexString(bytes[i] & 0xFF)).forEach(shaHex -> {
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        });
        return hexString.toString();
    }

    /**
     * calendar 转换为日期
     *
     * @param calendar 日历
     * @return 日期字符串
     */
    static String convertCalendarToDateString(final Calendar calendar) {
        return convertCalendarToDateString(calendar, dataPattern);
    }

    /**
     * calendar 转换为日期字符串
     *
     * @param calendar    日历
     * @param dataPattern 格式化
     * @return 日期字符串
     */
    static String convertCalendarToDateString(final Calendar calendar, final String dataPattern) {
        if (calendar == null) {
            return "";
        } else {
            final Date date = calendar.getTime();
            final DateFormat dateFormat = new SimpleDateFormat(dataPattern);
            final String dateString = dateFormat.format(date);
            return dateString;
        }
    }

    /**
     * 根据指定的算法加密文件数据, 返回固定长度的十六进制小写哈希值
     *
     * @param multipartFile 需要加密的文件
     * @param algorithm     加密算法, 例如: MD5, SHA-1, SHA-256, SHA-512 等
     * @return 字符串
     * @throws Exception 异常
     */
    static String encrypt(final InputStreamSource multipartFile, final String algorithm) throws Exception {
        String result;
        InputStream in = null;

        try {
            // 1. 根据算法名称获实现了算法的加密实例
            final MessageDigest digest = MessageDigest.getInstance(algorithm);

            in = multipartFile.getInputStream();
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                // 2. 文件数据通常比较大, 使用 update() 方法逐步添加
                digest.update(buf, 0, len);
            }

            // 3. 计算数据的哈希值, 添加完数据后 digest() 方法只能被调用一次
            final byte[] cipher = digest.digest();

            // 4. 将结果转换为十六进制小写
            result = bytesToString(cipher);

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final Exception e) {
                    // nothing
                }
            }
        }
        return result;
    }

    /**
     * 获取所有的属性（包含其父类）
     *
     * @param aClass 类
     * @return 所有的字段
     */
    static List<Field> getAllModelFields(Class aClass) {
        final List<Field> fields = new ArrayList<>();
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
    static Long getDateDiff(final Date date1, final Date date2, final TimeUnit timeUnit) {
        final long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取一个所有字段均为null的对象
     *
     * @param tClass 类对象
     * @return Object
     */
    static Object getNullFieldsObject(final Class<?> tClass) {
        Object object = null;
        try {
            object = Class.forName(tClass.getName()).newInstance();
        } catch (final InstantiationException e) {
            logger.error("实例化对象发生InstantiationException异常:" + tClass.getName());
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            logger.error("实例化对象发生IllegalAccessException异常:" + tClass.getName());
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            logger.error("实例化对象发生ClassNotFoundException异常:" + tClass.getName());
            e.printStackTrace();
        }
        if (object != null) {
            CommonService.setAllFieldsToNull(object);
        }
        return object;
    }


    /**
     * 获取一个随机的手机号
     *
     * @return 随机字符串
     */
    static String getRandomMobileNumber() {
        return "138888" + getRandomUniqueId().toString();
    }

    /**
     * 获取随机的INT数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    static int getRandomNumberInts(final int min, final int max) {
        final Random random = new Random();
        final OptionalInt number = random.ints(min, (max + 1)).findFirst();
        if (number.isPresent()) {
            return number.getAsInt();
        } else {
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
    static long getRandomNumberLongs(final long min, final long max) {
        final Random random = new Random();
        final OptionalLong asLong = random.longs(min, (max + 1)).findFirst();
        if (asLong.isPresent()) {
            return asLong.getAsLong();
        } else {
            return Long.parseLong(null);
        }
    }


    /**
     * 获取长度为length的随机字符串
     *
     * @param length 长度
     * @return
     */
    static String getRandomStringByLength(final int length) {
        final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final Random random = new Random();
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final int num = random.nextInt(62);
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
     *
     * @param begin 最小值（不大于20000）
     * @return 随机数
     */
    static Long getRandomUniqueId(final Long begin) {
        return CommonService.getRandomUniqueId(begin, 20000L);
    }

    /**
     * 获取一个随机的唯一的ID
     * 注意：该方法只能在单元测试中使用
     *
     * @param begin 最小值
     * @param end   最大值
     * @return 随机ID
     */
    static Long getRandomUniqueId(final Long begin, final Long end) {
        Long id;
        do {
            id = CommonService.getRandomNumberLongs(begin, end);
        } while (CommonService.IDS.contains(id));
        IDS.add(id);
        return id;
    }

    /**
     * 获取日期开始时间 00:00
     *
     * @param date 日期
     * @return java.util.Date 0点的日期
     **/
    static Date getStartOfDay(final Date date) {
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        final LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 生成一次性密码
     *
     * @param seed          种子
     * @param effectiveTime 有效时间
     * @return 6位密码
     */
    static String makeOneTimePassword(final String seed, final Long effectiveTime) {
        return CommonService.makeOneTimePassword(seed, Calendar.getInstance().getTimeInMillis(), effectiveTime);
    }

    /**
     * 生成一次性密码
     *
     * @param seed          种子
     * @param timeMillis    时间戳
     * @param effectiveTime 有效时长
     * @return 6位密码
     */
    static String makeOneTimePassword(final String seed, final Long timeMillis, final Long effectiveTime) {
        final long longSeed = Long.parseLong(seed);
        final long superPasswordTime = timeMillis / effectiveTime + longSeed;
        final String plaintext = Long.toString(superPasswordTime);
        return CommonService.sha256(plaintext).substring(Integer.valueOf(String.valueOf(longSeed % 40)), 6);
    }

    /**
     * md5加密
     * https://www.dexcoder.com/selfly/article/4026
     *
     * @param str 明文
     * @return 密文
     */
    static String md5(final String str) throws Exception {
        try {
            // 生成一个MD5加密计算摘要
            final MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (final Exception e) {
            throw new Exception("MD5加密出现错误");
        }
    }

    /**
     * 将所有的字段设置为null
     *
     * @param object 原对象
     * @return 设置字段均为null后的对象
     */
    static Object setAllFieldsToNull(final Object object) {
        final List<Field> fields = getAllModelFields(object.getClass());
        try {
            for (final Field field : fields) {
                final String name = field.getName();
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
        } catch (final IllegalAccessException e) {
            logger.info("设置字段值为null发生异常", object);
            e.printStackTrace();
        }

        return object;
    }

    /**
     * sha1加密
     *
     * @param data 加密前数据
     * @return 加密后数据
     */
    static String sha1(final String data) {
        try {
            final MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");

            digest.update(data.getBytes());
            final byte[] messageDigest = digest.digest();
            // Create Hex String
            final StringBuilder hexString = new StringBuilder();
            // 字节数组转换为 十六进制 数
            IntStream.range(0, messageDigest.length).mapToObj(i -> Integer.toHexString(messageDigest[i] & 0xFF)).forEach(shaHex -> {
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            });
            return hexString.toString();

        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * sha256加密
     *
     * @param plaintext 明文
     * @return java.lang.String 加密后的文字
     * @throws NoSuchAlgorithmException 加密算法不存在
     * @author htx
     * @date 上午6:19 19-7-15
     **/
    static String sha256(final String plaintext) {
        try {
            // 获取sha-256加密字节数组
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
            return convertByteArrayToUnsignedHexString(hash);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return plaintext;
    }

    /**
     * 将sql.Date 转换成Calendar
     *
     * @param date sql.Date
     * @return Calendar
     * @author panjie
     */
    static Calendar sqlDateToCalendar(final java.sql.Date date) {
        Calendar calendar = null;
        if (date != null) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
        }

        return calendar;
    }


}
