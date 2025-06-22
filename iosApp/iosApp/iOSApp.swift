import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

@UIApplicationMain
class AppDelegate: NSObject, UIApplicationDelegate {
    // Provide deepLinkFilter and deepLinkMapper if needed
    let rinku = RinkuIos.init(deepLinkFilter: nil, deepLinkMapper: nil)
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        rinku.onDeepLinkReceived(url: url.absoluteString)
        return true
    }

    func application(_ application: UIApplication, continue userActivity: NSUserActivity, restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
        if userActivity.activityType == NSUserActivityTypeBrowsingWeb, let url = userActivity.webpageURL {
            let urlString = url.absoluteString
            rinku.onDeepLinkReceived(userActivity: userActivity)
        }
        return true
    }
}
