package com.example.andriod_wv

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

import android.content.Context
import android.graphics.Color
import android.net.http.SslError
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.widget.TextView
import io.flutter.plugin.platform.PlatformView


import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformViewFactory

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import java.io.InputStream
import java.lang.reflect.Field
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class MainActivity : FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory(
                "<platform-view-type>",
                NativeViewFactory()
            )
    }
}


internal class NativeView(context: Context, id: Int, creationParams: Map<String?, Any?>?) :
    PlatformView {
    private val webView: WebView

    override fun getView(): View {
        return webView
    }

    override fun dispose() {}

    init {
        webView = WebView(context)
        webView.apply { loadUrl("https://www.android.com/") }

//        textView.textSize = 72f
//        textView.setBackgroundColor(Color.rgb(255, 255, 255))
//        textView.text = "Rendered on a native Android view (id: $id)"
    }
}

class NativeViewFactory : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val creationParams = args as Map<String?, Any?>?
        return NativeView(context, viewId, creationParams)
    }
}

class PlatformViewPlugin : FlutterPlugin {
    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        binding
            .platformViewRegistry
            .registerViewFactory("<platform-view-type>", NativeViewFactory())
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {}
}
//
//private fun fromInputStream(caInput: InputStream): TrustManagerFactory {
//    val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
//    val ca = caInput.use { cf.generateCertificate(it) }
//    val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
//        load(null, null)
//        setCertificateEntry("ca", ca)
//    }
//    val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
//    val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
//    tmf.init(keyStore)
//    return  tmf
//}


//
//override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
//    Log.d("WEB_VIEW_EXAMPLE", "onReceivedSslError")
//    var passVerify = false
//    if (error.primaryError == SslError.SSL_UNTRUSTED) {
//        val cert = error.certificate
//        val subjectDN = cert.issuedTo.dName
//        Log.d("WEB_VIEW_EXAMPLE", "subjectDN: $subjectDN")
//        try {
//            val f: Field = cert.javaClass.getDeclaredField("mX509Certificate")
//            f.setAccessible(true)
//            val x509 = f.get(cert) as X509Certificate
//            val chain = arrayOf(x509)
//            for (trustManager in tmf.getTrustManagers()) {
//                if (trustManager is X509TrustManager) {
//                    val x509TrustManager: X509TrustManager = trustManager as X509TrustManager
//                    try {
//                        x509TrustManager.checkServerTrusted(chain, "generic")
//                        passVerify = true
//                        break
//                    } catch (e: Exception) {
//                        Log.e("WEB_VIEW_EXAMPLE", "verify trustManager failed", e)
//                        passVerify = false
//                    }
//                }
//            }
//            Log.d("WEB_VIEW_EXAMPLE", "passVerify: $passVerify")
//        } catch (e: Exception) {
//            Log.e("WEB_VIEW_EXAMPLE", "verify cert fail", e)
//        }
//    }
//    if (passVerify == true) handler.proceed() else handler.cancel()
//}