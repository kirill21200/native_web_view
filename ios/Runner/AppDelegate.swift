import Flutter
import UIKit
import WebKit

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]?
    ) -> Bool {
        GeneratedPluginRegistrant.register(with: self)

        weak var registrar = self.registrar(forPlugin: "plugin-name")

        let factory = FLNativeViewFactory(messenger: registrar!.messenger())
        self.registrar(forPlugin: "<plugin-name>")!.register(
            factory,
            withId: "<platform-view-type>")
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
}



class FLNativeViewFactory: NSObject, FlutterPlatformViewFactory {
    private var messenger: FlutterBinaryMessenger

    init(messenger: FlutterBinaryMessenger) {
        self.messenger = messenger
        super.init()
    }

    func create(
        withFrame frame: CGRect,
        viewIdentifier viewId: Int64,
        arguments args: Any?
    ) -> FlutterPlatformView {
        return FLNativeView(
            frame: frame,
            viewIdentifier: viewId,
            arguments: args,
            binaryMessenger: messenger)
    }

    /// Implementing this method is only necessary when the `arguments` in `createWithFrame` is not `nil`.
    public func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
          return FlutterStandardMessageCodec.sharedInstance()
    }
}

class FLNativeView: NSObject, FlutterPlatformView {
    private var _view: UIView

    init(
        frame: CGRect,
        viewIdentifier viewId: Int64,
        arguments args: Any?,
        binaryMessenger messenger: FlutterBinaryMessenger?
    ) {
        _view = UIView()
         super.init()
        // iOS views can be created here
        createNativeView(view: _view)
    }

    func view() -> UIView {
        return _view
    }

    func createNativeView(view _view: UIView){
//         _view.backgroundColor = UIColor.blue
//         let nativeLabel = UILabel()
//         nativeLabel.text = "Native text from iOS"
//         nativeLabel.textColor = UIColor.white
//         nativeLabel.textAlignment = .center
//         nativeLabel.frame = CGRect(x: 0, y: 0, width: 180, height: 48.0)
//         _view.addSubview(nativeLabel)



         var webView: WKWebView!

         let webConfiguration = WKWebViewConfiguration()
         webView = WKWebView(frame: CGRect(x:0, y:0, width:500, height:800), configuration: webConfiguration)

         let myUrl = URL(string:"https://kras-abs.ru/")
         let myRequest = URLRequest(url:myUrl!)

         webView.load(myRequest)

         _view.addSubview(webView)

        //  webView.load(URLRequest(url: "https://www.apple.com/"))



    }
}

class FLPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let factory = FLNativeViewFactory(messenger: registrar.messenger())
        registrar.register(factory, withId: "<platform-view-type>")
    }
}