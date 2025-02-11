package work.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 促销计算引擎
 */
class PromotionCalculator {
    private List<Promotion> promotions;

    public PromotionCalculator(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public double calculateMaxDiscountDP(double originalPrice) {
        int n = promotions.size();
        int maxState = 1 << n;
        double[] dp = new double[maxState];

        Arrays.fill(dp, originalPrice);
        dp[0] = originalPrice;

        // 遍历所有可能的促销组合 state(状态压缩)
        for (int state = 1; state < maxState; state++) {
            for (int i = 0; i < n; i++) {
                // 检查 state 是否包含 i
                // 检查某个整数 state 的第 i 位是否为 1
                if ((state & (1 << i)) != 0) {
                    // 即去掉 i 后的状态
                    int prevState = state ^ (1 << i);
                    // 检查促销 i 是否可以应用： 是否可叠加 是否有互斥关系
                    if (canApplyPromo(promotions.get(i), prevState, state)) {
                        dp[state] = Math.min(dp[state], promotions.get(i).applyDiscount(dp[prevState]));
                    }
                }
            }
        }
        // 返回最优解（选择所有促销时的最小价格）
        return dp[maxState - 1];
    }

    private boolean canApplyPromo(Promotion promo, int prevState, int currentState) {
        List<Promotion> selectedPromotions = new ArrayList<>();
        for (int i = 0; i < promotions.size(); i++) {
            if ((currentState & (1 << i)) != 0) {
                selectedPromotions.add(promotions.get(i));
            }
        }

        if (!promo.isStackable()) {
            for (Promotion p : selectedPromotions) {
                if (!p.isStackable()) {
                    return false;
                }
            }
        }

        if (promo.isExclusive()) {
            for (Promotion p : selectedPromotions) {
                if (p.isExclusive()) {
                    return false;
                }
            }
        }
        return true;
    }
}
