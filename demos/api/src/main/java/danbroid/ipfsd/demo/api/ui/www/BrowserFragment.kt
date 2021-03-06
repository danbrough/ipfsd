package danbroid.ipfsd.demo.api.ui.www

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import danbroid.ipfsd.demo.api.DemoNavGraph
import danbroid.ipfsd.demo.api.databinding.FragmentBrowserBinding


class BrowserFragment : Fragment() {

  val url: String
    get() = arguments?.getString(DemoNavGraph.args.url, null)
      ?: throw IllegalArgumentException("${DemoNavGraph.args.url} not specified")

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

  override fun onStart() {
    super.onStart()

  }

  override fun onStop() {
    super.onStop()

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
      log.info("onPageStarted() $url")
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


    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
      log.trace("showOverrideUrlLoading() ${request.url}")
      view.loadUrl(request.url.toString())
      return true
    }

  }

  private var _binding: FragmentBrowserBinding? = null

  // This property is only valid between onCreateView and
// onDestroyView.
  private inline val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ) = FragmentBrowserBinding.inflate(inflater, container, false).let {
    _binding = it
    it.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding.webView.destroy()
    _binding = null
  }

  @SuppressLint("SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val webView = binding.webView
    if (savedInstanceState != null) {
      // context.debugToast("found existing state")

      webView.restoreState(savedInstanceState)
      return
    }

    binding.webView.webViewClient = WebClient()

    //progress_bar.visibility = View.GONE
    //shadow.visibility = View.GONE


    with(webView.settings) {
      // this.minimumFontSize=12
      javaScriptCanOpenWindowsAutomatically = true
      javaScriptEnabled = true
      cacheMode = WebSettings.LOAD_DEFAULT
      setSupportZoom(false)
      builtInZoomControls = false
      displayZoomControls = false
    }

    webView.apply {
      isFocusableInTouchMode = true
      // addJavascriptInterface(Console(activity!!), "console")
    }

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    //webView?.addJavascriptInterface(Console(), "audienz")

    log.debug("loading url: ${url}")
    webView.loadUrl(url)
  }


  /*override fun onDestroy() {
    super.onDestroy()
    webView?.destroy()
  }*/

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    binding.webView.saveState(outState)
  }


}


private val log = danbroid.logging.getLog(BrowserFragment::class)
