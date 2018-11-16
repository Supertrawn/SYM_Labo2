package heig_vd.sym_labo2.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public final static String GRAPHQL_URL = "http://sym.iict.ch/api/graphql";
    public final static String JSON_URL = "http://sym.iict.ch/rest/json";
    public final static String TXT_URL = "http://sym.iict.ch/rest/txt" ;

    public static void displayContactSupportErrorMessage(Context context) {
        Toast.makeText(context, "Sorry there was an error, please contact the support", Toast.LENGTH_SHORT).show();
    }
}
