package com.deiselw.timeinfo;

/**
 * Created by Deise on 15/02/2018.
 */

public class BillingManager implements PurchasesUpdatedListener {

    private BillingClient mBillingClient;

    public BillingManager(Activity activity) {
        mBillingClient = BillingClient.newBuilder(activity).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    @Override
    void onPurchasesUpdated(@BillingResponse int responseCode,
                            List<Purchase> purchases) {
        if (responseCode == BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (responseCode == BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }
    }
}
