package com.sanganericart.fms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

/**
 * SanganeriCart FMS - team ke phone ke liye
 * ---------------------------------------------------------------
 * Ye app kuch naya nahi dikhati. Wahi portal andar chalta hai jo
 * browser me chalta hai. Farak sirf ek, aur wahi sabse zaroori hai:
 *
 *   Portal me hara "Call" button dabate hi CALL LAG JATI HAI.
 *   Dialer screen beech me nahi aati, green button dabana nahi
 *   padta. Ek tap, bas.
 *
 * Browser ye kabhi nahi kar sakta - Android call lagane ki ijazat
 * sirf app ko deta hai (CALL_PHONE). Isi wajah se TeleCRM jaise
 * sabhi CRM ki bhi apni app hoti hai.
 *
 * Call company ke apne SIM se hi jati hai - customer ko wahi number
 * dikhta hai jo aapne diya hua hai.
 *
 * DHYAN: is file me ek bhi bahar ki library nahi hai - sirf Android
 * ka apna saamaan. Isliye build kabhi kisi library ke jhagde me
 * nahi phansta.
 */
public class MainActivity extends Activity {

    /** Login page. Kabhi link badle to sirf yahi line badalni hai. */
    private static final String DEFAULT_URL = "https://sanganeri-fms.pages.dev/";

    private static final int REQ_CALL = 101;

    /** Net na ho ya link galat ho to yahi dikhta hai - do saaf button ke saath */
    private static final String ERROR_PAGE =
        "<html><head><meta name='viewport' content='width=device-width,initial-scale=1'>"
      + "<style>body{font:16px/1.6 sans-serif;padding:34px 24px;color:#17251E;"
      + "background:#FAFBF7}h2{color:#7B1824;margin:0 0 6px}p{color:#5E6E64}"
      + "a{display:inline-block;margin:14px 10px 0 0;padding:11px 18px;border-radius:9px;"
      + "background:#7B1824;color:#fff;text-decoration:none;font-weight:700}"
      + "a.b{background:#fff;color:#7B1824;border:1px solid #DFE6DF}</style></head>"
      + "<body><h2>Portal khul nahi paya</h2>"
      + "<p>Internet band ho sakta hai, ya link badal gaya ho. "
      + "Pehle Try again dabayein.</p>"
      + "<a href='fmsretry:'>Try again</a>"
      + "<a class='b' href='fmsconfig:'>Change link</a></body></html>";

    private WebView web;
    private SharedPreferences prefs;

    /** Jis number par call karni hai - permission maangne ke beech me sambhal kar */
    private String pendingNumber = null;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("fms", MODE_PRIVATE);
        web   = findViewById(R.id.web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);          // portal localStorage istemal karta hai
        s.setDatabaseEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);
        s.setSupportMultipleWindows(false);
        s.setJavaScriptCanOpenWindowsAutomatically(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);

        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest req) {
                return handleUrl(req.getUrl().toString());
            }

            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(WebView v, String url) {
                return handleUrl(url);
            }

            @Override
            public void onReceivedError(WebView v, WebResourceRequest req,
                                        WebResourceError err) {
                if (req != null && !req.isForMainFrame()) return;
                v.loadDataWithBaseURL(null, ERROR_PAGE, "text/html", "utf-8", null);
            }
        });

        web.loadUrl(prefs.getString("url", DEFAULT_URL));
    }

    /**
     * Har link yahan se guzarta hai.
     *   tel:...        -> seedha call
     *   wa.me / mail   -> WhatsApp / mail app me
     *   baaki sab      -> isi app ke andar
     */
    private boolean handleUrl(String url) {
        if (url == null) return false;
        String low = url.toLowerCase();

        if (low.startsWith("fmsconfig:")) {      // error page ka "Change link"
            askForUrl();
            return true;
        }
        if (low.startsWith("fmsretry:")) {
            web.loadUrl(prefs.getString("url", DEFAULT_URL));
            return true;
        }
        if (low.startsWith("tel:")) {
            placeCall(Uri.decode(url.substring(4)));
            return true;
        }
        if (low.startsWith("whatsapp:") || low.startsWith("https://wa.me/")
                || low.startsWith("http://wa.me/") || low.startsWith("mailto:")
                || low.startsWith("sms:") || low.startsWith("intent:")) {
            openOutside(url);
            return true;
        }
        return false;                      // portal ke apne page andar hi khulen
    }

    /** Ek tap = call. Permission pehli baar hi poochhi jati hai. */
    private void placeCall(String number) {
        String n = number == null ? "" : number.replaceAll("[^0-9+]", "");
        if (n.isEmpty()) return;

        if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            pendingNumber = n;
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQ_CALL);
            return;
        }
        try {
            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + n));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (RuntimeException e) {
            dialOnly(n);                   // kisi wajah se na chale to dialer khol do
        }
    }

    /** Permission na mile to purana tarika - dialer number ke saath khul jaye */
    private void dialOnly(String n) {
        try {
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + n));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (RuntimeException e) {
            Toast.makeText(this, "Call nahi lag payi: " + n, Toast.LENGTH_LONG).show();
        }
    }

    private void openOutside(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (RuntimeException e) {
            Toast.makeText(this, "Ye link kholne wali app nahi mili",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int code, String[] perms, int[] res) {
        super.onRequestPermissionsResult(code, perms, res);
        if (code != REQ_CALL) return;
        String n = pendingNumber;
        pendingNumber = null;
        if (n == null) return;
        if (res.length > 0 && res[0] == PackageManager.PERMISSION_GRANTED) {
            placeCall(n);
        } else {
            Toast.makeText(this,
                    "Ek tap me call ke liye \"Phone calls\" ki ijazat deni hogi. "
                  + "Abhi dialer khol rahe hain.", Toast.LENGTH_LONG).show();
            dialOnly(n);
        }
    }

    /** Link kabhi badalna pade to - error page se khulta hai */
    private void askForUrl() {
        final EditText box = new EditText(this);
        box.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        box.setText(prefs.getString("url", DEFAULT_URL));
        new AlertDialog.Builder(this)
                .setTitle("Portal ka link")
                .setView(box)
                .setPositiveButton("Save", (d, w) -> {
                    String u = box.getText().toString().trim();
                    if (!u.startsWith("http")) u = "https://" + u;
                    prefs.edit().putString("url", u).apply();
                    web.loadUrl(u);
                })
                .setNegativeButton("Rehne do", null)
                .setNeutralButton("Wapas default", (d, w) -> {
                    prefs.edit().remove("url").apply();
                    web.loadUrl(DEFAULT_URL);
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }
}
