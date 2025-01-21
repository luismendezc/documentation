### **3. Observer**

The Observer pattern allows one object (observer) to listen for changes in another object (subject).

#### Example:
```swift
import Foundation

class NotificationCenter {
    static let shared = NotificationCenter()
    
    private var observers: [String: [(String) -> Void]] = [:]
    
    func addObserver(for key: String, observer: @escaping (String) -> Void) {
        observers[key, default: []].append(observer)
    }
    
    func postNotification(key: String, message: String) {
        observers[key]?.forEach { $0(message) }
    }
}

// Usage
NotificationCenter.shared.addObserver(for: "UserLoggedIn") { message in
    print("Received notification: \(message)")
}

NotificationCenter.shared.postNotification(key: "UserLoggedIn", message: "User Alice logged in")
// Output: Received notification: User Alice logged in

```
