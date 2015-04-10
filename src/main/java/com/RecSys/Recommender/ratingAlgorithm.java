package com.RecSys.Recommender;

class ratingAlgorithm {

	private static final int weightClicks = 1;
	private static final int weightBuys = 5;

	// clicks are rated 1, buys are rated 5, total sum is returned
	public static float algorithm1(int clicks, int buys, int amountClicksBuys) {

		float rating = clicks * weightClicks + buys * weightBuys;

		return rating;

	}

	// fraction of rating/amountClicksBuys is returned
	public static float algorithm2(int clicks, int buys, int amountClicksBuys) {

		float rating = clicks * weightClicks + buys * weightBuys;
		float fraction = rating / amountClicksBuys * 5;

		return fraction;
	}

	// rating is computed based on certain ranges
	public static float algorithm3(int clicks, int buys, int amountClicksBuys) {

		int rating = clicks * weightClicks + buys * weightBuys;

		// range has to be float in order for mahout to work later on
		float range;

		if (rating < 3) {
			range = 0;
		} else if (rating < 6) {
			range = 1;
		} else if (rating < 9) {
			range = 2;
		} else if (rating < 12) {
			range = 3;
		} else if (rating < 15) {
			range = 4;
		} else 
			range = 5;

		return range;
	}

}
