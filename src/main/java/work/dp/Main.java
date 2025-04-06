package work.dp;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Promotion> promotions = Arrays.asList(
            new DiscountPromotion("8折优惠", 0.8),
            new CashOffPromotion("满100减10", 100, 10),
            new CouponPromotion("50元优惠券", 50),
            new PointsRedemptionPromotion("1000积分抵10元", 1000, 10),
            new FreeShippingPromotion("满50元包邮", 50),
            new BuyXGetYPromotion("买2送1", 2, 1),
            new TimeLimitedDiscountPromotion("限时9折", 0.9, 19, 22)
        );

        PromotionCalculator calculator = new PromotionCalculator(promotions);
        double originalPrice = 200;
        double finalPrice = calculator.calculateMaxDiscountDP(originalPrice);

        System.out.println("原价: " + originalPrice + " 元");
        System.out.println("最优价格: " + finalPrice + " 元");
        System.out.println("最大优惠: " + (originalPrice - finalPrice) + " 元");
    }
}
