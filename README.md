# CS-GO-Perfect-Sensitivity
An app to help find your perfect Sensitivity in CS:GO.

The math behind this application is based on this post by Impulsive http://bit.ly/1Qwn5Ee with the variation done by /u/mr_sneakyTV here: http://bit.ly/1SmNQfY

## Web app

A browser-based version of the tool lives in the [`web/`](web/) directory. To use it, open [`web/index.html`](web/index.html) in your browser. No build step is required.

The web experience mirrors the original desktop workflow:

1. Enter your current sensitivity and click **Start** to generate initial low and high values.
2. Click a **Copy command** button to copy a value (with the `sensitivity` console command) and test it in-game.
3. Select whether the low or high option felt better, then choose **Continue** to tighten the range.
4. When you are satisfied, click **Finish** to copy the final averaged sensitivity.


