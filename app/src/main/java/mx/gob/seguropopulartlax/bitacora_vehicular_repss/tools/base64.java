package mx.gob.seguropopulartlax.bitacora_vehicular_repss.tools;

import android.util.Base64;

public class base64 {

    public static String encode(String pass) {
        String s ="";
        try{
            byte[] b = pass.getBytes("UTF-8");
            s = Base64.encodeToString(b, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }
}
