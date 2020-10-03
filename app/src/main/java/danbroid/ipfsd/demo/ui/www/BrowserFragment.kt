package danbroid.ipfsd.demo.ui.www

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import danbroid.ipfsd.demo.IPFSClientModel
import danbroid.ipfsd.demo.R
import kotlinx.android.synthetic.main.fragment_browser.*


class BrowserFragment : Fragment() {

  val model: IPFSClientModel by viewModels()



  val url: String = "http://www.python.org"

  val loading = object : MutableLiveData<Boolean>() {
    override fun setValue(value: Boolean?) {
      if (super.getValue() == value) return
      super.setValue(value)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  val onBackPressedCallback = object : OnBackPressedCallback(false) {
    override fun handleOnBackPressed() {
      //log.error("handleOnBackPressed()")
      //TODO  webView?.goBack()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    //  inflater.inflate(R.menu.menu_browser, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

/*
  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_open_in_browser -> {
      //TODO   webView?.originalUrl.also { url ->
        startActivity(Intent(Intent.ACTION_VIEW).also {
          it.data = Uri.parse(url)
        })
      }
      true
    }
    else -> super.onOptionsItemSelected(item)
  }
*/


  inner class WebClient : WebViewClient() {


    override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
      //log.info("onPageStarted() $url")
      //TODO  activityInterface?.setToolbarTitle(getString(R.string.msg_loading))
      loading.postValue(true)
    }

    override fun onPageFinished(view: WebView, url: String) {
      //   log.warn("onPageFinished() $url canGoBack: ${view.canGoBack()}")
      onBackPressedCallback.isEnabled = view.canGoBack()
      //TODO  activityInterface?.setToolbarTitle(view.title!!)

      requireActivity().title = view.title
      loading.postValue(false)
    }

    override fun onReceivedError(
      view: WebView?,
      request: WebResourceRequest?,
      error: WebResourceError?
    ) {
      super.onReceivedError(view, request, error)
      loading.postValue(false)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
      super.onReceivedSslError(view, handler, error)
      loading.postValue(false)
    }


  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    if (savedInstanceState != null) {
      // context.debugToast("found existing state")
      webView?.restoreState(savedInstanceState)
      return
    }

    webView?.webViewClient = WebClient()

    //progress_bar.visibility = View.GONE
    //shadow.visibility = View.GONE


    with(webView!!.settings)
    {
      // this.minimumFontSize=12

      javaScriptCanOpenWindowsAutomatically = true
      javaScriptEnabled = true
      cacheMode = WebSettings.LOAD_DEFAULT
      setSupportZoom(true)
      builtInZoomControls = true
      displayZoomControls = false
    }

    webView?.apply {
      isFocusableInTouchMode = true
      // addJavascriptInterface(Console(activity!!), "console")
    }

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

    //webView?.addJavascriptInterface(Console(), "audienz")


    log.debug("loading url: ${url}")
    webView?.loadUrl(url)
  }


  override fun onDestroy() {
    super.onDestroy()
    webView?.destroy()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    webView?.saveState(outState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ) = inflater.inflate(R.layout.fragment_browser, container, false)


}


private val log = org.slf4j.LoggerFactory.getLogger(BrowserFragment::class.java)
