import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinKt.doInitKoin(databaseDriverFactory: DatabaseDriverFactory())
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
