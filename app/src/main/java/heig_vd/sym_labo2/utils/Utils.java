package heig_vd.sym_labo2.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @Class       : Utils
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Use to get static common method and configurations
 *
 * @Comment(s)  : -
 */
public class Utils {
    public final static String GRAPHQL_URL = "http://sym.iict.ch/api/graphql";
    public final static String JSON_URL = "http://sym.iict.ch/rest/json";
    public final static String TXT_URL = "http://sym.iict.ch/rest/txt" ;

    public static void displayContactSupportErrorMessageToast(Context context) {
        Toast.makeText(
                context,
                "Sorry there was an error, please contact the support",
                Toast.LENGTH_SHORT).show();
    }

    public static void displayPleaseFillTheRequestBodyToal(Context context) {
        Toast.makeText(
                context,
                "Please Fill the request Body",
                Toast.LENGTH_SHORT).show();
    }
}
