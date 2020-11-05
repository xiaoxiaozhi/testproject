package com.myapplication.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.myapplication.R
import kotlinx.android.synthetic.main.activity_hybrid.*


class HybridActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hybrid)
        showDialog1.setOnClickListener {
            webView!!.post {
                //webView 初始化完成后调用
                webView!!.loadUrl("javascript:callJs()") //会使页面刷新,这种方式调用js代码不会有弹出框
            }
        }
        showDialog2.setOnClickListener {
            webView!!.post {
                // 只需要将第一种方法的loadUrl()换成下面该方法即可
                webView!!.evaluateJavascript("javascript:callJs()") {
                    //这里不需要再写 java代码的
                }
            }
        }
        webView.loadUrl("file:///android_asset/test.html")
        //        webView.loadUrl("file:///android_asset/javascript.html");
// 由于设置了弹窗检验调用结果,所以需要支持js对话框
// webview只是载体，内容的渲染需要使用webviewChromClient类去实现
// 通过设置WebChromeClient对象处理JavaScript的对话框
//设置响应js 的Alert()函数
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                Log.i(this.javaClass.simpleName, "onJsAlert_message:" + message)
//                val b = AlertDialog.Builder(this@HybridActivity)
//                b.setTitle("Alert")
//                b.setMessage(message)
//                b.setPositiveButton(android.R.string.ok) { dialog, which -> result.confirm() }
//                b.setCancelable(false)
//                b.create().show()
                return true
            }

            //对 js的prompt方法拦截，jsprompt是一个 带编辑框的提示框
            override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
                Log.i(this.javaClass.simpleName, "onJsPrompt_message:" + message)
                //参数result:代表消息框的返回值(输入值)
                result?.confirm("传回js")//传回js
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }


            // 拦截JS的确认框
            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                Log.i(this.javaClass.simpleName, "onJsConfirm_message:" + message)
//                result?.confirm()//传递给js true
//                result?.cancel()//传递给js false
                return super.onJsConfirm(view, url, message, result)
            }
        })
        webView.getSettings().javaScriptEnabled = true //允许activity使用该HTML的js方法
        webView.addJavascriptInterface(AndroidtoJs(), "test") //AndroidtoJS类对象映射到js的test对象,js才能调用android代码，存在严重漏洞
// 复写WebViewClient类的shouldOverrideUrlLoading方法
        // 复写WebViewClient类的shouldOverrideUrlLoading方法
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean { // 步骤2：根据协议的参数，判断是否是所需要的url
// 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
//假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
                val uri: Uri = Uri.parse(url)
                // 如果url的协议 = 预先约定的 js 协议
// 就解析往下解析参数
                if (uri.getScheme().equals("js")) { // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
// 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) { //  步骤3：
// 执行JS所需要调用的逻辑
                        println("js调用了Android的方法")
                        // 可以在协议上带有参数并传递到Android上
                        val params: HashMap<String, String> = HashMap()
                        val collection: Set<String> = uri.getQueryParameterNames()

                    }
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        )
    }

    // 继承自Object类
    class AndroidtoJs : Any() {
        // 定义JS需要调用的方法
// 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        fun hello(msg: String?) {
            println("JS调用了Android的hello方法")
        }
    }
}