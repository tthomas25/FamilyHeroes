package com.example.thomas.familyheroes.MainActivity;

/**
 * Created by Thomas on 09/10/2014.
 */
import android.content.Context;
import android.content.Intent;

public class CommonUtilities {

    // give your server registration url here
    static final String SERVER_URL = "http://thomaslanternier.fr/family_heroes/register.php";

    // Google project id
    static final String SENDER_ID = "934377170755";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "FamilyHeroes GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.example.thomas.familyheroes.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}