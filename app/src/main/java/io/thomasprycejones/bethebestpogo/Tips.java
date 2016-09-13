package io.thomasprycejones.bethebestpogo;

import android.content.Context;

/**
 * Created by Thomas Pryce Jones on 07-09-2016.
 */
public class Tips {
    public Context context;
    private String titulo;
    private String abstracttip;
    private String img;
    private String timestamp;
    private String url;

    public Tips (Context con, String titulo, String abstractnew,  String img, String timestamp, String url)
    {
        this.context = con.getApplicationContext();
        this.timestamp=timestamp;
        this.titulo = titulo;
        this.abstracttip = abstractnew;
        this.img = img;
        this.url =url;
    }

    public String getTitulo(){return this.titulo;}
    public String getabstractTip(){return this.abstracttip;}
    public String getTimestamp(){return this.timestamp;}
    public String getImg(){return this.img;}
    public String getUrl(){return this.url;}


}
