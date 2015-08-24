package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceUtil {

    /**
     * Utility method for setting scale to 2 and rounding up/
     * 
     * @param value
     * @return
     */
    public static BigDecimal roundUp(BigDecimal value) {
        return value.setScale(2, RoundingMode.UP);
	}

    public static BigDecimal roundUp(String value) {
        return new BigDecimal(value).setScale(2, RoundingMode.UP);
    }

    public static BigDecimal roundUp(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.UP);
    }
}
