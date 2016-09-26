package bris.es.budolearning.slidingtabs;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.io.Serializable;

import bris.es.budolearning.fragments.FragmentAbstract;
import bris.es.budolearning.fragments.FragmentAlumno;
import bris.es.budolearning.fragments.FragmentAlumnoGrado;
import bris.es.budolearning.fragments.FragmentDisciplinas;
import bris.es.budolearning.fragments.FragmentEstadisticas;
import bris.es.budolearning.fragments.FragmentFicheros;
import bris.es.budolearning.fragments.FragmentGrados;
import bris.es.budolearning.fragments.FragmentPagina;
import bris.es.budolearning.fragments.FragmentPassword;
import bris.es.budolearning.fragments.FragmentPuntos;
import bris.es.budolearning.fragments.FragmentRecursos;

public class PagerItem implements Serializable{
    private Class aClass;
    private final CharSequence mTitle;
    private final int mIndicatorColor;
    private final int mDividerColor;
    private final CharSequence mUrl;

    public static int DEFAULT_INDICATOR_COLOR = Color.rgb(205,149,12);//Color.rgb(255,215,0);
    public static int DEFAULT_DIVIDER_COLOR = Color.DKGRAY;

    public PagerItem(Class c, CharSequence title) {
        aClass = c;
        mTitle = title;
        mIndicatorColor = DEFAULT_INDICATOR_COLOR;
        mDividerColor = DEFAULT_DIVIDER_COLOR;
        mUrl = null;
    }
    public PagerItem(Class c, CharSequence title, CharSequence url) {
        aClass = c;
        mTitle = title;
        mIndicatorColor = DEFAULT_INDICATOR_COLOR;
        mDividerColor = DEFAULT_DIVIDER_COLOR;
        mUrl = url;
    }

    /**
     * @return A new {@link Fragment} to be displayed by a {@link ViewPager}
     */
    public Fragment createFragment() {
        FragmentAbstract fragment = null;
        if(aClass.equals(FragmentDisciplinas.class)) {
            fragment = new FragmentDisciplinas();
        } else if(aClass.equals(FragmentGrados.class)) {
            fragment = new FragmentGrados();
        } else if(aClass.equals(FragmentRecursos.class)) {
            fragment = new FragmentRecursos();
        } else if(aClass.equals(FragmentFicheros.class)) {
            fragment = new FragmentFicheros();
        } else if(aClass.equals(FragmentAlumno.class)) {
            fragment = new FragmentAlumno();
        } else if(aClass.equals(FragmentAlumnoGrado.class)) {
            fragment = new FragmentAlumnoGrado();
        } else if(aClass.equals(FragmentEstadisticas.class)) {
            fragment = new FragmentEstadisticas();
        } else if(aClass.equals(FragmentPassword.class)) {
            fragment = new FragmentPassword();
        } else if(aClass.equals(FragmentPuntos.class)) {
            fragment = new FragmentPuntos();
        } else if(aClass.equals(FragmentPagina.class)) {
            fragment = new FragmentPagina();
            ((FragmentPagina)fragment).setUrl(mUrl.toString());
        }
        return fragment;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public int getDividerColor() {
        return mDividerColor;
    }
}
