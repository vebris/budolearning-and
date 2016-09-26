package bris.es.budolearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bris.es.budolearning.R;
import bris.es.budolearning.task.TaskUsuario;

public class FragmentPagina extends FragmentAbstract {

    private TaskUsuario taskUsuario;
    private View view;
    private String url;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_webview, container, false);

        webView = ((WebView)view.findViewById(R.id.webView));
        webView.setVerticalScrollBarEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        setHasOptionsMenu(false);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        setupWebView(savedInstanceState);
    }

    private void setupWebView(Bundle savedInstanceState) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:hacerMovil()");
            }
        });

        webView.loadUrl("javascript: function hacerMovil(){$('.row0').css('display','none');$('.row1').css('display','none');$('.row5').css('display','none')}");
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
            webView.loadUrl("javascript:hacerMovil()");
        } else {
            webView.loadUrl(url);
        }
    }

    public void setUrl(String url) {
        this.url=url;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

}