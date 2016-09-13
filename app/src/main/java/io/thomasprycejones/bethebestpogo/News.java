package io.thomasprycejones.bethebestpogo;

import android.content.Context;

/**
 * Created by Thomas Pryce Jones on 07-09-2016.
 */
public class News {
    public Context context;
    private String source;
    private String titulo;
    private String abstractnew;
    private String img;
    private String timestamp;
    private String url;

    public News (Context con, String source, String titulo, String abstractnew,  String img, String timestamp, String url)
    {
        this.context = con.getApplicationContext();
        this.timestamp=timestamp;
        this.source = source;
        this.titulo = titulo;
        this.abstractnew = abstractnew;
        this.img = img;
        this.url =url;
    }

    public String getTitulo(){return this.titulo;}
    public String getAbstractnew(){return this.abstractnew;}
    public String getSource(){return this.source;}
    public String getTimestamp(){return this.timestamp;}
    public String getImg(){return this.img;}
    public String getUrl(){return this.url;}


}
