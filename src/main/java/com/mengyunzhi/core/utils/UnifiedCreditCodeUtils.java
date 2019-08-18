package com.mengyunzhi.core.utils;

import com.mengyunzhi.core.exception.ValidationException;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

/**
 * 统一社会停用用代码
 */
public interface UnifiedCreditCodeUtils {
    Logger logger = LoggerFactory.getLogger(UnifiedCreditCodeUtils.class);
    String baseCode = "0123456789ABCDEFGHJKLMNPQRTUWXY";
    char[] baseCodeArray = baseCode.toCharArray();
    int[] wi = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};

    /**
     * 生成供较验使用的 Code Map
     *
     * @return BidiMap
     */
    static BidiMap<Character, Integer> generateCodes() {
        BidiMap<Character, Integer> codes = new TreeBidiMap<>();
        for (int i = 0; i < baseCode.length(); i++) {
            codes.put(baseCodeArray[i], i);
        }

        return codes;
    }

    /**
     * 较验社会统一信用代码
     *
     * @param unifiedCreditCode 统一社会信息代码
     * @return 符合: true
     */
    static boolean validateUnifiedCreditCode(String unifiedCreditCode) {
        if ((unifiedCreditCode.equals("")) || unifiedCreditCode.length() != 18) {
            return false;
        }

        Map<Character, Integer> codes = generateCodes();
        int parityBit;
        try {
            parityBit = getParityBit(unifiedCreditCode, codes);
        } catch (ValidationException e) {
            return false;
        }

        return parityBit == codes.get(unifiedCreditCode.charAt(unifiedCreditCode.length() - 1));
    }

    /**
     * 获取较验码
     *
     * @param unifiedCreditCode 统一社会信息代码
     * @param codes             带有映射关系的国家代码
     * @return 较难位的值
     */
    static int getParityBit(String unifiedCreditCode, Map<Character, Integer> codes) {
        char[] businessCodeArray = unifiedCreditCode.toCharArray();

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char key = businessCodeArray[i];
            if (baseCode.indexOf(key) == -1) {
                throw new ValidationException("第" + String.valueOf(i + 1) + "位传入了非法的字符" + key);
            }
            sum += (codes.get(key) * wi[i]);
        }
        int result = 31 - sum % 31;
        return result == 31 ? 0 : result;
    }

    /**
     * 获取一个随机的统一社会信用代码
     *
     * @return 统一社会信用代码
     */
    static String generateOneUnifiedCreditCode() {
        Random random = new Random();
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < 17; ++i) {
            int num = random.nextInt(baseCode.length() - 1);
            buf.append(baseCode.charAt(num));
        }

        String code = buf.toString();
        String upperCode = code.toUpperCase();
        BidiMap<Character, Integer> codes = generateCodes();
        int parityBit = getParityBit(upperCode, codes);

        if (codes.getKey(parityBit) == null) {
            logger.debug("生成社会统一信用代码不符合规则");
            upperCode = generateOneUnifiedCreditCode();
        } else {
            upperCode = upperCode + codes.getKey(parityBit);
        }

        return upperCode;
    }
}
