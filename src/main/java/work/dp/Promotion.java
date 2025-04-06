package work.dp;

import java.util.*;

abstract class Promotion {
    protected String name;
    /**
     * 是否叠加
     */
    protected boolean stackable;
    /**
     * 是否互斥
     */
    protected boolean exclusive;

    public Promotion(String name, boolean stackable, boolean exclusive) {
        this.name = name;
        this.stackable = stackable;
        this.exclusive = exclusive;
    }

    public String getName() {
        return name;
    }

    public boolean isStackable() {
        return stackable;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public abstract double applyDiscount(double originalPrice);
}

// 折扣类促销
class DiscountPromotion extends Promotion {
    private double discountRate;

    public DiscountPromotion(String name, double discountRate) {
        super(name, true, true);
        this.discountRate = discountRate;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice * discountRate;
    }
}

// 满减类促销
class CashOffPromotion extends Promotion {
    private double threshold;
    private double discountAmount;

    public CashOffPromotion(String name, double threshold, double discountAmount) {
        super(name, true, false);
        this.threshold = threshold;
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice >= threshold ? originalPrice - discountAmount : originalPrice;
    }
}

// 代金券类促销
class CouponPromotion extends Promotion {
    private double couponValue;

    public CouponPromotion(String name, double couponValue) {
        super(name, false, true);
        this.couponValue = couponValue;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - couponValue;
    }
}

// **新支持**：积分兑换
class PointsRedemptionPromotion extends Promotion {
    private int requiredPoints;
    private double discountValue;

    public PointsRedemptionPromotion(String name, int requiredPoints, double discountValue) {
        super(name, true, false);
        this.requiredPoints = requiredPoints;
        this.discountValue = discountValue;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - discountValue;
    }
}

// **新支持**：包邮
class FreeShippingPromotion extends Promotion {
    private double minOrderAmount;

    public FreeShippingPromotion(String name, double minOrderAmount) {
        super(name, true, false);
        this.minOrderAmount = minOrderAmount;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice;
    }
}

// **新支持**：买赠（仅计算等效折扣）
class BuyXGetYPromotion extends Promotion {
    private int buyCount;
    private int freeCount;

    public BuyXGetYPromotion(String name, int buyCount, int freeCount) {
        super(name, true, false);
        this.buyCount = buyCount;
        this.freeCount = freeCount;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        double effectiveDiscount = (double) freeCount / (buyCount + freeCount);
        return originalPrice * (1 - effectiveDiscount);
    }
}

// **新支持**：限时折扣
class TimeLimitedDiscountPromotion extends Promotion {
    private double discountRate;
    private int startHour;
    private int endHour;

    public TimeLimitedDiscountPromotion(String name, double discountRate, int startHour, int endHour) {
        super(name, true, true);
        this.discountRate = discountRate;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if (currentHour >= startHour && currentHour <= endHour) {
            return originalPrice * discountRate;
        }
        return originalPrice;
    }
}
