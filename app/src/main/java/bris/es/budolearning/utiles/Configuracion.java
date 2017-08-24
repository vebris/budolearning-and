package bris.es.budolearning.utiles;

import java.io.Serializable;

public class Configuracion implements Serializable{

    public static final int SOLO_WIFI = 1;

    private String url;
    private boolean recordarUsuario;

    private int tamanoCachePeticiones;
    private int tamanoCacheImagenes;
    private int tamanoCacheVideos;

    private int usoRedes;

    private boolean publicidadBannerAdMob = false;
    private boolean publicidadInterstialAdMob = false;

    private boolean verUltimaDescarga = true;
    private boolean vistaReducida = false;

    public boolean isVistaReducida(){return vistaReducida;}
    public void setVistaReducida(boolean vistaReducida) {this.vistaReducida = vistaReducida;}

    public int getUsoRedes() {
        return usoRedes;
    }
    public void setUsoRedes(int usoRedes) {
        this.usoRedes = usoRedes;
    }
    public void setUsoRedes(String usoRedes) {
        this.usoRedes = Integer.parseInt(usoRedes);
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean isRecordarUsuario() {
        return recordarUsuario;
    }
    public void setRecordarUsuario(boolean recordarUsuario) {
        this.recordarUsuario = recordarUsuario;
    }
    public boolean isPublicidadBannerAdMob() {
        return publicidadBannerAdMob;
    }
    public void setPublicidadBannerAdMob(boolean publicidadBannerAdMob) {
        this.publicidadBannerAdMob = publicidadBannerAdMob;
    }
    public boolean isPublicidadInterstialAdMob() {
        return publicidadInterstialAdMob;
    }
    public void setPublicidadInterstialAdMob(boolean publicidadInterstialAdMob) {
        this.publicidadInterstialAdMob = publicidadInterstialAdMob;
    }
    public boolean isVerUltimaDescarga() {
        return verUltimaDescarga;
    }
    public void setVerUltimaDescarga(boolean verUltimaDescarga) {
        this.verUltimaDescarga = verUltimaDescarga;
    }
    public int getTamanoCachePeticiones() {
        return tamanoCachePeticiones;
    }
    public void setTamanoCachePeticiones(int tamanoCachePeticiones) {
        this.tamanoCachePeticiones = tamanoCachePeticiones;
    }
    public int getTamanoCacheImagenes() {
        return tamanoCacheImagenes;
    }
    public void setTamanoCacheImagenes(int tamanoCacheImagenes) {
        this.tamanoCacheImagenes = tamanoCacheImagenes;
    }
    public int getTamanoCacheVideos() {
        return tamanoCacheVideos;
    }
    public void setTamanoCacheVideos(int tamanoCacheVideos) {
        this.tamanoCacheVideos = tamanoCacheVideos;
    }
}
