public class LoyaltyPointsService {

    public int calculatePoints(UserActivity activity) {

        // For example, if the user made a purchase of $100, they get 10 points per $10 spent.
        
        if (activity.getType().equals("PURCHASE")) {
            return activity.getAmount() / 10 * 10;
        }
        return 0;
    }
}